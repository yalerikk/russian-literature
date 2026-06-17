<template>
  <div class="author-detail-page">
    <div class="author-detail-content">
      <!-- Хлебные крошки -->
      <div class="breadcrumb-nav">
        <template v-if="breadcrumbPath === 'authors'">
          <router-link to="/authors" class="breadcrumb-item">Авторы</router-link>
          <span class="separator">/</span>
          <span class="breadcrumb-item current">{{ authorName || 'Автор' }}</span>
        </template>
        <template v-else-if="breadcrumbPath === 'search'">
          <span class="breadcrumb-item">Поиск</span>
          <span class="separator">/</span>
          <span class="breadcrumb-item current">{{ authorName || 'Автор' }}</span>
        </template>
      </div>

      <h1 class="author-main-title">{{ authorName }}</h1>

      <!-- Заголовок и фото -->
      <div class="author-header">
        <div class="author-photo-large">
          <img :src="authorPhoto || '/images/authors/default-author.jpg'" :alt="authorName" @error="handleImageError">
        </div>
        <div class="author-info">
          <h1 class="author-title">{{ authorName }}</h1>
          
          <!-- Красивый вывод дат жизни -->
          <div class="author-dates" v-if="formattedDates">
            {{ formattedDates }}
          </div>
          <div class="author-age" v-if="ageText">
            {{ ageText }}
          </div>
          
          <div class="author-bio" v-if="authorBio">
            {{ authorBio }}
          </div>
        </div>
      </div>

      <!-- Книги автора -->
      <div class="author-books-section" v-if="authorBookCount > 0">
        <h2 class="section-title">Книги автора</h2>
        <FilterableBookList :fixed-params="{ authorId }" @book-click="goToBook" />
      </div>
      <div v-else-if="authorBookCount === 0" class="author-books-empty">
        У автора пока нет книг.
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { apiClient } from '../services/api'
import FilterableBookList from '../components/FilterableBookList.vue'

const route = useRoute()
const router = useRouter()
const authorId = route.params.id
const authorName = ref('')
const authorBio = ref('')
const authorPhoto = ref('')
const birthDateRaw = ref(null)
const deathDateRaw = ref(null)
const authorBookCount = ref(0)
const breadcrumbPath = computed(() => route.query.from || 'authors')

const formatDateFull = (dateStr) => {
  if (!dateStr) return null
  const date = new Date(dateStr)
  if (isNaN(date)) return dateStr
  const months = [
    'января', 'февраля', 'марта', 'апреля', 'мая', 'июня',
    'июля', 'августа', 'сентября', 'октября', 'ноября', 'декабря'
  ]
  return `${date.getDate()} ${months[date.getMonth()]} ${date.getFullYear()}`
}

const formattedDates = computed(() => {
  const birth = birthDateRaw.value
  const death = deathDateRaw.value
  
  const birthFull = formatDateFull(birth)
  const deathFull = formatDateFull(death)
  if (birthFull && deathFull) return `${birthFull} — ${deathFull}`
  if (birthFull) return `род. ${birthFull}`
  if (deathFull) return `ум. ${deathFull}`
})

const ageText = computed(() => {
  if (!birthDateRaw.value || !deathDateRaw.value) return null
  const birth = new Date(birthDateRaw.value)
  const death = new Date(deathDateRaw.value)
  if (isNaN(birth) || isNaN(death)) return null
  let age = death.getFullYear() - birth.getFullYear()
  const hasBirthdayPassed = (death.getMonth() > birth.getMonth()) || 
    (death.getMonth() === birth.getMonth() && death.getDate() >= birth.getDate())
  if (!hasBirthdayPassed) age--
  if (age > 0) return `Прожил ${age} ${declineYears(age)}`
  return null
})

function declineYears(years) {
  const lastDigit = years % 10
  const lastTwo = years % 100
  if (lastTwo >= 11 && lastTwo <= 14) return 'лет'
  if (lastDigit === 1) return 'год'
  if (lastDigit >= 2 && lastDigit <= 4) return 'года'
  return 'лет'
}

async function loadAuthor() {
  try {
    const data = await apiClient.get(`/authors/${authorId}`)
    authorName.value = `${data.firstName} ${data.lastName} ${data.middleName || ''}`.trim()
    authorBio.value = data.biography || ''
    authorPhoto.value = data.photoUrl || ''
    birthDateRaw.value = data.birthDate || null
    deathDateRaw.value = data.deathDate || null
    authorBookCount.value = data.bookCount || 0
  } catch (err) {
    console.error(err)
    authorName.value = 'Автор'
  }
}

const handleImageError = (e) => {
  e.target.src = '/images/cover.png'
}

const goToBook = (bookId) => {
  router.push({
    path: `/books/${bookId}`,
    query: {
      from: 'author',
      authorId: authorId, 
      authorName: authorName.value 
    }
  })
  console.log(bookId, authorId, authorName.value)
}

onMounted(() => {
    loadAuthor()
})
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;600;700&display=swap');

.author-detail-page {
  font-family: "Rubik", sans-serif;
  padding: 50px 50px 0;
  background: #f8f9fa;
  min-height: 100vh;
}
.author-detail-content {
  padding: 0 100px 50px;
}
.breadcrumb-nav {
  display: flex;
  gap: 8px;
  align-items: center;
  margin-bottom: 30px;
}
.breadcrumb-item {
  font-size: 12px;
  color: #902923;
  text-decoration: none;
}
.breadcrumb-item.current {
  color: #73706C;
}
.separator {
  color: #902923;
}
.author-header {
  font-family: 'Manrope', sans-serif;
  display: flex;
  gap: 40px;
  margin: 50px 0;
  background: white;
  border-radius: 30px;
  padding: 40px;
}
.author-photo-large {
  flex: 0 0 300px;
  height: 100%;
  border-radius: 20px;
  overflow: hidden;
  background: #e0e0e0;
}
.author-photo-large img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  aspect-ratio: 3/4;
}
.author-info {
  flex: 1;
}
.author-main-title {
  font-family: 'Manrope', sans-serif;
  font-weight: 600;
  font-size: 24px;
  color: var(--eerie-black, #1B1B1B);
}
.author-title {
  font-size: 32px;
  font-weight: 600;
  margin-bottom: 20px;
}
.author-dates {
  font-size: 18px;
  color: #902923;
  font-weight: 500;
  margin-bottom: 12px;
  letter-spacing: 0.3px;
}
.author-age {
  font-size: 14px;
  color: #73706C;
  margin-bottom: 20px;
  font-style: italic;
}
.author-bio {
  font-size: 14px;
  line-height: 1.5;
  color: #1B1B1B;
}
.section-title {
  font-size: 24px;
  font-weight: 500;
  margin-bottom: 30px;
}
.author-books-section {
  border-radius: 30px;
  padding: 30px;
}
@media (max-width: 992px) {
  .author-detail-content {
    padding: 0 24px;
  }
  .author-header {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }
}
</style>