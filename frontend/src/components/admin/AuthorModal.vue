<template>
  <div v-if="visible" class="modal-overlay" @click.self="close">
    <div class="modal-container">
      <div class="modal-header">
        <h3>{{ isEdit ? 'Редактировать автора' : 'Добавить автора' }}</h3>
        <button class="close-btn" @click="close">×</button>
      </div>
      <div class="modal-body">
        <div class="form-group">
          <label>Фамилия</label>
          <input v-model="form.lastName" type="text" class="modal-input" />
        </div>
        <div class="form-group">
          <label>Имя</label>
          <input v-model="form.firstName" type="text" class="modal-input" />
        </div>
        <div class="form-group">
          <label>Отчество</label>
          <input v-model="form.middleName" type="text" class="modal-input" />
        </div>
        <div class="form-row">
          <div class="form-group half">
            <label>Дата рождения</label>
            <input v-model="form.birthDate" type="date" class="modal-input" />
          </div>
          <div class="form-group half">
            <label>Дата смерти</label>
            <input v-model="form.deathDate" type="date" class="modal-input" />
          </div>
        </div>
        <div class="form-group">
          <label>Биография</label>
          <textarea v-model="form.biography" rows="4" class="modal-input"></textarea>
        </div>
        <div class="form-group">
          <label>Фото</label>
          <input type="file" accept="image/*" @change="onFileChange" />
          <div v-if="form.photoUrl" class="photo-preview">
            <img :src="form.photoUrl" alt="preview" width="80" />
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
import { ref, reactive } from 'vue'
import { apiClient } from '../../services/api'
import { formatErrorMessage } from '../../utils/errorFormatter'

const emit = defineEmits(['saved'])
const visible = ref(false)
const isEdit = ref(false)
const editingId = ref(null)
const saving = ref(false)
const errorMessage = ref('')
const oldPhotoUrl = ref('')

const form = reactive({
  firstName: '',
  lastName: '',
  middleName: '',
  birthDate: '',
  deathDate: '',
  biography: '',
  photoUrl: ''
})

function resetForm() {
  form.firstName = ''
  form.lastName = ''
  form.middleName = ''
  form.birthDate = ''
  form.deathDate = ''
  form.biography = ''
  form.photoUrl = ''
  errorMessage.value = ''
}

function open(author = null) {
  resetForm()
  if (author) {
    isEdit.value = true
    editingId.value = author.id
    form.firstName = author.firstName || ''
    form.lastName = author.lastName || ''
    form.middleName = author.middleName || ''
    form.birthDate = author.birthDate ? author.birthDate.split('T')[0] : ''
    form.deathDate = author.deathDate ? author.deathDate.split('T')[0] : ''
    form.biography = author.biography || ''
    form.photoUrl = author.photoUrl || ''
  } else {
    isEdit.value = false
    editingId.value = null
  }
  visible.value = true
}

function close() {
  visible.value = false
}

async function onFileChange(e) {
  const file = e.target.files[0]
  if (!file) return
  try {
    const formData = new FormData()
    formData.append('file', file)
    const token = localStorage.getItem('jwt_token')
    const response = await fetch('/api/images/upload/author-photo', {
      method: 'POST',
      headers: {
        Authorization: token ? `Bearer ${token}` : undefined
      },
      body: formData
    })
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}))
      throw new Error(errorData.message || 'Ошибка загрузки фото')
    }
    const data = await response.json()
    form.photoUrl = data.url
    errorMessage.value = ''
  } catch (err) {
    errorMessage.value = err.message;
  }
}

async function save() {
  // 1. Проверка обязательных полей
  if (!form.firstName.trim()) { errorMessage.value = 'Имя обязательно'; return }
  if (!form.lastName.trim()) { errorMessage.value = 'Фамилия обязательна'; return }
  if (!form.middleName.trim()) { errorMessage.value = 'Отчество обязательно'; return }
  if (!form.biography || form.biography.trim().length < 10) {
    errorMessage.value = 'Биография должна быть не менее 10 символов'
    return
  }
  if (form.biography.length > 2000) {
    errorMessage.value = 'Биография не должна превышать 2000 символов'
    return
  }
  if (!form.birthDate) {
    errorMessage.value = 'Дата рождения обязательна'
    return
  }
  // 2. Логические проверки дат
  const birth = new Date(form.birthDate)
  const today = new Date();
  today.setHours(0, 0, 0, 0); // сравниваем только даты без времени
  if (birth >= today) {
    errorMessage.value = 'Дата рождения должна быть раньше сегодняшнего дня';
    return;
  }
  if (form.deathDate) {
    const death = new Date(form.deathDate)
    if (death < birth) {
      errorMessage.value = 'Дата смерти не может быть раньше даты рождения'
      return
    }
    if (death > now) {
      errorMessage.value = 'Дата смерти не может быть в будущем'
      return
    }
    let age = death.getFullYear() - birth.getFullYear()
    const monthDiff = death.getMonth() - birth.getMonth()
    if (monthDiff < 0 || (monthDiff === 0 && death.getDate() < birth.getDate())) age--
    if (age < 10) errorMessage.value = 'Возраст при смерти слишком мал (<10 лет)'
    if (age > 150) errorMessage.value = 'Возраст при смерти слишком велик (>150 лет)'
    if (age < 10 || age > 150) return
  }
  saving.value = true
  try {
    const payload = {
      firstName: form.firstName.trim(),
      lastName: form.lastName.trim(),
      middleName: form.middleName?.trim() || null,
      birthDate: form.birthDate || null,
      deathDate: form.deathDate || null,
      biography: form.biography?.trim() || null,
      photoUrl: form.photoUrl || oldPhotoUrl.value || null
    }
    if (isEdit.value) {
      await apiClient.put(`/authors/${editingId.value}`, payload)
    } else {
      await apiClient.post('/authors', payload)
    }
    emit('saved')
    close()
    alert(isEdit.value ? 'Автор успешно обновлен' : 'Автор успешно добавлен')
  } catch (err) {
    errorMessage.value = formatErrorMessage(err, 'author')
    console.error('Author save error:', err)
  } finally {
    saving.value = false
  }
}

defineExpose({ open })
</script>

<style scoped>
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
  border-radius: 16px;
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
</style>