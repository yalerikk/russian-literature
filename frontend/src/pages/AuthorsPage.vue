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
      <div class="authors-container">
        <!-- Состояние загрузки -->
        <div v-if="loading" class="loading-state">
          <div class="spinner"></div>
          <p>Загружаем авторов...</p>
        </div>

        <!-- Состояние ошибки -->
        <div v-else-if="error" class="error-state">
          <p>Ошибка: {{ error }}</p>
          <button @click="loadAuthors" class="btn-retry">Повторить</button>
        </div>

        <!-- Состояние "нет авторов" -->
        <div v-else-if="authors.length === 0" class="empty-state">
          <div class="empty-icon">👤</div>
          <h3>Авторов пока нет</h3>
          <p>Добавьте первого автора</p>
        </div>

        <!-- Блок авторов -->
        <div v-else class="authors-grid">
          <div class="authors-block">
            <AuthorCard 
              v-for="author in authors" 
              :key="author.id"
              :author="author"
              @click="viewAuthor(author.id)"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { authorService } from '../services/authors.js'
import AuthorCard from '../components/AuthorCard.vue'

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

.authors-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 50px 0;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}

/* Сетка авторов */
.authors-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 24px;
  margin-top: 0;
  border-radius: 16px;
  padding: 20px 0px;
  background: white;
}

.authors-block {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: row;
  gap: 20px;
  padding: 0 30px;
}

/* Состояния */
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

.empty-state {
  text-align: center;
  padding: 80px 20px;
  background: var(--light-background, #F8F9FF);
  border-radius: 16px;
  border: 2px dashed var(--light-gray, #E0E0E0);
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.empty-state h3 {
  font-size: 20px;
  font-weight: 600;
  margin: 0 0 8px 0;
  color: var(--eerie-black, #1B1B1B);
}

.empty-state p {
  font-size: 14px;
  color: var(--text-secondary, #B2AEAB);
  margin: 0 0 24px 0;
}

/* Адаптивность */
@media (max-width: 1400px) {
  .authors-grid {
    grid-template-columns: repeat(4, 1fr);
  }
}

@media (max-width: 1200px) {
  .authors-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 992px) {
  .authors-content,
  .breadcrumb-nav,
  .page-header {
    padding: 0 24px;
  }
  
  .breadcrumb-nav {
    padding-top: 40px;
  }
  
  .authors-grid {
    grid-template-columns: repeat(2, 1fr);
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
  
  .authors-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
  }
}

@media (max-width: 576px) {
  .authors-content,
  .breadcrumb-nav,
  .page-header {
    padding: 0 16px;
  }
  
  .authors-grid {
    grid-template-columns: 1fr;
    max-width: 300px;
    margin: 0 auto;
  }
  
  .breadcrumb-nav {
    padding-top: 25px;
  }
}
</style>