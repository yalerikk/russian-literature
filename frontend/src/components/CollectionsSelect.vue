<template>
  <div class="collections-select" ref="selectRef">
    <!-- Хедер -->
    <div class="collections-header">
      <span class="header-title">Коллекции</span>
      <button class="close-btn" @click="$emit('close')">×</button>
    </div>
    
    <!-- Список коллекций -->
    <div class="collections-list">
      <div 
        v-for="collection in collections"
        :key="collection.id"
        class="collection-item"
        :class="{ selected: selectedCollection?.id === collection.id }"
        @click="selectCollection(collection)"
      >
        <svg 
          v-if="selectedCollection?.id === collection.id" 
          width="16" 
          height="16" 
          viewBox="0 0 16 16" 
          fill="none" 
          xmlns="http://www.w3.org/2000/svg"
        >
          <path d="M6.36646 10.1L12.0165 4.45C12.1498 4.31667 12.3054 4.25 12.4831 4.25C12.6609 4.25 12.8165 4.31667 12.9498 4.45C13.0831 4.58333 13.1498 4.74178 13.1498 4.92533C13.1498 5.10889 13.0831 5.26711 12.9498 5.4L6.83313 11.5333C6.69979 11.6667 6.54424 11.7333 6.36646 11.7333C6.18868 11.7333 6.03313 11.6667 5.8998 11.5333L3.03313 8.66667C2.89979 8.53333 2.83579 8.37511 2.84113 8.192C2.84646 8.00889 2.91602 7.85044 3.04979 7.71667C3.18357 7.58289 3.34202 7.51622 3.52513 7.51667C3.70824 7.51711 3.86646 7.58378 3.99979 7.71667L6.36646 10.1Z" fill="#902923" />
          <path d="M6.36646 10.1L12.0165 4.45C12.1498 4.31667 12.3054 4.25 12.4831 4.25C12.6609 4.25 12.8165 4.31667 12.9498 4.45C13.0831 4.58333 13.1498 4.74178 13.1498 4.92533C13.1498 5.10889 13.0831 5.26711 12.9498 5.4L6.83313 11.5333C6.69979 11.6667 6.54424 11.7333 6.36646 11.7333C6.18868 11.7333 6.03313 11.6667 5.8998 11.5333L3.03313 8.66667C2.89979 8.53333 2.83579 8.37511 2.84113 8.192C2.84646 8.00889 2.91602 7.85044 3.04979 7.71667C3.18357 7.58289 3.34202 7.51622 3.52513 7.51667C3.70824 7.51711 3.86646 7.58378 3.99979 7.71667L6.36646 10.1Z" fill="#902923" fill-opacity="0.2" />
        </svg>
        
        <svg 
          v-else
          width="16" 
          height="16" 
          viewBox="0 0 16 16" 
          fill="none" 
          xmlns="http://www.w3.org/2000/svg"
        >
          <path d="M6.36646 10.1L12.0165 4.45C12.1498 4.31667 12.3054 4.25 12.4831 4.25C12.6609 4.25 12.8165 4.31667 12.9498 4.45C13.0831 4.58333 13.1498 4.74178 13.1498 4.92533C13.1498 5.10889 13.0831 5.26711 12.9498 5.4L6.83313 11.5333C6.69979 11.6667 6.54424 11.7333 6.36646 11.7333C6.18868 11.7333 6.03313 11.6667 5.8998 11.5333L3.03313 8.66667C2.89979 8.53333 2.83579 8.37511 2.84113 8.192C2.84646 8.00889 2.91602 7.85044 3.04979 7.71667C3.18357 7.58289 3.34202 7.51622 3.52513 7.51667C3.70824 7.51711 3.86646 7.58378 3.99979 7.71667L6.36646 10.1Z" fill="white" />
        </svg>
        
        <span class="collection-name">{{ collection.name }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const props = defineProps({
  selectedCollection: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['select', 'close'])

const selectRef = ref(null)
const collections = ref([
  { id: 1, name: 'Избранное' },
  { id: 2, name: 'Прочитанные' },
  { id: 3, name: 'Отложенные' },
  { id: 4, name: 'Классика' },
  { id: 5, name: 'Современная литература' }
])

const selectCollection = (collection) => {
  emit('select', collection)
}

// Закрытие по клику вне компонента
const handleClickOutside = (event) => {
  if (selectRef.value && !selectRef.value.contains(event.target)) {
    emit('close')
  }
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})
</script>

<style scoped>
.collections-select {
  background: var(--white, #FFFFFF);
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  z-index: 1000;
}

.collections-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 10px;
  padding: 12px 16px;
  border-bottom: 1px solid var(--platinum, #F3F3F4);
}

.header-title {
  font-family: 'Rubik', sans-serif;
  font-weight: 500;
  font-size: 16px;
  line-height: 150%;
  color: var(--eerie-black, #1B1B1B);
}

.close-btn {
  background: transparent;
  border: none;
  font-size: 24px;
  color: var(--eerie-black);
  cursor: pointer;
  padding: 0;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  line-height: 1;
}

.collections-list {
  padding: 0;
  max-height: 300px;
  overflow-y: auto;
}

.collection-item {
  display: flex;
  align-items: center;
  gap: 8px;
  border-bottom: 1px solid var(--platinum, #F3F3F4);
  padding: 8px 0 8px 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.collection-item:last-child {
  border-bottom: none;
}

.collection-item:hover {
  background: var(--platinum, #F3F3F4);
}

.collection-item.selected {
  background: rgba(144, 41, 35, 0.1);
}

.collection-item.selected .collection-name {
  font-weight: 500;
}

.collection-name {
  font-family: 'Rubik', sans-serif;
  font-weight: 400;
  font-size: 14px;
  line-height: 150%;
  color: var(--eerie-black, #1B1B1B);
  transition: font-weight 0.3s;
}
</style>