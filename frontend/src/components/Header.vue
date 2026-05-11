<template>
  <header class="app-header">
    <div class="container">
      <div class="header-content">
        <!-- Логотип -->
        <div class="logo" @click="goToHome">
          <div class="logo-icon">
            <svg width="48" height="48" viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M6 34.3V20C6 18.9333 6.392 18.0333 7.176 17.3C7.96 16.5667 8.88467 16.2333 9.95 16.3C12.5833 16.7 15.092 17.4753 17.476 18.626C19.86 19.7767 22.0347 21.268 24 23.1C25.9667 21.2667 28.142 19.7747 30.526 18.624C32.91 17.4733 35.418 16.6987 38.05 16.3C39.1167 16.2333 40.042 16.5667 40.826 17.3C41.61 18.0333 42.0013 18.9333 42 20V34.3C42 35.3667 41.65 36.2753 40.95 37.026C40.25 37.7767 39.3667 38.1847 38.3 38.25C36.1667 38.5833 34.1 39.1333 32.1 39.9C30.1 40.6667 28.2333 41.6833 26.5 42.95C26.1333 43.25 25.742 43.4747 25.326 43.624C24.91 43.7733 24.468 43.8487 24 43.85C23.532 43.8513 23.09 43.7767 22.674 43.626C22.258 43.4753 21.8667 43.25 21.5 42.95C19.7667 41.6833 17.9 40.6667 15.9 39.9C13.9 39.1333 11.8333 38.5833 9.7 38.25C8.63333 38.1833 7.75 37.7753 7.05 37.026C6.35 36.2767 6 35.368 6 34.3ZM24 39.8C26.1 38.2333 28.3333 36.9833 30.7 36.05C33.0667 35.1167 35.5 34.5 38 34.2V20.4C35.5667 20.8333 33.1747 21.7087 30.824 23.026C28.4733 24.3433 26.1987 26.1013 24 28.3C21.8 26.1 19.5253 24.342 17.176 23.026C14.8267 21.71 12.4347 20.8347 10 20.4V34.2C12.5 34.5 14.9333 35.1167 17.3 36.05C19.6667 36.9833 21.9 38.2333 24 39.8ZM24 18C21.8 18 19.9167 17.2167 18.35 15.65C16.7833 14.0833 16 12.2 16 10C16 7.8 16.7833 5.91667 18.35 4.35C19.9167 2.78333 21.8 2 24 2C26.2 2 28.0833 2.78333 29.65 4.35C31.2167 5.91667 32 7.8 32 10C32 12.2 31.2167 14.0833 29.65 15.65C28.0833 17.2167 26.2 18 24 18ZM24 14C25.1 14 26.042 13.6087 26.826 12.826C27.61 12.0433 28.0013 11.1013 28 10C27.9987 8.89867 27.6073 7.95733 26.826 7.176C26.0447 6.39467 25.1027 6.00267 24 6C22.8973 5.99733 21.956 6.38933 21.176 7.176C20.396 7.96267 20.004 8.904 20 10C19.996 11.096 20.388 12.038 21.176 12.826C21.964 13.614 22.9053 14.0053 24 14Z" fill="#1B1B1B"/>
            </svg>
          </div>
          <div class="logo-text">Knishka</div>
        </div>
        
        <!-- Центральные кнопки -->
        <div class="center-buttons">
          <!-- Каталог -->
          <button 
            class="catalog-btn"
            :class="{ active: isActive('/catalog') }"
            @click="goToCatalog"
          >
            <div class="btn-icon">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M8.50998 2H15.49C15.722 2 15.9 2 16.056 2.015C17.164 2.124 18.071 2.79 18.456 3.687H5.54398C5.92898 2.79 6.83598 2.124 7.94398 2.015C8.09798 2 8.27598 2 8.50998 2ZM6.30998 4.723C4.91998 4.723 3.77998 5.563 3.39998 6.677L3.37598 6.747C3.77398 6.627 4.18898 6.547 4.60798 6.494C5.68798 6.355 7.05398 6.355 8.63998 6.355H15.532C17.118 6.355 18.483 6.355 19.564 6.494C19.984 6.548 20.398 6.626 20.796 6.747L20.773 6.677C20.393 5.563 19.253 4.723 17.862 4.723H6.30998Z" fill="currentColor"/>
                <path d="M8.67208 7.54199H15.3281C18.7021 7.54199 20.3901 7.54199 21.3381 8.52899C22.2861 9.51599 22.0621 11.04 21.6161 14.089L21.1941 16.981C20.8441 19.372 20.6691 20.568 19.7721 21.284C18.8751 22 17.5521 22 14.9051 22H9.09509C6.44909 22 5.12509 22 4.22809 21.284C3.33109 20.568 3.15609 19.372 2.80609 16.981L2.38409 14.09C1.93709 11.04 1.71408 9.51599 2.66208 8.52899C3.61009 7.54199 5.29808 7.54199 8.67208 7.54199ZM8.00008 18C8.00008 17.586 8.37309 17.25 8.83309 17.25H15.1671C15.6271 17.25 16.0001 17.586 16.0001 18C16.0001 18.414 15.6271 18.75 15.1671 18.75H8.83309C8.37309 18.75 8.00008 18.414 8.00008 18Z" fill="currentColor"/>
              </svg>
            </div>
            <span class="btn-text">Каталог</span>
          </button>
          
          <!-- Поиск -->
          <div class="search-wrapper">
            <div class="search-container">
              <input 
                type="text" 
                class="search-input" 
                placeholder="Найти произведение/автора"
                v-model="searchQueryLocal"
                @focus="showSuggest = true"
                @blur="closeSuggest"
                @keyup.enter="performSearch"
              />
              <button class="search-icon" @click="performSearch">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <path d="M10.397 3C8.43507 3 6.55349 3.77938 5.16619 5.16668C3.77889 6.55398 2.99951 8.43556 2.99951 10.3975C2.99951 12.3594 3.77889 14.241 5.16619 15.6283C6.55349 17.0156 8.43507 17.795 10.397 17.795C12.359 17.795 14.2405 17.0156 15.6278 15.6283C17.0151 14.241 17.7945 12.3594 17.7945 10.3975C17.7945 8.43556 17.0151 6.55398 15.6278 5.16668C14.2405 3.77938 12.359 3 10.397 3ZM3.98701 10.397C3.97641 9.54843 4.1344 8.70619 4.4518 7.91914C4.7692 7.13209 5.23971 6.41588 5.83603 5.81207C6.43235 5.20825 7.14262 4.72884 7.92565 4.40163C8.70867 4.07442 9.54887 3.90593 10.3975 3.90593C11.2462 3.90593 12.0864 4.07442 12.8694 4.40163C13.6524 4.72884 14.3627 5.20825 14.959 5.81207C15.5553 6.41588 16.0258 7.13209 16.3432 7.91914C16.6606 8.70619 16.8186 9.54843 16.808 10.397C16.808 12.0973 16.1326 13.728 14.9303 14.9303C13.728 16.1326 12.0973 16.808 10.397 16.808C8.69671 16.808 7.06605 16.1326 5.86375 14.9303C4.66145 13.728 3.98701 12.0973 3.98701 10.397Z" fill="#1B1B1B" />
                  <path d="M16.8381 15.7921C16.7702 15.7198 16.6885 15.6619 16.5978 15.6218C16.5072 15.5817 16.4094 15.5602 16.3102 15.5587C16.2111 15.5571 16.1127 15.5755 16.0208 15.6127C15.9289 15.6499 15.8454 15.7052 15.7753 15.7753C15.7052 15.8454 15.6499 15.9289 15.6127 16.0208C15.5755 16.1127 15.5571 16.2111 15.5587 16.3102C15.5602 16.4094 15.5817 16.5072 15.6218 16.5978C15.6619 16.6885 15.7198 16.7702 15.7921 16.8381L19.7371 20.7831C19.8049 20.8553 19.8866 20.9132 19.9773 20.9533C20.0679 20.9934 20.1658 21.0149 20.2649 21.0164C20.364 21.018 20.4624 20.9996 20.5543 20.9624C20.6462 20.9252 20.7297 20.8699 20.7998 20.7998C20.8699 20.7297 20.9252 20.6462 20.9624 20.5543C20.9996 20.4624 21.018 20.364 21.0164 20.2649C21.0149 20.1658 20.9934 20.0679 20.9533 19.9773C20.9132 19.8866 20.8553 19.8049 20.7831 19.7371L16.8381 15.7921Z" fill="#1B1B1B" />
                </svg>
              </button>
            </div>
            <SuggestDropdown 
              :query="searchQueryLocal" 
              :visible="showSuggest"
            />
          </div>
        </div>
        
        <!-- Правые кнопки -->
        <div class="right-buttons">
          <!-- Авторы -->
          <button 
            class="icon-btn"
            :class="{ active: isActive('/authors') }"
            @click="goToAuthors"
          >
            <div class="icon-wrapper">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M22.5 21C22.5 21 24 21 24 19.5C24 18 22.5 13.5 16.5 13.5C10.5 13.5 9 18 9 19.5C9 21 10.5 21 10.5 21H22.5ZM10.533 19.5L10.5 19.494C10.5015 19.098 10.7505 17.949 11.64 16.914C12.468 15.9435 13.923 15 16.5 15C19.0755 15 20.5305 15.945 21.36 16.914C22.2495 17.949 22.497 19.0995 22.5 19.494L22.488 19.497L22.467 19.5H10.533ZM16.5 10.5C17.2956 10.5 18.0587 10.1839 18.6213 9.62132C19.1839 9.05871 19.5 8.29565 19.5 7.5C19.5 6.70435 19.1839 5.94129 18.6213 5.37868C18.0587 4.81607 17.2956 4.5 16.5 4.5C15.7044 4.5 14.9413 4.81607 14.3787 5.37868C13.8161 5.94129 13.5 6.70435 13.5 7.5C13.5 8.29565 13.8161 9.05871 14.3787 9.62132C14.9413 10.1839 15.7044 10.5 16.5 10.5ZM21 7.5C21 8.09095 20.8836 8.67611 20.6575 9.22208C20.4313 9.76804 20.0998 10.2641 19.682 10.682C19.2641 11.0998 18.768 11.4313 18.2221 11.6575C17.6761 11.8836 17.0909 12 16.5 12C15.9091 12 15.3239 11.8836 14.7779 11.6575C14.232 11.4313 13.7359 11.0998 13.318 10.682C12.9002 10.2641 12.5687 9.76804 12.3425 9.22208C12.1164 8.67611 12 8.09095 12 7.5C12 6.30653 12.4741 5.16193 13.318 4.31802C14.1619 3.47411 15.3065 3 16.5 3C17.6935 3 18.8381 3.47411 19.682 4.31802C20.5259 5.16193 21 6.30653 21 7.5ZM10.404 13.92C9.80364 13.7324 9.18526 13.6082 8.559 13.5495C8.20706 13.5152 7.85361 13.4987 7.5 13.5C1.5 13.5 0 18 0 19.5C0 20.5 0.5 21 1.5 21H7.824C7.60174 20.5317 7.49084 20.0183 7.5 19.5C7.5 17.985 8.0655 16.437 9.135 15.144C9.4995 14.703 9.924 14.2905 10.404 13.92ZM7.38 15C6.49279 16.3342 6.01327 17.8978 6 19.5H1.5C1.5 19.11 1.746 17.955 2.64 16.914C3.4575 15.96 4.878 15.03 7.38 15.0015V15ZM2.25 8.25C2.25 7.05653 2.72411 5.91193 3.56802 5.06802C4.41193 4.22411 5.55653 3.75 6.75 3.75C7.94347 3.75 9.08807 4.22411 9.93198 5.06802C10.7759 5.91193 11.25 7.05653 11.25 8.25C11.25 9.44347 10.7759 10.5881 9.93198 11.432C9.08807 12.2759 7.94347 12.75 6.75 12.75C5.55653 12.75 4.41193 12.2759 3.56802 11.432C2.72411 10.5881 2.25 9.44347 2.25 8.25ZM6.75 5.25C5.95435 5.25 5.19129 5.56607 4.62868 6.12868C4.06607 6.69129 3.75 7.45435 3.75 8.25C3.75 9.04565 4.06607 9.80871 4.62868 10.3713C5.19129 10.9339 5.95435 11.25 6.75 11.25C7.54565 11.25 8.30871 10.9339 8.87132 10.3713C9.43393 9.80871 9.75 9.04565 9.75 8.25C9.75 7.45435 9.43393 6.69129 8.87132 6.12868C8.30871 5.56607 7.54565 5.25 6.75 5.25Z" fill="currentColor" />
              </svg>
            </div>
            <span class="icon-label">Авторы</span>
          </button>
          
          <!-- Мои книги -->
          <button
            class="icon-btn"
            :class="{ active: isActive('/profile/my-books') }"
            @click="handleProtectedClick('myBooks')"
          >
            <div class="icon-wrapper">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M3 22.5H2.25C1.85218 22.5 1.47064 22.342 1.18934 22.0607C0.908035 21.7794 0.75 21.3978 0.75 21V5.25C0.75 4.85218 0.908035 4.47064 1.18934 4.18934C1.47064 3.90804 1.85218 3.75 2.25 3.75H3C3.39782 3.75 3.77936 3.90804 4.06066 4.18934C4.34196 4.47064 4.5 4.85218 4.5 5.25V21C4.5 21.3978 4.34196 21.7794 4.06066 22.0607C3.77936 22.342 3.39782 22.5 3 22.5ZM11.25 8.25C11.25 7.85218 11.092 7.47064 10.8107 7.18934C10.5294 6.90804 10.1478 6.75 9.75 6.75H6.75C6.35218 6.75 5.97064 6.90804 5.68934 7.18934C5.40804 7.47064 5.25 7.85218 5.25 8.25V9.5625C5.25 9.61223 5.26975 9.65992 5.30492 9.69508C5.34008 9.73025 5.38777 9.75 5.4375 9.75H11.0625C11.1122 9.75 11.1599 9.73025 11.1951 9.69508C11.2302 9.65992 11.25 9.61223 11.25 9.5625V8.25ZM5.25 21C5.25 21.3978 5.40804 21.7794 5.68934 22.0607C5.97064 22.342 6.35218 22.5 6.75 22.5H9.75C10.1478 22.5 10.5294 22.342 10.8107 22.0607C11.092 21.7794 11.25 21.3978 11.25 21V19.5938C11.25 19.5689 11.2401 19.545 11.2225 19.5275C11.205 19.5099 11.1811 19.5 11.1562 19.5H5.34375C5.31889 19.5 5.29504 19.5099 5.27746 19.5275C5.25988 19.545 5.25 19.5689 5.25 19.5938V21Z" fill="currentColor" />
                <path d="M11.1562 11.25H5.34375C5.29197 11.25 5.25 11.292 5.25 11.3438V17.9062C5.25 17.958 5.29197 18 5.34375 18H11.1562C11.208 18 11.25 17.958 11.25 17.9062V11.3438C11.25 11.292 11.208 11.25 11.1562 11.25Z" fill="currentColor" />
                <path d="M15 22.5H13.5C13.1022 22.5 12.7206 22.342 12.4393 22.0607C12.158 21.7794 12 21.3978 12 21V3C12 2.60218 12.158 2.22064 12.4393 1.93934C12.7206 1.65804 13.1022 1.5 13.5 1.5H15C15.3978 1.5 15.7794 1.65804 16.0607 1.93934C16.342 2.22064 16.5 2.60218 16.5 3V21C16.5 21.3978 16.342 21.7794 16.0607 22.0607C15.7794 22.342 15.3978 22.5 15 22.5ZM23.2448 20.8805L21.7341 4.94297C21.6647 4.20937 20.94 3.67734 20.1155 3.75656L18.6225 3.89719C17.798 3.97547 17.1858 4.63359 17.2552 5.36719L18.7659 21.3047C18.8353 22.0383 19.56 22.5703 20.3845 22.4911L21.8775 22.3505C22.702 22.2722 23.3142 21.6141 23.2448 20.8805Z" fill="currentColor" />
              </svg>
            </div>
            <span class="icon-label">Мои книги</span>
          </button>
          
          <!-- Избранное -->
          <button 
            class="icon-btn"
            :class="{ active: isActive('/profile/favorites') }"
            @click="handleProtectedClick('favorites')"
          >
            <div class="icon-wrapper">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M12 5.49999L11.46 6.01999C11.53 6.09257 11.6138 6.15031 11.7066 6.18974C11.7994 6.22918 11.8992 6.24951 12 6.24951C12.1008 6.24951 12.2006 6.22918 12.2934 6.18974C12.3862 6.15031 12.47 6.09257 12.54 6.01999L12 5.49999ZM9.426 18.322C7.91 17.127 6.253 15.96 4.938 14.48C3.65 13.028 2.75 11.335 2.75 9.13699H1.25C1.25 11.803 2.36 13.837 3.817 15.476C5.247 17.086 7.071 18.376 8.497 19.5L9.426 18.322ZM2.75 9.13699C2.75 6.98699 3.965 5.18299 5.624 4.42399C7.236 3.68699 9.402 3.88199 11.46 6.01999L12.54 4.98099C10.1 2.44399 7.264 2.02499 5 3.05999C2.786 4.07299 1.25 6.42499 1.25 9.13699H2.75ZM8.497 19.5C9.01 19.904 9.56 20.334 10.117 20.66C10.674 20.986 11.31 21.25 12 21.25V19.75C11.69 19.75 11.326 19.63 10.874 19.365C10.421 19.101 9.952 18.737 9.426 18.322L8.497 19.5ZM15.503 19.5C16.929 18.375 18.753 17.087 20.183 15.476C21.64 13.836 22.75 11.803 22.75 9.13699H21.25C21.25 11.335 20.35 13.028 19.062 14.48C17.747 15.96 16.09 17.127 14.574 18.322L15.503 19.5ZM22.75 9.13699C22.75 6.42499 21.215 4.07299 19 3.05999C16.736 2.02499 13.902 2.44399 11.46 4.97999L12.54 6.01999C14.598 3.88299 16.764 3.68699 18.376 4.42399C20.035 5.18299 21.25 6.98599 21.25 9.13699H22.75ZM14.574 18.322C14.048 18.737 13.579 19.101 13.126 19.365C12.673 19.629 12.31 19.75 12 19.75V21.25C12.69 21.25 13.326 20.985 13.883 20.66C14.441 20.334 14.99 19.904 15.503 19.5L14.574 18.322Z"  fill="currentColor" />
              </svg>
            </div>
            <span class="icon-label">Избранное</span>
          </button>
          
          <!-- Войти/Профиль -->
          <ProfileDropdownMenu v-if="isAuthenticated" :class="{ active: isActive('/profile/edit') }" />
          <button v-else class="auth-btn" @click="openAuthModal">
            <span class="auth-text">Войти</span>
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M8.99995 20.4001C8.84082 20.4001 8.68821 20.3369 8.57569 20.2244C8.46316 20.1118 8.39995 19.9592 8.39995 19.8001C8.39995 19.641 8.46316 19.4884 8.57569 19.3758C8.68821 19.2633 8.84082 19.2001 8.99995 19.2001H16.8C17.4365 19.2001 18.0469 18.9472 18.497 18.4972C18.9471 18.0471 19.1999 17.4366 19.1999 16.8001V7.2001C19.1999 6.56358 18.9471 5.95313 18.497 5.50304C18.0469 5.05295 17.4365 4.8001 16.8 4.8001H8.99995C8.84082 4.8001 8.68821 4.73688 8.57569 4.62436C8.46316 4.51184 8.39995 4.35923 8.39995 4.2001C8.39995 4.04097 8.46316 3.88836 8.57569 3.77583C8.68821 3.66331 8.84082 3.6001 8.99995 3.6001H16.8C17.7547 3.6001 18.6704 3.97938 19.3455 4.65451C20.0207 5.32964 20.4 6.24532 20.4 7.2001V16.8001C20.4 17.7549 20.0207 18.6706 19.3455 19.3457C18.6704 20.0208 17.7547 20.4001 16.8 20.4001H8.99995ZM9.17515 7.3753C9.23089 7.31942 9.2971 7.27509 9.36999 7.24484C9.44288 7.21459 9.52103 7.19903 9.59995 7.19903C9.67887 7.19903 9.75702 7.21459 9.82991 7.24484C9.9028 7.27509 9.96902 7.31942 10.0248 7.3753L14.2248 11.5753C14.2806 11.631 14.325 11.6972 14.3552 11.7701C14.3855 11.843 14.401 11.9212 14.401 12.0001C14.401 12.079 14.3855 12.1572 14.3552 12.2301C14.325 12.303 14.2806 12.3692 14.2248 12.4249L10.0248 16.6249C9.91209 16.7376 9.75928 16.8009 9.59995 16.8009C9.44062 16.8009 9.28781 16.7376 9.17515 16.6249C9.06249 16.5122 8.99919 16.3594 8.99919 16.2001C8.99919 16.0408 9.06249 15.888 9.17515 15.7753L12.3516 12.6001H1.79995C1.64082 12.6001 1.48821 12.5369 1.37569 12.4244C1.26317 12.3118 1.19995 12.1592 1.19995 12.0001C1.19995 11.841 1.26317 11.6884 1.37569 11.5758C1.48821 11.4633 1.64082 11.4001 1.79995 11.4001H12.3516L9.17515 8.2249C9.11927 8.16916 9.07494 8.10295 9.0447 8.03006C9.01445 7.95716 8.99888 7.87902 8.99888 7.8001C8.99888 7.72118 9.01445 7.64303 9.0447 7.57014C9.07494 7.49724 9.11927 7.43103 9.17515 7.3753Z" fill="currentColor" />
            </svg>
          </button>
        </div>
      </div>
    </div>
  </header>
  <AuthModal v-if="showAuthModal" @close="showAuthModal = false" @success="onAuthSuccess" />
</template>

<script>
import { ref, watch, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import SuggestDropdown from './SuggestDropdown.vue'
import AuthModal from './AuthModal.vue'
import { useFavorites } from '../stores/favorites';
import { eventBus } from '../utils/eventBus';
import { authService } from '../services/authService';
import ProfileDropdownMenu from './ProfileDropdownMenu.vue'

export default {
  name: 'AppHeader',
  components: { SuggestDropdown, AuthModal, ProfileDropdownMenu },
  setup() {
    const route = useRoute()
    const router = useRouter()
    const favoritesStore = useFavorites();
    const searchQueryLocal = ref('')
    const showSuggest = ref(false)
    const isAuthenticated = authService.isAuthenticated;
    const showAuthModal = ref(false)
    
    const currentRoute = computed(() => route.path)
    
    const isActive = (path) => {
      if (path === '/catalog') {
        return currentRoute.value.startsWith('/catalog') || 
              (currentRoute.value.startsWith('/books') && route.query.from === 'catalog')
      }
      if (path === '/profile/edit') {
        return currentRoute.value === '/profile/edit'
      }
      if (path === '/profile/my-books') {
        return currentRoute.value === '/profile/my-books' || currentRoute.value.startsWith('/profile/my-books/')
      }
      if (path === '/profile/favorites') {
        return currentRoute.value === '/profile/favorites'
      }
      return currentRoute.value.startsWith(path)
    }
    
    const goToHome = () => router.push('/')
    const goToCatalog = () => router.push('/catalog')
    const goToAuthors = () => router.push('/authors')
    const goToMyBooks = () => router.push('/profile/my-books')
    const goToFavorites = () => router.push('/profile/favorites')

    watch(
      () => route.query.q,
      (newQ) => {
        if (newQ !== undefined) searchQueryLocal.value = newQ
      },
      { immediate: true }
    )

    onMounted(() => {
      if (authService.isAuthenticated.value) {
        favoritesStore.loadFavorites();
      }
      eventBus.addEventListener('show-auth-modal', () => {
        console.log('show-auth-modal received'); // проверка
        showAuthModal.value = true;
      });
    });

    const performSearch = () => {
      const q = searchQueryLocal.value.trim()
      if (!q) return
      router.push({ name: 'Search', query: { q } })
      showSuggest.value = false
      document.activeElement?.blur()
    }

    const closeSuggest = () => {
      setTimeout(() => {
        showSuggest.value = false
      }, 200)
    }

    const openAuthModal = () => {
      if (isAuthenticated.value) {
        router.push('/profile')
      } else {
        showAuthModal.value = true
      }
    }

    const onAuthSuccess = () => {
      showAuthModal.value = false;
      favoritesStore.loadFavorites();
      router.push('/'); 
    };
    
    const handleProtectedClick = (page) => {
      if (!isAuthenticated.value) {
        if (confirm('Войдите, чтобы продолжить')) {
          openAuthModal()
        }
        return
      }
      if (page === 'myBooks') goToMyBooks()
      if (page === 'favorites') goToFavorites()
    }

    const handleAuthClick = openAuthModal
    
    return {
      searchQueryLocal,
      showSuggest,
      isActive,
      isAuthenticated, 
      showAuthModal, 
      openAuthModal, 
      onAuthSuccess, 
      goToCatalog,
      goToAuthors,
      goToMyBooks,
      goToFavorites,
      performSearch,
      handleProtectedClick,
      handleAuthClick,
      closeSuggest
    }
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Rubik:wght@400;500&display=swap');

:root {
  --font-family: "Rubik", sans-serif;
  --burnt-umber: #902923;
  --eerie-black: #1b1b1b;
  --dim-grey: #73706c;
  --white: #fff;
  --ash-grey: #aec3b0;
}

.app-header {
  background: #FFFFFF;
  border-bottom: 1px solid #F0F0F0;
  padding: 9px 0;
  position: sticky;
  top: 0;
  z-index: 1000;
  font-family: 'Rubik', sans-serif;
  color: var(--eerie-black);
  font-size: 16px;
}

.container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 25px;
  align-items: center;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-direction: row;
  gap: 40px;
}

/* Логотип */
.logo {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  flex-direction: row;
  gap: 2px;
  cursor: pointer;
}

.logo-text {
  font-weight: 700;
  font-size: 36px;
  line-height: 150%;
}

/* Центральные кнопки */
.center-buttons {
  display: flex;
  align-items: center;
  gap: 16px;
  flex: 1;
  max-width: 600px;
}

/* Стили для кнопки Каталог */
.catalog-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: row;
  gap: 8px;
  padding: 12px 20px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  text-decoration: none;
  border: none;
  background: transparent;
  color: var(--eerie-black); 
  font-weight: 500;
  font-size: 18px;
}

/* Поиск */
.search-wrapper {
  display: flex;
  align-items: center;
  flex: 1;
  position: relative;
}

.search-container {
  display: flex;
  align-items: center;
  flex: 1;
  background: var(--white);
  border: 1px solid #E0E0E0;
  border-radius: 8px;
  padding: 0 8px;
  transition: all 0.3s ease;
}

.search-container:focus-within {
  border-color: #902923;
}

.search-input {
  flex: 1;
  padding: 12px 8px;
  border: none;
  background: transparent;
  outline: none;
}

.search-input::placeholder {
  color: #73706C;
}

.search-icon {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: transparent;
  border: none;
  cursor: pointer;
  border-radius: 6px;
  transition: background-color 0.3s ease;
}

.search-icon:hover {
  background: rgba(27, 27, 27, 0.05);
}

/* Правые кнопки */
.right-buttons {
  display: flex;
  align-items: center;
  gap: 24px;
  flex-shrink: 0;
}

.icon-btn {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 4px 8px;
  background: transparent;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  min-width: 64px;
}

.icon-wrapper {
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.icon-label {
  font-size: 12px;
  font-weight: 400;
}

/* Кнопки */
.catalog-btn,
.icon-btn {
  border: none;
  transition: all 0.3s ease;
}

.catalog-btn:hover,
.icon-btn:hover {
  background: rgba(138, 102, 100, 0.1);
  color: #902923;
}

.btn-text,
.icon-label {
  white-space: nowrap;
}

/* Кнопка авторизации */
.auth-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 20px;
  background: transparent;
  border: 1px solid #8DA98F;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 400;
  transition: all 0.3s ease;
}

.auth-btn:hover {
  background: rgba(174, 195, 176, 0.1);
  color: #8DA98F;
}

.auth-text {
  white-space: nowrap;
}

/* Кнопки активны*/
.catalog-btn.active {
  background: #902923;
  color: #FFFFFF;
}

.icon-btn.active,
.auth-btn.active {
  background: transparent;
  color: #902923;
}

.catalog-btn:active .btn-text,
.icon-btn:active .icon-label,
.auth-btn:active .auth-text {
  font-weight: 500;
}

/* Адаптивность */
@media (max-width: 1200px) {
  .header-content {
    gap: 24px;
  }
  
  .center-buttons {
    flex-direction: column;
    gap: 12px;
    max-width: 400px;
  }
  
  .catalog-btn, .auth-btn {
    padding: 10px 16px;
  }
}

@media (max-width: 768px) {
  .container {
    padding: 0 16px;
  }
  
  .header-content {
    flex-direction: column;
    gap: 16px;
  }
  
  .center-buttons {
    width: 100%;
    max-width: 100%;
  }
  
  .right-buttons {
    width: 100%;
    justify-content: space-between;
    gap: 12px;
  }
  
  .icon-btn {
    min-width: auto;
    padding: 4px;
  }
  
  .logo-text {
    font-size: 20px;
  }
  
  .catalog-btn {
    font-size: 16px;
    padding: 8px 12px;
  }
}
</style>