<template>
  <div class="profile-edit-page">
    <div class="profile-edit-content">
      <!-- Хлебные крошки -->
      <div class="breadcrumb-nav">
        <router-link to="/profile/edit" class="breadcrumb-item">Профиль</router-link>
        <span class="separator">/</span>
        <span class="breadcrumb-item current">Управление аккаунтом</span>
      </div>

      <h1 class="page-title">Управление аккаунтом</h1>

      <!-- Секция 1: форма редактирования -->
      <div class="edit-section">
        <div class="edit-card">
          <div class="input-group">
            <label class="input-label">Логин</label>
            <input
              type="text"
              v-model="form.username"
              class="profile-input"
              :class="{ error: errors.username }"
              placeholder="Введите логин"
            />
            <div v-if="errors.username" class="error-message">{{ errors.username }}</div>
          </div>

          <div class="input-group">
            <label class="input-label">Email</label>
            <input
              type="email"
              v-model="form.email"
              class="profile-input"
              :class="{ error: errors.email }"
              placeholder="example@domain.com"
            />
            <div v-if="errors.email" class="error-message">{{ errors.email }}</div>
          </div>

          <div class="input-group">
            <label class="input-label">Новый пароль</label>
            <div class="password-wrapper">
              <input
                :type="showPassword ? 'text' : 'password'"
                v-model="form.password"
                class="profile-input"
                :class="{ error: errors.password }"
                placeholder="Новый пароль"
              />
              <button type="button" class="toggle-password" @click="showPassword = !showPassword">
                <svg v-if="!showPassword" width="20" height="20" viewBox="0 0 24 24" fill="none">
                  <path d="M3.0498 9.30995C3.00729 9.18301 2.9907 9.04882 3.00102 8.91534C3.01134 8.78187 3.04836 8.65183 3.10987 8.53292C3.17139 8.41402 3.25615 8.30868 3.35914 8.22315C3.46213 8.13762 3.58124 8.07363 3.70941 8.03499C3.83759 7.99635 3.97222 7.98384 4.10532 7.9982C4.23842 8.01256 4.36728 8.0535 4.48427 8.11859C4.60125 8.18369 4.70397 8.2716 4.78634 8.37714C4.86871 8.48267 4.92906 8.60366 4.9638 8.73295C7.0498 15.719 16.9458 15.72 19.0338 8.73695C19.0712 8.61102 19.1331 8.49368 19.2159 8.39165C19.2986 8.28962 19.4007 8.2049 19.5162 8.14231C19.6317 8.07972 19.7584 8.0405 19.8891 8.02689C20.0198 8.01327 20.1519 8.02553 20.2778 8.06295C20.4037 8.10038 20.5211 8.16225 20.6231 8.24502C20.7251 8.32779 20.8099 8.42985 20.8724 8.54536C20.935 8.66088 20.9742 8.78759 20.9879 8.91826C21.0015 9.04894 20.9892 9.18102 20.9518 9.30695C20.5882 10.5582 19.9711 11.7213 19.1388 12.724L20.4138 14C20.596 14.1886 20.6967 14.4412 20.6945 14.7034C20.6922 14.9656 20.587 15.2164 20.4016 15.4018C20.2162 15.5872 19.9654 15.6923 19.7032 15.6946C19.441 15.6969 19.1884 15.5961 18.9998 15.414L17.6888 14.103C16.9814 14.6367 16.1998 15.0642 15.3688 15.372L15.7258 16.707C15.7639 16.835 15.7759 16.9694 15.7612 17.1022C15.7465 17.2349 15.7053 17.3634 15.6401 17.48C15.5749 17.5966 15.487 17.699 15.3816 17.7811C15.2762 17.8631 15.1554 17.9233 15.0264 17.9579C14.8974 17.9925 14.7627 18.0009 14.6304 17.9826C14.498 17.9643 14.3707 17.9197 14.2559 17.8514C14.1411 17.783 14.0411 17.6924 13.9619 17.5848C13.8827 17.4772 13.8259 17.3549 13.7948 17.225L13.4308 15.868C12.4838 16.008 11.5158 16.008 10.5688 15.868L10.2048 17.225C10.1737 17.3549 10.1168 17.4772 10.0376 17.5848C9.95845 17.6924 9.8585 17.783 9.7437 17.8514C9.6289 17.9197 9.50157 17.9643 9.36923 17.9826C9.2369 18.0009 9.10224 17.9925 8.97321 17.9579C8.84418 17.9233 8.72339 17.8631 8.61797 17.7811C8.51256 17.699 8.42466 17.5966 8.35947 17.48C8.29427 17.3634 8.2531 17.2349 8.23839 17.1022C8.22367 16.9694 8.23571 16.835 8.2738 16.707L8.6308 15.372C7.79974 15.0639 7.01815 14.636 6.3108 14.102L5.0008 15.414C4.81329 15.6017 4.55887 15.7073 4.2935 15.7075C4.02814 15.7077 3.77357 15.6025 3.5858 15.415C3.39802 15.2274 3.29243 14.973 3.29224 14.7077C3.29205 14.4423 3.39729 14.1877 3.5848 14L4.8598 12.725C4.0758 11.789 3.4498 10.651 3.0478 9.31095L3.0498 9.30995Z" fill="#73706C" />
                </svg>
                <svg v-else width="20" height="20" viewBox="0 0 24 24" fill="none">
                  <path d="M12 4C14.787 4 17.263 5.257 19.026 6.813C19.911 7.594 20.64 8.471 21.154 9.344C21.659 10.201 22 11.13 22 12C22 12.87 21.66 13.799 21.154 14.656C20.64 15.529 19.911 16.406 19.026 17.187C17.263 18.743 14.786 20 12 20C9.213 20 6.737 18.743 4.974 17.187C4.089 16.406 3.36 15.529 2.846 14.656C2.34 13.799 2 12.87 2 12C2 11.13 2.34 10.201 2.846 9.344C3.36 8.471 4.089 7.594 4.974 6.813C6.737 5.257 9.214 4 12 4Z" stroke="#73706C" stroke-width="1.5" fill="none"/>
                  <path d="M12 9C12.088 9 12.175 9.00367 12.261 9.011C12.0439 9.39185 11.9579 9.8335 12.0163 10.268C12.0747 10.7025 12.2743 11.1057 12.5843 11.4157C12.8943 11.7257 13.2975 11.9253 13.732 11.9837C14.1665 12.0421 14.6081 11.9561 14.989 11.739C15.0416 12.3412 14.911 12.9452 14.6145 13.4719C14.3179 13.9986 13.8692 14.4234 13.327 14.6907C12.7849 14.958 12.1746 15.0553 11.5762 14.9699C10.9778 14.8844 10.4192 14.6202 9.97357 14.2118C9.52792 13.8034 9.21603 13.27 9.07876 12.6813C8.94149 12.0926 8.98524 11.4762 9.20429 10.9128C9.42334 10.3495 9.80746 9.8654 10.3063 9.52407C10.8052 9.18275 11.3955 9.00008 12 9Z" fill="#73706C" />
                </svg>
              </button>
            </div>
            <div v-if="errors.password" class="error-message">{{ errors.password }}</div>
          </div>

          <div class="input-group">
            <label class="input-label">Подтверждение нового пароля</label>
            <div class="password-wrapper">
              <input
                :type="showConfirmPassword ? 'text' : 'password'"
                v-model="form.confirmPassword"
                class="profile-input"
                :class="{ error: errors.confirmPassword }"
                placeholder="Повторите пароль"
              />
              <button type="button" class="toggle-password" @click="showConfirmPassword = !showConfirmPassword">
                <svg v-if="!showConfirmPassword" width="20" height="20" viewBox="0 0 24 24" fill="none">
                  <path d="M3.0498 9.30995C3.00729 9.18301 2.9907 9.04882 3.00102 8.91534C3.01134 8.78187 3.04836 8.65183 3.10987 8.53292C3.17139 8.41402 3.25615 8.30868 3.35914 8.22315C3.46213 8.13762 3.58124 8.07363 3.70941 8.03499C3.83759 7.99635 3.97222 7.98384 4.10532 7.9982C4.23842 8.01256 4.36728 8.0535 4.48427 8.11859C4.60125 8.18369 4.70397 8.2716 4.78634 8.37714C4.86871 8.48267 4.92906 8.60366 4.9638 8.73295C7.0498 15.719 16.9458 15.72 19.0338 8.73695C19.0712 8.61102 19.1331 8.49368 19.2159 8.39165C19.2986 8.28962 19.4007 8.2049 19.5162 8.14231C19.6317 8.07972 19.7584 8.0405 19.8891 8.02689C20.0198 8.01327 20.1519 8.02553 20.2778 8.06295C20.4037 8.10038 20.5211 8.16225 20.6231 8.24502C20.7251 8.32779 20.8099 8.42985 20.8724 8.54536C20.935 8.66088 20.9742 8.78759 20.9879 8.91826C21.0015 9.04894 20.9892 9.18102 20.9518 9.30695C20.5882 10.5582 19.9711 11.7213 19.1388 12.724L20.4138 14C20.596 14.1886 20.6967 14.4412 20.6945 14.7034C20.6922 14.9656 20.587 15.2164 20.4016 15.4018C20.2162 15.5872 19.9654 15.6923 19.7032 15.6946C19.441 15.6969 19.1884 15.5961 18.9998 15.414L17.6888 14.103C16.9814 14.6367 16.1998 15.0642 15.3688 15.372L15.7258 16.707C15.7639 16.835 15.7759 16.9694 15.7612 17.1022C15.7465 17.2349 15.7053 17.3634 15.6401 17.48C15.5749 17.5966 15.487 17.699 15.3816 17.7811C15.2762 17.8631 15.1554 17.9233 15.0264 17.9579C14.8974 17.9925 14.7627 18.0009 14.6304 17.9826C14.498 17.9643 14.3707 17.9197 14.2559 17.8514C14.1411 17.783 14.0411 17.6924 13.9619 17.5848C13.8827 17.4772 13.8259 17.3549 13.7948 17.225L13.4308 15.868C12.4838 16.008 11.5158 16.008 10.5688 15.868L10.2048 17.225C10.1737 17.3549 10.1168 17.4772 10.0376 17.5848C9.95845 17.6924 9.8585 17.783 9.7437 17.8514C9.6289 17.9197 9.50157 17.9643 9.36923 17.9826C9.2369 18.0009 9.10224 17.9925 8.97321 17.9579C8.84418 17.9233 8.72339 17.8631 8.61797 17.7811C8.51256 17.699 8.42466 17.5966 8.35947 17.48C8.29427 17.3634 8.2531 17.2349 8.23839 17.1022C8.22367 16.9694 8.23571 16.835 8.2738 16.707L8.6308 15.372C7.79974 15.0639 7.01815 14.636 6.3108 14.102L5.0008 15.414C4.81329 15.6017 4.55887 15.7073 4.2935 15.7075C4.02814 15.7077 3.77357 15.6025 3.5858 15.415C3.39802 15.2274 3.29243 14.973 3.29224 14.7077C3.29205 14.4423 3.39729 14.1877 3.5848 14L4.8598 12.725C4.0758 11.789 3.4498 10.651 3.0478 9.31095L3.0498 9.30995Z" fill="#73706C" />
                </svg>
                <svg v-else width="20" height="20" viewBox="0 0 24 24" fill="none">
                  <path d="M12 4C14.787 4 17.263 5.257 19.026 6.813C19.911 7.594 20.64 8.471 21.154 9.344C21.659 10.201 22 11.13 22 12C22 12.87 21.66 13.799 21.154 14.656C20.64 15.529 19.911 16.406 19.026 17.187C17.263 18.743 14.786 20 12 20C9.213 20 6.737 18.743 4.974 17.187C4.089 16.406 3.36 15.529 2.846 14.656C2.34 13.799 2 12.87 2 12C2 11.13 2.34 10.201 2.846 9.344C3.36 8.471 4.089 7.594 4.974 6.813C6.737 5.257 9.214 4 12 4Z" stroke="#73706C" stroke-width="1.5" fill="none"/>
                  <path d="M12 9C12.088 9 12.175 9.00367 12.261 9.011C12.0439 9.39185 11.9579 9.8335 12.0163 10.268C12.0747 10.7025 12.2743 11.1057 12.5843 11.4157C12.8943 11.7257 13.2975 11.9253 13.732 11.9837C14.1665 12.0421 14.6081 11.9561 14.989 11.739C15.0416 12.3412 14.911 12.9452 14.6145 13.4719C14.3179 13.9986 13.8692 14.4234 13.327 14.6907C12.7849 14.958 12.1746 15.0553 11.5762 14.9699C10.9778 14.8844 10.4192 14.6202 9.97357 14.2118C9.52792 13.8034 9.21603 13.27 9.07876 12.6813C8.94149 12.0926 8.98524 11.4762 9.20429 10.9128C9.42334 10.3495 9.80746 9.8654 10.3063 9.52407C10.8052 9.18275 11.3955 9.00008 12 9Z" fill="#73706C" />
                </svg>
              </button>
            </div>
            <div v-if="errors.confirmPassword" class="error-message">{{ errors.confirmPassword }}</div>
          </div>

          <button class="save-button" @click="saveProfile" :disabled="saving">
            {{ saving ? 'Сохранение...' : 'Сохранить' }}
          </button>
        </div>
      </div>

      <!-- Секция 2: правила редактирования -->
      <div class="rules-section">
        <div class="rules-card">
          <h2 class="rules-header">Правила редактирования данных</h2>

          <div class="rule-block">
            <h3 class="rule-title">Логин</h3>
            <ul class="rule-list">
              <li>Должен содержать от 3 до 30 символов.</li>
              <li>Не содержит специальных символов (используйте только латинские буквы, цифры и подчеркивание).</li>
              <li>Для обеспечения уникальности, ввод следует проверять, существует ли данный логин.</li>
            </ul>
          </div>

          <div class="rule-block">
            <h3 class="rule-title">Email</h3>
            <ul class="rule-list">
              <li>Должен соответствовать формату <code>example@domain.com</code>.</li>
              <li>Не допускаются пробелы в начале или конце.</li>
              <li>Проверяется на уникальность — если email уже используется, необходимо выбрать другой.</li>
            </ul>
          </div>

          <div class="rule-block">
            <h3 class="rule-title">Пароль</h3>
            <ul class="rule-list">
              <li>Должен быть не менее 6 символов.</li>
              <li>Запрещено использование кириллических символов.</li>
              <li>Обязательно: одна заглавная латинская буква, одна строчная латинская буква и одна цифра.</li>
            </ul>
          </div>

          <div class="rule-block">
            <h3 class="rule-title">Обратите внимание</h3>
            <ul class="rule-list">
              <li>Если изменения пароля не требуются, поле пароля можно оставить пустым.</li>
              <li>Все изменения вступят в силу только после сохранения.</li>
            </ul>
          </div>

          <div class="action-buttons">
            <button class="delete-button" @click="confirmDeleteAccount">
              <span>Удалить аккаунт</span>
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                <path d="M7 4C7 3.46957 7.21071 2.96086 7.58579 2.58579C7.96086 2.21071 8.46957 2 9 2H15C15.5304 2 16.0391 2.21071 16.4142 2.58579C16.7893 2.96086 17 3.46957 17 4V6H21C21.2652 6 21.5196 6.10536 21.7071 6.29289C21.8946 6.48043 22 6.73478 22 7C22 7.26522 21.8946 7.51957 21.7071 7.70711C21.5196 7.89464 21.2652 8 21 8H19.931L19.064 20.142C19.0281 20.6466 18.8023 21.1188 18.4321 21.4636C18.0619 21.8083 17.5749 22 17.069 22H6.93C6.42414 22 5.93707 21.8083 5.56688 21.4636C5.1967 21.1188 4.97092 20.6466 4.935 20.142L4.07 8H3C2.73478 8 2.48043 7.89464 2.29289 7.70711C2.10536 7.51957 2 7.26522 2 7C2 6.73478 2.10536 6.48043 2.29289 6.29289C2.48043 6.10536 2.73478 6 3 6H7V4ZM9 6H15V4H9V6ZM6.074 8L6.931 20H17.07L17.927 8H6.074ZM10 10C10.2652 10 10.5196 10.1054 10.7071 10.2929C10.8946 10.4804 11 10.7348 11 11V17C11 17.2652 10.8946 17.5196 10.7071 17.7071C10.5196 17.8946 10.2652 18 10 18C9.73478 18 9.48043 17.8946 9.29289 17.7071C9.10536 17.5196 9 17.2652 9 17V11C9 10.7348 9.10536 10.4804 9.29289 10.2929C9.48043 10.1054 9.73478 10 10 10ZM14 10C14.2652 10 14.5196 10.1054 14.7071 10.2929C14.8946 10.4804 15 10.7348 15 11V17C15 17.2652 14.8946 17.5196 14.7071 17.7071C14.5196 17.8946 14.2652 18 14 18C13.7348 18 13.4804 17.8946 13.2929 17.7071C13.1054 17.5196 13 17.2652 13 17V11C13 10.7348 13.1054 10.4804 13.2929 10.2929C13.4804 10.1054 13.7348 10 14 10Z" fill="white"/>
              </svg>
            </button>
            <button class="logout-button" @click="logout">
              Выйти из аккаунта
            </button>
          </div>
        </div>
      </div>

      <!-- Модалки -->
      <ConfirmModal ref="confirmModal" />
      <AuthModal v-if="showAuthModal" @close="showAuthModal = false" @success="onAuthSuccess" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { apiClient } from '../services/api'
import { authService } from '../services/authService'
import ConfirmModal from '../components/ConfirmModal.vue'
import AuthModal from '../components/AuthModal.vue'

const router = useRouter()
const confirmModal = ref(null)
const showAuthModal = ref(false)

const form = ref({
  username: '',
  email: '',
  password: '',
  confirmPassword: ''
})
const errors = ref({
  username: '',
  email: '',
  password: '',
  confirmPassword: ''
})
const saving = ref(false)
const showPassword = ref(false)
const showConfirmPassword = ref(false)

// Загрузить текущие данные пользователя
async function loadUserData() {
  try {
    const res = await apiClient.get('/users/me');
    form.value.username = res.username;
    form.value.email = res.email;
  } catch (err) {
    console.error('Ошибка загрузки профиля', err);
  }
}

// Валидация на фронте (по правилам бэка)
function validateForm() {
  errors.value = { username: '', email: '', password: '', confirmPassword: '' }
  let isValid = true

  if (!form.value.username.trim()) {
    errors.value.username = 'Логин обязателен'
    isValid = false
  } else if (form.value.username.length < 3 || form.value.username.length > 30) {
    errors.value.username = 'Логин должен быть от 3 до 30 символов'
    isValid = false
  } else if (!/^[a-zA-Z0-9_]+$/.test(form.value.username)) {
    errors.value.username = 'Только латинские буквы, цифры и подчеркивание'
    isValid = false
  }

  if (!form.value.email.trim()) {
    errors.value.email = 'Email обязателен'
    isValid = false
  } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.value.email)) {
    errors.value.email = 'Неверный формат email'
    isValid = false
  }

  if (form.value.password) {
    if (form.value.password.length < 6) {
      errors.value.password = 'Пароль должен быть не менее 6 символов'
      isValid = false
    } else if (/[а-яА-ЯёЁ]/.test(form.value.password)) {
      errors.value.password = 'Запрещены кириллические символы'
      isValid = false
    } else if (!/[A-Z]/.test(form.value.password)) {
      errors.value.password = 'Нужна хотя бы одна заглавная латинская буква'
      isValid = false
    } else if (!/[a-z]/.test(form.value.password)) {
      errors.value.password = 'Нужна хотя бы одна строчная латинская буква'
      isValid = false
    } else if (!/\d/.test(form.value.password)) {
      errors.value.password = 'Нужна хотя бы одна цифра'
      isValid = false
    }

    if (form.value.password !== form.value.confirmPassword) {
      errors.value.confirmPassword = 'Пароли не совпадают'
      isValid = false
    }
  }

  return isValid
}

async function saveProfile() {
  if (!validateForm()) return
  saving.value = true
  try {
    const userId = authService.getUserIdFromToken()
    const payload = {
      username: form.value.username,
      email: form.value.email,
      password: form.value.password || undefined,
      role: 'READER' // роль не меняем
    }
    await apiClient.put(`/users/${userId}`, payload)
    alert('Данные успешно обновлены')
    router.go(0)
  } catch (err) {
    const msg = err.data?.message || err.message || 'Ошибка сохранения';
    if (msg.toLowerCase().includes('username')) errors.value.username = msg;
    else if (msg.toLowerCase().includes('email')) errors.value.email = msg;
    else alert(msg);
  } finally {
    saving.value = false
  }
}

function logout() {
  authService.logout()
  router.push('/')
}

async function confirmDeleteAccount() {
  const confirmed = await confirmModal.value.open('Вы уверены, что хотите удалить аккаунт? Это действие необратимо.')
  if (!confirmed) return
  try {
    const userId = getUserIdFromToken()
    await apiClient.delete(`/users/${userId}`)
    authService.logout()
    router.push('/')
  } catch (err) {
    alert('Не удалось удалить аккаунт. ' + (err.response?.data?.message || ''))
  }
}

function onAuthSuccess() {
  showAuthModal.value = false
  loadUserData()
}

onMounted(() => {
  if (!authService.isAuthenticated.value) {
    showAuthModal.value = true
  } else {
    loadUserData()
  }
})
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;600;700&display=swap');
@import url('https://fonts.googleapis.com/css2?family=Rubik:wght@400;500;600;700&display=swap');

.profile-edit-page {
  font-family: 'Manrope', sans-serif;
  width: 100%;
  min-height: 100vh;
  background-color: #f8f9fa;
  color: #1B1B1B;
  padding: 50px 50px 0;
}

.profile-edit-content {
  padding: 0 100px 50px;
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
  color: #902923;
  text-decoration: none;
}
.breadcrumb-item.current {
  color: #73706C;
}
.separator {
  color: #902923;
  font-size: 12px;
}
.page-title {
  font-family: 'Manrope', sans-serif;
  font-weight: 600;
  font-size: 24px;
  margin-bottom: 30px;
}

/* Секция 1 — фон на всю ширину */
.edit-section {
  background: rgba(230, 220, 211, 0.5);
  margin: 0 -1000px 30px -1000px;
  padding: 30px 100px;
  display: flex;
  justify-content: center;
}
.edit-card {
  width: 712px;
  max-width: 100%;
  background: white;
  border-radius: 12px;
  padding: 32px;
  display: flex;
  flex-direction: column;
  gap: 26px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}
.input-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.input-label {
  font-family: 'Manrope', sans-serif;
  font-weight: 500;
  font-size: 16px;
  color: #1B1B1B;
}
.profile-input {
  width: 100%;
  background: #F3F3F4;
  border: none;
  border-radius: 6px;
  padding: 12px;
  font-size: 16px;
  outline: none;
}
.profile-input.error {
  outline: 1px solid rgba(144,41,35,0.5);
}
.password-wrapper {
  position: relative;
  width: 100%;
}
.password-wrapper .profile-input {
  padding-right: 40px;
}
.toggle-password {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  padding: 0;
}
.error-message {
  font-size: 12px;
  color: rgba(144,41,35,0.6);
}
.save-button {
  width: 140px;
  background: #AEC3B0;
  border: none;
  border-radius: 6px;
  padding: 10px;
  font-family: 'Manrope', sans-serif;
  font-weight: 500;
  font-size: 16px;
  color: white;
  cursor: pointer;
  transition: 0.2s;
}
.save-button:hover {
  background: #8DA98F;
}
.save-button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* Секция 2 — правила */
.rules-section {
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
}
.rules-card {
  width: 712px;
  max-width: 100%;
  background: white;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 36px;
}
.rules-header {
  font-family: 'Manrope', sans-serif;
  font-weight: 500;
  font-size: 20px;
  color: #902923;
  margin: 0;
}
.rule-block {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.rule-title {
  font-family: 'Manrope', sans-serif;
  font-weight: 500;
  font-size: 16px;
  color: #1B1B1B;
  margin: 0;
}
.rule-list {
  margin: 0;
  padding-left: 20px;
  font-family: 'Manrope', sans-serif;
  font-weight: 400;
  font-size: 14px;
  color: #1B1B1B;
  line-height: 1.5;
}
.rule-list li {
  margin-bottom: 4px;
}
.action-buttons {
  display: flex;
  gap: 20px;
  margin-top: 30px;
}
.logout-button {
  background: #E0E0E0;
  border: none;
  border-radius: 6px;
  padding: 10px 20px;
  font-family: 'Manrope', sans-serif;
  font-size: 16px;
  cursor: pointer;
  transition: 0.2s;
}
.logout-button:hover {
  background: #C0C0C0;
}
.delete-button {
  display: flex;
  align-items: center;
  gap: 8px;
  background: #902923;
  border: none;
  border-radius: 6px;
  padding: 8px 16px;
  font-family: 'Manrope', sans-serif;
  font-weight: 400;
  font-size: 16px;
  color: white;
  cursor: pointer;
  transition: 0.2s;
}
.delete-button:hover {
  background: #731b17;
}
</style>