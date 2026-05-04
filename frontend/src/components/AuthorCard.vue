<template>
  <div class="author-card" @click="$emit('click')">
    <div class="author-photo">
      <img 
        :src="displayPhotoUrl"
        :alt="fullName"
        @error="handleImageError"
      />
    </div>
    <div class="author-info">
      <h3 class="author-name">{{ shortName }}</h3>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'

const props = defineProps({
  author: {
    type: Object,
    required: true
  }
})

const photoUrl = ref(props.author.photoUrl?.trim() || '')

const shortName = computed(() => {
  const firstNameInitial = props.author.firstName?.charAt(0).toUpperCase() + '.'
  let middleNameInitial = ''
  if (props.author.middleName && props.author.middleName.trim()) {
    middleNameInitial = props.author.middleName.charAt(0).toUpperCase() + '.'
  }
  const lastName = props.author.lastName || ''
  if (middleNameInitial) {
    return `${firstNameInitial} ${middleNameInitial} ${lastName}`
  }
  return `${firstNameInitial} ${lastName}`
})

const fullName = computed(() => {
  return `${props.author.lastName || ''} ${props.author.firstName || ''} ${props.author.middleName || ''}`.trim()
})

const displayPhotoUrl = computed(() => {
  if (photoUrl.value && (photoUrl.value.startsWith('http://') || photoUrl.value.startsWith('https://'))) {
    return photoUrl.value
  }
  return '/images/cover.png'
})

function handleImageError(event) {
  if (photoUrl.value !== '/images/cover.png') {
    photoUrl.value = '/images/cover.png'
  }
  event.stopPropagation()
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;600;700&display=swap');

.author-card {
  background: white;
  border-radius: 12px;
  padding: 8px;
  display: flex;
  flex-direction: column;
  gap: 14px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
  text-align: center;
  min-height: 252px;
}
.author-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0,0,0,0.12);
}
.author-photo {
  width: 100%;
  height: 200px;
  border-radius: 12px;
  overflow: hidden;
  flex-shrink: 0;
  background: #f0f0f0;
}
.author-photo img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}
.author-card:hover .author-photo img {
  transform: scale(1.05);
}
.author-name {
  font-family: 'Manrope', sans-serif;
  font-size: 14px;
  font-weight: 600;
  line-height: 1.4;
  height: 100%;
  text-align: left;
  margin: 0;
  color: #1B1B1B;
  flex-grow: 1;
  display: flex;
  align-items: flex-start; 
  justify-content: flex-start; 
  overflow-wrap: break-word;
  word-break: break-word;
}
</style>