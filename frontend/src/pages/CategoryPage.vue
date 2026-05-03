<template>
  <div class="category-page">
    <div class="category-content">
      <Breadcrumbs :items="breadcrumbs" />
      <h1 class="category-title">{{ categoryName }}</h1>
      <FilterableBookList 
        v-if="currentCategory"
        :fixed-params="{ categoryCode }" 
        @book-click="goToBook"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import Breadcrumbs from '../components/Breadcrumbs.vue'
import FilterableBookList from '../components/FilterableBookList.vue'
import { apiClient } from '../services/api'

const route = useRoute()
const router = useRouter()
const categoryCode = route.params.code
const categoryName = ref('')
const currentCategory = ref(null)

onMounted(async () => {
  try {
    const data = await apiClient.get(`/api/catalog/categories/code/${categoryCode}`)
    currentCategory.value = data
    categoryName.value = data.name
  } catch (err) {
    console.error(err)
    categoryName.value = 'Категория'
  }
})

const breadcrumbs = computed(() => [
  { label: 'Каталог', link: '/catalog' },
  { label: categoryName.value }
])

const goToBook = (bookId) => {
  console.log('Переход с категорией:', currentCategory.value)
  if (!currentCategory.value) {
    console.warn('Категория не загружена, повтор через 100ms')
    setTimeout(() => goToBook(bookId), 100)
    return
  }
  console.log('Переход с категорией:', currentCategory.value)
  router.push({
    path: `/books/${bookId}`,
    query: {
      from: 'catalog',
      categoryCode: currentCategory.value.code,
      categoryName: currentCategory.value.name
    }
  })
}
</script>

<style scoped>
.category-page {
  font-family: 'Rubik', sans-serif;
  padding: 50px 50px 0;
  background: #f8f9fa;
}

.category-content {
  padding: 0 100px 50px;
}

.category-title {
  font-size: 28px;
  margin: 20px 0 30px;
}

.category-layout {
  display: flex;
  gap: 40px;
}

.filters-sidebar {
  flex: 0 0 240px;
}

.books-section {
  flex: 1;
}

.books-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 40px;
}
</style>