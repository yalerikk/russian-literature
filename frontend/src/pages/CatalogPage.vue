<template>
  <div class="catalog-page">
    <!-- Основной контент -->
    <div class="catalog-content">
      <!-- Состояние загрузки -->
      <div v-if="loading" class="loading-state">
        <div class="spinner"></div>
        <p>Загружаем каталог...</p>
      </div>

      <!-- Состояние ошибки -->
      <div v-else-if="error" class="error-state">
        <p>Ошибка: {{ error }}</p>
        <button @click="loadCatalog" class="btn-retry">Повторить</button>
      </div>

      <!-- Состояние "нет категорий" -->
      <div v-else-if="!categories || categories.length === 0" class="empty-state">
        <div class="empty-icon">📚</div>
        <h3>Каталог пуст</h3>
        <p>Скоро здесь появятся книги</p>
      </div>

      <!-- Категории со слайдерами -->
      <div v-else class="catalog-categories">
        <BooksSlider 
          v-for="category in sortedCategories"
          :key="category.id"
          :title="category.name"
          :code="category.code"
          :category-name="category.name"
          :books-to-show="category.booksToShow"
          :show-more-button="true"
          @book-click="(bookId, category) => goToBook(bookId, category)"
          @more-click="goToCategory(category)"
          class="category-slider"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { catalogService } from '../services/catalog.js'
import BooksSlider from '../components/BooksSlider.vue'

const router = useRouter()

// Состояния
const categories = ref([])
const loading = ref(false)
const error = ref(null)

// Сортированные категории по displayOrder
const sortedCategories = computed(() => {
  return [...categories.value].sort((a, b) => a.displayOrder - b.displayOrder)
})

// Загрузка каталога
const loadCatalog = async () => {
  loading.value = true
  error.value = null
  
  try {
    const response = await catalogService.getCatalogPage()
    console.log('Загружен каталог:', response)
    
    categories.value = response.categories || []
  } catch (err) {
    console.error('Ошибка загрузки каталога:', err)
    error.value = err.message || 'Не удалось загрузить каталог'
  } finally {
    loading.value = false
  }
}

// Переход на страницу книги
const goToBook = (bookId, category) => {
  router.push({
    path: `/books/${bookId}`,
    query: { 
      from: 'catalog',
      categoryCode: category.code,
      categoryName: category.name
    }
  })
}

const goToCategory = (category) => {
  router.push({ name: 'Category', params: { code: category.code } })
}

// Инициализация
onMounted(async () => {
  await loadCatalog()
})
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;600;700&display=swap');

.catalog-page {
  font-family: 'Manrope', sans-serif;
  width: 100%;
  min-height: 100vh;
  background-color: var(--background-color, #f8f9fa);
  color: var(--eerie-black, #1B1B1B);
  padding: 50px 50px 0;
}

/* Основной контент */
.catalog-content {
  padding: 0 100px 50px;
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

/* Категории */
.catalog-categories {
  display: flex;
  flex-direction: column;
  gap: 40px; /* Расстояние между категориями */
}

.category-slider {
  margin: 0;
}

/* Адаптивность */
@media (max-width: 992px) {
  .breadcrumb-nav,
  .catalog-content {
    padding: 0 24px;
  }
  
  .breadcrumb-nav {
    padding-top: 40px;
  }
  
  .catalog-content {
    padding-bottom: 40px;
  }
  
  .catalog-categories {
    gap: 32px;
  }
}

@media (max-width: 768px) {
  .breadcrumb-nav,
  .catalog-content {
    padding: 0 16px;
  }
  
  .breadcrumb-nav {
    padding-top: 30px;
  }
  
  .catalog-content {
    padding-bottom: 30px;
  }
  
  .catalog-categories {
    gap: 28px;
  }
}

@media (max-width: 576px) {
  .catalog-categories {
    gap: 24px;
  }
}
</style>