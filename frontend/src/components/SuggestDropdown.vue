<template>
  <div class="suggest-dropdown" v-if="visible && limitedSuggestions.length">
    <div
      v-for="item in limitedSuggestions"
      :key="item.type + '-' + item.id"
      class="suggest-item"
      @click="goTo(item)"
    >
      <div class="suggest-icon">
        <svg v-if="item.type === 'BOOK'" width="36" height="36" viewBox="0 0 36 36" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M12 9H25.5V12H12V9Z" fill="black" />
          <path d="M30 3H9C6.525 3 4.5 5.025 4.5 7.5V28.5C4.5 30.975 6.525 33 9 33H31.5V30H9C8.175 30 7.5 29.325 7.5 28.5C7.5 27.675 8.175 27 9 27H30C30.825 27 31.5 26.325 31.5 25.5V4.5C31.5 3.675 30.825 3 30 3ZM21 24H9C8.475 24 7.965 24.105 7.5 24.27V7.5C7.5 6.675 8.175 6 9 6H28.5V24H21Z" fill="black" />
        </svg>
        <svg v-else width="36" height="36" viewBox="0 0 36 36" fill="none">
          <path d="M8.99925 30.7499C13.3971 30.7567 17.7165 29.5862 21.5093 27.3599C22.008 27.0674 21.9667 26.3489 21.4485 26.0917L18.75 24.7499C21.2272 24.7499 26.25 23.7472 26.25 23.7472C31.6102 18.5362 34.335 11.0197 33.636 3.6157C33.6059 3.29367 33.4643 2.99227 33.2356 2.76358C33.0069 2.53488 32.7055 2.39326 32.3835 2.3632C28.5182 1.99317 24.6198 2.53228 21 3.93745C21 3.93745 19.761 6.85645 19.5 8.24995L17.982 6.1372C17.8745 5.98534 17.7134 5.87992 17.5312 5.8422C17.349 5.80448 17.1592 5.83727 17.0002 5.93395C9.948 10.2652 5.25 17.5199 5.25 27.3749" stroke="black" stroke-width="2.25" stroke-linecap="round" stroke-linejoin="round" />
          <path d="M2.25 33.75C6 22.875 18 13.5 27.75 8.25" stroke="black" stroke-width="2.25" stroke-linecap="round" stroke-linejoin="round" />
        </svg>
      </div>
      <div class="suggest-info">
        <div class="suggest-title">{{ item.title }}</div>
        <div class="suggest-subtitle">{{ item.subtitle }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { apiClient } from '../services/api'

const props = defineProps({
  query: { type: String, default: '' },
  visible: { type: Boolean, default: false }
})

const route = useRoute()
const router = useRouter()
const suggestions = ref([])
let debounceTimer = null
let abortController = null
const query = computed(() => route.query.q || '')

const MAX_ITEMS = 10

const limitedSuggestions = computed(() => {
  return suggestions.value.slice(0, MAX_ITEMS)
})

async function fetchSuggestions(searchQuery) {
  if (!searchQuery || searchQuery.trim().length === 0) {
    suggestions.value = []
    return
  }
  if (abortController) abortController.abort()
  abortController = new AbortController()
  try {
    const data = await apiClient.get(`/api/search/suggest?query=${encodeURIComponent(searchQuery)}`, {
      signal: abortController.signal
    })
    suggestions.value = data || []
  } catch (err) {
    if (err.name !== 'AbortError') console.error(err)
  }
}

function goTo(item) {
  if (item.type === 'BOOK') router.push({ path: `/books/${item.id}`, query: { from: 'search' } })
  else if (item.type === 'AUTHOR') router.push({ path: `/authors/${item.id}`, query: { from: 'search' } })
}

watch(() => props.query, (newQuery) => {
  if (debounceTimer) clearTimeout(debounceTimer)
  debounceTimer = setTimeout(() => fetchSuggestions(newQuery), 300)
})

onUnmounted(() => {
  if (debounceTimer) clearTimeout(debounceTimer)
  if (abortController) abortController.abort()
})
</script>

<style scoped>
.suggest-dropdown {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  width: 100%; 
  background: white;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0,0,0,0.12);
  margin-top: 8px;
  z-index: 1000;
  max-height: 400px;
  overflow-y: auto;
}
.suggest-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  cursor: pointer;
  transition: background 0.2s;
  border-bottom: 1px solid #F0F0F0;
}
.suggest-item:hover {
  background: #F8F8F8;
}
.suggest-icon {
  flex-shrink: 0;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.suggest-info {
  flex: 1;
}
.suggest-title {
  font-family: 'Rubik', sans-serif;
  font-weight: 500;
  font-size: 14px;
  color: #1B1B1B;
}
.suggest-subtitle {
  font-family: 'Rubik', sans-serif;
  font-size: 12px;
  color: #73706C;
}
</style>