<template>
  <div class="modal-overlay" @click.self="close">
    <div class="modal-container">
      <h2 class="modal-title">{{ isLoginMode ? 'Вход' : 'Регистрация' }}</h2>
      <LoginForm 
        v-if="isLoginMode" 
        @switch-to-register="isLoginMode = false"
        @success="onSuccess" 
      />
      <RegisterForm 
        v-else 
        @switch-to-login="isLoginMode = true"
        @success="onSuccess" 
      />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import LoginForm from './LoginForm.vue'
import RegisterForm from './RegisterForm.vue'

const emit = defineEmits(['close', 'success'])
const isLoginMode = ref(true) 

const close = () => emit('close')
const onSuccess = () => {
  emit('success')
  emit('close')
  setTimeout(() => window.location.reload(), 100)
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
}
.modal-container {
  background: white;
  border-radius: 16px;
  padding: 26px 40px;
  width: 380px;
  box-shadow: 4px 6px 16px rgba(0, 0, 0, 0.2);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 26px;
}
.modal-title {
  font-family: var(--font-family, 'Rubik', sans-serif);
  font-weight: 700;
  font-size: 32px;
  line-height: 150%;
  text-align: center;
  color: var(--eerie-black, #1b1b1b);
  margin: 0;
}
</style>