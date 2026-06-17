<template>
  <div class="my-books-page">
    <div class="my-books-content">
      <div class="breadcrumb-nav">
        <span class="breadcrumb-item">Мои книги</span>
      </div>

      <h1 class="page-title">Мои книги</h1>

      <div v-if="loading" class="loading-state">
        <div class="spinner"></div>
        <p>Загружаем ваши коллекции...</p>
      </div>

      <div v-else-if="error" class="error-state">
        <p>Ошибка: {{ error }}</p>
        <button @click="loadCollections" class="btn-retry">Повторить</button>
      </div>

      <div v-else class="collections-sliders">
        <BooksSlider
          v-for="status in statuses"
          :key="status.code"
          :title="status.title"
          :books="booksByStatus[status.code]"
          :books-to-show="7"
          :show-more-button="true"
          @more-click="goToCollection(status.code)"
          @book-click="goToBook"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import BooksSlider from '../components/BooksSlider.vue'
import { apiClient } from '../services/api'

const router = useRouter()
const loading = ref(false)
const error = ref(null)

const booksByStatus = ref({
  WISHLIST: [],
  READING: [],
  READ: []
})

const statuses = [
  { code: 'WISHLIST', title: 'Отложить' },
  { code: 'READING', title: 'Читаю' },
  { code: 'READ', title: 'Прочитано' }
]

const loadCollections = async () => {
  loading.value = true
  error.value = null
  try {
    const promises = statuses.map(async (status) => {
      try {
        const response = await apiClient.get('/users/me/books/slider', {
          params: { status: status.code, page: 0, size: 7 }
        })
        const books = response.content.map(ub => ({
          id: ub.bookId,           // ← ID книги, а не записи
          title: ub.bookTitle,
          coverUrl: ub.coverUrl,
          authorShortName: ub.authorShortName,
          rating: ub.rating || 0,
          ratingCount: ub.ratingCount || 0,
          favorite: ub.favorite
        }))
        console.log(`[MyBooks] Для статуса ${status.code} получено книг:`, books.length)
        booksByStatus.value[status.code] = books
      } catch (err) {
        console.error(`Ошибка коллекции ${status.code}:`, err)
        booksByStatus.value[status.code] = []
      }
    })
    await Promise.all(promises)
  } catch (err) {
    error.value = err.message || 'Не удалось загрузить коллекции'
  } finally {
    loading.value = false
  }
}

const goToCollection = (statusCode) => {
  router.push(`/profile/books/${statusCode.toLowerCase()}`)
}

const goToBook = (bookId) => router.push(`/books/${bookId}`)

onMounted(async () => {
  await loadCollections()
})
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;600;700&display=swap');

.my-books-page {
  font-family: 'Manrope', sans-serif;
  width: 100%;
  min-height: 100vh;
  background-color: var(--background-color, #f8f9fa);
  color: var(--eerie-black, #1B1B1B);
  padding: 50px 50px 0;
}

/* Основной контент (аналог .catalog-content) */
.my-books-content {
  padding: 0 100px 50px;
}

/* Хлебные крошки – стиль как в каталоге */
.breadcrumb-nav {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
  margin-bottom: 20px;
}
.breadcrumb-item {
  font-family: 'Rubik', sans-serif;
  font-weight: 400;
  font-size: 12px;
  line-height: 150%;
  color: #902923;
  letter-spacing: 0.5px;
}
.breadcrumb-item.current {
  color: #73706C;
}
.separator {
  color: #902923;
  font-size: 12px;
}

/* Заголовок страницы */
.page-title {
  font-family: 'Manrope', sans-serif;
  font-weight: 600;
  font-size: 24px;
  color: #1B1B1B;
  margin-bottom: 30px;
}

/* Слайдеры коллекций */
.collections-sliders {
  display: flex;
  flex-direction: column;
  gap: 40px;
}

/* Состояния загрузки, ошибки, пустоты – копируем из CatalogPage */
.loading-state {
  text-align: center;
  padding: 80px 0;
}
.spinner {
  width: 40px;
  height: 40px;
  border: 3px solid var(--light-gray, #E0E0E0);
  border-top-color: var(--primary-color, #667eea);
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 20px;
}
@keyframes spin {
  to { transform: rotate(360deg); }
}
.error-state {
  text-align: center;
  padding: 40px;
  background: #FFF5F5;
  border-radius: 12px;
  border: 1px solid #FED7D7;
}
.error-state p {
  color: #C53030;
  margin-bottom: 16px;
}
.btn-retry {
  background: var(--primary-color, #667eea);
  color: white;
  border: none;
  border-radius: 8px;
  padding: 8px 16px;
  font-family: 'Manrope', sans-serif;
  font-size: 14px;
  cursor: pointer;
  transition: opacity 0.3s;
}
.btn-retry:hover {
  opacity: 0.9;
}
</style>