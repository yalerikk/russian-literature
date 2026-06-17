<template>
  <div class="reader-page" :class="`theme-${currentTheme}`">

    <!-- Toast -->
    <Transition name="toast">
      <div v-if="toastVisible" class="toast">
        🎉 Книга прочитана! Добавлена в «Прочитано»
      </div>
    </Transition>

    <!-- ══════════════════════════════════
         TOOLBAR
    ══════════════════════════════════ -->
    <header class="reader-toolbar">
      <div class="toolbar-left">
        <button class="icon-btn" @click="goBack" aria-label="Назад">
          <svg width="36" height="36" viewBox="0 0 36 36" fill="none">
            <path d="M21 10.5L13.5 18L21 25.5" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </button>
        <span class="book-info">{{ authorShortName }}&nbsp;&nbsp;«{{ bookTitle }}»</span>
      </div>

      <div class="toolbar-right">
        <div class="theme-group">
          <span class="theme-label">Темы:</span>
          <button
            v-for="t in THEMES" :key="t.id"
            class="theme-btn"
            :class="['theme-btn--' + t.id, { active: currentTheme === t.id }]"
            @click="setTheme(t.id)"
          >{{ t.label }}</button>
        </div>

        <button class="icon-btn" @click="showToc = true" aria-label="Оглавление">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
            <path d="M5 8H19M5 12H19M5 16H11" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </button>

        <button class="icon-btn" @click="showSettings = true" aria-label="Настройки">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
            <path d="M12 15C13.6569 15 15 13.6569 15 12C15 10.3431 13.6569 9 12 9C10.3431 9 9 10.3431 9 12C9 13.6569 10.3431 15 12 15Z" stroke="currentColor" stroke-width="2.25"/>
            <path d="M13.765 2.152C13.398 2 12.932 2 12 2C11.068 2 10.602 2 10.235 2.152C9.99214 2.25251 9.77151 2.3999 9.58569 2.58572C9.39986 2.77155 9.25248 2.99218 9.15196 3.235C9.05996 3.458 9.02296 3.719 9.00896 4.098C9.00245 4.37193 8.92657 4.63973 8.7884 4.87635C8.65024 5.11298 8.45432 5.31069 8.21896 5.451C7.97977 5.58477 7.71055 5.65567 7.43649 5.65707C7.16244 5.65847 6.89251 5.59032 6.65196 5.459C6.31596 5.281 6.07296 5.183 5.83196 5.151C5.30628 5.08187 4.77466 5.22431 4.35396 5.547C4.03996 5.79 3.80596 6.193 3.33996 7C2.87396 7.807 2.63996 8.21 2.58896 8.605C2.5546 8.86545 2.57188 9.13012 2.63983 9.38389C2.70778 9.63767 2.82505 9.87556 2.98496 10.084C3.13296 10.276 3.33996 10.437 3.66096 10.639C4.13396 10.936 4.43796 11.442 4.43796 12C4.43796 12.558 4.13396 13.064 3.66096 13.36C3.33996 13.563 3.13196 13.724 2.98496 13.916C2.82505 14.1244 2.70778 14.3623 2.63983 14.6161C2.57188 14.8699 2.5546 15.1345 2.58896 15.395C2.64096 15.789 2.87396 16.193 3.33896 17C3.80596 17.807 4.03896 18.21 4.35396 18.453C4.5624 18.6129 4.8003 18.7302 5.05407 18.7981C5.30784 18.8661 5.57251 18.8834 5.83296 18.849C6.07296 18.817 6.31596 18.719 6.65196 18.541C6.89251 18.4097 7.16244 18.3415 7.43649 18.3429C7.71055 18.3443 7.97977 18.4152 8.21896 18.549C8.70196 18.829 8.98896 19.344 9.00896 19.902C9.02296 20.282 9.05896 20.542 9.15196 20.765C9.25248 21.0078 9.39986 21.2284 9.58569 21.4143C9.77151 21.6001 9.99214 21.7475 10.235 21.848C10.602 22 11.068 22 12 22C12.932 22 13.398 22 13.765 21.848C14.0078 21.7475 14.2284 21.6001 14.4142 21.4143C14.6001 21.2284 14.7474 21.0078 14.848 20.765C14.94 20.542 14.977 20.282 14.991 19.902C15.011 19.344 15.298 18.828 15.781 18.549C16.0202 18.4152 16.2894 18.3443 16.5634 18.3429C16.8375 18.3415 17.1074 18.4097 17.348 18.541C17.684 18.719 17.927 18.817 18.167 18.849C18.4274 18.8834 18.6921 18.8661 18.9459 18.7981C19.1996 18.7302 19.4375 18.6129 19.646 18.453C19.961 18.211 20.194 17.807 20.66 17C21.126 16.193 21.36 15.79 21.411 15.395C21.4453 15.1345 21.428 14.8699 21.3601 14.6161C21.2921 14.3623 21.1749 14.1244 21.015 13.916C20.867 13.724 20.66 13.563 20.339 13.361C20.1049 13.2184 19.9108 13.0187 19.7749 12.7807C19.639 12.5427 19.5658 12.2741 19.562 12C19.562 11.442 19.866 10.936 20.339 10.64C20.66 10.437 20.868 10.276 21.015 10.084C21.1749 9.87556 21.2921 9.63767 21.3601 9.38389C21.428 9.13012 21.4453 8.86545 21.411 8.605C21.359 8.211 21.126 7.807 20.661 7C20.194 6.193 19.961 5.79 19.646 5.547C19.4375 5.38709 19.1996 5.26981 18.9459 5.20187C18.6921 5.13392 18.4274 5.11664 18.167 5.151C17.927 5.183 17.684 5.281 17.347 5.459C17.1065 5.59014 16.8368 5.6582 16.5629 5.6568C16.2891 5.6554 16.02 5.58459 15.781 5.451C15.5456 5.31069 15.3497 5.11298 15.2115 4.87635C15.0734 4.63973 14.9975 4.37193 14.991 4.098C14.977 3.718 14.941 3.458 14.848 3.235C14.7474 2.99218 14.6001 2.77155 14.4142 2.58572C14.2284 2.3999 14.0078 2.25251 13.765 2.152Z" stroke="currentColor" stroke-width="2.25"/>
          </svg>
        </button>
      </div>
    </header>

    <!-- ══════════════════════════════════
         MAIN
    ══════════════════════════════════ -->
    <main class="reader-main">
      <!-- Initial book loading -->
      <div v-if="isLoading" class="reader-loading">
        <div class="loading-spinner"></div>
        <p class="loading-text">
          {{ isGeneratingLocations ? `Подготовка страниц… ${locationsProgress}%` : 'Загрузка книги…' }}
        </p>
      </div>

      <!-- Recalculating after font/size change -->
      <div v-if="isRecalculating" class="recalc-overlay">
        <div class="loading-spinner"></div>
        <p class="loading-text">Пересчёт страниц…</p>
      </div>

      <div ref="viewerEl" class="epub-viewer" :class="{ hidden: isLoading }"></div>
    </main>

    <!-- ══════════════════════════════════
         FOOTER
    ══════════════════════════════════ -->
    <footer class="reader-footer">
      <!-- ref so we can measure its width for epub -->
      <div ref="footerInnerEl" class="footer-inner">

        <!-- Prev / Next + percent -->
        <div class="footer-nav-row">
          <button class="nav-text-btn" :disabled="currentPage <= 1" @click="prevPage">
            <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
              <path d="M9.33333 4.66683L6 8.00016L9.33333 11.3335" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            Предыдущая страница
          </button>

          <span v-if="readingPercent > 0" class="reading-percent">{{ readingPercent }}%</span>

          <button class="nav-text-btn" :disabled="currentPage >= totalPages" @click="nextPage">
            Следующая страница
            <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
              <path d="M6.66667 11.3332L10 7.99984L6.66667 4.6665" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </button>
        </div>

        <!-- Pagination card -->
        <div class="pagination-card">
          <div class="pagination-strip-row">
            <span class="pagination-label">Страницы:</span>
            <div class="page-strip">
              <button class="strip-arrow" :disabled="currentPage <= 1" @click="prevPage">
                <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
                  <path d="M9.33333 4.66683L6 8.00016L9.33333 11.3335"
                    :stroke="currentPage <= 1 ? '#73706C' : '#902923'"
                    stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </button>

              <template v-for="p in visiblePageNumbers" :key="p">
                <button v-if="p !== '...'" class="page-num-btn" :class="{ active: p === currentPage }" @click="goToPage(p)">{{ p }}</button>
                <span v-else class="page-dots">…</span>
              </template>

              <button class="strip-arrow" :disabled="currentPage >= totalPages" @click="nextPage">
                <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
                  <path d="M6.66667 11.3332L10 7.99984L6.66667 4.6665"
                    :stroke="currentPage >= totalPages ? '#73706C' : '#902923'"
                    stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </button>
            </div>
          </div>
        </div>

      </div>
    </footer>

    <!-- ══════════════════════════════════
         TOC
    ══════════════════════════════════ -->
    <Transition name="toc">
      <div v-if="showToc" class="overlay" @click.self="showToc = false">
        <div class="toc-panel">
          <div class="toc-panel__header">
            <span>Оглавление</span>
            <button class="icon-btn" @click="showToc = false">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
                <path d="M18 6L6 18M6 6L18 18" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
              </svg>
            </button>
          </div>
          <div class="toc-panel__list">
            <button v-for="item in toc" :key="item.href" class="toc-item" @click="goToChapter(item.href)">
              {{ item.label }}
            </button>
            <p v-if="toc.length === 0" class="toc-empty">Оглавление недоступно</p>
          </div>
        </div>
      </div>
    </Transition>

    <!-- ══════════════════════════════════
         SETTINGS
    ══════════════════════════════════ -->
    <Transition name="fade">
      <div v-if="showSettings" class="overlay" @click.self="showSettings = false">
        <div class="settings-modal">
          <div class="settings-modal__header">
            <span>Настройки отображения</span>
            <button class="icon-btn" @click="showSettings = false">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
                <path d="M18 6L6 18M6 6L18 18" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
              </svg>
            </button>
          </div>

          <div class="settings-group">
            <label class="settings-label">Шрифт</label>
            <div class="font-family-options">
              <button v-for="f in FONT_OPTIONS" :key="f.value"
                class="font-family-btn" :class="{ active: fontFamily === f.value }"
                :style="{ fontFamily: f.value }"
                @click="applyDisplaySettings(f.value, fontSize, lineHeight)">
                {{ f.label }}
              </button>
            </div>
          </div>

          <div class="settings-group">
            <label class="settings-label">Размер шрифта</label>
            <div class="size-row">
              <button class="size-btn" :disabled="fontSize <= 12 || isRecalculating"
                @click="applyDisplaySettings(fontFamily, fontSize - 2, lineHeight)">A−</button>
              <span class="size-display">{{ fontSize }}px</span>
              <button class="size-btn" :disabled="fontSize >= 28 || isRecalculating"
                @click="applyDisplaySettings(fontFamily, fontSize + 2, lineHeight)">A+</button>
            </div>
          </div>

          <div class="settings-group">
            <label class="settings-label">Межстрочный интервал</label>
            <div class="size-row">
              <button class="size-btn" :disabled="lineHeight <= 1.2 || isRecalculating"
                @click="applyDisplaySettings(fontFamily, fontSize, +(lineHeight - 0.1).toFixed(1))">−</button>
              <span class="size-display">{{ lineHeight.toFixed(1) }}</span>
              <button class="size-btn" :disabled="lineHeight >= 2.5 || isRecalculating"
                @click="applyDisplaySettings(fontFamily, fontSize, +(lineHeight + 0.1).toFixed(1))">+</button>
            </div>
          </div>

          <p v-if="isRecalculating" class="recalc-note">Пересчёт страниц…</p>
        </div>
      </div>
    </Transition>

  </div>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import ePub from 'epubjs'
import { apiClient } from '../services/api'

// ─── Constants ───────────────────────────────────────────────
const CHARS_PER_PAGE = 1024

const THEMES = [
  { id: 'light', label: '1' },
  { id: 'dark',  label: '2' },
  { id: 'sepia', label: '3' },
]

const THEME_COLORS = {
  light: { bg: '#F3F3F4', text: '#1B1B1B' },
  dark:  { bg: '#1e1e1e', text: '#eeeeee' },
  sepia: { bg: '#faf0e6', text: '#5b4636' },
}

const FONT_OPTIONS = [
  { label: 'Serif',      value: '"Times New Roman", serif' },
  { label: 'Sans-serif', value: '"Arial", sans-serif' },
  { label: 'Mono',       value: '"Courier New", monospace' },
]

// ─── Router ──────────────────────────────────────────────────
const route  = useRoute()
const router = useRouter()
const bookId = route.params.id

// ─── Refs ────────────────────────────────────────────────────
const viewerEl     = ref(null)   // epub.js mount point
const footerInnerEl = ref(null)  // used to measure width for epub container

// ─── State ───────────────────────────────────────────────────
const bookTitle       = ref('')
const authorShortName = ref('')

const isLoading             = ref(true)
const isGeneratingLocations = ref(false)
const isRecalculating       = ref(false)
const locationsProgress     = ref(0)

const currentTheme = ref('light')
const showToc      = ref(false)
const showSettings = ref(false)
const toastVisible = ref(false)

const fontFamily = ref('"Times New Roman", serif')
const fontSize   = ref(16)
const lineHeight = ref(1.5)

const currentPage    = ref(1)
const totalPages     = ref(1)
const readingPercent = ref(0)
const toc            = ref([])

let book       = null
let rendition  = null
let saveTimer  = null
let currentCfi = null
let bookStatus = null

// ─── Computed ────────────────────────────────────────────────
const visiblePageNumbers = computed(() => {
  const total   = totalPages.value
  const current = currentPage.value
  const MAX     = 7
  if (total <= MAX) return Array.from({ length: total }, (_, i) => i + 1)

  const half  = Math.floor(MAX / 2)
  let start   = Math.max(1, current - half)
  let end     = Math.min(total, current + half)
  if (current - half < 1)     end   = Math.min(total, MAX)
  if (current + half > total) start = Math.max(1, total - MAX + 1)

  const pages = []
  if (start > 1) { pages.push(1); if (start > 2) pages.push('...') }
  for (let i = start; i <= end; i++) pages.push(i)
  if (end < total) { if (end < total - 1) pages.push('...'); pages.push(total) }
  return pages
})

// ─── Lifecycle ───────────────────────────────────────────────
onMounted(() => {
  loadBook()
  window.addEventListener('beforeunload', onBeforeUnload)
})

onBeforeUnmount(() => {
  window.removeEventListener('beforeunload', onBeforeUnload)
  clearTimeout(saveTimer)
  rendition?.destroy()
  book?.destroy()
})

// ─── Helpers: measure container ──────────────────────────────
// Returns the pixel width of the footer-inner div — epub will use same width
function getEpubWidth() {
  if (footerInnerEl.value) {
    return footerInnerEl.value.clientWidth
  }
  // fallback: screen minus 64px padding (2 × 32px)
  return Math.min(window.innerWidth - 64, 856)
}

function getEpubHeight() {
  // available height = window - toolbar(72) - footer(approx 130)
  return window.innerHeight - 72 - 150
}

// ─── Load book ───────────────────────────────────────────────
async function loadBook() {
  try {
    const [bookData, fileData] = await Promise.all([
      apiClient.get(`/books/${bookId}`),
      apiClient.get(`/books/${bookId}/download?format=EPUB`),
    ])
    bookTitle.value       = bookData.title
    authorShortName.value = bookData.author?.shortName || ''

    const res = await fetch(fileData.url)
    if (!res.ok) throw new Error(`HTTP ${res.status}`)
    const buffer = await res.arrayBuffer()

    book = ePub(buffer)

    // Wait for DOM to paint so we can measure footerInnerEl
    await nextTick()

    rendition = book.renderTo(viewerEl.value, {
      width:  getEpubWidth(),
      height: getEpubHeight(),
      spread: 'none',
      flow:   'paginated',
    })

    // Register all themes upfront
    registerAllThemes()
    rendition.themes.select(currentTheme.value)

    await rendition.display()

    // Generate locations
    isGeneratingLocations.value = true
    book.locations.on('progress', pct => { locationsProgress.value = Math.round(pct * 100) })
    await book.locations.generate(CHARS_PER_PAGE)
    isGeneratingLocations.value = false
    totalPages.value = book.locations.length() || 1

    // TOC
    const nav = await book.loaded.navigation
    if (nav?.toc) {
      toc.value = nav.toc.map(i => ({ href: i.href, label: i.label?.trim() || i.href }))
    }

    // Restore progress (server-authoritative)
    await restoreProgress()

    isLoading.value = false
    rendition.on('relocated', onRelocated)

  } catch (err) {
    console.error('[Reader] Ошибка загрузки:', err)
    alert('Не удалось загрузить книгу.')
    router.push(`/books/${bookId}`)
  }
}

// ─── Theme registration ──────────────────────────────────────
// Register all three themes so select() works instantly without flicker.
// Font/size are baked in so the registered rules are always current.
function buildThemeRules(themeId) {
  const { bg, text } = THEME_COLORS[themeId]
  return {
    'html': { 'background': `${bg} !important` },
    'body': {
      'background':  `${bg} !important`,
      'color':       `${text} !important`,
      'font-family': `${fontFamily.value} !important`,
      'font-size':   `${fontSize.value}px !important`,
      'line-height': `${lineHeight.value} !important`,
    },
    'p, div, span, h1, h2, h3, h4, h5, h6, li, td, th': {
      'color': `${text} !important`,
    },
  }
}

function registerAllThemes() {
  THEMES.forEach(t => rendition.themes.register(t.id, buildThemeRules(t.id)))
}

// ─── Apply display settings + regenerate locations ───────────
// Called when font family, size, or line-height changes.
async function applyDisplaySettings(newFamily, newSize, newLineHeight) {
  fontFamily.value = newFamily
  fontSize.value   = Math.min(28, Math.max(12, newSize))
  lineHeight.value = parseFloat(Math.min(2.5, Math.max(1.2, newLineHeight)).toFixed(1))

  if (!rendition) return

  // 1. Re-register themes with new font values and re-select current
  registerAllThemes()
  rendition.themes.select(currentTheme.value)

  // 2. Re-render with the same width/height so page geometry is correct
  const pctBefore = currentCfi ? book.locations.percentageFromCfi(currentCfi) : 0

  isRecalculating.value = true

  // Resize rendition to same dimensions (triggers epub.js re-layout)
  rendition.resize(getEpubWidth(), getEpubHeight())

  // Regenerate locations to reflect new font size
  locationsProgress.value = 0
  book.locations.on('progress', pct => { locationsProgress.value = Math.round(pct * 100) })
  await book.locations.generate(CHARS_PER_PAGE)
  totalPages.value = book.locations.length() || 1

  // Restore position
  if (pctBefore > 0) {
    const cfi = book.locations.cfiFromPercentage(pctBefore)
    if (cfi) await rendition.display(cfi)
  }

  isRecalculating.value = false
}

// ─── Theme switch (no regeneration needed — only colors) ─────
function setTheme(id) {
  currentTheme.value = id
  if (!rendition) return
  // Re-register to bake current font values, then select
  registerAllThemes()
  rendition.themes.select(id)
}

// ─── Restore progress ────────────────────────────────────────
async function restoreProgress() {
  try {
    const statusRes = await apiClient.get(`/users/me/books/${bookId}/status`).catch(() => null)
    bookStatus = statusRes?.status || null

    if (bookStatus === 'READ') {
      const lastCfi = book.locations.cfiFromPercentage(1)
      if (lastCfi) await rendition.display(lastCfi)
      return
    }

    // Server is authoritative
    const progressRes = await apiClient.get(`/users/me/books/${bookId}/progress`).catch(() => ({ progress: 0 }))
    const serverPct = (progressRes?.progress || 0) / 100

    if (serverPct > 0) {
      const cfi = book.locations.cfiFromPercentage(serverPct)
      if (cfi) await rendition.display(cfi)
    } else {
      // Server has 0 — localStorage fallback (offline / dev reset)
      const local = parseFloat(localStorage.getItem(`reader_cfi_pct_${bookId}`) || '0')
      if (local > 0 && local < 1) {
        const cfi = book.locations.cfiFromPercentage(local)
        if (cfi) await rendition.display(cfi)
      }
    }
  } catch (e) {
    console.warn('[Reader] restoreProgress error:', e)
  }
}

// ─── Page tracking ───────────────────────────────────────────
function onRelocated(location) {
  currentCfi = location?.start?.cfi
  if (!currentCfi || !book?.locations) return

  const pct = book.locations.percentageFromCfi(currentCfi)
  currentPage.value    = Math.max(1, Math.round(pct * totalPages.value)) || 1
  readingPercent.value = Math.round(pct * 100)

  clearTimeout(saveTimer)
  saveTimer = setTimeout(() => saveProgress(pct), 1500)
}

// ─── Save progress ───────────────────────────────────────────
async function saveProgress(pct) {
  if (pct == null || bookStatus === 'READ') return

  const isLastPage  = currentPage.value >= totalPages.value
  const newProgress = isLastPage ? 100 : Math.min(99, Math.floor(pct * 100))

  try {
    localStorage.setItem(`reader_cfi_pct_${bookId}`, pct)
    await apiClient.patch(`/users/me/books/${bookId}/progress?progress=${newProgress}`)
    if (newProgress >= 100) {
      bookStatus = 'READ'
      showToast()
    }
  } catch (e) {
    console.warn('[Reader] Ошибка сохранения прогресса:', e)
  }
}

function onBeforeUnload() {
  clearTimeout(saveTimer)
  if (currentCfi && book?.locations && bookStatus !== 'READ') {
    saveProgress(book.locations.percentageFromCfi(currentCfi))
  }
}

function showToast() {
  toastVisible.value = true
  setTimeout(() => { toastVisible.value = false }, 4000)
}

// ─── Navigation ──────────────────────────────────────────────
async function prevPage() { await rendition?.prev() }
async function nextPage() { await rendition?.next() }

async function goToPage(pageNum) {
  if (!book?.locations) return
  const pct = (pageNum - 1) / Math.max(totalPages.value - 1, 1)
  const cfi = book.locations.cfiFromPercentage(pct)
  if (cfi) await rendition.display(cfi)
}

async function goToChapter(href) {
  await rendition?.display(href)
  showToc.value = false
}

async function goBack() {
  if (currentCfi && book?.locations && bookStatus !== 'READ') {
    await saveProgress(book.locations.percentageFromCfi(currentCfi))
  }
  router.push(`/books/${bookId}`)
}
</script>

<style scoped>
/* ── Variables & themes ── */
.reader-page {
  --toolbar-bg: #ffffff;
  --card-bg:    #ffffff;
  --page-bg:    #F3F3F4;
  --text:       #1B1B1B;
  --border:     rgba(0,0,0,0.08);
  --accent:     #902923;
  --grey:       #73706C;

  position: fixed; inset: 0;
  display: flex; flex-direction: column;
  background: var(--page-bg); color: var(--text);
  font-family: var(--font-family, 'Rubik', sans-serif);
  z-index: 1000; transition: background 0.2s, color 0.2s;
}
.reader-page.theme-dark {
  --toolbar-bg: #2d2d2d; --card-bg: #2d2d2d;
  --page-bg: #1e1e1e; --text: #eeeeee;
  --border: rgba(255,255,255,0.08); --grey: #aaaaaa;
}
.reader-page.theme-sepia {
  --toolbar-bg: #e0d3c1; --card-bg: #e0d3c1;
  --page-bg: #faf0e6; --text: #5b4636;
  --border: rgba(91,70,54,0.12);
}

/* ── Toast ── */
.toast {
  position: fixed; top: 24px; left: 50%; transform: translateX(-50%);
  background: #1B1B1B; color: #fff; padding: 12px 24px; border-radius: 10px;
  font-size: 14px; z-index: 2000; box-shadow: 0 4px 20px rgba(0,0,0,0.25); white-space: nowrap;
}
.toast-enter-active, .toast-leave-active { transition: opacity 0.3s, transform 0.3s; }
.toast-enter-from, .toast-leave-to { opacity: 0; transform: translateX(-50%) translateY(-8px); }

/* ── Toolbar ── */
.reader-toolbar {
  flex-shrink: 0; height: 72px; padding: 0 32px;
  display: flex; align-items: center; justify-content: space-between;
  background: var(--toolbar-bg); border-bottom: 1px solid var(--border);
}
.toolbar-left  { display: flex; align-items: center; gap: 8px; }
.toolbar-right { display: flex; align-items: center; gap: 30px; }
.book-info {
  font-weight: 500; font-size: 20px; line-height: 1.5; color: var(--text);
  white-space: nowrap; overflow: hidden; text-overflow: ellipsis; max-width: 480px;
}
.icon-btn {
  background: none; border: none; cursor: pointer; padding: 0;
  display: flex; align-items: center; color: var(--text); opacity: 0.8; transition: opacity 0.15s;
}
.icon-btn:hover { opacity: 1; }

.theme-group { display: flex; align-items: center; gap: 10px; }
.theme-label { font-size: 16px; color: var(--text); }
.theme-btn {
  width: 24px; height: 24px; border-radius: 4px; cursor: pointer; font-size: 12px;
  display: flex; align-items: center; justify-content: center; padding: 0; transition: border-color 0.15s;
}
.theme-btn--light        { background: #F3F3F4; color: #1B1B1B; border: 1px solid #F3F3F4; }
.theme-btn--light.active { border-color: #902923; }
.theme-btn--dark         { background: #1B1B1B; color: #ffffff; border: 1px solid #1B1B1B; }
.theme-btn--dark.active  { border-color: #ffffff; }
.theme-btn--sepia        { background: #34312d; color: #e6dcd3; border: 1px solid #34312d; }
.theme-btn--sepia.active { border-color: #e6dcd3; }

/* ── Main ── */
.reader-main {
  flex: 1; overflow: hidden; position: relative;
  background: var(--page-bg);
  display: flex; justify-content: center; align-items: stretch;
}
/* epub-viewer matches footer-inner width via epub.js width param set from JS */
.epub-viewer { flex-shrink: 0; }
.epub-viewer.hidden { visibility: hidden; }

/* Initial load overlay */
.reader-loading {
  position: absolute; inset: 0;
  display: flex; flex-direction: column; align-items: center; justify-content: center; gap: 16px;
  background: var(--page-bg); z-index: 10;
}
/* Recalc overlay — sits on top of epub, doesn't block the full page */
.recalc-overlay {
  position: absolute; inset: 0;
  display: flex; flex-direction: column; align-items: center; justify-content: center; gap: 12px;
  background: rgba(0,0,0,0.25); z-index: 9; backdrop-filter: blur(2px);
}
.loading-spinner {
  width: 40px; height: 40px;
  border: 3px solid rgba(255,255,255,0.3); border-top-color: var(--accent);
  border-radius: 50%; animation: spin 0.8s linear infinite;
}
@keyframes spin { to { transform: rotate(360deg); } }
.loading-text { font-size: 14px; color: var(--grey); }

/* ── Footer ── */
.reader-footer {
  flex-shrink: 0; background: var(--page-bg); padding: 0 32px 20px;
}
.footer-inner { max-width: 856px; margin: 0 auto; }

.footer-nav-row {
  padding: 14px 0 10px;
  display: flex; justify-content: space-between; align-items: center;
}
.nav-text-btn {
  display: flex; align-items: center; gap: 6px; padding: 8px 12px; border-radius: 8px;
  background: var(--card-bg); border: 1px solid var(--border);
  cursor: pointer; font-family: inherit; font-size: 14px; line-height: 1.5;
  text-decoration: underline; text-decoration-skip-ink: none;
  color: var(--accent); transition: opacity 0.15s;
}
.nav-text-btn:disabled { opacity: 0.35; cursor: default; color: var(--grey); }
.nav-text-btn svg path { stroke: currentColor; }
.reading-percent { font-size: 13px; color: var(--grey); font-variant-numeric: tabular-nums; }

.pagination-card { background: var(--card-bg); border-radius: 12px; padding: 16px 20px; }
.pagination-strip-row { display: flex; align-items: center; gap: 10px; flex-wrap: wrap; }
.pagination-label { font-size: 14px; color: var(--text); white-space: nowrap; }

.page-strip { display: flex; align-items: center; gap: 4px; flex-wrap: wrap; }
.strip-arrow { background: none; border: none; cursor: pointer; padding: 0; display: flex; align-items: center; }
.strip-arrow:disabled { cursor: not-allowed; }
.page-num-btn {
  min-width: 26px; height: 18px; font-size: 12px; font-weight: 600; line-height: 1.5;
  background: none; border: none; cursor: pointer; color: var(--accent); padding: 0 4px; border-radius: 2px;
}
.page-num-btn.active { color: var(--grey); font-weight: 500; }
.page-dots { font-size: 12px; color: var(--grey); padding: 0 2px; }

/* ── TOC ── */
.overlay { position: fixed; inset: 0; background: rgba(0,0,0,0.45); z-index: 1200; display: flex; }
.toc-panel {
  width: 320px; height: 100%; background: var(--card-bg);
  display: flex; flex-direction: column; box-shadow: 4px 0 24px rgba(0,0,0,0.15);
}
.toc-panel__header {
  display: flex; align-items: center; justify-content: space-between;
  padding: 20px 24px; font-size: 18px; font-weight: 600; border-bottom: 1px solid var(--border);
}
.toc-panel__list { flex: 1; overflow-y: auto; padding: 12px 0; }
.toc-item {
  display: block; width: 100%; text-align: left; padding: 12px 24px; font-size: 15px;
  background: none; border: none; cursor: pointer; color: var(--text);
  border-bottom: 1px solid var(--border); transition: background 0.1s;
}
.toc-item:hover { background: rgba(144,41,35,0.05); }
.toc-empty { padding: 24px; color: var(--grey); font-size: 14px; }

/* ── Settings ── */
.settings-modal {
  width: 360px; background: var(--card-bg); border-radius: 16px; padding: 28px;
  margin: auto; box-shadow: 0 8px 40px rgba(0,0,0,0.18);
}
.settings-modal__header {
  display: flex; align-items: center; justify-content: space-between;
  font-size: 18px; font-weight: 600; margin-bottom: 24px;
}
.settings-group { margin-bottom: 20px; }
.settings-label {
  display: block; font-size: 13px; font-weight: 600;
  text-transform: uppercase; letter-spacing: 0.06em; color: var(--grey); margin-bottom: 10px;
}
.font-family-options { display: flex; gap: 8px; flex-wrap: wrap; }
.font-family-btn {
  padding: 8px 14px; border-radius: 8px; border: 1px solid var(--border);
  background: none; cursor: pointer; font-size: 14px; color: var(--text);
  transition: border-color 0.15s, background 0.15s;
}
.font-family-btn.active { border-color: var(--accent); background: rgba(144,41,35,0.06); }
.size-row { display: flex; align-items: center; gap: 16px; }
.size-btn {
  width: 36px; height: 36px; border-radius: 8px; border: 1px solid var(--border);
  background: none; cursor: pointer; font-size: 14px; font-weight: 600; color: var(--text);
}
.size-btn:hover:not(:disabled) { background: rgba(144,41,35,0.06); }
.size-btn:disabled { opacity: 0.35; cursor: default; }
.size-display { min-width: 48px; text-align: center; font-size: 15px; font-weight: 500; }
.recalc-note { font-size: 13px; color: var(--grey); margin: 0; text-align: center; }

/* ── Transitions ── */
.toc-enter-active, .toc-leave-active { transition: opacity 0.2s; }
.toc-enter-active .toc-panel, .toc-leave-active .toc-panel { transition: transform 0.25s ease; }
.toc-enter-from .toc-panel, .toc-leave-to .toc-panel { transform: translateX(-100%); }
.toc-enter-from, .toc-leave-to { opacity: 0; }
.fade-enter-active, .fade-leave-active { transition: opacity 0.2s; }
.fade-enter-from, .fade-leave-to { opacity: 0; }

/* ── Responsive ── */
@media (max-width: 920px) {
  .reader-toolbar, .reader-footer { padding-left: 16px; padding-right: 16px; }
  .book-info { font-size: 16px; max-width: 220px; }
  .toolbar-right { gap: 16px; }
}
@media (max-width: 600px) {
  .theme-label { display: none; }
  .book-info   { max-width: 130px; }
}
</style>