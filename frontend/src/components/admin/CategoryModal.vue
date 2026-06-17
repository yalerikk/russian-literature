<template>
  <div v-if="visible" class="modal-overlay" @click.self="close">
    <div class="modal-container" style="width: 600px;">
      <div class="modal-header">
        <h3>{{ isEdit ? 'Редактировать подборку' : 'Создать подборку' }}</h3>
        <button class="close-btn" @click="close">×</button>
      </div>
      <div class="modal-body">
        <div v-if="isEdit" class="form-group">
          <label>Название *</label>
          <input v-model="editName" type="text" class="modal-input" />
        </div>

        <div v-if="isEdit" class="form-group">
          <label>Активна</label>
          <label class="checkbox-label">
            <input type="checkbox" v-model="editIsActive" /> Да / Нет
          </label>
        </div>

        <!-- Для создания – выбор тегов (группировка) -->
        <div v-if="!isEdit" class="form-group">
          <label>Теги (выберите по одному из каждой группы)</label>
          <div v-for="(group, groupName) in tagGroups" :key="groupName" class="tag-group">
            <div class="tag-group-label">{{ tagGroupLabels[groupName] }}</div>
            <div class="tag-select">
              <select v-model="selectedTagIds[groupName]" @change="updateTagIds" class="modal-input">
                <option :value="null">-- Не выбрано --</option>
                <option v-for="t in group" :key="t.id" :value="t.id">{{ t.name }}</option>
              </select>
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
import { ref, reactive, computed, onMounted } from 'vue'
import { apiClient } from '../../services/api'
import { formatErrorMessage } from '../../utils/errorFormatter'

const emit = defineEmits(['saved'])
const visible = ref(false)
const isEdit = ref(false)
const editingId = ref(null)
const saving = ref(false)
const errorMessage = ref('')

const editName = ref('')
const editIsActive = ref(true)

// Выбор тегов при создании
const allTags = ref([])
const selectedTagIds = reactive({ GRADE: null, LEVEL: null, CATEGORY: null, READING_TYPE: null })

const tagGroups = computed(() => {
  const groups = { GRADE: [], LEVEL: [], CATEGORY: [], READING_TYPE: [] }
  allTags.value.forEach(tag => {
    if (groups[tag.type]) groups[tag.type].push(tag)
  })
  return groups
})
const tagGroupLabels = { GRADE: 'Класс', LEVEL: 'Уровень', CATEGORY: 'Категория литературы', READING_TYPE: 'Тип чтения' }

async function loadAllTags() {
  try {
    const res = await apiClient.get('/tags')
    allTags.value = res
  } catch (e) {
    console.error(e)
  }
}

function resetCreateForm() {
  selectedTagIds.GRADE = null
  selectedTagIds.LEVEL = null
  selectedTagIds.CATEGORY = null
  selectedTagIds.READING_TYPE = null
}

function updateTagIds() {
  // просто триггер для реактивности – ничего не делает, но можно удалить
}

function open(category = null) {
  resetCreateForm()
  errorMessage.value = ''
  if (category && category.criteriaType === 'CUSTOM') {
    isEdit.value = true
    editingId.value = category.id
    editName.value = category.name
    editIsActive.value = category.isActive
  } else if (category) {
    alert('Системную подборку нельзя редактировать')
    return
  } else {
    isEdit.value = false
    editingId.value = null
    editName.value = ''
    editIsActive.value = true
  }
  visible.value = true
}

function close() {
  visible.value = false
}

async function save() {
  saving.value = true
  errorMessage.value = ''
  try {
    if (isEdit.value) {
      const payload = {}
      if (editName.value.trim()) payload.name = editName.value.trim()
      if (editName.value.trim().length < 2 || editName.value.trim().length > 100) {
        errorMessage.value = 'Название должно быть от 2 до 100 символов'
        return
      }
      if (editIsActive.value !== undefined) payload.isActive = editIsActive.value
      await apiClient.put(`/api/catalog/categories/${editingId.value}`, payload)
    } else {
      const tagIds = Object.values(selectedTagIds).filter(id => id !== null)
      if (tagIds.length === 0) {
        errorMessage.value = 'Выберите хотя бы один тег'
        saving.value = false
        return
      }
      await apiClient.post('/api/catalog/categories', { tagIds })
    }
    emit('saved')
    close()
    alert(isEdit.value ? 'Подборка успешно обновлена' : 'Подборка успешно создана')
  } catch (err) {
    errorMessage.value = formatErrorMessage(err, 'category')
    console.error('Category save error:', err)
  } finally {
    saving.value = false
  }
}

onMounted(loadAllTags)
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
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 4px;
}
.modal-input {
  font-family: 'Manrope', sans-serif;
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
}
.tag-group {
  margin-top: 12px;
}
.tag-group-label {
  font-weight: 500;
  font-size: 13px;
  margin-bottom: 6px;
  color: #555;
}
.tag-select {
  width: 100%;
}
.checkbox-label {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  font-size: 14px;
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