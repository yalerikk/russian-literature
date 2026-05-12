import { ref } from "vue";
import { apiClient } from "../services/api";

export function useAdminCrud(fetchUrl, apiBaseUrl, pageSize = 10) {
  const items = ref([]);
  const loading = ref(true);
  const currentPage = ref(0);
  const totalPages = ref(0);
  const error = ref(null);

  async function loadItems() {
    loading.value = true;
    error.value = null;
    try {
      const res = await apiClient.get(
        `${fetchUrl}?page=${currentPage.value}&size=${pageSize}`
      );
      items.value = res.content;
      totalPages.value = res.totalPages;
    } catch (err) {
      error.value = err.message || "Ошибка загрузки";
    } finally {
      loading.value = false;
    }
  }

  function onPageChange(page) {
    currentPage.value = page;
    loadItems();
  }

  // Поддержка разных эндпоинтов – удаляем /admin/list, если есть
  async function deleteItem(id, confirmMessage) {
    if (!confirm(confirmMessage)) return false;
    const baseUrl = apiBaseUrl || fetchUrl.replace('/admin/list', '');
    try {
      await apiClient.delete(`${baseUrl}/${id}`);
      await loadItems();
      return true;
    } catch (err) {
      const msg = err.data?.details || err.message || "Ошибка";
      alert(msg);
      return false;
    }
  }

  async function saveItem(payload, isEdit, id) {
    const baseUrl = apiBaseUrl || fetchUrl.replace("/admin/list", "");
    if (isEdit) {
      return apiClient.put(`${baseUrl}/${id}`, payload);
    } else {
      return apiClient.post(baseUrl, payload);
    }
  }

  return {
    items,
    loading,
    currentPage,
    totalPages,
    error,
    loadItems,
    onPageChange,
    deleteItem,
    saveItem,
  };
}
