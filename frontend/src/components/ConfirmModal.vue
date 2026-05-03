<template>
  <div v-if="visible" class="modal-overlay" @click.self="cancel">
    <div class="modal-container">
      <div class="modal-header">
        <h3>Подтверждение</h3>
        <button class="close-btn" @click="cancel">×</button>
      </div>
      <div class="modal-body">
        <p>{{ message }}</p>
      </div>
      <div class="modal-footer">
        <button class="btn-cancel" @click="cancel">Отмена</button>
        <button class="btn-confirm" @click="confirm">Удалить</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'

const visible = ref(false)
const message = ref('')
let resolver = null

function open(msg) {
  message.value = msg
  visible.value = true
  return new Promise(resolve => {
    resolver = resolve
  })
}

function confirm() {
  resolver(true)
  visible.value = false
}

function cancel() {
  resolver(false)
  visible.value = false
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
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}
.modal-container {
  background: white;
  border-radius: 16px;
  width: 400px;
  max-width: 90%;
  padding: 20px;
}
.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
}
.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 20px;
}
.btn-confirm {
  background: #902923;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 8px;
  cursor: pointer;
}
.btn-cancel {
  background: #ccc;
  border: none;
  padding: 8px 16px;
  border-radius: 8px;
  cursor: pointer;
}
</style>