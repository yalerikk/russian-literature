<template>
  <div v-if="visible" class="modal-overlay" @click.self="close">
    <div class="modal-container">
      <div class="modal-header">
        <h3>{{ isEdit ? 'Редактировать' : 'Добавить' }} {{ entityName.toLowerCase() }}</h3>
        <button class="close-btn" @click="close">×</button>
      </div>
      <div class="modal-body">
        <div class="form-group">
          <label>Название *</label>
          <input v-model="form.name" type="text" class="modal-input" />
        </div>
        <div v-if="hasType" class="form-group">
          <label>Тип</label>
          <select v-model="form.type" class="modal-input">
            <option value="GRADE">Класс</option>
            <option value="LEVEL">Уровень</option>
            <option value="CATEGORY">Категория литературы</option>
            <option value="READING_TYPE">Тип чтения</option>
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

const props = defineProps({
  entityName: { type: String, required: true },
  apiBaseUrl: { type: String, required: true },  // /genres, /tags
  hasType: { type: Boolean, default: false }
})
const emit = defineEmits(['saved'])

const visible = ref(false)
const isEdit = ref(false)
const editingId = ref(null)
const saving = ref(false)
const errorMessage = ref('')
const form = reactive({ name: '', type: 'GRADE' })

function resetForm() {
  form.name = ''
  form.type = 'GRADE'
  errorMessage.value = ''
}

function open(item = null) {
  resetForm()
  if (item) {
    isEdit.value = true
    editingId.value = item.id
    form.name = item.name || ''
    if (props.hasType) form.type = item.type || 'GRADE'
  } else {
    isEdit.value = false
    editingId.value = null
  }
  visible.value = true
}

function close() { visible.value = false }

async function save() {
  if (!form.name.trim()) {
    errorMessage.value = 'Название обязательно'
    return
  }
  saving.value = true
  try {
    const payload = { name: form.name }
    if (props.hasType) payload.type = form.type
    if (isEdit.value) {
      await apiClient.put(`${props.apiBaseUrl}/${editingId.value}`, payload)
    } else {
      await apiClient.post(props.apiBaseUrl, payload)
    }
    emit('saved')
    close()
  } catch (err) {
    errorMessage.value = err.data?.details || err.message || 'Ошибка сохранения'
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