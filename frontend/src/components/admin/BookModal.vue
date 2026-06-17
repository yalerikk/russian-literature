<template>
  <div v-if="visible" class="modal-overlay" @click.self="close">
    <div class="modal-container" style="width: 700px;">
      <div class="modal-header">
        <h3>{{ isEdit ? 'Редактировать книгу' : 'Добавить книгу' }}</h3>
        <button class="close-btn" @click="close">×</button>
      </div>
      <div class="modal-body">
        <div class="form-group">
          <label>Название</label>
          <input v-model="form.title" type="text" class="modal-input" />
        </div>

        <div class="form-group">
          <label>Год издания</label>
          <input v-model.number="form.publicationYear" type="number" class="modal-input" />
        </div>

        <div class="form-group">
          <label>Описание</label>
          <textarea v-model="form.description" rows="3" class="modal-input"></textarea>
        </div>

        <div class="form-group">
          <label>Автор</label>
          <select v-model="form.authorId" class="modal-input">
            <option :value="null">-- Выберите автора --</option>
            <option v-for="a in authors" :key="a.id" :value="a.id">
              {{ a.lastName }} {{ a.firstName }} {{ a.middleName }}
            </option>
          </select>
        </div>

        <div class="form-group">
          <label>Жанры (можно несколько)</label>
          <div class="multiselect">
            <div class="multiselect-tags">
              <span v-for="g in selectedGenres" :key="g.id" class="multiselect-tag">
                {{ g.name }}
                <button @click="removeGenre(g.id)" type="button">×</button>
              </span>
            </div>
            <select v-model="tempGenreId" @change="addGenre" class="modal-input">
              <option :value="null">-- Добавить жанр --</option>
              <option v-for="g in allGenres" :key="g.id" :value="g.id">{{ g.name }}</option>
            </select>
          </div>
        </div>

        <!-- Теги, сгруппированные по типу -->
        <div class="form-group">
          <label>Теги</label>
          <div v-for="(group, groupName) in tagGroups" :key="groupName" class="tag-group">
            <div class="tag-group-label">{{ tagGroupLabels[groupName] }}</div>
            <div class="multiselect">
              <div class="multiselect-tags">
                <span v-for="t in selectedTagsByType[groupName]" :key="t.id" class="multiselect-tag">
                  {{ t.name }}
                  <button @click="removeTag(t.id)" type="button">×</button>
                </span>
              </div>
              <select v-model="tempTagIds[groupName]" @change="addTag(groupName)" class="modal-input">
                <option :value="null">-- Добавить тег --</option>
                <option v-for="t in group" :key="t.id" :value="t.id">{{ t.name }}</option>
              </select>
            </div>
          </div>
        </div>

        <div class="form-group">
          <label>Обложка</label>
          <input type="file" accept="image/*" @change="onCoverChange" />
          <div v-if="form.coverUrl" class="photo-preview">
            <img :src="form.coverUrl" width="80" />
          </div>
        </div>

        <!-- Блок файлов (только при редактировании) -->
        <div v-if="isEdit && editingId" class="form-group">
          <label>Файлы книги</label>
          <div v-if="filesLoading" class="loading-files">Загрузка...</div>
          <div v-else>
            <div v-for="file in files" :key="file.id" class="file-item">
              <span class="file-name">{{ file.format }}</span>
              <button class="delete-file-btn" @click="deleteFile(file.id)" title="Удалить">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none">
                    <path d="M6 18L18 6M6 6L18 18" stroke="currentColor" stroke-width="2"   stroke-linecap="round"/>
                  </svg>
              </button>
            </div>
            <div class="upload-buttons">
              <button
                v-for="format in allowedFormats"
                :key="format"
                :disabled="hasFile(format)"
                @click="uploadFile(format)"
                class="upload-format-btn"
              >
                <span v-if="uploadingFormat === format">Загрузка {{ format }}...</span>
                <span v-else>{{ format }}</span>
              </button>
            </div>
          </div>
        </div>

        <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
      </div>
      <div class="modal-footer">
        <button class="btn-cancel" @click="close">Отмена</button>
        <button class="btn-save" @click="save" :disabled="saving">{{ saving ? 'Сохранение...' : 'Сохранить' }}</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed, watch } from 'vue'
import { apiClient } from '../../services/api'
import { formatErrorMessage } from '../../utils/errorFormatter'

const emit = defineEmits(['saved'])
const visible = ref(false)
const isEdit = ref(false)
const editingId = ref(null)
const saving = ref(false)
const errorMessage = ref('')
let hasUnsavedChanges = false

const form = reactive({
  title: '',
  publicationYear: null,
  description: '',
  authorId: null,
  genreIds: [],
  tagIds: [],
  coverUrl: ''
})

// Справочники
const authors = ref([])
const allGenres = ref([])
const allTags = ref([])

// Выбранные жанры и теги
const selectedGenres = ref([])
const selectedTags = ref([])
const tempGenreId = ref(null)

// Файлы
const files = ref([])
const filesLoading = ref(false)
const allowedFormats = ['PDF', 'EPUB', 'FB2', 'TXT']
const uploadingFormat = ref(null) // какой формат сейчас загружается

// Группировка тегов
const tagGroups = computed(() => {
  const groups = { GRADE: [], LEVEL: [], CATEGORY: [], READING_TYPE: [] }
  allTags.value.forEach(tag => {
    if (groups[tag.type]) groups[tag.type].push(tag)
  })
  return groups
})
const tagGroupLabels = { GRADE: 'Класс', LEVEL: 'Уровень', CATEGORY: 'Категория литературы', READING_TYPE: 'Тип чтения' }
const selectedTagsByType = ref({ GRADE: [], LEVEL: [], CATEGORY: [], READING_TYPE: [] })
const tempTagIds = ref({ GRADE: null, LEVEL: null, CATEGORY: null, READING_TYPE: null })

// Флаг несохранённых изменений
function markUnsaved() {
  hasUnsavedChanges = true
}

// Слежка за изменениями полей
watch([form, selectedGenres, selectedTagsByType], () => {
  markUnsaved()
}, { deep: true })

async function loadSelectOptions() {
  try {
    const [authorsRes, genresRes, tagsRes] = await Promise.all([
      apiClient.get('/authors/for-select'),
      apiClient.get('/genres'),
      apiClient.get('/tags')
    ])
    authors.value = authorsRes
    allGenres.value = genresRes
    allTags.value = tagsRes
  } catch (e) {
    console.error('Ошибка загрузки справочников', e)
  }
}

function resetForm() {
  form.title = ''
  form.publicationYear = null
  form.description = ''
  form.authorId = null
  form.genreIds = []
  form.tagIds = []
  form.coverUrl = ''
  selectedGenres.value = []
  selectedTags.value = []
  selectedTagsByType.value = { GRADE: [], LEVEL: [], CATEGORY: [], READING_TYPE: [] }
  tempGenreId.value = null
  tempTagIds.value = { GRADE: null, LEVEL: null, CATEGORY: null, READING_TYPE: null }
  hasUnsavedChanges = false
}

function open(book = null) {
  resetForm()
  if (book) {
    isEdit.value = true
    editingId.value = book.id
    form.title = book.title || ''
    form.publicationYear = book.publicationYear || null
    form.description = book.description || ''
    form.authorId = book.author?.id || null
    form.coverUrl = book.coverUrl || ''
    // Жанры
    form.genreIds = (book.genres || []).map(g => g.id)
    selectedGenres.value = allGenres.value.filter(g => form.genreIds.includes(g.id))
    // Теги
    form.tagIds = (book.tags || []).map(t => t.id)
    const tags = allTags.value.filter(t => form.tagIds.includes(t.id))
    selectedTags.value = tags
    const byType = { GRADE: [], LEVEL: [], CATEGORY: [], READING_TYPE: [] }
    tags.forEach(t => { byType[t.type].push(t) })
    selectedTagsByType.value = byType
    loadFiles()
  } else {
    isEdit.value = false
    editingId.value = null
  }
  visible.value = true
}

function close() {
  if (hasUnsavedChanges && !confirm('Есть несохранённые изменения. Закрыть без сохранения?')) {
    return
  }
  visible.value = false
}

function addGenre() {
  if (!tempGenreId.value) return
  const genre = allGenres.value.find(g => g.id === tempGenreId.value)
  if (genre && !selectedGenres.value.some(sg => sg.id === genre.id)) {
    selectedGenres.value.push(genre)
    form.genreIds.push(genre.id)
  }
  tempGenreId.value = null
}
function removeGenre(id) {
  selectedGenres.value = selectedGenres.value.filter(g => g.id !== id)
  form.genreIds = form.genreIds.filter(gid => gid !== id)
}

function addTag(groupName) {
  const tagId = tempTagIds.value[groupName]
  if (!tagId) return
  const tag = allTags.value.find(t => t.id === tagId)
  if (!tag) return

  const oldTag = selectedTags.value.find(t => t.type === groupName)
  if (oldTag) {
    removeTag(oldTag.id)
  }

  if (!selectedTags.value.some(st => st.id === tag.id)) {
    selectedTags.value.push(tag)
    selectedTagsByType.value[groupName] = [tag]
    form.tagIds.push(tag.id)
  }
  tempTagIds.value[groupName] = null
}
function removeTag(id) {
  const tag = selectedTags.value.find(t => t.id === id)
  if (tag) {
    selectedTags.value = selectedTags.value.filter(t => t.id !== id)
    if (selectedTagsByType.value[tag.type]) {
      selectedTagsByType.value[tag.type] = selectedTagsByType.value[tag.type].filter(t => t.id !== id)
    }
    form.tagIds = form.tagIds.filter(tid => tid !== id)
  }
}

async function onCoverChange(e) {
  const file = e.target.files[0]
  if (!file) return
  const formData = new FormData()
  formData.append('file', file)
  const token = localStorage.getItem('jwt_token')
  try {
    const response = await fetch('/api/images/upload/book-cover', {
      method: 'POST',
      headers: { Authorization: token ? `Bearer ${token}` : undefined },
      body: formData
    })
    const data = await response.json()
    if (response.ok) {
      form.coverUrl = data.url
      errorMessage.value = ''
    } else {
      errorMessage.value = data.error || 'Ошибка загрузки обложки'
    }
  } catch (err) {
    errorMessage.value = 'Ошибка соединения'
  }
}

// Файлы
async function loadFiles() {
  if (!editingId.value) return
  filesLoading.value = true
  try {
    const res = await apiClient.get(`/books/${editingId.value}/files`)
    files.value = res
  } catch (e) {
    console.error(e)
  } finally {
    filesLoading.value = false
  }
}

function hasFile(format) {
  return files.value.some(f => f.format === format)
}

async function uploadFile(format) {
  if (format !== 'EPUB' && !hasFile('EPUB')) {
    alert('Сначала загрузите файл в формате EPUB');
    return;
  }
  const input = document.createElement('input')
  input.type = 'file'
  input.accept = format === 'PDF' ? '.pdf' : format === 'EPUB' ? '.epub' : format === 'FB2' ? '.fb2' : '.txt'
  input.onchange = async (e) => {
    const file = e.target.files[0]
    if (!file) return
    uploadingFormat.value = format
    const formData = new FormData()
    formData.append('file', file)
    const token = localStorage.getItem('jwt_token')
    try {
      const res = await fetch(`/books/${editingId.value}/files?format=${format}`, {
        method: 'POST',
        headers: { Authorization: token ? `Bearer ${token}` : undefined },
        body: formData
      })
      if (!res.ok) {
        const errData = await res.json().catch(() => ({}))
        throw new Error(errData.error || 'Ошибка загрузки')
      }
      await loadFiles()
      markUnsaved()
    } catch (err) {
      alert(err.message)
    } finally {
      uploadingFormat.value = null
    }
  }
  input.click()
}

async function deleteFile(fileId) {
  if (!confirm('Удалить файл?')) return
  try {
    await apiClient.delete(`/books/${editingId.value}/files/${fileId}`)
    await loadFiles()
    markUnsaved()
  } catch (err) {
    alert(err.message || 'Ошибка удаления')
  }
}

async function save() {
  if (!form.title.trim()) {
    errorMessage.value = 'Название обязательно'
    return
  }
  if (form.title.length > 255) { 
    errorMessage.value = 'Название не должно превышать 255 символов'; 
    return 
  }
  if (!form.publicationYear || form.publicationYear <= 0) {
    errorMessage.value = 'Год издания обязателен'
    return
  }
  if (form.publicationYear < 1500 || form.publicationYear > new Date().getFullYear()) {
    errorMessage.value = 'Некорректный год издания'
    return
  }
  if (form.description && (form.description.length < 10 || form.description.length > 1000)) {
    errorMessage.value = 'Описание должно быть от 10 до 1000 символов'
    return
  }
  if (!form.authorId) {
    errorMessage.value = 'Выберите автора'
    return
  }
  if (form.genreIds.length === 0) {
    errorMessage.value = 'Выберите хотя бы один жанр'
    return
  }
  if (form.tagIds.length === 0) {
    errorMessage.value = 'Выберите хотя бы один тег'
    return
  }
  saving.value = true
  try {
    const payload = {
      title: form.title,
      publicationYear: form.publicationYear,
      description: form.description,
      authorId: form.authorId,
      genreIds: form.genreIds,
      tagIds: form.tagIds,
      coverUrl: form.coverUrl
    }
    if (isEdit.value) {
      await apiClient.put(`/books/${editingId.value}`, payload)
    } else {
      await apiClient.post('/books', payload)
    }
    hasUnsavedChanges = false
    emit('saved')
    close()
    alert(isEdit.value ? 'Книга успешно обновлена' : 'Книга успешно добавлена')
  } catch (err) {
    errorMessage.value = formatErrorMessage(err, 'book');
    console.error('Book save error:', err)
  } finally {
    saving.value = false
  }
}

onMounted(loadSelectOptions)
defineExpose({ open })
</script>

<style scoped>
.multiselect {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.multiselect-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}
.multiselect-tag {
  background: #F3F3F4;
  border-radius: 16px;
  padding: 4px 8px;
  font-size: 12px;
  display: inline-flex;
  align-items: center;
  gap: 6px;
}
.multiselect-tag button {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 14px;
}
.photo-preview {
  margin-top: 8px;
}

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
  z-index: 1100;
}

.modal-container {
  background: white;
  border-radius: 20px;
  width: 500px;
  max-width: 90%;
  max-height: 90vh;
  overflow-y: auto;
  padding: 20px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.modal-header h3 {
  font-family: 'Manrope', sans-serif;
  font-weight: 600;
  font-size: 20px;
  margin: 0;
}

.close-btn {
  background: none;
  border: none;
  font-size: 28px;
  cursor: pointer;
}

.modal-body {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
  margin-bottom: 16px;
}

.form-group label {
  font-family: 'Manrope', sans-serif;
  display: block;
  font-size: 14px;
  margin-bottom: 6px;
  font-weight: 500;
}

.modal-input {
  font-family: 'Manrope', sans-serif;
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
}

.form-row {
  display: flex;
  gap: 16px;
}

.form-group.half {
  flex: 1;
}

.photo-preview {
  margin-top: 8px;
}

.error-message {
  color: #902923;
  font-size: 14px;
  margin-top: 8px;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
}

.btn-cancel,
.btn-save {
  padding: 8px 20px;
  border-radius: 8px;
  font-family: 'Manrope', sans-serif;
  font-weight: 500;
  font-size: 14px;
  cursor: pointer;
  border: none;
}

.btn-cancel {
  background: #e0e0e0;
}

.btn-save {
  background: #aec3b0;
  color: white;
}

.btn-save:hover {
  background: #8da98f;
}

/* ===== Блок файлов ===== */
.loading-files {
  font-size: 14px;
  color: #73706C;
  padding: 8px 0;
}

.file-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #F3F3F4;
  border-radius: 8px;
  padding: 8px 12px;
  margin-bottom: 8px;
}

.file-name {
  font-family: 'Manrope', sans-serif;
  font-size: 14px;
  font-weight: 500;
  color: #1B1B1B;
}

.delete-file-btn {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: transparent;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #73706C;
  transition: all 0.2s;
}

.delete-file-btn:hover {
  background: rgba(0, 0, 0, 0.08);
  color: #902923;
}

/* Кнопки загрузки форматов – как на странице книги */
.upload-buttons {
  display: flex;
  gap: 14px;
  flex-wrap: wrap;
  margin-top: 12px;
}

.format-btn {
  border: 1px solid rgba(52, 49, 45, 0.04);
  border-radius: 16px;
  padding: 4px 10px;
  background: #F3F3F4;
  font-family: 'Manrope', sans-serif;
  font-weight: 400;
  font-size: 14px;
  color: #1B1B1B;
  cursor: pointer;
  transition: all 0.3s;
  text-transform: uppercase;
}

.format-btn:hover:not(:disabled) {
  background: #e8e8e8;
}

.format-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.format-btn.uploading {
  cursor: wait;
  opacity: 0.7;
}
</style>