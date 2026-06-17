<template>
  <div class="admin-page">
    <div class="admin-content">
      <div class="breadcrumb-nav">
        <router-link to="/profile/my-books" class="breadcrumb-item">Профиль</router-link>
        <span class="separator">/</span>
        <span class="breadcrumb-item current">Пользователи</span>
      </div>

      <div class="page-header">
        <h1 class="page-title">Пользователи</h1>
      </div>

      <AdminTable
        :columns="columns"
        :items="items"
        :loading="loading"
        :current-page="currentPage"
        :total-pages="totalPages"
        @page-change="onPageChange"
      >
        <template #cell-username="{ value }">{{ value }}</template>
        <template #cell-email="{ value }">{{ value }}</template>
        <template #cell-role="{ value }">
          <span :class="['role-badge', value === 'ADMIN' ? 'admin' : 'reader']">{{ value }}</span>
        </template>
        <template #cell-actions="{ item }">
          <div class="actions-cell">
            <button class="action-btn edit" @click="openEditModal(item)">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
                <path d="M5 19H6.425L16.2 9.225L14.775 7.8L5 17.575V19ZM4 21C3.71667 21 3.47933 20.904 3.288 20.712C3.09667 20.52 3.00067 20.2827 3 20V17.575C3 17.3083 3.05 17.054 3.15 16.812C3.25 16.57 3.39167 16.3577 3.575 16.175L16.2 3.575C16.4 3.39167 16.621 3.25 16.863 3.15C17.105 3.05 17.359 3 17.625 3C17.891 3 18.1493 3.05 18.4 3.15C18.6507 3.25 18.8673 3.4 19.05 3.6L20.425 5C20.625 5.18333 20.7707 5.4 20.862 5.65C20.9533 5.9 20.9993 6.15 21 6.4C21 6.66667 20.954 6.921 20.862 7.163C20.77 7.405 20.6243 7.62567 20.425 7.825L7.825 20.425C7.64167 20.6083 7.429 20.75 7.187 20.85C6.945 20.95 6.691 21 6.425 21H4ZM15.475 8.525L14.775 7.8L16.2 9.225L15.475 8.525Z" fill="#2770C2"/>
              </svg>
            </button>
            <button class="action-btn delete" @click="confirmDelete(item)">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
                <path d="M7 4C7 3.46957 7.21071 2.96086 7.58579 2.58579C7.96086 2.21071 8.46957 2 9 2H15C15.5304 2 16.0391 2.21071 16.4142 2.58579C16.7893 2.96086 17 3.46957 17 4V6H21C21.2652 6 21.5196 6.10536 21.7071 6.29289C21.8946 6.48043 22 6.73478 22 7C22 7.26522 21.8946 7.51957 21.7071 7.70711C21.5196 7.89464 21.2652 8 21 8H19.931L19.064 20.142C19.0281 20.6466 18.8023 21.1188 18.4321 21.4636C18.0619 21.8083 17.5749 22 17.069 22H6.93C6.42414 22 5.93707 21.8083 5.56688 21.4636C5.1967 21.1188 4.97092 20.6466 4.935 20.142L4.07 8H3C2.73478 8 2.48043 7.89464 2.29289 7.70711C2.10536 7.51957 2 7.26522 2 7C2 6.73478 2.10536 6.48043 2.29289 6.29289C2.48043 6.10536 2.73478 6 3 6H7V4ZM9 6H15V4H9V6ZM6.074 8L6.931 20H17.07L17.927 8H6.074ZM10 10C10.2652 10 10.5196 10.1054 10.7071 10.2929C10.8946 10.4804 11 10.7348 11 11V17C11 17.2652 10.8946 17.5196 10.7071 17.7071C10.5196 17.8946 10.2652 18 10 18C9.73478 18 9.48043 17.8946 9.29289 17.7071C9.10536 17.5196 9 17.2652 9 17V11C9 10.7348 9.10536 10.4804 9.29289 10.2929C9.48043 10.1054 9.73478 10 10 10ZM14 10C14.2652 10 14.5196 10.1054 14.7071 10.2929C14.8946 10.4804 15 10.7348 15 11V17C15 17.2652 14.8946 17.5196 14.7071 17.7071C14.5196 17.8946 14.2652 18 14 18C13.7348 18 13.4804 17.8946 13.2929 17.7071C13.1054 17.5196 13 17.2652 13 17V11C13 10.7348 13.1054 10.4804 13.2929 10.2929C13.4804 10.1054 13.7348 10 14 10Z" fill="#902923"/>
                <path d="M7 4C7 3.46957 7.21071 2.96086 7.58579 2.58579C7.96086 2.21071 8.46957 2 9 2H15C15.5304 2 16.0391 2.21071 16.4142 2.58579C16.7893 2.96086 17 3.46957 17 4V6H21C21.2652 6 21.5196 6.10536 21.7071 6.29289C21.8946 6.48043 22 6.73478 22 7C22 7.26522 21.8946 7.51957 21.7071 7.70711C21.5196 7.89464 21.2652 8 21 8H19.931L19.064 20.142C19.0281 20.6466 18.8023 21.1188 18.4321 21.4636C18.0619 21.8083 17.5749 22 17.069 22H6.93C6.42414 22 5.93707 21.8083 5.56688 21.4636C5.1967 21.1188 4.97092 20.6466 4.935 20.142L4.07 8H3C2.73478 8 2.48043 7.89464 2.29289 7.70711C2.10536 7.51957 2 7.26522 2 7C2 6.73478 2.10536 6.48043 2.29289 6.29289C2.48043 6.10536 2.73478 6 3 6H7V4ZM9 6H15V4H9V6ZM6.074 8L6.931 20H17.07L17.927 8H6.074ZM10 10C10.2652 10 10.5196 10.1054 10.7071 10.2929C10.8946 10.4804 11 10.7348 11 11V17C11 17.2652 10.8946 17.5196 10.7071 17.7071C10.5196 17.8946 10.2652 18 10 18C9.73478 18 9.48043 17.8946 9.29289 17.7071C9.10536 17.5196 9 17.2652 9 17V11C9 10.7348 9.10536 10.4804 9.29289 10.2929C9.48043 10.1054 9.73478 10 10 10ZM14 10C14.2652 10 14.5196 10.1054 14.7071 10.2929C14.8946 10.4804 15 10.7348 15 11V17C15 17.2652 14.8946 17.5196 14.7071 17.7071C14.5196 17.8946 14.2652 18 14 18C13.7348 18 13.4804 17.8946 13.2929 17.7071C13.1054 17.5196 13 17.2652 13 17V11C13 10.7348 13.1054 10.4804 13.2929 10.2929C13.4804 10.1054 13.7348 10 14 10Z" fill="#902923" fill-opacity="0.2"/>
              </svg>
            </button>
          </div>
        </template>
      </AdminTable>
    </div>

    <UserModal ref="userModal" @saved="loadItems" />
    <ConfirmModal ref="confirmModal" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useAdminCrud } from '../../composables/useAdminCrud'
import AdminTable from '../../components/admin/AdminTable.vue'
import UserModal from '../../components/admin/UserModal.vue'
import ConfirmModal from '../../components/ConfirmModal.vue'
import { apiClient } from '../../services/api'
import { formatErrorMessage } from '../../utils/errorFormatter'

const columns = [
  { key: 'username', label: 'Логин' },
  { key: 'email', label: 'Email' },
  { key: 'role', label: 'Роль' },
  { key: 'actions', label: 'Действия', className: 'actions-cell' }
]

const {
  items,
  loading,
  currentPage,
  totalPages,
  loadItems,
  onPageChange
} = useAdminCrud('/users/admin/list', '/users',10)

const userModal = ref(null)
const confirmModal = ref(null)

function openEditModal(user) {
  userModal.value.open(user)
}

async function confirmDelete(user) {
  const confirmed = await confirmModal.value.open(`Удалить пользователя «${user.username}»?`)
  if (!confirmed) return
  try {
    await apiClient.delete(`/users/${user.id}`)
    alert('Пользователь успешно удален')
    await loadItems()
  } catch (err) {
    const msg = formatErrorMessage(err, 'user')
    alert(msg)
  }
}

onMounted(loadItems)
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
.role-badge {
  display: inline-block;
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
  background: #F3F3F4;
  color: #1B1B1B;
}
.role-badge.admin {
  background: #902923;
  color: white;
}
.role-badge.reader {
  background: #AEC3B0;
  color: white;
}
</style>