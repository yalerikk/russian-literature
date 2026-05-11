<template>
  <div class="auth-form">
    <div class="input-group">
      <input type="text" v-model="username" placeholder="Логин" class="auth-input" :class="{ error: usernameError }" />
      <div v-if="usernameError" class="error-message">{{ usernameError }}</div>
    </div>
    <div class="input-group">
      <input type="email" v-model="email" placeholder="Email" class="auth-input" :class="{ error: emailError }" />
      <div v-if="emailError" class="error-message">{{ emailError }}</div>
    </div>
    <div class="input-group">
      <div class="password-wrapper">
        <input :type="showPassword ? 'text' : 'password'" v-model="password" placeholder="Пароль" class="auth-input" :class="{ error: passwordError }" />
        <button type="button" class="toggle-password" @click="showPassword = !showPassword">
          <svg v-if="!showPassword" width="24" height="24" viewBox="0 0 24 24" fill="none">
            <path d="M3.0498 9.30995C3.00729 9.18301 2.9907 9.04882 3.00102 8.91534C3.01134 8.78187 3.04836 8.65183 3.10987 8.53292C3.17139 8.41402 3.25615 8.30868 3.35914 8.22315C3.46213 8.13762 3.58124 8.07363 3.70941 8.03499C3.83759 7.99635 3.97222 7.98384 4.10532 7.9982C4.23842 8.01256 4.36728 8.0535 4.48427 8.11859C4.60125 8.18369 4.70397 8.2716 4.78634 8.37714C4.86871 8.48267 4.92906 8.60366 4.9638 8.73295C7.0498 15.719 16.9458 15.72 19.0338 8.73695C19.0712 8.61102 19.1331 8.49368 19.2159 8.39165C19.2986 8.28962 19.4007 8.2049 19.5162 8.14231C19.6317 8.07972 19.7584 8.0405 19.8891 8.02689C20.0198 8.01327 20.1519 8.02553 20.2778 8.06295C20.4037 8.10038 20.5211 8.16225 20.6231 8.24502C20.7251 8.32779 20.8099 8.42985 20.8724 8.54536C20.935 8.66088 20.9742 8.78759 20.9879 8.91826C21.0015 9.04894 20.9892 9.18102 20.9518 9.30695C20.5882 10.5582 19.9711 11.7213 19.1388 12.724L20.4138 14C20.596 14.1886 20.6967 14.4412 20.6945 14.7034C20.6922 14.9656 20.587 15.2164 20.4016 15.4018C20.2162 15.5872 19.9654 15.6923 19.7032 15.6946C19.441 15.6969 19.1884 15.5961 18.9998 15.414L17.6888 14.103C16.9814 14.6367 16.1998 15.0642 15.3688 15.372L15.7258 16.707C15.7639 16.835 15.7759 16.9694 15.7612 17.1022C15.7465 17.2349 15.7053 17.3634 15.6401 17.48C15.5749 17.5966 15.487 17.699 15.3816 17.7811C15.2762 17.8631 15.1554 17.9233 15.0264 17.9579C14.8974 17.9925 14.7627 18.0009 14.6304 17.9826C14.498 17.9643 14.3707 17.9197 14.2559 17.8514C14.1411 17.783 14.0411 17.6924 13.9619 17.5848C13.8827 17.4772 13.8259 17.3549 13.7948 17.225L13.4308 15.868C12.4838 16.008 11.5158 16.008 10.5688 15.868L10.2048 17.225C10.1737 17.3549 10.1168 17.4772 10.0376 17.5848C9.95845 17.6924 9.8585 17.783 9.7437 17.8514C9.6289 17.9197 9.50157 17.9643 9.36923 17.9826C9.2369 18.0009 9.10224 17.9925 8.97321 17.9579C8.84418 17.9233 8.72339 17.8631 8.61797 17.7811C8.51256 17.699 8.42466 17.5966 8.35947 17.48C8.29427 17.3634 8.2531 17.2349 8.23839 17.1022C8.22367 16.9694 8.23571 16.835 8.2738 16.707L8.6308 15.372C7.79974 15.0639 7.01815 14.636 6.3108 14.102L5.0008 15.414C4.81329 15.6017 4.55887 15.7073 4.2935 15.7075C4.02814 15.7077 3.77357 15.6025 3.5858 15.415C3.39802 15.2274 3.29243 14.973 3.29224 14.7077C3.29205 14.4423 3.39729 14.1877 3.5848 14L4.8598 12.725C4.0758 11.789 3.4498 10.651 3.0478 9.31095L3.0498 9.30995Z" fill="#73706C" />
          </svg>
          <svg v-else width="24" height="24" viewBox="0 0 24 24" fill="none">
            <path d="M12 4C14.787 4 17.263 5.257 19.026 6.813C19.911 7.594 20.64 8.471 21.154 9.344C21.659 10.201 22 11.13 22 12C22 12.87 21.66 13.799 21.154 14.656C20.64 15.529 19.911 16.406 19.026 17.187C17.263 18.743 14.786 20 12 20C9.213 20 6.737 18.743 4.974 17.187C4.089 16.406 3.36 15.529 2.846 14.656C2.34 13.799 2 12.87 2 12C2 11.13 2.34 10.201 2.846 9.344C3.36 8.471 4.089 7.594 4.974 6.813C6.737 5.257 9.214 4 12 4ZM12 6C9.816 6 7.792 6.993 6.298 8.312C5.554 8.968 4.966 9.685 4.569 10.359C4.163 11.049 4 11.62 4 12C4 12.38 4.163 12.951 4.569 13.641C4.966 14.315 5.554 15.031 6.298 15.688C7.792 17.007 9.816 18 12 18C14.184 18 16.208 17.007 17.702 15.688C18.446 15.031 19.034 14.315 19.431 13.641C19.837 12.951 20 12.38 20 12C20 11.62 19.837 11.049 19.431 10.359C19.034 9.685 18.446 8.969 17.702 8.312C16.208 6.993 14.184 6 12 6ZM12 9C12.088 9 12.175 9.00367 12.261 9.011C12.0439 9.39185 11.9579 9.8335 12.0163 10.268C12.0747 10.7025 12.2743 11.1057 12.5843 11.4157C12.8943 11.7257 13.2975 11.9253 13.732 11.9837C14.1665 12.0421 14.6081 11.9561 14.989 11.739C15.0416 12.3412 14.911 12.9452 14.6145 13.4719C14.3179 13.9986 13.8692 14.4234 13.327 14.6907C12.7849 14.958 12.1746 15.0553 11.5762 14.9699C10.9778 14.8844 10.4192 14.6202 9.97357 14.2118C9.52792 13.8034 9.21603 13.27 9.07876 12.6813C8.94149 12.0926 8.98524 11.4762 9.20429 10.9128C9.42334 10.3495 9.80746 9.8654 10.3063 9.52407C10.8052 9.18275 11.3955 9.00008 12 9Z" fill="#73706C" />
          </svg>
        </button>
      </div>
      <div v-if="passwordError" class="error-message">{{ passwordError }}</div>
    </div>
    <div class="input-group">
      <div class="password-wrapper">
        <input :type="showConfirm ? 'text' : 'password'" v-model="confirmPassword" placeholder="Подтверждение пароля" class="auth-input" :class="{ error: confirmError }" />
        <button type="button" class="toggle-password" @click="showConfirm = !showConfirm">
          <svg v-if="!showConfirm" width="24" height="24" viewBox="0 0 24 24" fill="none">
            <path d="M3.0498 9.30995C3.00729 9.18301 2.9907 9.04882 3.00102 8.91534C3.01134 8.78187 3.04836 8.65183 3.10987 8.53292C3.17139 8.41402 3.25615 8.30868 3.35914 8.22315C3.46213 8.13762 3.58124 8.07363 3.70941 8.03499C3.83759 7.99635 3.97222 7.98384 4.10532 7.9982C4.23842 8.01256 4.36728 8.0535 4.48427 8.11859C4.60125 8.18369 4.70397 8.2716 4.78634 8.37714C4.86871 8.48267 4.92906 8.60366 4.9638 8.73295C7.0498 15.719 16.9458 15.72 19.0338 8.73695C19.0712 8.61102 19.1331 8.49368 19.2159 8.39165C19.2986 8.28962 19.4007 8.2049 19.5162 8.14231C19.6317 8.07972 19.7584 8.0405 19.8891 8.02689C20.0198 8.01327 20.1519 8.02553 20.2778 8.06295C20.4037 8.10038 20.5211 8.16225 20.6231 8.24502C20.7251 8.32779 20.8099 8.42985 20.8724 8.54536C20.935 8.66088 20.9742 8.78759 20.9879 8.91826C21.0015 9.04894 20.9892 9.18102 20.9518 9.30695C20.5882 10.5582 19.9711 11.7213 19.1388 12.724L20.4138 14C20.596 14.1886 20.6967 14.4412 20.6945 14.7034C20.6922 14.9656 20.587 15.2164 20.4016 15.4018C20.2162 15.5872 19.9654 15.6923 19.7032 15.6946C19.441 15.6969 19.1884 15.5961 18.9998 15.414L17.6888 14.103C16.9814 14.6367 16.1998 15.0642 15.3688 15.372L15.7258 16.707C15.7639 16.835 15.7759 16.9694 15.7612 17.1022C15.7465 17.2349 15.7053 17.3634 15.6401 17.48C15.5749 17.5966 15.487 17.699 15.3816 17.7811C15.2762 17.8631 15.1554 17.9233 15.0264 17.9579C14.8974 17.9925 14.7627 18.0009 14.6304 17.9826C14.498 17.9643 14.3707 17.9197 14.2559 17.8514C14.1411 17.783 14.0411 17.6924 13.9619 17.5848C13.8827 17.4772 13.8259 17.3549 13.7948 17.225L13.4308 15.868C12.4838 16.008 11.5158 16.008 10.5688 15.868L10.2048 17.225C10.1737 17.3549 10.1168 17.4772 10.0376 17.5848C9.95845 17.6924 9.8585 17.783 9.7437 17.8514C9.6289 17.9197 9.50157 17.9643 9.36923 17.9826C9.2369 18.0009 9.10224 17.9925 8.97321 17.9579C8.84418 17.9233 8.72339 17.8631 8.61797 17.7811C8.51256 17.699 8.42466 17.5966 8.35947 17.48C8.29427 17.3634 8.2531 17.2349 8.23839 17.1022C8.22367 16.9694 8.23571 16.835 8.2738 16.707L8.6308 15.372C7.79974 15.0639 7.01815 14.636 6.3108 14.102L5.0008 15.414C4.81329 15.6017 4.55887 15.7073 4.2935 15.7075C4.02814 15.7077 3.77357 15.6025 3.5858 15.415C3.39802 15.2274 3.29243 14.973 3.29224 14.7077C3.29205 14.4423 3.39729 14.1877 3.5848 14L4.8598 12.725C4.0758 11.789 3.4498 10.651 3.0478 9.31095L3.0498 9.30995Z" fill="#73706C" />
          </svg>
          <svg v-else width="24" height="24" viewBox="0 0 24 24" fill="none">
            <path d="M12 4C14.787 4 17.263 5.257 19.026 6.813C19.911 7.594 20.64 8.471 21.154 9.344C21.659 10.201 22 11.13 22 12C22 12.87 21.66 13.799 21.154 14.656C20.64 15.529 19.911 16.406 19.026 17.187C17.263 18.743 14.786 20 12 20C9.213 20 6.737 18.743 4.974 17.187C4.089 16.406 3.36 15.529 2.846 14.656C2.34 13.799 2 12.87 2 12C2 11.13 2.34 10.201 2.846 9.344C3.36 8.471 4.089 7.594 4.974 6.813C6.737 5.257 9.214 4 12 4ZM12 6C9.816 6 7.792 6.993 6.298 8.312C5.554 8.968 4.966 9.685 4.569 10.359C4.163 11.049 4 11.62 4 12C4 12.38 4.163 12.951 4.569 13.641C4.966 14.315 5.554 15.031 6.298 15.688C7.792 17.007 9.816 18 12 18C14.184 18 16.208 17.007 17.702 15.688C18.446 15.031 19.034 14.315 19.431 13.641C19.837 12.951 20 12.38 20 12C20 11.62 19.837 11.049 19.431 10.359C19.034 9.685 18.446 8.969 17.702 8.312C16.208 6.993 14.184 6 12 6ZM12 9C12.088 9 12.175 9.00367 12.261 9.011C12.0439 9.39185 11.9579 9.8335 12.0163 10.268C12.0747 10.7025 12.2743 11.1057 12.5843 11.4157C12.8943 11.7257 13.2975 11.9253 13.732 11.9837C14.1665 12.0421 14.6081 11.9561 14.989 11.739C15.0416 12.3412 14.911 12.9452 14.6145 13.4719C14.3179 13.9986 13.8692 14.4234 13.327 14.6907C12.7849 14.958 12.1746 15.0553 11.5762 14.9699C10.9778 14.8844 10.4192 14.6202 9.97357 14.2118C9.52792 13.8034 9.21603 13.27 9.07876 12.6813C8.94149 12.0926 8.98524 11.4762 9.20429 10.9128C9.42334 10.3495 9.80746 9.8654 10.3063 9.52407C10.8052 9.18275 11.3955 9.00008 12 9Z" fill="#73706C" />
          </svg>
        </button>
      </div>
      <div v-if="confirmError" class="error-message">{{ confirmError }}</div>
    </div>

    <div class="button-group">
      <button class="auth-button" @click="handleSubmit" :disabled="loading">{{ loading ? 'Регистрация...' : 'Зарегистрироваться' }}</button>
    </div>

    <div class="footer-links">
      <span>Уже есть аккаунт? </span>
      <button class="link-button" @click="$emit('switch-to-login')">Войдите!</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { authService } from '../services/authService'

const emit = defineEmits(['success', 'switch-to-login'])
const username = ref('')
const email = ref('')
const password = ref('')
const confirmPassword = ref('')
const loading = ref(false)
const usernameError = ref('')
const emailError = ref('')
const passwordError = ref('')
const confirmError = ref('')
const showPassword = ref(false)
const showConfirm = ref(false)

async function handleSubmit() {
  usernameError.value = ''
  emailError.value = ''
  passwordError.value = ''
  confirmError.value = ''
  if (!username.value.trim()) usernameError.value = 'Логин обязателен'
  else if (username.value.length < 2) usernameError.value = 'Минимум 2 символа'
  if (!email.value.trim()) emailError.value = 'Email обязателен'
  else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value)) emailError.value = 'Неверный формат email'
  if (!password.value) passwordError.value = 'Пароль обязателен'
  else if (password.value.length < 6) passwordError.value = 'Минимум 6 символов'
  if (password.value !== confirmPassword.value) confirmError.value = 'Пароли не совпадают'
  if (usernameError.value || emailError.value || passwordError.value || confirmError.value) return

  loading.value = true
  try {
    await authService.register(username.value, email.value, password.value)
    emit('success')
  } catch (err) {
    const msg = err.response?.data?.message || 'Ошибка регистрации'
    if (msg.toLowerCase().includes('username')) usernameError.value = msg
    else if (msg.toLowerCase().includes('email')) emailError.value = msg
    else passwordError.value = msg
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-form { 
  display: flex; 
  flex-direction: column; 
  align-items: center; 
  gap: 26px; 
  width: 300px; 
}
.input-group { 
  width: 100%; 
  display: flex; 
  flex-direction: column; 
  gap: 4px; 
}
.auth-input { 
  width: 100%; 
  border-radius: 6px; 
  padding: 10px; 
  background: #F3F3F4; 
  border: none; 
  font-size: 16px; 
  outline: none; 
}
.auth-input.error { 
  outline: 1px solid rgba(144,41,35,0.5); 
}
.password-wrapper { 
  display: flex; 
  align-items: center; 
  position: relative; 
  width: 100%;
}
.password-wrapper .auth-input {
  width: 100%;
  padding-right: 40px;
}
.toggle-password { 
  position: absolute; 
  right: 10px; 
  background: none; 
  border: none; 
  cursor: pointer; 
  padding: 0;
  display: flex;
  align-items: center;
}
.error-message { 
  font-size: 12px; 
  color: rgba(144,41,35,0.6); 
  margin-top: 4px; 
}
.button-group {
  width: 100%;
}
.auth-button { 
  width: 100%; 
  padding: 10px; 
  background: #AEC3B0; 
  border: none; 
  border-radius: 6px; 
  color: white; 
  font-weight: 500; 
  font-size: 16px; 
  cursor: pointer; 
  transition: 0.2s; 
}
.auth-button:hover { 
  background: #8DA98F; 
}
.footer-links { 
  display: flex; 
  gap: 3px; 
  font-size: 16px; 
}
.link-button { 
  background: none; 
  border: none; 
  color: #AEC3B0; 
  cursor: pointer; 
  text-decoration: none; 
  font-weight: 400; 
}
.link-button:hover { 
  text-decoration: underline; 
}
input[type="password"]::-webkit-credentials-auto-fill-button,
input[type="password"]::-webkit-strong-password-auto-fill-button {
  display: none !important;
  visibility: hidden;
  pointer-events: none;
}
</style>