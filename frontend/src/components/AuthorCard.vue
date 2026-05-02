<template>
  <div 
    class="author-card"
    :class="{ 'clickable': clickable }"
    @click="handleClick"
  >
    <!-- Фото автора -->
    <div class="author-photo">
      <img 
        :src="getPhotoUrl(author.photoUrl)" 
        :alt="getFullName(author)"
        @error="handleImageError"
      />
    </div>
    
    <!-- ФИО автора -->
    <div class="author-name">
      {{ formatFullName(author) }}
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'

const props = defineProps({
  author: {
    type: Object,
    required: true
  },
  clickable: {
    type: Boolean,
    default: true
  }
})

const emit = defineEmits(['click'])

// Форматирование ФИО в формате "Ф. И. О."
const formatFullName = (author) => {
  const lastNameInitial = author.lastName
  const firstNameInitial = author.firstName.charAt(0).toUpperCase() + '.'
  
  if (author.middleName && author.middleName.trim()) {
    const middleNameInitial = author.middleName.charAt(0).toUpperCase() + '.'
    return `${firstNameInitial} ${middleNameInitial} ${lastNameInitial}`
  }
  
  return `${lastNameInitial} ${firstNameInitial}`
}

// Полное ФИО для alt тега
const getFullName = (author) => {
  return `${author.lastName} ${author.firstName} ${author.middleName || ''}`
}

// Обработка URL фотографии
const getPhotoUrl = (photoUrl) => {
  if (!photoUrl) {
    return '/images/authors/default-author.jpg'
  }
  
  // Если URL начинается с http(s), используем как есть
  if (photoUrl.startsWith('http://') || photoUrl.startsWith('https://')) {
    return photoUrl
  }
  
  // Иначе предполагаем, что это относительный путь внутри проекта
  return photoUrl
}

// Обработка ошибок загрузки изображений
const handleImageError = (event) => {
  event.target.src = '/images/authors/default-author.jpg'
  console.warn(`Не удалось загрузить фото автора: ${props.author.photoUrl}`)
}

const handleClick = () => {
  if (props.clickable) {
    emit('click', props.author.id)
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;600;700&display=swap');

.author-card {
  background: var(--card-background, white);
  border-radius: 12px;
  padding: 8px;
  display: flex;
  align-items: flex-start;
  justify-content: center;
  flex-direction: column;
  gap: 14px;
  transition: all 0.3s ease;
  min-height: 252px; /* Фиксированная минимальная высота */
  width: 176px;
  cursor: default;
}

.author-card.clickable {
  cursor: pointer;
}

.author-card.clickable:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

/* Фото автора - фиксированная высота */
.author-photo {
  width: 100%;
  height: 200px; /* Фиксированная высота для фото */
  border-radius: 12px;
  overflow: hidden;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  flex-shrink: 0; /* Запрещаем сжатие */
}

.author-photo img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.author-card.clickable:hover .author-photo img {
  transform: scale(1.05);
}

/* ФИО автора */
.author-name {
  font-family: 'Manrope', sans-serif;
  font-weight: 600;
  font-size: 16px;
  line-height: 1.4;
  height: 100%;
  color: var(--eerie-black, #1B1B1B);
  text-align: left; /* Текст прижат к левому краю */
  flex-grow: 1;
  display: flex;
  align-items: flex-start; /* Выравнивание по верху */
  justify-content: flex-start; /* Выравнивание по левому краю */
  overflow-wrap: break-word;
  word-break: break-word;
}

/* Адаптивность */
@media (max-width: 992px) {
  .author-photo {
    height: 180px;
  }
  
  .author-name {
    font-size: 15px;
  }
}

@media (max-width: 768px) {
  .author-photo {
    height: 160px;
  }
  
  .author-name {
    font-size: 14px;
  }
}

@media (max-width: 576px) {
  .author-photo {
    height: 200px;
  }
  
  .author-name {
    font-size: 16px;
  }
}
</style>