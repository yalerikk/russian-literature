<template>
  <div class="table-wrapper">
    <div class="table-header">
      <div
        v-for="col in columns"
        :key="col.key"
        :class="['header-cell', col.className]"
      >
        {{ col.label }}
      </div>
    </div>

    <div class="table-body">
      <div v-if="loading" class="empty-row">Загрузка...</div>
      <div v-else-if="items.length === 0" class="empty-row">
        <div class="empty-icon">📭</div>
        <h3>Нет данных</h3>
        <p>Добавьте первую запись</p>
      </div>
      <div v-else v-for="item in items" :key="item.id" class="table-row">
        <div
          v-for="col in columns"
          :key="col.key"
          :class="['row-cell', col.className]"
        >
          <slot :name="`cell-${col.key}`" :item="item" :value="item[col.key]">
            {{ item[col.key] }}
          </slot>
        </div>
      </div>
    </div>
  </div>

  <Pagination
    v-if="totalPages > 1"
    :current-page="currentPage"
    :total-pages="totalPages"
    @page-change="$emit('page-change', $event)"
  />
</template>

<script setup>
import Pagination from '../Pagination.vue'

defineProps({
  columns: { type: Array, required: true },
  items: { type: Array, required: true },
  loading: Boolean,
  currentPage: Number,
  totalPages: Number,
})

defineEmits(['page-change'])
</script>

<style scoped>
.table-wrapper {
  width: 100%;
  background: white;
  border-radius: 12px;
  overflow: hidden;
  margin-bottom: 30px;
}
.table-header {
  display: flex;
  align-items: center;
  background: #F3F3F4;
  padding: 32px;
  gap: 26px;
  width: 100%;
}
.header-cell {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 10px;
  border-radius: 6px;
  font-weight: 400;
  font-size: 16px;
  color: #1B1B1B;
  background: #FFF;
  width: 100%;
}
.header-cell.actions-cell {
  flex: 0 0 140px;
  justify-content: center;
  font-weight: 500;
  background: #AEC3B0;
  color: white;
}
.table-body {
  display: flex;
  flex-direction: column;
  width: 100%;
}
.table-row {
  display: flex;
  align-items: center;
  padding: 0 32px;
  gap: 26px;
  width: 100%;
}
.empty-row {
  text-align: center;
  padding: 40px;
  font-size: 16px;
  color: #73706C;
}
.row-cell {
  width: 100%;
  display: flex;
  background: #F3F3F4;
  border-radius: 6px;
  padding: 10px;
  color: #1B1B1B;
  font-weight: 400;
  font-size: 16px;
  text-align: center;
  justify-content: center;
}
/* Специальный стиль для ячейки действий в строках */
.row-cell.actions-cell {
  flex: 0 0 140px;
  background: transparent;
  padding: 0;
  justify-content: center;
  gap: 10px;
}
</style>