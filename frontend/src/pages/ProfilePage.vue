<template>
  <div class="profile-page">
    <h1>Личный кабинет</h1>
    <p>Добро пожаловать!</p>
    <button @click="logout">Выйти</button>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { authService } from '../services/authService'

const router = useRouter()
function logout() {
  authService.logout()
  router.push('/')
}
</script>