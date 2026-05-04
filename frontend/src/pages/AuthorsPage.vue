<template>
  <div class="authors-page">
    <!-- Якорная навигация -->
    <div class="breadcrumb-nav">
      <span class="breadcrumb-item">Авторы</span>
    </div>

    <!-- Заголовок -->
    <div class="page-header">
      <h1 class="page-title">Авторы</h1>
    </div>

    <!-- Основной контент -->
    <div class="authors-content">
      <AuthorsGrid 
        :authors="authors"
        :loading="loading"
        :error="error"
        @retry="loadAuthors"
        @author-click="viewAuthor"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { authorService } from '../services/authors.js'
import AuthorsGrid from '../components/AuthorsGrid.vue'

// Состояния
const authors = ref([])
const loading = ref(false)
const error = ref(null)
const router = useRouter()

// Загрузка авторов
const loadAuthors = async () => {
  loading.value = true
  error.value = null
  
  try {
    authors.value = await authorService.getAllAuthors()
  } catch (err) {
    console.error('Ошибка загрузки авторов:', err)
    error.value = err.message || 'Не удалось загрузить авторов'
  } finally {
    loading.value = false
  }
}

// Переход на страницу автора
const viewAuthor = (authorId) => {
  router.push(`/authors/${authorId}`)
}

// Инициализация
onMounted(async () => {
  await loadAuthors()
})
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Rubik:wght@400;500;600;700&display=swap');

.authors-page {
  font-family: 'Rubik', sans-serif;
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  min-height: 100vh;
  background-color: var(--background-color, #f8f9fa);
}

/* Якорная навигация */
.breadcrumb-nav {
  padding: 0 50px;
  margin-bottom: 24px;
  padding-top: 50px;
}

.breadcrumb-item {
  font-weight: 400;
  font-size: 12px;
  line-height: 150%;
  color: var(--burnt-umber, #8B4513);
  letter-spacing: 0.5px;
}

/* Заголовок страницы */
.page-header {
  padding: 0 50px;
  margin-bottom: 50px; 
}

.page-title {
  font-weight: 500;
  font-size: 24px;
  line-height: 150%;
  color: var(--eerie-black, #1B1B1B);
  margin: 0;
}

/* Основной контент */
.authors-content {
  width: 100%;
  margin: 0 auto;
  padding: 0 50px 50px; 
}

/* Адаптивность */
@media (max-width: 992px) {
  .authors-content,
  .breadcrumb-nav,
  .page-header {
    padding: 0 24px;
  }
  
  .breadcrumb-nav {
    padding-top: 40px;
  }
}

@media (max-width: 768px) {
  .breadcrumb-nav {
    padding-top: 30px;
    margin-bottom: 16px;
  }
  
  .page-header {
    margin-bottom: 30px;
  }
  
  .page-title {
    font-size: 20px;
  }
}

@media (max-width: 576px) {
  .authors-content,
  .breadcrumb-nav,
  .page-header {
    padding: 0 16px;
  }
  
  .breadcrumb-nav {
    padding-top: 25px;
  }
}
</style>