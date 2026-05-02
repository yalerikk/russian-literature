<template>
  <footer class="app-footer">
    <div class="container">
      <div class="footer-content">
        <!-- Левая колонка: Лого и описание -->
        <div class="footer-column">
          <div class="footer-logo">
            <div class="logo-icon">
              <svg width="32" height="32" viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M6 34.3V20C6 18.9333 6.392 18.0333 7.176 17.3C7.96 16.5667 8.88467 16.2333 9.95 16.3C12.5833 16.7 15.092 17.4753 17.476 18.626C19.86 19.7767 22.0347 21.268 24 23.1C25.9667 21.2667 28.142 19.7747 30.526 18.624C32.91 17.4733 35.418 16.6987 38.05 16.3C39.1167 16.2333 40.042 16.5667 40.826 17.3C41.61 18.0333 42.0013 18.9333 42 20V34.3C42 35.3667 41.65 36.2753 40.95 37.026C40.25 37.7767 39.3667 38.1847 38.3 38.25C36.1667 38.5833 34.1 39.1333 32.1 39.9C30.1 40.6667 28.2333 41.6833 26.5 42.95C26.1333 43.25 25.742 43.4747 25.326 43.624C24.91 43.7733 24.468 43.8487 24 43.85C23.532 43.8513 23.09 43.7767 22.674 43.626C22.258 43.4753 21.8667 43.25 21.5 42.95C19.7667 41.6833 17.9 40.6667 15.9 39.9C13.9 39.1333 11.8333 38.5833 9.7 38.25C8.63333 38.1833 7.75 37.7753 7.05 37.026C6.35 36.2767 6 35.368 6 34.3ZM24 39.8C26.1 38.2333 28.3333 36.9833 30.7 36.05C33.0667 35.1167 35.5 34.5 38 34.2V20.4C35.5667 20.8333 33.1747 21.7087 30.824 23.026C28.4733 24.3433 26.1987 26.1013 24 28.3C21.8 26.1 19.5253 24.342 17.176 23.026C14.8267 21.71 12.4347 20.8347 10 20.4V34.2C12.5 34.5 14.9333 35.1167 17.3 36.05C19.6667 36.9833 21.9 38.2333 24 39.8ZM24 18C21.8 18 19.9167 17.2167 18.35 15.65C16.7833 14.0833 16 12.2 16 10C16 7.8 16.7833 5.91667 18.35 4.35C19.9167 2.78333 21.8 2 24 2C26.2 2 28.0833 2.78333 29.65 4.35C31.2167 5.91667 32 7.8 32 10C32 12.2 31.2167 14.0833 29.65 15.65C28.0833 17.2167 26.2 18 24 18ZM24 14C25.1 14 26.042 13.6087 26.826 12.826C27.61 12.0433 28.0013 11.1013 28 10C27.9987 8.89867 27.6073 7.95733 26.826 7.176C26.0447 6.39467 25.1027 6.00267 24 6C22.8973 5.99733 21.956 6.38933 21.176 7.176C20.396 7.96267 20.004 8.904 20 10C19.996 11.096 20.388 12.038 21.176 12.826C21.964 13.614 22.9053 14.0053 24 14Z" fill="#FFFFFF"/>
            </svg>
            </div>
            <div class="logo-text">Knishka</div>
          </div>
          <p class="footer-description">
            Образовательная платформа для изучения русской литературы. 
            Бесплатный доступ к классическим произведениям для учащихся и преподавателей.
          </p>
        </div>
        
        <!-- Центральная колонка: Навигация -->
        <div class="footer-column">
          <h3 class="footer-title">Навигация</h3>
          <ul class="footer-links">
            <li><router-link to="/" class="footer-link">Главная</router-link></li>
            <li><router-link to="/catalog" class="footer-link">Каталог</router-link></li>
            <li><router-link to="/authors" class="footer-link">Авторы</router-link></li>
            <li><router-link to="/catalog/category/new" class="footer-link">Новинки</router-link></li>
            <li><router-link to="/catalog/category/popular" class="footer-link">Популярное</router-link></li>
          </ul>
        </div>
        
        <!-- Правая колонка: Контакты -->
        <div class="footer-column">
          <h3 class="footer-title">Контакты</h3>
          <ul class="footer-contacts">
            <li class="contact-item">
              <div class="contact-icon">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                  <path d="M21 10C21 17 12 23 12 23C12 23 3 17 3 10C3 7.61305 3.94821 5.32387 5.63604 3.63604C7.32387 1.94821 9.61305 1 12 1C14.3869 1 16.6761 1.94821 18.364 3.63604C20.0518 5.32387 21 7.61305 21 10Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                  <path d="M12 13C13.6569 13 15 11.6569 15 10C15 8.34315 13.6569 7 12 7C10.3431 7 9 8.34315 9 10C9 11.6569 10.3431 13 12 13Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </div>
              <span>Москва, ул. Образцова, 11</span>
            </li>
            <li class="contact-item">
              <div class="contact-icon">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                  <path d="M22 16.92V19.92C22.0001 20.1985 21.9182 20.4713 21.7638 20.7055C21.6094 20.9397 21.389 21.1251 21.13 21.24C20.729 21.411 20.267 21.5 19.75 21.5C16.85 21.5 14.09 20.67 11.75 19.15C9.61 17.75 7.86 15.77 6.7 13.42C5.53 11.07 5 8.43 5 5.75C5 5.23 5.09 4.77 5.26 4.37C5.375 4.111 5.5603 3.89063 5.7945 3.73622C6.02869 3.58181 6.3015 3.49994 6.58 3.5H9.58C10.05 3.5 10.46 3.79 10.61 4.24L11.48 7.01C11.58 7.32 11.54 7.66 11.37 7.94C11.2 8.22 10.92 8.41 10.6 8.46L8.63 8.75C9.69 11.33 11.67 13.31 14.25 14.37L14.54 12.4C14.59 12.08 14.78 11.8 15.06 11.63C15.34 11.46 15.68 11.42 15.99 11.52L18.76 12.39C19.21 12.54 19.5 12.95 19.5 13.42V16.42C19.5 16.94 19.41 17.41 19.24 17.81C19.125 18.07 18.9397 18.2894 18.7055 18.4438C18.4713 18.5982 18.1985 18.6801 17.92 18.68H16.92" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </div>
              <span>+7 (999) 123-45-67</span>
            </li>
            <li class="contact-item">
              <div class="contact-icon">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                  <path d="M4 4H20C21.1 4 22 4.9 22 6V18C22 19.1 21.1 20 20 20H4C2.9 20 2 19.1 2 18V6C2 4.9 2.9 4 4 4Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                  <path d="M22 6L12 13L2 6" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </div>
              <span>support@knishka.ru</span>
            </li>
          </ul>
        </div>
      </div>
      
      <!-- Нижняя часть: копирайт -->
      <div class="footer-bottom">
        <div class="copyright">
          © 2025 Knishka. Все права защищены.
        </div>
        <div class="footer-social">
          <a href="#" class="social-link" aria-label="VKontakte">
            <svg width="24" height="24" viewBox="0 0 36 36" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M3 8.25H8.25C8.25 20.25 15 21.75 15 21.75L15.003 8.25H20.253L20.2485 15.75C26.9985 12.75 27.7485 8.25 27.7485 8.25H33C33 8.25 31.5 15 25.641 18.2295C28.665 20.0265 31.902 22.9965 33 27.75H27.75C27.75 27.75 26.25 23.25 20.25 21L20.253 27.75C2.8305 27.75 3.003 11.25 3 8.25Z" stroke="#B2AEAB" stroke-width="2.25" stroke-linecap="round" stroke-linejoin="round" />
            </svg>
          </a>
          <a href="#" class="social-link" aria-label="Telegram">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
              <path d="M21 5L2 12.5L9 15M21 5L15 21L9 15M21 5L9 15" stroke="#B2AEAB" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </a>
          <a href="#" class="social-link" aria-label="YouTube">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
              <path d="M22.54 6.42C22.4213 5.94541 22.1793 5.51057 21.8387 5.15941C21.498 4.80824 21.0708 4.55318 20.6 4.42C18.88 4 12 4 12 4C12 4 5.12 4 3.4 4.46C2.92925 4.59318 2.50198 4.84824 2.16135 5.19941C1.82072 5.55057 1.57875 5.98541 1.46 6.46C1.14521 8.20556 0.991236 9.97631 1 11.75C0.991236 13.5237 1.14521 15.2944 1.46 17.04C1.59096 17.5158 1.83831 17.9517 2.17814 18.3063C2.51798 18.6609 2.93942 18.9226 3.4 19.06C5.12 19.5 12 19.5 12 19.5C12 19.5 18.88 19.5 20.6 19.04C21.0708 18.9068 21.498 18.6518 21.8387 18.3006C22.1793 17.9494 22.4213 17.5146 22.54 17.04C22.8528 15.2944 23.0068 13.5237 22.996 11.75C23.0068 9.97631 22.8528 8.20556 22.54 6.42Z" stroke="#B2AEAB" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
              <path d="M9.75 15.02L15.5 11.75L9.75 8.48V15.02Z" stroke="#B2AEAB" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </a>
        </div>
      </div>
    </div>
  </footer>
</template>

<script>
export default {
  name: 'AppFooter'
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400&display=swap');
@import url('https://fonts.googleapis.com/css2?family=Rubik:wght@400;500&display=swap');

.app-footer {
  background: #1B1B1B;
  color: #FFFFFF;
  padding: 48px 0 24px;
  margin-top: auto;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
}

.footer-content {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 48px;
  margin-bottom: 48px;
}

.footer-column {
  display: flex;
  flex-direction: column;
}

.footer-logo {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
}

.footer-logo .logo-icon {
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.footer-logo .logo-icon svg {
  stroke: #FFFFFF;
}

.footer-logo .logo-text {
  font-family: 'Roboto', sans-serif;
  font-size: 24px;
  font-weight: 400;
  color: #FFFFFF;
}

.footer-description {
  font-family: 'Rubik', sans-serif;
  font-size: 14px;
  line-height: 1.6;
  color: #B2AEAB;
  margin: 0;
}

.footer-title {
  font-family: 'Rubik', sans-serif;
  font-size: 18px;
  font-weight: 500;
  color: #FFFFFF;
  margin: 0 0 20px 0;
}

.footer-links {
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-links li {
  margin-bottom: 12px;
}

.footer-link {
  font-family: 'Rubik', sans-serif;
  font-size: 14px;
  color: #B2AEAB;
  text-decoration: none;
  transition: color 0.3s ease;
}

.footer-link:hover {
  color: #FFFFFF;
}

.footer-contacts {
  list-style: none;
  padding: 0;
  margin: 0;
}

.contact-item {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
  font-family: 'Rubik', sans-serif;
  font-size: 14px;
  color: #B2AEAB;
}

.contact-icon {
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.contact-icon svg {
  stroke: #B2AEAB;
}

.footer-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 24px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.copyright {
  font-family: 'Rubik', sans-serif;
  font-size: 14px;
  color: #B2AEAB;
}

.footer-social {
  display: flex;
  gap: 16px;
}

.social-link {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  transition: all 0.3s ease;
}

.social-link:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-2px);
}

.social-link svg {
  stroke: #FFFFFF;
}

/* Адаптивность */
@media (max-width: 768px) {
  .app-footer {
    padding: 32px 0 20px;
  }
  
  .footer-content {
    grid-template-columns: 1fr;
    gap: 32px;
    margin-bottom: 32px;
  }
  
  .footer-bottom {
    flex-direction: column;
    gap: 16px;
    text-align: center;
  }
  
  .container {
    padding: 0 16px;
  }
}
</style>