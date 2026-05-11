<template>
  <div class="collection-page">
    <div class="collection-content">
      <div class="breadcrumb-nav">
        <router-link to="/profile/my-books" class="breadcrumb-item">Мои книги</router-link>
        <span class="separator">/</span>
        <span class="breadcrumb-item current">{{ title }}</span>
      </div>

      <h1 class="page-title">{{ title }}</h1>

      <FilterableBookList 
        :fetch-url="`/users/me/books`"
        :fixed-params="{ status: statusCode }"
        @book-click="goToBook"
      />
    </div>
  </div>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'
import FilterableBookList from '../components/FilterableBookList.vue'

const route = useRoute()
const router = useRouter()

const statusCode = route.params.status.toUpperCase()
const titleMap = { wishlist: 'Хочу прочитать', reading: 'Читаю', read: 'Прочитано' }
const title = titleMap[route.params.status] || 'Коллекция'

const goToBook = (bookId) => {
  router.push({
    path: `/books/${bookId}`,
    query: { from: 'collection', status: statusCode.toLowerCase() }
  })
}
</script>

<style scoped>
.collection-page { 
  font-family: 'Rubik', sans-serif;
  padding: 50px 50px 0;
  background: #f8f9fa;
}
.collection-content {
  padding: 0 100px 50px;
}
.page-title {
  font-family: 'Manrope', sans-serif;
  font-weight: 600;
  font-size: 24px;
  color: #1B1B1B;
  margin-bottom: 30px;
}
.breadcrumb-nav {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
  margin-bottom: 20px;
}
.breadcrumb-item {
  font-family: 'Rubik', sans-serif;
  font-weight: 400;
  font-size: 12px;
  line-height: 150%;
  color: #902923;
  letter-spacing: 0.5px;
  text-decoration: none;
}
.breadcrumb-item.current {
  color: #73706C;
}
.separator {
  color: #902923;
  font-size: 12px;
}
.collection-title {
  font-size: 28px;
  margin: 20px 0 30px;
  font-weight: 500;
  color: #1B1B1B;
}
</style>