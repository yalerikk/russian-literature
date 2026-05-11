<template>
  <div class="favorites-page">
    <div class="favorites-content">
      <div class="breadcrumb-nav">
        <span class="breadcrumb-item">Избранное</span>
      </div>
  
      <h1 class="page-title">Избранные книги</h1>
  
      <FilterableBookList 
        :fetch-url="'/users/me/books/favorites'" 
        @book-click="goToBook"
      />
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import FilterableBookList from '../components/FilterableBookList.vue'

const router = useRouter()
const goToBook = (bookId) => {
  router.push({
    path: `/books/${bookId}`,
    query: { from: 'favorites' }
  })
}
</script>

<style scoped>
.favorites-page { 
  font-family: 'Rubik', sans-serif;
  padding: 50px 50px 0;
  background: #f8f9fa;
}
.favorites-content {
  padding: 0 100px 50px;
}
.breadcrumb-nav { 
  margin-bottom: 20px; 
}
.breadcrumb-item { 
  font-size: 12px; 
  color: #902923; 
}
.page-title { 
  font-size: 28px; 
  margin-bottom: 30px; 
}
</style>