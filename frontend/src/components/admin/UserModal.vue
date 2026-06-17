<template>
  <div v-if="visible" class="modal-overlay" @click.self="close">
    <div class="modal-container">
      <div class="modal-header">
        <h3>{{ isEdit ? 'Редактировать пользователя' : 'Добавить пользователя' }}</h3>
        <button class="close-btn" @click="close">×</button>
      </div>
      <div class="modal-body">
        <div class="form-group">
          <label>Логин</label>
          <input v-model="form.username" type="text" class="modal-input" />
        </div>
        <div class="form-group">
          <label>Email</label>
          <input v-model="form.email" type="email" class="modal-input" />
        </div>
        <div class="form-group">
          <label>Пароль</label>
          <input v-model="form.password" type="password" placeholder="Оставьте пустым, если не меняете" class="modal-input" />
        </div>
        <div class="form-group">
          <label>Роль</label>
          <select v-model="form.role" class="modal-input">
            <option value="READER">Читатель</option>
            <option value="ADMIN">Администратор</option>
          </select>
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

const form = reactive({
  username: '',
  email: '',
  password: '',
  role: 'READER'
})

function resetForm() {
  form.username = ''
  form.email = ''
  form.password = ''
  form.role = 'READER'
  errorMessage.value = ''
}

function open(user = null) {
  resetForm()
  if (user) {
    isEdit.value = true
    editingId.value = user.id
    form.username = user.username || ''
    form.email = user.email || ''
    form.role = user.role || 'READER'
  } else {
    isEdit.value = false
    editingId.value = null
  }
  visible.value = true
}

function close() {
  visible.value = false
}

async function save() {
  if (!form.username.trim()) { errorMessage.value = 'Логин обязателен'; return }
  if (form.username.length < 2 || form.username.length > 50) { 
    errorMessage.value = 'Логин от 2 до 50 символов'; 
    return 
  }
  if (!/^[a-zA-Z0-9_]+$/.test(form.username)) { 
    errorMessage.value = 'Только латиница, цифры и подчёркивание'; 
    return 
  }
  if (!form.email.trim() || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)) { 
    errorMessage.value = 'Некорректный email'; 
    return 
  }
  if (!isEdit.value && !form.password) { 
    errorMessage.value = 'Пароль обязателен для нового пользователя'; 
    return 
  }
  if (form.password) {
    if (form.password.length < 6) { 
      errorMessage.value = 'Пароль не менее 6 символов'; 
      return 
    }
    if (/[а-яА-ЯёЁ]/.test(form.password)) { 
      errorMessage.value = 'Пароль не должен содержать кириллицу';
      return 
    }
    if (!/[A-Z]/.test(form.password)) { 
      errorMessage.value = 'Нужна заглавная буква'; 
      return 
    }
    if (!/[a-z]/.test(form.password)) { 
      errorMessage.value = 'Нужна строчная буква'; 
      return 
    }
    if (!/\d/.test(form.password)) { 
      errorMessage.value = 'Нужна цифра'; 
      return 
    }
  }
  saving.value = true
  try {
    const payload = {
      username: form.username,
      email: form.email,
      role: form.role
    }
    if (isEdit.value) {
      await apiClient.put(`/users/${editingId.value}`, payload)
    } else {
      await apiClient.post('/users/register', payload)
    }
    emit('saved')
    close()
    alert(isEdit.value ? 'Пользователь успешно обновлен' : 'Пользователь успешно добавлен')
  } catch (err) {
    errorMessage.value = formatErrorMessage(err, 'user')
    console.error('User save error:', err)
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