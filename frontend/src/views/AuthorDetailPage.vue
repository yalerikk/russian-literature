<template>
  <div class="author-detail-page">
    <div class="container">
      <h1>Страница автора (в разработке)</h1>
      <p>ID автора: {{ authorId }}</p>
      <router-link to="/authors">← Назад к списку авторов</router-link>
    </div>
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router'
const route = useRoute()
const authorId = route.params.id
</script>

<style scoped>
.author-detail-page {
  padding: 50px;
  text-align: center;
}
</style>