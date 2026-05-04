<template>
  <div class="search-page">
    <div class="search-content">
      <!-- Хлебные крошки -->
      <div class="breadcrumb-nav">
        <span class="breadcrumb-item">Поиск</span>
        <span class="separator">/</span>
        <span class="breadcrumb-item current">“{{ query }}”</span>
      </div>

      <h1 class="page-title">Результаты поиска</h1>

      <!-- Вкладки -->
      <div class="search-tabs">
        <button 
          :class="['tab-btn', { active: activeTab === 'books' }]"
          @click="activeTab = 'books'"
        >
          <svg width="36" height="36" viewBox="0 0 36 36" fill="none">
            <path d="M12 9H25.5V12H12V9Z" fill="currentColor" />
            <path d="M30 3H9C6.525 3 4.5 5.025 4.5 7.5V28.5C4.5 30.975 6.525 33 9 33H31.5V30H9C8.175 30 7.5 29.325 7.5 28.5C7.5 27.675 8.175 27 9 27H30C30.825 27 31.5 26.325 31.5 25.5V4.5C31.5 3.675 30.825 3 30 3ZM21 24H9C8.475 24 7.965 24.105 7.5 24.27V7.5C7.5 6.675 8.175 6 9 6H28.5V24H21Z" fill="currentColor" />
          </svg>
          Книги
        </button>
        <button 
          :class="['tab-btn', { active: activeTab === 'authors' }]"
          @click="activeTab = 'authors'; loadAuthors()"
        >
          <svg width="36" height="36" viewBox="0 0 36 36" fill="none">
            <path d="M10.41 21.0495C10.0791 21.9313 9.77594 22.8232 9.501 23.724C10.941 22.6785 12.6525 22.014 14.628 21.768C18.3975 21.297 21.747 18.8085 23.442 15.681L21.258 13.4985L23.3775 11.376L24.8775 9.87298C25.5225 9.22948 26.25 8.03698 27.0195 6.32248C18.63 7.62298 13.494 12.759 10.41 21.0495ZM25.5 13.4955L27 14.9955C25.5 19.4955 21 23.9955 15 24.7455C10.997 25.2455 8.496 27.9955 7.497 32.9955H4.5C6 23.9955 9 2.99548 31.5 2.99548C30 7.49148 28.5015 10.49 27.0045 11.991L25.5 13.4955Z" fill="currentColor" />
          </svg>
          Авторы
        </button>
      </div>

      <!-- Контент вкладок -->
      <div v-if="activeTab === 'books'" class="tab-content">
        <FilterableBookList 
          :fixed-params="{ searchQuery: query }"
        />
      </div>
      <div v-else class="tab-content">
        <AuthorsGrid 
          :authors="authors"
          :loading="authorsLoading"
          :error="authorsError"
          @retry="loadAuthors"
          @author-click="goToAuthor"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import FilterableBookList from '../components/FilterableBookList.vue'
import AuthorsGrid from '../components/AuthorsGrid.vue'
import { apiClient } from '../services/api'

const route = useRoute()
const router = useRouter()
const query = computed(() => route.query.q || '')

const activeTab = ref('books')
const authors = ref([])
const authorsLoading = ref(false)
const authorsError = ref('')

async function loadAuthors() {
  if (!query.value) {
    console.log('loadAuthors: query пуст')
    return
  }
  authorsLoading.value = true
  authorsError.value = ''
  try {
    const url = `/api/search/authors?query=${encodeURIComponent(query.value)}&page=0&size=20`
    const data = await apiClient.get(url)
    authors.value = data.content || []
  } catch (err) {
    console.error('Ошибка загрузки авторов:', err)
    authorsError.value = 'Не удалось загрузить авторов'
  } finally {
    authorsLoading.value = false
  }
}

const goToAuthor = (id) => {
  router.push(`/authors/${id}`)
}

watch([activeTab, () => query.value], ([newTab, newQuery]) => {
  if (newTab === 'authors' && newQuery) {
    loadAuthors()
  }
}, { immediate: true })
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Rubik:wght@400;500;600&display=swap');
.search-page {
  font-family: 'Rubik', sans-serif;
  padding: 50px 50px 0;
  background: #f8f9fa;
  min-height: 100vh;
}
.search-content {
  padding: 0 100px 50px;
}
.breadcrumb-nav {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 20px;
}
.breadcrumb-item {
  font-family: 'Rubik', sans-serif;
  font-weight: 400;
  font-size: 12px;
  line-height: 150%;
  color: #902923;
  letter-spacing: 0.5px;
  text-decoration: none;
}
.breadcrumb-item.current {
  color: #73706C;
}
.separator {
  color: #902923;
  font-size: 12px;
}
.page-title {
  font-family: 'Manrope', sans-serif;
  font-weight: 600;
  font-size: 24px;
  color: var(--eerie-black, #1B1B1B);
  margin-bottom: 24px;
}
.search-tabs {
  display: flex;
  gap: 20px;
  margin-bottom: 40px;
}
.tab-btn {
  font-family: 'Manrope', sans-serif;
  display: flex;
  align-items: center;
  gap: 8px;
  background: transparent;
  border: none;
  font-size: 18px;
  font-weight: 500;
  color: #73706C;
  background: white;
  cursor: pointer;
  padding: 8px 16px;
  border-radius: 40px;
  transition: all 0.3s;
}
.tab-btn svg {
  width: 24px;
  height: 24px;
}
.tab-btn.active {
  background: #902923;
  color: white;
}
.tab-btn.active svg {
  fill: white;
}
@media (max-width: 992px) {
  .search-content { padding: 0 24px; }
  .page-title { font-size: 24px; }
}
</style>