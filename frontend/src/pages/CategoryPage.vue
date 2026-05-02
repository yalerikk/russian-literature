<template>
  <div class="category-page">
    <div class="category-content">
      <Breadcrumbs :items="breadcrumbs" />
      <h1 class="category-title">{{ categoryName }}</h1>
      <FilterableBookList :fixed-params="{ categoryCode }" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import Breadcrumbs from '../components/Breadcrumbs.vue'
import FilterableBookList from '../components/FilterableBookList.vue'
import { apiClient } from '../services/api'

const route = useRoute()
const categoryCode = route.params.code
const categoryName = ref('')

onMounted(async () => {
  try {
    const data = await apiClient.get(`/api/catalog/categories/code/${categoryCode}`)
    categoryName.value = data.name
  } catch (err) {
    console.error(err)
    categoryName.value = categoryName
  }
})

const breadcrumbs = computed(() => [
  { label: 'Каталог', link: '/catalog' },
  { label: categoryName.value }
])
</script>

<style scoped>
.category-page {
  font-family: 'Rubik', sans-serif;
  padding: 50px 50px 0;
  background: #f8f9fa;
}

.category-content {
  padding: 0 100px 50px;
}

.category-title {
  font-size: 28px;
  margin: 20px 0 30px;
}

.category-layout {
  display: flex;
  gap: 40px;
}

.filters-sidebar {
  flex: 0 0 240px;
}

.books-section {
  flex: 1;
}

.books-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 40px;
}
</style>