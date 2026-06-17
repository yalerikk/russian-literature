<template>
  <div class="filters-container">
    <!-- Жанры (множественный выбор) -->
    <div class="filter-block">
      <div class="filter-header">Жанры</div>
      <div class="filter-list">
        <div class="filter-item" :class="{ active: selectedGenreIds.length === 0 }" @click="resetGenres">
            <div class="filter-icon">
                <svg v-if="selectedGenreIds.length === 0" width="16" height="16" viewBox="0 0 16 16" fill="none">
                    <path d="M6.36646 10.1L12.0165 4.45C12.1498 4.31667 12.3054 4.25 12.4831 4.25C12.6609 4.25 12.8165 4.31667 12.9498 4.45C13.0831 4.58333 13.1498 4.74178 13.1498 4.92533C13.1498 5.10889 13.0831 5.26711 12.9498 5.4L6.83313 11.5333C6.69979 11.6667 6.54424 11.7333 6.36646 11.7333C6.18868 11.7333 6.03313 11.6667 5.8998 11.5333L3.03313 8.66667C2.89979 8.53333 2.83579 8.37511 2.84113 8.192C2.84646 8.00889 2.91602 7.85044 3.04979 7.71667C3.18357 7.58289 3.34202 7.51622 3.52513 7.51667C3.70824 7.51711 3.86646 7.58378 3.99979 7.71667L6.36646 10.1Z" fill="#902923" />
                </svg>
            </div>
          <span>Все</span>
        </div>
        <div
          v-for="genre in genres"
          :key="genre.id"
          class="filter-item"
          :class="{ active: selectedGenreIds.includes(genre.id) }"
          @click="toggleGenre(genre.id)"
        >
            <div class="filter-icon">
                <svg v-if="selectedGenreIds.includes(genre.id)" width="16" height="16" viewBox="0 0 16 16" fill="none">
                    <path d="M6.36646 10.1L12.0165 4.45C12.1498 4.31667 12.3054 4.25 12.4831 4.25C12.6609 4.25 12.8165 4.31667 12.9498 4.45C13.0831 4.58333 13.1498 4.74178 13.1498 4.92533C13.1498 5.10889 13.0831 5.26711 12.9498 5.4L6.83313 11.5333C6.69979 11.6667 6.54424 11.7333 6.36646 11.7333C6.18868 11.7333 6.03313 11.6667 5.8998 11.5333L3.03313 8.66667C2.89979 8.53333 2.83579 8.37511 2.84113 8.192C2.84646 8.00889 2.91602 7.85044 3.04979 7.71667C3.18357 7.58289 3.34202 7.51622 3.52513 7.51667C3.70824 7.51711 3.86646 7.58378 3.99979 7.71667L6.36646 10.1Z" fill="#902923" />
                </svg>
            </div>
          <span>{{ genre.name }}</span>
        </div>
      </div>
    </div>

    <!-- Класс -->
    <div class="filter-block" v-if="!isFixedGrade">
      <div class="filter-header">Класс</div>
      <div class="filter-list">
        <div class="filter-item" :class="{ active: selectedGrade === null }" @click="!isFixedGrade && selectGrade(null)">
            <div class="filter-icon">
                <svg v-if="selectedGrade === null" width="16" height="16" viewBox="0 0 16 16" fill="none">
                    <path d="M6.36646 10.1L12.0165 4.45C12.1498 4.31667 12.3054 4.25 12.4831 4.25C12.6609 4.25 12.8165 4.31667 12.9498 4.45C13.0831 4.58333 13.1498 4.74178 13.1498 4.92533C13.1498 5.10889 13.0831 5.26711 12.9498 5.4L6.83313 11.5333C6.69979 11.6667 6.54424 11.7333 6.36646 11.7333C6.18868 11.7333 6.03313 11.6667 5.8998 11.5333L3.03313 8.66667C2.89979 8.53333 2.83579 8.37511 2.84113 8.192C2.84646 8.00889 2.91602 7.85044 3.04979 7.71667C3.18357 7.58289 3.34202 7.51622 3.52513 7.51667C3.70824 7.51711 3.86646 7.58378 3.99979 7.71667L6.36646 10.1Z" fill="#902923" />
                </svg>
            </div>
          <span>Все</span>
        </div>
        <div
          v-for="grade in gradeOptions"
          :key="grade.id"
          class="filter-item"
          :class="{ active: selectedGrade === grade.id }"
          @click="!isFixedGrade && selectGrade(grade.id)"
        >
            <div class="filter-icon">
                <svg v-if="selectedGrade === grade.id" width="16" height="16" viewBox="0 0 16 16" fill="none">
                    <path d="M6.36646 10.1L12.0165 4.45C12.1498 4.31667 12.3054 4.25 12.4831 4.25C12.6609 4.25 12.8165 4.31667 12.9498 4.45C13.0831 4.58333 13.1498 4.74178 13.1498 4.92533C13.1498 5.10889 13.0831 5.26711 12.9498 5.4L6.83313 11.5333C6.69979 11.6667 6.54424 11.7333 6.36646 11.7333C6.18868 11.7333 6.03313 11.6667 5.8998 11.5333L3.03313 8.66667C2.89979 8.53333 2.83579 8.37511 2.84113 8.192C2.84646 8.00889 2.91602 7.85044 3.04979 7.71667C3.18357 7.58289 3.34202 7.51622 3.52513 7.51667C3.70824 7.51711 3.86646 7.58378 3.99979 7.71667L6.36646 10.1Z" fill="#902923" />
                </svg>
            </div>
          <span>{{ grade.name }}</span>
        </div>
      </div>
    </div>

    <!-- Уровень -->
    <div class="filter-block" v-if="!isFixedLevel">
      <div class="filter-header">Уровень</div>
      <div class="filter-list">
        <div class="filter-item" :class="{ active: selectedLevel === null }" @click="!isFixedLevel && selectLevel(null)">
            <div class="filter-icon">
                <svg v-if="selectedLevel === null" width="16" height="16" viewBox="0 0 16 16" fill="none">
                    <path d="M6.36646 10.1L12.0165 4.45C12.1498 4.31667 12.3054 4.25 12.4831 4.25C12.6609 4.25 12.8165 4.31667 12.9498 4.45C13.0831 4.58333 13.1498 4.74178 13.1498 4.92533C13.1498 5.10889 13.0831 5.26711 12.9498 5.4L6.83313 11.5333C6.69979 11.6667 6.54424 11.7333 6.36646 11.7333C6.18868 11.7333 6.03313 11.6667 5.8998 11.5333L3.03313 8.66667C2.89979 8.53333 2.83579 8.37511 2.84113 8.192C2.84646 8.00889 2.91602 7.85044 3.04979 7.71667C3.18357 7.58289 3.34202 7.51622 3.52513 7.51667C3.70824 7.51711 3.86646 7.58378 3.99979 7.71667L6.36646 10.1Z" fill="#902923" />
                </svg>
            </div>
          <span>Все</span>
        </div>
        <div
          v-for="level in levelOptions"
          :key="level.id"
          class="filter-item"
          :class="{ active: selectedLevel === level.id }"
          @click="!isFixedLevel && selectLevel(level.id)"
        >
            <div class="filter-icon">
                <svg v-if="selectedLevel === level.id" width="16" height="16" viewBox="0 0 16 16" fill="none">
                    <path d="M6.36646 10.1L12.0165 4.45C12.1498 4.31667 12.3054 4.25 12.4831 4.25C12.6609 4.25 12.8165 4.31667 12.9498 4.45C13.0831 4.58333 13.1498 4.74178 13.1498 4.92533C13.1498 5.10889 13.0831 5.26711 12.9498 5.4L6.83313 11.5333C6.69979 11.6667 6.54424 11.7333 6.36646 11.7333C6.18868 11.7333 6.03313 11.6667 5.8998 11.5333L3.03313 8.66667C2.89979 8.53333 2.83579 8.37511 2.84113 8.192C2.84646 8.00889 2.91602 7.85044 3.04979 7.71667C3.18357 7.58289 3.34202 7.51622 3.52513 7.51667C3.70824 7.51711 3.86646 7.58378 3.99979 7.71667L6.36646 10.1Z" fill="#902923" />
                </svg>
            </div>
          <span>{{ level.name }}</span>
        </div>
      </div>
    </div>

    <!-- Литература (короткие названия) -->
    <div class="filter-block" v-if="!isFixedLiterature">
      <div class="filter-header">Литература</div>
      <div class="filter-list">
        <div class="filter-item" :class="{ active: selectedLiterature === null }" @click="!isFixedLiterature && selectLiterature(null)">
            <div class="filter-icon">
                <svg v-if="selectedLiterature === null" width="16" height="16" viewBox="0 0 16 16" fill="none">
                    <path d="M6.36646 10.1L12.0165 4.45C12.1498 4.31667 12.3054 4.25 12.4831 4.25C12.6609 4.25 12.8165 4.31667 12.9498 4.45C13.0831 4.58333 13.1498 4.74178 13.1498 4.92533C13.1498 5.10889 13.0831 5.26711 12.9498 5.4L6.83313 11.5333C6.69979 11.6667 6.54424 11.7333 6.36646 11.7333C6.18868 11.7333 6.03313 11.6667 5.8998 11.5333L3.03313 8.66667C2.89979 8.53333 2.83579 8.37511 2.84113 8.192C2.84646 8.00889 2.91602 7.85044 3.04979 7.71667C3.18357 7.58289 3.34202 7.51622 3.52513 7.51667C3.70824 7.51711 3.86646 7.58378 3.99979 7.71667L6.36646 10.1Z" fill="#902923" />
                </svg>
            </div>
          <span>Все</span>
        </div>
        <div
          v-for="lit in literatureOptions"
          :key="lit.id"
          class="filter-item"
          :class="{ active: selectedLiterature === lit.id }"
          @click="!isFixedLiterature && selectLiterature(lit.id)"
        >
            <div class="filter-icon">
                <svg v-if="selectedLiterature === lit.id" width="16" height="16" viewBox="0 0 16 16" fill="none">
                    <path d="M6.36646 10.1L12.0165 4.45C12.1498 4.31667 12.3054 4.25 12.4831 4.25C12.6609 4.25 12.8165 4.31667 12.9498 4.45C13.0831 4.58333 13.1498 4.74178 13.1498 4.92533C13.1498 5.10889 13.0831 5.26711 12.9498 5.4L6.83313 11.5333C6.69979 11.6667 6.54424 11.7333 6.36646 11.7333C6.18868 11.7333 6.03313 11.6667 5.8998 11.5333L3.03313 8.66667C2.89979 8.53333 2.83579 8.37511 2.84113 8.192C2.84646 8.00889 2.91602 7.85044 3.04979 7.71667C3.18357 7.58289 3.34202 7.51622 3.52513 7.51667C3.70824 7.51711 3.86646 7.58378 3.99979 7.71667L6.36646 10.1Z" fill="#902923" />
                </svg>
            </div>
          <span>{{ lit.shortName }}</span>
        </div>
      </div>
    </div>

    <!-- Тип чтения (короткие названия) -->
    <div class="filter-block" v-if="!isFixedReadingType">
      <div class="filter-header">Тип</div>
      <div class="filter-list">
        <div class="filter-item" :class="{ active: selectedReadingType === null }" @click="!isFixedReadingType && selectReadingType(null)">
            <div class="filter-icon">
                <svg v-if="selectedReadingType === null" width="16" height="16" viewBox="0 0 16 16" fill="none">
                    <path d="M6.36646 10.1L12.0165 4.45C12.1498 4.31667 12.3054 4.25 12.4831 4.25C12.6609 4.25 12.8165 4.31667 12.9498 4.45C13.0831 4.58333 13.1498 4.74178 13.1498 4.92533C13.1498 5.10889 13.0831 5.26711 12.9498 5.4L6.83313 11.5333C6.69979 11.6667 6.54424 11.7333 6.36646 11.7333C6.18868 11.7333 6.03313 11.6667 5.8998 11.5333L3.03313 8.66667C2.89979 8.53333 2.83579 8.37511 2.84113 8.192C2.84646 8.00889 2.91602 7.85044 3.04979 7.71667C3.18357 7.58289 3.34202 7.51622 3.52513 7.51667C3.70824 7.51711 3.86646 7.58378 3.99979 7.71667L6.36646 10.1Z" fill="#902923" />
                </svg>
            </div>
          <span>Все</span>
        </div>
        <div
          v-for="rt in readingTypeOptions"
          :key="rt.id"
          class="filter-item"
          :class="{ active: selectedReadingType === rt.id }"
          @click="!isFixedReadingType && selectReadingType(rt.id)"
        >
            <div class="filter-icon">
                <svg v-if="selectedReadingType === rt.id" width="16" height="16" viewBox="0 0 16 16" fill="none">
                    <path d="M6.36646 10.1L12.0165 4.45C12.1498 4.31667 12.3054 4.25 12.4831 4.25C12.6609 4.25 12.8165 4.31667 12.9498 4.45C13.0831 4.58333 13.1498 4.74178 13.1498 4.92533C13.1498 5.10889 13.0831 5.26711 12.9498 5.4L6.83313 11.5333C6.69979 11.6667 6.54424 11.7333 6.36646 11.7333C6.18868 11.7333 6.03313 11.6667 5.8998 11.5333L3.03313 8.66667C2.89979 8.53333 2.83579 8.37511 2.84113 8.192C2.84646 8.00889 2.91602 7.85044 3.04979 7.71667C3.18357 7.58289 3.34202 7.51622 3.52513 7.51667C3.70824 7.51711 3.86646 7.58378 3.99979 7.71667L6.36646 10.1Z" fill="#902923" />
                </svg>
            </div>
          <span>{{ rt.shortName }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { apiClient } from '../services/api'

const emit = defineEmits(['update:filters'])
const props = defineProps({
  fixedTags: { type: Array, default: () => [] }
})

const genres = ref([])
const gradeOptions = ref([])
const levelOptions = ref([])
const literatureOptions = ref([]) 
const readingTypeOptions = ref([]) 

const selectedGenreIds = ref([])
const selectedGrade = ref(null)
const selectedLevel = ref(null)
const selectedLiterature = ref(null)
const selectedReadingType = ref(null)

const isFixedGrade = computed(() => props.fixedTags.some(tag => tag.type === 'GRADE'))
const isFixedLevel = computed(() => props.fixedTags.some(tag => tag.type === 'LEVEL'))
const isFixedLiterature = computed(() => props.fixedTags.some(tag => tag.type === 'CATEGORY'))
const isFixedReadingType = computed(() => props.fixedTags.some(tag => tag.type === 'READING_TYPE'))

async function loadFiltersData() {
  console.log('BooksFilter: загрузка данных')
  try {
    const [genresData, gradesData, levelsData, literaturesData, readingTypesData] = await Promise.all([
      apiClient.get('/genres'),
      apiClient.get('/tags/by-type?type=GRADE'),
      apiClient.get('/tags/by-type?type=LEVEL'),
      apiClient.get('/tags/by-type?type=CATEGORY'),
      apiClient.get('/tags/by-type?type=READING_TYPE')
    ])
    genres.value = genresData
    gradeOptions.value = gradesData
    levelOptions.value = levelsData

    // Укорачиваем названия для литературы
    literatureOptions.value = literaturesData.map(item => ({
      ...item,
      shortName: item.name.replace(' литература', '')
    }))

    // Укорачиваем названия для типа чтения (убираем " чтение")
    readingTypeOptions.value = readingTypesData.map(item => ({
      ...item,
      shortName: item.name.replace(' чтение', '')
    }))

    props.fixedTags.forEach(tag => {
      switch (tag.type) {
        case 'GRADE':
          const grade = gradeOptions.value.find(g => g.id === tag.id)
          if (grade) selectedGrade.value = grade.id
          break
        case 'LEVEL':
          const level = levelOptions.value.find(l => l.id === tag.id)
          if (level) selectedLevel.value = level.id
          break
        case 'CATEGORY':
          const lit = literatureOptions.value.find(l => l.id === tag.id)
          if (lit) selectedLiterature.value = lit.id
          break
        case 'READING_TYPE':
          const rt = readingTypeOptions.value.find(r => r.id === tag.id)
          if (rt) selectedReadingType.value = rt.id
          break
      }
    })

    console.log('BooksFilter: данные загружены', { genres: genres.value, gradeOptions: gradeOptions.value })
    emitFilter()
  } catch (err) {
    console.error('BooksFilter: ошибка загрузки данных', err)
  }
}

function toggleGenre(id) {
  const index = selectedGenreIds.value.indexOf(id)
  if (index === -1) selectedGenreIds.value.push(id)
  else selectedGenreIds.value.splice(index, 1)
  emitFilter()
}

function resetGenres() {
  selectedGenreIds.value = []
  emitFilter()
}

function selectGrade(id) {
  selectedGrade.value = id
  emitFilter()
}
function selectLevel(id) {
  selectedLevel.value = id
  emitFilter()
}
function selectLiterature(id) {
  selectedLiterature.value = id
  emitFilter()
}
function selectReadingType(id) {
  selectedReadingType.value = id
  emitFilter()
}

function emitFilter() {
  const gradeName = gradeOptions.value.find(g => g.id === selectedGrade.value)?.name || null
  const levelName = levelOptions.value.find(l => l.id === selectedLevel.value)?.name || null
  const literatureName = literatureOptions.value.find(l => l.id === selectedLiterature.value)?.name || null
  const readingTypeName = readingTypeOptions.value.find(r => r.id === selectedReadingType.value)?.name || null

  const filters = {
    genreIds: selectedGenreIds.value,
    grade: !isFixedGrade.value ? gradeName : null,
    level: !isFixedLevel.value ? levelName : null,
    literature: !isFixedLiterature.value ? literatureName : null,
    readingType: !isFixedReadingType.value ? readingTypeName : null
  }
  console.log('BooksFilter: emit filters', filters)
  emit('update:filters', filters)
}

onMounted(() => {
  loadFiltersData()
})
</script>

<style scoped>
.filters-container {
  font-family: 'Rubik', sans-serif;
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.filter-block {
  background: white;
  border-radius: 12px;
  padding: 0 16px;
  width: 200px;
  display: flex;
  flex-direction: column;
  gap: 0;
}
.filter-header {
  padding: 12px 0;
  font-weight: 500;
  font-size: 16px;
  text-align: left;
}
.filter-list {
  display: flex;
  flex-direction: column;
}
.filter-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 0 8px 16px;
  border-bottom: 1px solid #F3F3F4;
  cursor: pointer;
  font-size: 14px;
  font-weight: 400;
}
.filter-item.active {
  font-weight: 500;
}
.filter-icon,
.filter-item svg {
  width: 16px;
  height: 16px;
}
.filter-icon {
  display: flex; 
  justify-content: center; 
  align-items: center;
}
</style>