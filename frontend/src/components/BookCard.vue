<template>
  <div class="book-card" @click="goToBookPage">
    <!-- Обложка книги -->
    <div class="book-cover">
      <img 
        :src="coverImageUrl" 
        :alt="book.title"
        class="cover-image"
        @error="handleImageError"
      >
    </div>
    
    <!-- Информация о книге -->
    <div class="book-info">
      <h3 class="book-title" :title="book.title">{{ book.title }}</h3>
      
      <div class="book-author">
        <span class="author-label">Автор:</span>
        <span class="author-name">{{ book.authorShortName || book.authorName || 'Автор не указан' }}</span>
      </div>
      
      <div class="book-stats">
        <div class="rating-info">
          <div class="rating-icon">
            <svg width="12" height="12" viewBox="0 0 12 12" fill="none" xmlns="http://www.w3.org/2000/svg">
              <!-- SVG звезды -->
              <path d="M3.66869 3.28407L0.937431 3.68006L0.889056 3.6899C0.815825 3.70935 0.749066 3.74787 0.695596 3.80155C0.642125 3.85523 0.603859 3.92214 0.584706 3.99545C0.565552 4.06876 0.566197 4.14583 0.586574 4.21881C0.606952 4.29178 0.646332 4.35804 0.700693 4.41082L2.67936 6.33683L2.21273 9.05739L2.20717 9.10448C2.20268 9.18022 2.21841 9.25579 2.25274 9.32345C2.28707 9.39112 2.33876 9.44844 2.40252 9.48956C2.46629 9.53068 2.53984 9.55411 2.61564 9.55745C2.69143 9.5608 2.76676 9.54394 2.8339 9.5086L5.27663 8.22431L7.71378 9.5086L7.75659 9.5283C7.82726 9.55613 7.90405 9.56466 7.9791 9.55302C8.05415 9.54138 8.12475 9.50999 8.18366 9.46206C8.24257 9.41412 8.28767 9.35139 8.31433 9.28027C8.341 9.20916 8.34826 9.13224 8.33538 9.05739L7.86833 6.33683L9.84785 4.41039L9.88124 4.374C9.92894 4.31526 9.96021 4.24491 9.97188 4.17014C9.98354 4.09537 9.97517 4.01885 9.94763 3.94836C9.92009 3.87788 9.87436 3.81595 9.81509 3.76889C9.75583 3.72184 9.68515 3.69133 9.61025 3.68049L6.87899 3.28407L5.65806 0.809666C5.62273 0.737974 5.56804 0.677604 5.50017 0.635389C5.43231 0.593175 5.35398 0.570801 5.27406 0.570801C5.19413 0.570801 5.11581 0.593175 5.04794 0.635389C4.98008 0.677604 4.92538 0.737974 4.89005 0.809666L3.66869 3.28407Z" fill="#1B1B1B" />
            </svg>
          </div>
          <span class="rating-value">{{ book.rating || 0 }}</span>
          <span class="rating-count">({{ book.ratingCount || 0 }})</span>
        </div>
        
        <!-- Иконка избранного -->
        <button 
          class="fav-btn"
          @click.stop="toggleFavorite"
          :title="isAuthenticated ? (isFavorite ? 'Удалить из избранного' : 'Добавить в избранное') : 'Войдите, чтобы добавить в избранное'"
        >
          <svg 
            v-if="isFavorite" 
            width="18" 
            height="18" 
            viewBox="0 0 18 18" 
            fill="none" 
            xmlns="http://www.w3.org/2000/svg"
            :class="{ 'favorite-active': isFavorite }"
          >
            <path d="M1.5 6.85279C1.5 10.5 4.515 12.4433 6.7215 14.1833C7.5 14.7968 8.25 15.375 9 15.375C9.75 15.375 10.5 14.7975 11.2785 14.1825C13.4858 12.444 16.5 10.5 16.5 6.85354C16.5 3.20704 12.375 0.618792 9 4.12579C5.625 0.618792 1.5 3.20554 1.5 6.85279Z" fill="#902923" />
            <path d="M1.5 6.85279C1.5 10.5 4.515 12.4433 6.7215 14.1833C7.5 14.7968 8.25 15.375 9 15.375C9.75 15.375 10.5 14.7975 11.2785 14.1825C13.4858 12.444 16.5 10.5 16.5 6.85354C16.5 3.20704 12.375 0.618792 9 4.12579C5.625 0.618792 1.5 3.20554 1.5 6.85279Z" fill="#902923" fill-opacity="0.2" />
          </svg>
          
          <svg 
            v-else 
            width="18" 
            height="18" 
            viewBox="0 0 20 20" 
            fill="none" 
            xmlns="http://www.w3.org/2000/svg"
          >
            <!-- SVG пустого сердца -->
            <g clip-path="url(#clip0_320_3512)">
              <path d="M9.13259 3.96956L8.72162 4.36532C8.77486 4.42056 8.8387 4.4645 8.90931 4.49451C8.97992 4.52453 9.05586 4.54 9.13259 4.54C9.20932 4.54 9.28525 4.52453 9.35587 4.49451C9.42648 4.4645 9.49032 4.42056 9.54356 4.36532L9.13259 3.96956ZM5.32804 12.2804C5.21097 12.1841 5.06045 12.1382 4.90958 12.153C4.75872 12.1677 4.61988 12.2417 4.5236 12.3588C4.42732 12.4758 4.38149 12.6263 4.39619 12.7772C4.41089 12.9281 4.48492 13.0669 4.60199 13.1632L5.32804 12.2804ZM1.78225 9.99033C1.81823 10.0561 1.86681 10.1141 1.92522 10.1611C1.98363 10.2081 2.05072 10.2431 2.12266 10.2642C2.1946 10.2852 2.26999 10.2919 2.34452 10.2839C2.41904 10.2758 2.49125 10.2531 2.55701 10.2171C2.62278 10.1811 2.68081 10.1326 2.7278 10.0742C2.77479 10.0157 2.80981 9.94866 2.83087 9.87671C2.85193 9.80477 2.85861 9.72938 2.85054 9.65486C2.84246 9.58033 2.81979 9.50812 2.78381 9.44236L1.78225 9.99033ZM2.09276 6.73755C2.09276 5.10126 3.01746 3.72831 4.28006 3.15066C5.50689 2.58976 7.15535 2.73817 8.72162 4.36532L9.54356 3.57457C7.68657 1.64376 5.5282 1.32487 3.80515 2.11257C2.12016 2.88353 0.951172 4.67355 0.951172 6.73755H2.09276ZM6.46659 14.6244C6.85701 14.9319 7.2756 15.2592 7.69951 15.5073C8.12342 15.7554 8.60746 15.9563 9.13259 15.9563V14.8147C8.89666 14.8147 8.61963 14.7234 8.27563 14.5217C7.93087 14.3208 7.57393 14.0437 7.17361 13.7279L6.46659 14.6244ZM11.7986 14.6244C12.8839 13.7682 14.272 12.788 15.3604 11.5619C16.4692 10.3138 17.314 8.76654 17.314 6.73755H16.1724C16.1724 8.41036 15.4875 9.69884 14.5072 10.8039C13.5064 11.9303 12.2453 12.8184 11.0916 13.7279L11.7986 14.6244ZM17.314 6.73755C17.314 4.67355 16.1458 2.88353 14.46 2.11257C12.737 1.32487 10.5801 1.64376 8.72162 3.57381L9.54356 4.36532C11.1098 2.73893 12.7583 2.58976 13.9851 3.15066C15.2477 3.72831 16.1724 5.1005 16.1724 6.73755H17.314ZM11.0916 13.7279C10.6912 14.0437 10.3343 14.3208 9.98954 14.5217C9.64478 14.7226 9.36852 14.8147 9.13259 14.8147V15.9563C9.65772 15.9563 10.1418 15.7546 10.5657 15.5073C10.9903 15.2592 11.4082 14.9319 11.7986 14.6244L11.0916 13.7279ZM7.17361 13.7279C6.56781 13.2507 5.95211 12.7941 5.32804 12.2804L4.60199 13.1632C5.23367 13.683 5.90188 14.1792 6.46659 14.6244L7.17361 13.7279ZM2.78381 9.44312C2.32529 8.61543 2.08732 7.68374 2.09276 6.73755H0.951172C0.951172 7.98417 1.27082 9.05498 1.78225 9.99033L2.78381 9.44312Z" fill="#902923" />
            </g>
          </svg>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'BookCard',
  props: {
    book: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      isFavorite: false,
      isAuthenticated: false
    }
  },
  computed: {
    coverImageUrl() {
      return this.book.coverUrl?.trim() ? this.book.coverUrl : '/images/cover.png'
    }
  },
  methods: {
    handleImageError(e) {
      e.target.src = '/images/cover.png'
    },

    goToBookPage() {
      const from = this.$route?.name || 'catalog'
      this.$router?.push({ path: `/books/${this.book.id}`, query: { from } })
    },
    
    toggleFavorite(e) {
      e.stopPropagation();
      if (!this.isAuthenticated) {
        this.showAuthNotification();
        return;
      }
      
      this.isFavorite = !this.isFavorite;
    },
    
    showAuthNotification() {
      if (confirm('Для добавления в избранное необходимо войти в систему. Перейти на страницу входа?')) {
        this.$router.push('/login');
      }
    }
  }
}
</script>

<style scoped>
/* Основные стили с шрифтом Manrope */
@import url('https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;600;700&display=swap');

.book-card {
  font-family: 'Manrope', sans-serif;
  background: #FFFFFF;
  border-radius: 18px;
  padding: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  max-width: 300px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  flex-direction: column;
  width: 140px;
  height: 270px;
}

.book-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
  border-color: #E0E0E0;
}

/* Обложка */
.book-cover {
  position: relative;
  width: 100%;
  height: 190px;
  border-radius: 12px;
  overflow: hidden;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.cover-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.rating-badge {
  position: absolute;
  top: 12px;
  right: 12px;
  background: rgba(255, 255, 255, 0.95);
  padding: 4px 8px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
  color: #1B1B1B;
  display: flex;
  align-items: center;
  gap: 2px;
  backdrop-filter: blur(4px);
}

/* Информация */
.book-info {
  width: 100%;
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  flex-direction: column;
  flex: 1;
  min-height: 0;
}

.book-title {
  width: 100%; 
  font-size: 14px;
  font-weight: 600;
  color: #1B1B1B;
  margin: 8px 0 6px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  line-height: 1.4;          
  min-height: 20px;
}

.book-author {
  display: flex;
  align-items: center;
  gap: 2px;
  font-size: 8px;
  width: 100%;
}

.author-label {
  font-weight: 600;
  color: #1B1B1B;
}

.author-name {
  font-weight: 400;
  color: #1B1B1B;
}

/* Статистика */
.book-stats {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  margin-top: 4px;
  margin-top: auto;
}

.rating-info {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 8px;
}

.rating-value {
  font-weight: 400;
  color: #1B1B1B;
}

.rating-count {
  font-weight: 400;
  color: #B2AEAB;
}

/* Кнопка избранного */
.fav-btn {
  background: transparent;
  border: none;
  padding: 4px;
  cursor: pointer;
  border-radius: 50%;
  width: 28px;
  height: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  margin-left: auto;
}

.fav-btn:hover {
  background: rgba(144, 41, 35, 0.1);
}

.fav-btn:hover svg {
  opacity: 0.8;
}

/* Активированное избранное */
.favorite-active {
  opacity: 1;
}

.fav-btn:hover .favorite-active {
  opacity: 0.5;
}

/* Адаптивность */
@media (max-width: 768px) {
  .book-card {
    max-width: 100%;
  }
  
  .book-cover {
    height: 180px;
  }
}
</style>