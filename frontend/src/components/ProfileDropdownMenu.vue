<template>
  <div class="dropdown" @click.stop>
    <button class="dropdown-toggle" @click="toggleDropdown">
      <span class="auth-text">Профиль</span>
      <div class="dropdown-icon">
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
          <path d="M7 10L12 15L17 10" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
      </div>
    </button>
    <div v-if="isOpen" class="dropdown-menu" @click="closeDropdown">
      <router-link to="/profile/my-books" class="dropdown-item">Мои книги</router-link>
      <router-link to="/profile/favorites" class="dropdown-item">Избранное</router-link>
      <router-link to="/profile/edit" class="dropdown-item">Управление аккаунтом</router-link>

      <!-- Админские пункты -->
      <template v-if="isAdmin">
        <router-link to="/admin/authors" class="dropdown-item">Авторы</router-link>
        <router-link to="/admin/users" class="dropdown-item">Пользователи</router-link>
        <router-link to="/admin/genres" class="dropdown-item">Жанры</router-link>
        <router-link to="/admin/tags" class="dropdown-item">Теги</router-link>
        <router-link to="/admin/books" class="dropdown-item">Книги</router-link>
        <router-link to="/admin/categories" class="dropdown-item">Подборки</router-link>
        <!-- позже добавятся: Книги, Категории -->
      </template>

      <hr class="dropdown-divider" />
      <button @click="logout" class="dropdown-item logout">Выйти</button>
    </div>
    <ConfirmModal ref="confirmModal" />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useFavorites } from '../stores/favorites';
import { authService } from '../services/authService';
import ConfirmModal from '../components/ConfirmModal.vue'
import { useRouter } from 'vue-router';

const isOpen = ref(false)
const favoritesStore = useFavorites();
const router = useRouter();
const isAuthenticated = authService.isAuthenticated
const confirmModal = ref(null)

const closeDropdown = () => { isOpen.value = false }

const isAdmin = computed(() => {
  const user = authService.getUserFromToken();
  return user?.role === 'ROLE_ADMIN';
});

const toggleDropdown = () => {
  isOpen.value = !isOpen.value
}

async function logout() {
  const confirmed = await confirmModal.value.open('Вы уверены, что хотите выйти из аккаунта?')
  if (!confirmed) return
  try {
    authService.logout()
    favoritesStore.clearFavorites();
    router.push('/');
  } catch (err) {
    alert('Не удалось выйти из аккаунта. ' + (err.response?.data?.message || ''))
  }
}
</script>

<style scoped>
.dropdown {
  position: relative;
}
.dropdown-toggle {
  position: relative;
  z-index: 2000;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 20px;
  background: transparent;
  border: 1px solid #8DA98F;
  border-radius: 8px;
  cursor: pointer;
  font-family: var(--font-family);
  font-weight: 400;
  font-size: 16px;
  color: var(--eerie-black);
  appearance: none;
  -webkit-appearance: none;
}
.dropdown-toggle::after {
  display: none;
}
.dropdown-toggle:hover {
  background: rgba(174, 195, 176, 0.1);
  color: #8DA98F;
}
.dropdown-toggle:hover .dropdown-icon {
  color: #8DA98F;
}
.dropdown-icon {
  width: 24px;
  height: 24px;
  color: currentColor;
  transition: color 0.3s;
}
.dropdown.active .dropdown-toggle {
  background-color: #8DA98F;
  color: #FFFFFF;
}
.dropdown.active .dropdown-toggle svg path {
  fill: white;
}
.dropdown.active .dropdown-toggle:hover {
  background-color: #aec3b0;
}
.dropdown-menu {
  position: absolute;
  display: block;
  z-index: 2000;
  top: 100%;
  right: 0;
  background: white;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0,0,0,0.12);
  min-width: 200px;
  z-index: 1000;
  margin-top: 8px;
  overflow: hidden;
}
.dropdown-item {
  display: block;
  padding: 12px 20px;
  text-decoration: none;
  font-family: var(--font-family);
  font-size: 14px;
  color: var(--eerie-black);
  transition: background 0.2s;
}
.dropdown-item:hover {
  background: #F5F5F5;
}
.dropdown-divider {
  margin: 0;
  border: none;
  border-top: 1px solid #E0E0E0;
}
.logout {
  background: none;
  border: none;
  width: 100%;
  text-align: left;
  cursor: pointer;
  color: #902923;
}
.logout:hover {
  background: #FFF0F0;
}
</style>