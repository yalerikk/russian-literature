<template>
  <div class="filterable-list">
    <div class="layout">
      <aside class="filters-sidebar">
        <BooksFilter @update:filters="applyFilters" />
      </aside>

      <div class="books-area">
        <div v-if="loading" class="loading">Загрузка...</div>
        <div v-else-if="error" class="error">{{ error }}</div>
        <div v-else-if="books.length === 0" class="empty">Книги не найдены</div>
        <div v-else>
          <div class="books-grid">
            <BookCard
              v-for="book in books"
              :key="book.id"
              :book="book"
              @click="goToBook(book.id)"
            />
          </div>
          <Pagination
            v-if="totalPages > 1"
            :current-page="currentPage"
            :total-pages="totalPages"
            @page-change="onPageChange"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import BooksFilter from './BooksFilter.vue'
import BookCard from './BookCard.vue'
import Pagination from './Pagination.vue'
import { apiClient } from '../services/api'

const props = defineProps({
  fetchUrl: { 
    type: String, 
    default: '/books/filter' 
  },
  fixedParams: {
    type: Object,
    default: () => ({})
  }
})

const books = ref([])
const loading = ref(false)
const error = ref(null)
const currentPage = ref(0)
const totalPages = ref(0)

const emit = defineEmits(['book-click'])

const activeFilters = ref({
  genreIds: [],
  grade: null,
  level: null,
  literature: null,
  readingType: null
})

async function fetchBooks() {
  loading.value = true
  error.value = null
  try {
    const params = new URLSearchParams()

    const fixed = { ...props.fixedParams }
    console.log(fixed)
    // Фиксированные параметры
    Object.entries(props.fixedParams).forEach(([key, value]) => {
      if (value !== undefined && value !== null && value !== '') {
        params.append(key, value)
      }
    })

    // Фильтры
    if (activeFilters.value.genreIds.length) {
      activeFilters.value.genreIds.forEach(id => params.append('genreIds', id))
    }
    if (activeFilters.value.grade) params.append('grade', activeFilters.value.grade)
    if (activeFilters.value.level) params.append('level', activeFilters.value.level)
    if (activeFilters.value.literature) params.append('literature', activeFilters.value.literature)
    if (activeFilters.value.readingType) params.append('readingType', activeFilters.value.readingType)

    params.append('page', currentPage.value)
    params.append('size', 20)

    const baseUrl = (props.fetchUrl || '/books/filter').split('?')[0]
    const url = `${baseUrl}?${params.toString()}`
    console.log('FilterableBookList: запрос к бэкенду', url)
    const response = await apiClient.get(url)
    console.log('FilterableBookList: ответ бэкенда', response)
    let booksData = response.content
    if (props.fixedParams.categoryCode === 'new') {
        booksData = booksData.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
    }
    books.value = booksData
    totalPages.value = response.totalPages
  } catch (err) {
    console.error('FilterableBookList: ошибка', err)
    error.value = 'Не удалось загрузить книги'
  } finally {
    loading.value = false
  }
}

function applyFilters(newFilters) {
  console.log('FilterableBookList: применены новые фильтры', newFilters)
  activeFilters.value = newFilters
  currentPage.value = 0
  fetchBooks()
}

function onPageChange(newPage) {
  console.log('FilterableBookList: смена страницы на', newPage)
  currentPage.value = newPage
  fetchBooks()
}

function goToBook(bookId) {
  emit('book-click', bookId)
}
watch(
  () => props.fixedParams?.searchQuery,
  (newQuery, oldQuery) => {
    if (newQuery !== oldQuery && newQuery !== undefined) {
      console.log('FilterableBookList: searchQuery изменился на', newQuery)
      // Сбрасываем страницу и перезагружаем
      currentPage.value = 0
      fetchBooks()
    }
  }
)

watch(() => props.fixedParams, () => {
  console.log('FilterableBookList: fixedParams изменились, сброс фильтров')
  activeFilters.value = {
    genreIds: [],
    grade: null,
    level: null,
    literature: null,
    readingType: null
  }
  currentPage.value = 0
  fetchBooks()
}, { deep: true })

fetchBooks()
</script>

<style scoped>
.filterable-list {
  width: 100%;
}
.layout {
  display: flex;
  gap: 40px;
}
.filters-sidebar {
  flex: 0 0 240px;
}
.books-area {
  flex: 1;
}
.books-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 20px;
  margin-bottom: 40px;
}
@media (max-width: 1200px) {
  .books-grid { grid-template-columns: repeat(3, 1fr); }
}
@media (max-width: 768px) {
  .books-grid { grid-template-columns: repeat(2, 1fr); }
}
.loading, .error, .empty {
  text-align: center;
  padding: 40px;
}
</style>