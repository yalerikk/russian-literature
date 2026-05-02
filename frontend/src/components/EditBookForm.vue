<template>
  <div class="edit-book-form">
    <h2>Редактировать книгу</h2>
    <p>Компонент в разработке...</p>
    <button @click="$emit('cancel')">Закрыть</button>
  </div>
</template>

<script>
export default {
  name: 'EditBookForm',
  props: {
    book: Object
  },
  emits: ['success', 'cancel']
}
</script>