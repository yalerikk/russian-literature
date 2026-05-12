<template>
  <AdminSimpleEntityPage 
    fetch-url="/genres/admin/list" 
    api-base-url="/genres"
    entity-name="Жанры" 
    :has-type="false" 
  />
</template>

<script setup>
import AdminSimpleEntityPage from '../admin/AdminSimpleEntityPage.vue'
</script>

<style scoped>
.admin-page {
  font-family: 'Rubik', sans-serif;
  padding: 50px 50px 0;
  background: #f8f9fa;
}
.admin-content {
  padding: 0 100px 50px;
}
.breadcrumb-nav {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
  margin-bottom: 24px;
}
.breadcrumb-item {
  font-family: 'Rubik', sans-serif;
  font-weight: 400;
  font-size: 12px;
  line-height: 150%;
  color: #902923;
  letter-spacing: 0.5px;
  text-decoration: none;
}
.breadcrumb-item.current {
  color: #73706C;
}
.separator {
  color: #902923;
  font-size: 12px;
}
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}
.page-title {
  font-family: 'Manrope', sans-serif;
  font-weight: 600;
  font-size: 24px;
  margin: 0;
}
.add-button {
  font-family: 'Rubik', sans-serif;
  background: #AEC3B0;
  font-weight: 500;
  border: none;
  border-radius: 6px;
  padding: 10px 20px;
  color: white;
  cursor: pointer;
}
.add-button:hover {
  background: #8DA98F;
}
/* переопределяем для .actions-cell внутри таблицы */
.row-cell {
  width: 100%;
  display: flex;
  background: #F3F3F4;
  border-radius: 6px;
  padding: 10px;
  gap: 10px;
  color: #1B1B1B;
  font-weight: 400;
  font-size: 16px;
  line-height: 150%;
  text-align: center;
  justify-content: center;
}
.actions-cell {
  flex: 0 0 140px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-direction: row;
  gap: 10px;
  border-radius: 6px;
  padding: 10px;
}
.action-btn {
  width: 44px;
  height: 44px;
  border-radius: 6px;
  padding: 10px;
  background: white;
  gap: 10px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: 0.2s;
  border: 1px solid transparent;
}
.action-btn.edit {
  border: 1px solid #2770C2;
  border-radius: 6px;
  padding: 10px;
}
.action-btn.edit:hover {
  background: #eef4ff;
}
.action-btn.delete {
  border: 1px solid #902923;
  border-radius: 6px;
  padding: 10px;
}
.action-btn.delete:hover {
  background: #fff0f0;
}
</style>