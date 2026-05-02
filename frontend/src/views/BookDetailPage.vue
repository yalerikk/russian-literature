<template>
  <div class="book-detail-page">
    <div class="book-detail-content">
      <!-- Якорная навигация -->
      <div class="breadcrumb-nav">
        <template v-if="breadcrumbPath === 'catalog'">
          <span class="breadcrumb-item">Каталог</span>
          <span class="separator">/</span>
          <span class="breadcrumb-item" v-if="categoryName">{{ categoryName }}</span>
          <span class="separator" v-if="categoryName">/</span>
          <span class="breadcrumb-item current">{{ book?.title || 'Книга' }}</span>
        </template>
        <template v-else-if="breadcrumbPath === 'favorites'">
          <span class="breadcrumb-item">Избранное</span>
          <span class="separator">/</span>
          <span class="breadcrumb-item current">{{ book?.title || 'Книга' }}</span>
        </template>
        <template v-else-if="breadcrumbPath === 'my-books'">
          <span class="breadcrumb-item">Мои книги</span>
          <span class="separator">/</span>
          <span class="breadcrumb-item">{{ collectionName }}</span>
          <span class="separator">/</span>
          <span class="breadcrumb-item current">{{ book?.title || 'Книга' }}</span>
        </template>
      </div>

      <!-- Заголовок книги -->
      <h1 class="book-main-title">{{ book?.title || 'Загрузка...' }}</h1>

      <!-- Основной контент -->
      <div class="book-content">
        <!-- Левая часть: Карточка книги (3 колонки) -->
        <div class="book-card-section">
          <div class="book-card-detail">
            <!-- Фото книги -->
            <div class="book-photo">
              <img 
                :src="book?.coverUrl || '/images/cover.png'" 
                :alt="book?.title"
                @error="handleImageError"
              />
            </div>
            
            <!-- Информация о книге -->
            <div class="book-info-section">
              <!-- Иконка избранного -->
              <button 
                class="favorite-icon"
                @click="toggleFavorite"
                :title="isFavorite ? 'Убрать из избранного' : 'Добавить в избранное'"
              >
                <svg width="32" height="31" viewBox="0 0 32 31" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <path d="M15.9998 6.95441L15.2798 7.64774C15.3731 7.74452 15.485 7.82151 15.6087 7.87409C15.7324 7.92667 15.8654 7.95377 15.9998 7.95377C16.1343 7.95377 16.2673 7.92667 16.391 7.87409C16.5147 7.82151 16.6266 7.74452 16.7198 7.64774L15.9998 6.95441ZM9.3345 21.5144C9.1294 21.3457 8.8657 21.2654 8.6014 21.2912C8.33709 21.3169 8.09385 21.4466 7.92517 21.6517C7.75649 21.8568 7.6762 22.1206 7.70196 22.3849C7.72771 22.6492 7.8574 22.8924 8.0625 23.0611L9.3345 21.5144ZM3.1225 17.5024C3.18554 17.6176 3.27065 17.7193 3.37298 17.8016C3.4753 17.8839 3.59284 17.9453 3.71888 17.9822C3.84492 18.0191 3.97699 18.0308 4.10756 18.0166C4.23812 18.0025 4.36462 17.9628 4.47984 17.8997C4.59505 17.8367 4.69672 17.7516 4.77904 17.6493C4.86136 17.5469 4.92272 17.4294 4.95962 17.3034C4.99651 17.1773 5.00822 17.0453 4.99408 16.9147C4.97993 16.7841 4.94021 16.6576 4.87717 16.5424L3.1225 17.5024ZM3.6665 11.8037C3.6665 8.93708 5.2865 6.53174 7.4985 5.51974C9.64784 4.53708 12.5358 4.79708 15.2798 7.64774L16.7198 6.26241C13.4665 2.87974 9.68517 2.32108 6.6665 3.70108C3.7145 5.05174 1.6665 8.18774 1.6665 11.8037H3.6665ZM11.3292 25.6211C12.0132 26.1597 12.7465 26.7331 13.4892 27.1677C14.2318 27.6024 15.0798 27.9544 15.9998 27.9544V25.9544C15.5865 25.9544 15.1012 25.7944 14.4985 25.4411C13.8945 25.0891 13.2692 24.6037 12.5678 24.0504L11.3292 25.6211ZM20.6705 25.6211C22.5718 24.1211 25.0038 22.4037 26.9105 20.2557C28.8532 18.0691 30.3332 15.3584 30.3332 11.8037H28.3332C28.3332 14.7344 27.1332 16.9917 25.4158 18.9277C23.6625 20.9011 21.4532 22.4571 19.4318 24.0504L20.6705 25.6211ZM30.3332 11.8037C30.3332 8.18774 28.2865 5.05174 25.3332 3.70108C22.3145 2.32108 18.5358 2.87974 15.2798 6.26108L16.7198 7.64774C19.4638 4.79841 22.3518 4.53708 24.5012 5.51974C26.7132 6.53174 28.3332 8.93574 28.3332 11.8037H30.3332ZM19.4318 24.0504C18.7305 24.6037 18.1052 25.0891 17.5012 25.4411C16.8972 25.7931 16.4132 25.9544 15.9998 25.9544V27.9544C16.9198 27.9544 17.7678 27.6011 18.5105 27.1677C19.2545 26.7331 19.9865 26.1597 20.6705 25.6211L19.4318 24.0504ZM12.5678 24.0504C11.5065 23.2144 10.4278 22.4144 9.3345 21.5144L8.0625 23.0611C9.16917 23.9717 10.3398 24.8411 11.3292 25.6211L12.5678 24.0504ZM4.87717 16.5437C4.07387 15.0937 3.65696 13.4614 3.6665 11.8037H1.6665C1.6665 13.9877 2.2265 15.8637 3.1225 17.5024L4.87717 16.5437Z" fill="#902923" />
                </svg>
              </button>
              
              <!-- Информация о книге -->
              <div class="book-detail-info">
                <!-- Название книги -->
                <h4 class="book-title">{{ book?.title || 'Загрузка...' }}</h4>
                
                <div class="detail-item">
                  <span class="detail-label">Автор:</span>
                  <span class="detail-value">{{ formattedAuthorName }}</span>
                </div>
                
                <div class="detail-item" v-if="book?.genres?.length">
                  <span class="detail-label">Жанр:</span>
                  <span class="detail-value">{{ formattedGenres }}</span>
                </div>
                
                <div class="detail-item" v-if="book?.educationalTags?.length">
                  <span class="detail-label">Теги:</span>
                  <span class="detail-value">{{ formattedTags }}</span>
                </div>
                
                <div class="detail-item" v-if="book?.publicationYear">
                  <span class="detail-label">Год:</span>
                  <span class="detail-value">{{ book.publicationYear }}</span>
                </div>
                
                <div class="detail-item description">
                  <span class="detail-label">Описание:</span>
                  <p class="detail-value">{{ book?.description || 'Нет описания' }}</p>
                </div>
                
                <!-- Рейтинг -->
                <div class="book-rating" >
                    <div class="rating-info">
                      <div class="rating-icon">
                        <svg width="18" height="18" viewBox="0 0 18 18" fill="none" xmlns="http://www.w3.org/2000/svg">
                          <path d="M6.42731 5.75348L1.64231 6.44723L1.55756 6.46448C1.42927 6.49854 1.31231 6.56603 1.21863 6.66008C1.12496 6.75412 1.05792 6.87134 1.02436 6.99977C0.990804 7.1282 0.991934 7.26323 1.02763 7.39108C1.06333 7.51893 1.13233 7.63501 1.22756 7.72748L4.69406 11.1017L3.87656 15.868L3.86681 15.9505C3.85896 16.0832 3.88651 16.2156 3.94665 16.3341C4.00679 16.4526 4.09735 16.5531 4.20906 16.6251C4.32078 16.6971 4.44963 16.7382 4.58242 16.7441C4.71522 16.7499 4.84719 16.7204 4.96481 16.6585L9.24431 14.4085L13.5141 16.6585L13.5891 16.693C13.7129 16.7417 13.8474 16.7567 13.9789 16.7363C14.1104 16.7159 14.234 16.6609 14.3373 16.5769C14.4405 16.493 14.5195 16.383 14.5662 16.2585C14.6129 16.1339 14.6256 15.9991 14.6031 15.868L13.7848 11.1017L17.2528 7.72673L17.3113 7.66298C17.3949 7.56005 17.4497 7.43682 17.4701 7.30582C17.4905 7.17483 17.4759 7.04076 17.4276 6.91727C17.3794 6.79379 17.2993 6.6853 17.1954 6.60286C17.0916 6.52042 16.9678 6.46698 16.8366 6.44798L12.0516 5.75348L9.91256 1.41848C9.85067 1.29288 9.75485 1.18711 9.63596 1.11316C9.51706 1.0392 9.37984 1 9.23981 1C9.09979 1 8.96257 1.0392 8.84367 1.11316C8.72478 1.18711 8.62896 1.29288 8.56706 1.41848L6.42731 5.75348Z" fill="black" />
                        </svg>
                      </div>
                      <span class="rating-value">{{ book?.rating?.toFixed(1) || '0.0' }}</span>
                      <span class="rating-count">({{ book?.ratingCount || 0 }})</span>
                    </div>
                  </div>
                </div>

              <!-- Действия внутри карточки -->
              <div class="book-actions-in-card">
                <!-- Чтение онлайн -->
                <button class="action-btn read-online" @click="readOnline">
                  <svg width="22" height="22" viewBox="0 0 22 22" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M4.5835 7.3335H17.4168M4.5835 11.0002H17.4168M4.5835 14.6668H10.0835" stroke="black" stroke-width="1.83333" stroke-linecap="round" stroke-linejoin="round" />
                  </svg>
                  Читать онлайн
                </button>
                
                <!-- Скачивание -->
                <div class="download-section">
                  <div class="download-label">Скачать в формате:</div>
                  <div class="format-buttons">
                    <button class="format-btn" @click="download('epub')">EPUB</button>
                    <button class="format-btn" @click="download('fb2')">FB2</button>
                    <button class="format-btn" @click="download('pdf')">PDF</button>
                    <button class="format-btn" @click="download('txt')">TXT</button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Правая часть: Блок действий (1 колонка) -->
        <div class="actions-section">
          <!-- Блок действий 1 -->
          <div class="actions-block">
            <button class="action-btn" @click="addToCollection('read-later')">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M12 2C6.5 2 2 6.5 2 12C2 17.5 6.5 22 12 22C17.5 22 22 17.5 22 12C22 6.5 17.5 2 12 2ZM12 20C7.59 20 4 16.41 4 12C4 7.59 7.59 4 12 4C16.41 4 20 7.59 20 12C20 16.41 16.41 20 12 20ZM12.5 7H11V13L16.2 16.2L17 14.9L12.5 12.2V7Z" fill="#1B1B1B" />
              </svg>
              Отложить
            </button>
            
            <button class="action-btn" @click="markAsRead">
              <svg width="22" height="22" viewBox="0 0 22 22" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M8.75401 13.8875L16.5228 6.11875C16.7061 5.93542 16.92 5.84375 17.1644 5.84375C17.4089 5.84375 17.6228 5.93542 17.8061 6.11875C17.9894 6.30208 18.0811 6.51994 18.0811 6.77233C18.0811 7.02472 17.9894 7.24228 17.8061 7.425L9.39567 15.8583C9.21234 16.0417 8.99845 16.1333 8.75401 16.1333C8.50956 16.1333 8.29567 16.0417 8.11234 15.8583L4.17067 11.9167C3.98734 11.7333 3.89934 11.5158 3.90667 11.264C3.91401 11.0122 4.00965 10.7944 4.19359 10.6104C4.37753 10.4265 4.5954 10.3348 4.84717 10.3354C5.09895 10.336 5.31651 10.4277 5.49984 10.6104L8.75401 13.8875Z" fill="#1B1B1B" />
              </svg>
              Прочитано
            </button>
            
            <button class="action-btn collection-btn" @click="showCollections = !showCollections">
              Добавить в коллекцию
            </button>
            
            <!-- Разделитель -->
            <div class="divider"></div>
            
            <!-- Рейтинг -->
            <div class="rating-stars">
              <button 
                v-for="star in 5" 
                :key="star"
                class="star-btn"
                @click="rateBook(star)"
                :class="{ active: userRating >= star }"
              >
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <path 
                    :fill="userRating >= star ? '#902923' : 'none'"
                    d="M11.993 18.1801L5.821 21.4251L7 14.5521L2 9.68513L8.9 8.68513L11.986 2.43213L15.072 8.68513L21.972 9.68513L16.972 14.5521L18.151 21.4251L11.993 18.1801Z" 
                    stroke="#1B1B1B" 
                    stroke-width="1.7027" 
                    stroke-linecap="round" 
                    stroke-linejoin="round" 
                  />
                </svg>
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Компонент выбора коллекции -->
      <CollectionsSelect 
        v-if="showCollections"
        :selected-collection="selectedCollection"
        @select="handleCollectionSelect"
        @close="showCollections = false"
      />

      <!-- Слайдер "Другие книги автора" -->
      <div class="slider-section" v-if="authorBooks.length > 0">
        <BooksSlider
          :title="'Другие книги автора'"
          :books="authorBooks"
          @book-click="goToBook"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { bookService } from '../services/books.js'
import CollectionsSelect from '../components/CollectionsSelect.vue'
import BooksSlider from '../components/BooksSlider.vue'

const route = useRoute()
const router = useRouter()

const book = ref(null)
const authorBooks = ref([])
const loading = ref(true)
const error = ref(null)
const isFavorite = ref(false)
const userRating = ref(0)
const showCollections = ref(false)
const selectedCollection = ref(null)
const categoryName = ref(route.query.category || '')

// Определяем путь для хлебных крошек
const breadcrumbPath = computed(() => {
  const from = route.query.from
  
  if (from === 'favorites') return 'favorites'
  if (from === 'my-books') return 'my-books'
  return 'catalog'
})

const collectionName = computed(() => {
  return route.query.collection || 'Коллекция'
})

const formattedAuthorName = computed(() => {
  if (!book.value) return 'Автор не указан'
  
  if (book.value.authorName) return book.value.authorName
  if (book.value.author) {
    const author = book.value.author
    if (author.firstName && author.lastName) {
      return `${author.firstName} ${author.lastName}`
    }
    return author.fullName || 'Автор не указан'
  }
  return 'Автор не указан'
})

const formattedGenres = computed(() => {
  if (!book.value?.genres || book.value.genres.length === 0) return 'Не указаны'
  return book.value.genres.map(g => g.name).join(', ')
})

const formattedTags = computed(() => {
  if (!book.value?.educationalTags || book.value.educationalTags.length === 0) return 'Не указаны'
  return book.value.educationalTags.map(t => t.name).join(', ')
})

// Загрузка данных
const loadBookData = async () => {
  loading.value = true
  error.value = null
  
  try {
    const bookId = route.params.id
    console.log('Загрузка книги ID:', bookId)
    
    // Загружаем книгу по ID
    book.value = await bookService.getBookById(bookId)
    console.log('Загружена книга:', book.value)
    
    // Загружаем другие книги автора
    if (book.value.author?.id) {
      console.log('Загрузка книг автора ID:', book.value.author.id)
      authorBooks.value = await bookService.getBooksByAuthor(book.value.author.id)
      console.log('Загружены книги автора:', authorBooks.value)
    }
  } catch (err) {
    console.error('Ошибка загрузки книги:', err)
    error.value = err.message || 'Не удалось загрузить книгу'
  } finally {
    loading.value = false
  }
}

const goToBook = (bookId) => {
  console.log('Переход на страницу книги из слайдера:', bookId)
  
  // Сохраняем категорию, если она была
  const query = { from: 'author' }
  if (categoryName.value) {
    query.category = categoryName.value
  }
  
  router.push({
    path: `/books/${bookId}`,
    query: query
  })
}

// Обработчики действий
const toggleFavorite = () => {
  if (!isAuthenticated()) {
    showAuthNotification()
    return
  }
  
  isFavorite.value = !isFavorite.value
  console.log(isFavorite.value ? 'Добавлено в избранное' : 'Убрано из избранного')
}

const readOnline = () => {
  if (!book.value?.readUrl) {
    alert('Книга недоступна для чтения онлайн')
    return
  }
  window.open(book.value.readUrl, '_blank')
}

const download = (format) => {
  if (!isAuthenticated()) {
    showAuthNotification()
    return
  }
  
  console.log(`Скачивание в формате ${format}`)
  // Здесь будет логика скачивания
}

const addToCollection = (type) => {
  if (!isAuthenticated()) {
    showAuthNotification()
    return
  }
  
  console.log(`Добавление в коллекцию: ${type}`)
}

const markAsRead = () => {
  if (!isAuthenticated()) {
    showAuthNotification()
    return
  }
  
  console.log('Отметка как прочитанное')
}

const rateBook = (rating) => {
  if (!isAuthenticated()) {
    showAuthNotification()
    return
  }
  
  userRating.value = rating
  console.log(`Оценка: ${rating}`)
}

const handleCollectionSelect = (collection) => {
  selectedCollection.value = collection
  showCollections.value = false
  console.log('Выбрана коллекция:', collection)
}

const handleImageError = (event) => {
  event.target.src = '/images/cover.png'
}

const isAuthenticated = () => {
  // Заглушка - нужно подключить к вашему состоянию авторизации
  return false
}

const showAuthNotification = () => {
  if (confirm('Для выполнения действия необходимо войти в систему. Перейти на страницу входа?')) {
    router.push('/login')
  }
}

onMounted(() => {
  loadBookData()
})
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;600;700&display=swap');
@import url('https://fonts.googleapis.com/css2?family=Rubik:wght@400;500;600;700&display=swap');

.book-detail-page {
  font-family: 'Manrope', sans-serif;
  width: 100%;
  min-height: 100vh;
  background-color: var(--background-color, #f8f9fa);
  color: var(--eerie-black, #1B1B1B);
  padding: 50px 50px 0;
}

/* Обертка для контента */
.book-detail-content {
  padding: 0 50px 50px;
}

/* Якорная навигация */
.breadcrumb-nav {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.breadcrumb-item {
  font-family: 'Rubik', sans-serif;
  font-weight: 400;
  font-size: 12px;
  line-height: 150%;
  color: var(--burnt-umber, #902923);
  letter-spacing: 0.5px;
}

.breadcrumb-item.current {
  color: var(--dim-grey, #73706C);
}

.separator {
  color: var(--burnt-umber, #902923);
  font-size: 12px;
}

/* Заголовок книги */
.book-main-title {
  font-family: 'Manrope', sans-serif;
  font-weight: 600;
  font-size: 24px;
  color: var(--eerie-black, #1B1B1B);
  padding: 24px 0 50px;
}

/* Основной контент */
.book-content {
  display: grid;
  grid-template-columns: 3fr 1fr;
  gap: 50px;
}

/* Карточка книги (3 колонки) */
.book-card-section {
  margin: 0;
}

.book-card-detail {
  background: var(--white, #FFFFFF);
  border-radius: 30px;
  padding: 20px;
  display: grid;
  grid-template-columns: 1fr 2fr;
  gap: 50px;
}

.book-photo {
  width: 100%;
  border-radius: 10px;
  overflow: hidden;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.book-photo img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  aspect-ratio: 3/4;
}

.book-info-section {
  display: flex;
  flex-direction: column;
  gap: 40px;
  position: relative;
}

.favorite-icon {
  position: absolute;
  top: 0;
  right: 0;
  background: transparent;
  border: none;
  cursor: pointer;
  padding: 8px;
  transition: opacity 0.3s;
}

.favorite-icon:hover {
  opacity: 0.7;
}

.book-detail-info {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.book-title {
  font-family: 'Manrope', sans-serif;
  font-weight: 600;
  font-size: 24px;
  color: var(--eerie-black, #1B1B1B);
  margin: 0;
}

.detail-item {
  display: flex;
  flex-direction: row;
  gap: 4px;
}

.detail-item.description {
  gap: 14px;
  flex-direction: column;
}

.detail-label {
  font-family: 'Manrope', sans-serif;
  font-weight: 700;
  font-size: 14px;
  color: var(--eerie-black, #1B1B1B);
}

.detail-value {
  font-family: 'Manrope', sans-serif;
  font-weight: 400;
  font-size: 14px;
  color: var(--eerie-black, #1B1B1B);
  line-height: 1.5;
}

.book-rating {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  margin-top: auto;
}

.rating-info {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
}

.rating-value {
  font-weight: 400;
  color: #1B1B1B;
}

.rating-count {
  font-weight: 400;
  color: #B2AEAB;
}

.rating-icon {
  display: flex; 
  align-items: center;
}

/* Действия внутри карточки */
.book-actions-in-card {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* Блоки действий справа */
.actions-section {
  display: flex;
  flex-direction: column;
  gap: 50px;
}

.actions-block {
  background: var(--white, #FFFFFF);
  border-radius: 16px;
  padding: 12px 16px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* Общие стили для кнопок */
.action-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  border: 1px solid rgba(52, 49, 45, 0.04);
  border-radius: 16px;
  padding: 4px 14px;
  background: var(--platinum, #F3F3F4);
  font-family: 'Manrope', sans-serif;
  font-weight: 400;
  font-size: 14px;
  color: var(--eerie-black, #1B1B1B);
  cursor: pointer;
  transition: all 0.3s;
  justify-content: center;
  width: 100%;
  flex-direction: row;
  flex-wrap: nowrap;
}

.action-btn:hover {
  background: #e8e8e8;
}

.action-btn svg {
  flex-shrink: 0;
}

.action-btn.read-online {
  max-width: 160px;
}

/* Секция скачивания */
.download-section {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.download-label {
  font-family: 'Manrope', sans-serif;
  font-weight: 700;
  font-size: 14px;
  color: var(--eerie-black, #1B1B1B);
}

.format-buttons {
  display: flex;
  gap: 14px;
  flex-wrap: wrap;
}

.format-btn {
  border: 1px solid rgba(52, 49, 45, 0.04);
  border-radius: 16px;
  padding: 4px 10px;
  background: var(--platinum, #F3F3F4);
  font-family: 'Manrope', sans-serif;
  font-weight: 400;
  font-size: 14px;
  color: var(--eerie-black, #1B1B1B);
  cursor: pointer;
  transition: all 0.3s;
  text-transform: uppercase;
}

.format-btn:hover {
  background: #e8e8e8;
}

.collection-btn {
  padding: 7px 14px;
  gap: 10px;
}

.divider {
  height: 1px;
  width: 100%;
  background: rgba(144, 41, 35, 0.2);
  margin: 8px 0;
}

.rating-stars {
  display: flex;
  gap: 10px;
  justify-content: center;
}

.star-btn {
  background: transparent;
  border: none;
  cursor: pointer;
  padding: 4px;
  transition: transform 0.3s;
}

.star-btn:hover {
  transform: scale(1.1);
}

.star-btn.active svg path {
  fill: #902923;
}

/* Слайдер */
.slider-section {
  margin-top: 50px;
}

/* Адаптивность */
@media (max-width: 1200px) {
  .book-content {
    grid-template-columns: 2fr 1fr;
    gap: 30px;
  }
}

@media (max-width: 992px) {
  .breadcrumb-nav,
  .book-main-title,
  .book-content,
  .slider-section {
    padding: 0 24px;
  }
  
  .breadcrumb-nav {
    padding-top: 40px;
  }
  
  .book-main-title {
    margin-top: 40px;
    margin-bottom: 40px;
  }
  
  .book-content {
    grid-template-columns: 1fr;
    gap: 30px;
  }
  
  .book-card-detail {
    grid-template-columns: 1fr 2fr;
    gap: 30px;
  }
  
  .actions-section {
    order: -1; /* Действия сверху на мобилках */
  }
}

@media (max-width: 768px) {
  .breadcrumb-nav,
  .book-main-title,
  .book-content,
  .slider-section {
    padding: 0 16px;
  }
  
  .breadcrumb-nav {
    padding-top: 30px;
  }
  
  .book-main-title {
    font-size: 20px;
    margin-top: 30px;
    margin-bottom: 30px;
  }
  
  .book-card-detail {
    grid-template-columns: 1fr;
    gap: 20px;
  }
  
  .book-photo {
    max-width: 250px;
    margin: 0 auto;
  }
  
  .format-buttons {
    justify-content: center;
  }
}

@media (max-width: 576px) {
  .book-card-detail {
    padding: 16px;
    border-radius: 20px;
  }
  
  .action-btn {
    font-size: 13px;
    padding: 8px 12px;
  }
  
  .format-btn {
    font-size: 12px;
    padding: 6px 10px;
  }
}
</style>