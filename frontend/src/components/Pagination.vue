<template>
  <div class="pagination">
    <button class="page-arrow" :disabled="currentPage === 0" @click="goToPage(currentPage - 1)">
      <svg v-if="currentPage === 0" width="16" height="16" viewBox="0 0 16 16" fill="none">
        <path d="M9.33333 4.66668L6 8.00001L9.33333 11.3333" stroke="#73706C" stroke-linecap="round" stroke-linejoin="round" />
      </svg>
      <svg v-else width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
        <path d="M9.33333 4.66683L6 8.00016L9.33333 11.3335" stroke="#902923" stroke-linecap="round" stroke-linejoin="round" />
        <path d="M9.33333 4.66683L6 8.00016L9.33333 11.3335" stroke="#902923" stroke-opacity="0.2" stroke-linecap="round" stroke-linejoin="round" />
      </svg>
    </button>

    <div class="page-numbers">
      <template v-for="page in visiblePages" :key="page">
        <button
          v-if="page !== '...'"
          class="page-number"
          :class="{ active: page - 1 === currentPage }"
          @click="goToPage(page - 1)"
        >
          {{ page }}
        </button>
        <span v-else class="dots">...</span>
      </template>
    </div>

    <button class="page-arrow" :disabled="currentPage === totalPages - 1" @click="goToPage(currentPage + 1)">
      <svg v-if="currentPage === totalPages - 1" width="16" height="16" viewBox="0 0 16 16" fill="none">
        <path d="M6.66667 11.3333L10 7.99999L6.66667 4.66666" stroke="#73706C" stroke-linecap="round" stroke-linejoin="round" />
      </svg>
      <svg v-else width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
        <path d="M6.66667 11.3332L10 7.99984L6.66667 4.6665" stroke="#902923" stroke-linecap="round" stroke-linejoin="round" />
        <path d="M6.66667 11.3332L10 7.99984L6.66667 4.6665" stroke="#902923" stroke-opacity="0.2" stroke-linecap="round" stroke-linejoin="round" />
      </svg>
    </button>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  currentPage: { type: Number, required: true }, // 0-индексация
  totalPages: { type: Number, required: true },
  maxVisible: { type: Number, default: 5 } // сколько страниц показывать вокруг текущей
})

const emit = defineEmits(['pageChange'])

const goToPage = (page) => {
  if (page >= 0 && page < props.totalPages) {
    emit('pageChange', page)
  }
}

const visiblePages = computed(() => {
  const total = props.totalPages
  const current = props.currentPage
  const max = props.maxVisible

  if (total <= max) {
    return Array.from({ length: total }, (_, i) => i + 1)
  }

  const pages = []
  const leftOffset = Math.floor(max / 2)
  const rightOffset = max - leftOffset - 1

  let start = current - leftOffset
  let end = current + rightOffset

  if (start < 0) {
    end += -start
    start = 0
  }
  if (end >= total) {
    start -= end - total + 1
    end = total - 1
  }

  if (start > 0) {
    pages.push(1)
    if (start > 1) pages.push('...')
  }

  for (let i = start; i <= end; i++) {
    pages.push(i + 1)
  }

  if (end < total - 1) {
    if (end < total - 2) pages.push('...')
    pages.push(total)
  }

  return pages
})
</script>

<style scoped>
.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: row;
  gap: 10px;
}

.page-numbers {
  display: flex;
  align-items: center;
  gap: 10px;
}

.page-number, .dots {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 26px;
  height: 18px;
  font-family: var(--font-family, 'Rubik', sans-serif);
  font-weight: 400;
  font-size: 12px;
  line-height: 150%;
  text-align: center;
  cursor: pointer;
  background: none;
  border: none;
  padding: 0;
}

.page-number {
  color: #902923;  
}

.page-number.active {
  color: var(--dim-grey, #73706C);
  font-weight: 500;
}

.dots {
  color: var(--dim-grey, #73706C);
  cursor: default;
}

.page-arrow {
  background: none;
  border: none;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 0;
}

.page-arrow:disabled {
  cursor: not-allowed;
}
</style>