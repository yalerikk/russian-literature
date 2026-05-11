<template>
  <div class="rating-stars" @mouseleave="resetHover">
    <button
      v-for="star in 5"
      :key="star"
      class="star-btn"
      @click="setRating(star)"
      @mouseenter="hoverRating = star"
      :class="{ active: (hoverRating || currentRating) >= star }"
    >
      <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
        <path
          :fill="(hoverRating || currentRating) >= star ? '#1B1B1B' : 'none'"
          d="M11.993 18.1801L5.821 21.4251L7 14.5521L2 9.68513L8.9 8.68513L11.986 2.43213L15.072 8.68513L21.972 9.68513L16.972 14.5521L18.151 21.4251L11.993 18.1801Z"
          stroke="#1B1B1B"
          stroke-width="1.7027"
          stroke-linecap="round"
          stroke-linejoin="round"
        />
      </svg>
    </button>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  rating: { type: Number, default: 0 },      // текущая оценка пользователя (0-5)
  bookId: { type: Number, required: true },
  isAuthenticated: { type: Boolean, default: false }
})
const emit = defineEmits(['rate', 'authRequired'])

const currentRating = ref(props.rating)
const hoverRating = ref(0)

const resetHover = () => { hoverRating.value = 0 }

const setRating = (star) => {
  if (!props.isAuthenticated) {
    emit('authRequired')
    return
  }
  if (currentRating.value === star) {
    // Если кликнули на ту же звезду – удаляем оценку (вызовем 0)
    emit('rate', 0)
    currentRating.value = 0
  } else {
    emit('rate', star)
    currentRating.value = star
  }
}

watch(() => props.rating, (newVal) => {
  currentRating.value = newVal
})
</script>

<style scoped>
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
</style>