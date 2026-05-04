<template>
  <div class="authors-grid-container">
    <div v-if="loading" class="loading-state">
      <div class="spinner"></div>
        <p>Загружаем авторов...</p>
      </div>
    <div v-else-if="error" class="error-state">
      <p>{{ error }}</p>
      <button @click="retry" class="btn-retry">Повторить</button>
    </div>
    <div v-else-if="authors.length === 0" class="empty-state">
      <div class="empty-icon">👤</div>
      <h3>Авторов пока нет</h3>
      <p>Добавьте первого автора</p>
    </div>
    <div v-else class="authors-grid-wrapper">
      <div class="authors-grid">
        <AuthorCard
          v-for="author in authors"
          :key="author.id"
          :author="author"
          @click="viewAuthor(author.id)"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import AuthorCard from './AuthorCard.vue'

defineProps({
  authors: {
    type: Array,
    required: true,
    default: () => []
  },
  loading: { type: Boolean, default: false },
  error: { type: String, default: '' }
})

const emit = defineEmits(['retry', 'author-click'])

const retry = () => emit('retry')
const viewAuthor = (id) => emit('author-click', id)
</script>

<style scoped>
.authors-grid-wrapper {
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
  padding: 0 50px 0;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}

.authors-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 20px;
  row-gap: 24px;
  margin: 0 auto;
  border-radius: 16px;
  padding: 20px 30px;
  background: white;
  width: 100%;
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
  .authors-grid {
    grid-template-columns: repeat(2, 1fr);
    padding: 16px 20px;
  }
}

@media (max-width: 768px) {
  .authors-grid {
    gap: 16px;
  }
}

@media (max-width: 576px) {
  .authors-grid {
    grid-template-columns: 1fr;
    max-width: 300px;
    margin: 0 auto;
  }
}
</style>