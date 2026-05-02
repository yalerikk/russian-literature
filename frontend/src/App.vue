<template>
  <div id="app">
    <AppHeader />
    <main class="app-main">
      <router-view />
    </main>
    <AppFooter />
  </div>
</template>

<script>
import AppHeader from './components/Header.vue'
import AppFooter from './components/Footer.vue'

export default {
  name: 'App',
  components: {
    AppHeader,
    AppFooter
  }
}
</script>

<style>
/* Глобальные стили */
:root {
  --primary-color: #902923;
  --secondary-color: #1B1B1B;
  --accent-color: #AEC3B0;
  --text-primary: #1B1B1B;
  --text-secondary: #73706C;
  --text-muted: #B2AEAB;
  --bg-primary: #FFFFFF;
  --bg-secondary: #F8F9FA;
  --border-color: #E0E0E0;
  --shadow-sm: 0 1px 3px rgba(0, 0, 0, 0.1);
  --shadow-md: 0 4px 6px rgba(0, 0, 0, 0.1);
  --shadow-lg: 0 10px 15px rgba(0, 0, 0, 0.1);
  --radius-sm: 4px;
  --radius-md: 8px;
  --radius-lg: 12px;
  --radius-xl: 16px;
}

/* Сброс стилей */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html {
  font-size: 16px;
}

body {
  font-family: 'Rubik', sans-serif;
  color: var(--text-primary);
  background-color: var(--bg-primary);
  line-height: 1.6;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.app-main {
  flex: 1;
}

/* Глобальные классы утилиты */
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
}

.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 12px 24px;
  border: none;
  border-radius: var(--radius-md);
  font-family: 'Rubik', sans-serif;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  text-decoration: none;
  gap: 8px;
}

.btn-primary {
  background: linear-gradient(135deg, var(--primary-color), #B33A33);
  color: white;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.btn-secondary {
  background: var(--bg-secondary);
  color: var(--text-primary);
  border: 1px solid var(--border-color);
}

.btn-secondary:hover {
  background: var(--border-color);
}

/* Стили для форм */
.form-control {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  font-family: 'Rubik', sans-serif;
  font-size: 16px;
  color: var(--text-primary);
  background: var(--bg-primary);
  transition: all 0.3s ease;
}

.form-control:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 3px rgba(144, 41, 35, 0.1);
}

.form-label {
  display: block;
  margin-bottom: 8px;
  font-family: 'Rubik', sans-serif;
  font-size: 14px;
  font-weight: 500;
  color: var(--text-primary);
}

/* Стили для карточек */
.card {
  background: var(--bg-primary);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-lg);
  padding: 24px;
  box-shadow: var(--shadow-sm);
  transition: all 0.3s ease;
}

.card:hover {
  box-shadow: var(--shadow-md);
  transform: translateY(-2px);
}

/* Адаптивность */
@media (max-width: 768px) {
  .container {
    padding: 0 16px;
  }
  
  .btn {
    padding: 10px 20px;
    font-size: 14px;
  }
  
  .form-control {
    padding: 10px 14px;
    font-size: 14px;
  }
  
  .card {
    padding: 16px;
  }
}
</style>