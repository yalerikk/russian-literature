<template>
  <div class="books-slider">
    <div class="category-header">
      <h2 class="category-title">{{ title }}</h2>
      <button 
        v-if="showMoreButton"
        class="more-button"
        @click="$emit('more-click')"
      >
        Еще
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M10 17L15 12L10 7" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" />
          <path d="M10 17L15 12L10 7" stroke="currentColor" stroke-opacity="0.2" stroke-linecap="round" stroke-linejoin="round" />
        </svg>
      </button>
    </div>

    <div v-if="isLoading" class="loading-state">
      Загрузка книг...
    </div>
    <div v-else-if="loadError" class="error-state">
      {{ loadError }}
      <button @click="fetchBooks(currentPage)">Повторить</button>
    </div>
    <div v-else-if="books.length === 0" class="empty-state">
      Книг пока нет
    </div>
    <div v-else class="slider-container">
      <div class="slider-wrapper" ref="sliderRef">
        <button 
          class="slider-nav prev"
          @click="scrollPrev"
          :disabled="!canScrollPrev"
        >
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
            <path d="M15 18L9 12L15 6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </button>

        <div class="slider-track">
          <div class="slider-grid">
            <BookCard
              v-for="book in books"
              :key="book.id"
              :book="book"
              @click="$emit('book-click', book.id)"
            />
          </div>
        </div>

        <button 
          class="slider-nav next"
          @click="scrollNext"
          :disabled="!canScrollNext"
        >
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
            <path d="M9 18L15 12L9 6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, watch, nextTick } from 'vue'
import BookCard from './BookCard.vue'
import { catalogService } from '../services/catalog.js'

const props = defineProps({
  title: String,
  code: {
    type: String,
    required: true
  },
  booksToShow: {
    type: Number,
    default: 7
  },
  showMoreButton: Boolean
})

const emit = defineEmits(['book-click', 'more-click'])

// Состояние
const isLoading = ref(true)
const loadError = ref(null)
const books = ref([])
const currentPage = ref(0)
const totalPages = ref(0)
const sliderRef = ref(null)
const offset = ref(0)
const itemWidth = ref(140)
const sliderWidth = ref(0)
const trackWidth = ref(0)

// Вычисляемые свойства
const displayedBooks = computed(() => books.value)

const canScrollPrev = computed(() => currentPage.value > 0)
const canScrollNext = computed(() => currentPage.value + 1 < totalPages.value)

// Методы API
async function fetchBooks(page = 0) {
  if (!props.code) return
  isLoading.value = true
  loadError.value = null

  try {
    const response = await fetch(`/api/catalog/category/${props.code}/books?page=${page}&size=${props.booksToShow}`)
    if (!response.ok) throw new Error('Ошибка загрузки данных')
    const data = await response.json()
    let booksData = data.content
    if (props.code === 'new') {
      booksData = booksData.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
    }
    books.value = booksData
    currentPage.value = data.number
    totalPages.value = data.totalPages
    offset.value = 0
  } catch (err) {
    loadError.value = err.message || 'Не удалось загрузить книги'
  } finally {
    isLoading.value = false
  }
}

const scrollPrev = () => {
  if (canScrollPrev.value) fetchBooks(currentPage.value - 1)
}

const scrollNext = () => {
  if (canScrollNext.value) fetchBooks(currentPage.value + 1)
}

// Обновление размеров слайдера
const updateSliderDimensions = () => {
  if (!sliderRef.value) return
  nextTick(() => {
    const wrapper = sliderRef.value
    const track = wrapper.querySelector('.slider-track')
    if (!wrapper || !track) return
    sliderWidth.value = wrapper.clientWidth
    trackWidth.value = track.scrollWidth
    if (trackWidth.value <= sliderWidth.value) offset.value = 0
  })
}

// Следим за изменением кода категории
watch(() => props.code, () => {
  fetchBooks(0)
})

// Ресайз
let resizeTimeout
const handleResize = () => {
  clearTimeout(resizeTimeout)
  resizeTimeout = setTimeout(updateSliderDimensions, 250)
}

onMounted(() => {
  if (props.code) fetchBooks(0)
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  clearTimeout(resizeTimeout)
})

// Обновляем размеры после загрузки книг
watch(books, () => {
  setTimeout(updateSliderDimensions, 100)
})
</script>

<style scoped>
.books-slider {
  margin-bottom: 40px;
  background-color: #fff;
  border-radius: 16px;
  padding: 12px 0px;
  position: relative;
}

.category-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 12px 0 14px;
  padding: 0px 18px; /* Отступ от горизонтальных границ */
  gap: 36px;
}

.category-title {
  font-family: 'Rubik', sans-serif;
  font-weight: 500;
  font-size: 24px;
  line-height: 150%;
  color: var(--eerie-black, #1B1B1B);
  margin: 0;
}

.more-button {
  display: flex;
  align-items: center;
  gap: 0px;
  background: transparent;
  border: none;
  font-family: 'Rubik', sans-serif;
  font-weight: 400;
  font-size: 16px;
  color: var(--burnt-umber, #902923);
  cursor: pointer;
  transition: color 0.3s;
}

.more-button:hover {
  color: var(--eerie-black, #1B1B1B);
}

.more-button svg {
  width: 24px;
  height: 24px;
}

/* Слайдер */
.slider-container {
  position: relative;
  width: 100%;
}

.slider-wrapper {
  position: relative;
  width: 100%;
  padding: 0 78px; /* Отступы для кнопок навигации */
  box-sizing: border-box;
}

.slider-grid-container {
  overflow-x: auto;
  scrollbar-width: thin;
  padding-bottom: 8px;
}

.slider-track {
  display: flex;
  transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  will-change: transform;
}

.slider-grid {
  display: grid;
  align-items: center;
  justify-content: flex-start;
  grid-template-columns: repeat(7, 130px);
  gap: 16px;
  width: max-content;
}

/* Навигационные кнопки */
.slider-nav {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 40px;
  height: 40px;
  border-radius: 100%;
  background-color: #fff;
  border: 1px solid var(--white, #FFFFFF);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 10;
  color: #852B36;
  transition: all 0.3s ease;
  box-shadow: 2px 2px 16px 0 rgba(144, 41, 35, 0.3);
  margin: 0;
  padding: 0;
}

.slider-nav:disabled {
  opacity: 0.3;
  cursor: not-allowed;
}
.loading-state, .error-state, .empty-state {
  text-align: center;
  padding: 30px;
}

.slider-nav:hover:not(:disabled) {
  background: var(--platinum, #F3F3F4);
  border: 1px solid var(--platinum, #F3F3F4);
  box-shadow: 2px 2px 20px 0 rgba(144, 41, 35, 0.4);
  transform: translateY(-50%) scale(1.05);
}

.slider-nav:active:not(:disabled) {
  transform: translateY(-50%) scale(0.95);
}

.slider-nav:disabled {
  opacity: 0.3;
  cursor: not-allowed;
  box-shadow: 2px 2px 8px 0 rgba(144, 41, 35, 0.2);
}

.slider-nav.prev {
  left: 18px; /* Отступ от левого края слайдера */
}

.slider-nav.next {
  right: 18px; /* Отступ от правого края слайдера */
}

.slider-nav svg {
  width: 24px;
  height: 24px;
}

/* Адаптивность */


@media (max-width: 992px) {
  .category-title {
    font-size: 20px;
  }
  
  .more-button {
    font-size: 14px;
  }
  
  .slider-nav {
    width: 36px;
    height: 36px;
  }
  
  .slider-wrapper {
    padding: 0 46px;
  }
  
  .slider-nav.prev {
    left: 16px;
  }
  
  .slider-nav.next {
    right: 16px;
  }
}

@media (max-width: 768px) {
  .books-slider {
    margin-bottom: 32px;
    padding: 8px 0px;
  }
  
  .category-header {
    padding: 0px 16px;
    margin: 8px 0 12px;
  }
  
  .slider-item {
    width: 150px;
  }
  
  .slider-wrapper {
    padding: 0 42px;
  }
  
  .slider-nav {
    width: 32px;
    height: 32px;
  }
  
  .slider-nav svg {
    width: 20px;
    height: 20px;
  }
  
  .slider-nav.prev {
    left: 12px;
  }
  
  .slider-nav.next {
    right: 12px;
  }
}

@media (max-width: 576px) {
  .books-slider {
    margin-bottom: 24px;
    border-radius: 12px;
  }
  
  .category-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
    padding: 0px 12px;
  }
  
  .category-title {
    font-size: 18px;
  }
  
  .more-button {
    align-self: flex-end;
    font-size: 13px;
  }
  
  .slider-wrapper {
    padding: 0 38px;
  }
  
  .slider-nav {
    display: none;
  }
  
  /* Для мобильных разрешаем горизонтальный скролл */
  .slider-wrapper {
    overflow-x: auto;
    scrollbar-width: none;
    -ms-overflow-style: none;
    padding: 0 12px;
  }
  
  .slider-wrapper::-webkit-scrollbar {
    display: none;
  }
  
  .slider-track {
    flex-wrap: nowrap;
  }
}
</style>