import { apiClient } from "./api";

export const userBookService = {
  // Получить статус книги для пользователя (избранное + коллекция)
  async getBookStatus(bookId) {
    try {
      const [favorite, collection] = await Promise.all([
        apiClient.get(`/users/me/books/${bookId}/favorite`).catch(() => false),
        apiClient.get(`/users/me/books/${bookId}/status`).catch(() => null),
      ]);
      return { isFavorite: favorite === true, status: collection };
    } catch {
      return { isFavorite: false, status: null };
    }
  },

  // Добавить/обновить книгу в коллекции
  async addToUserBooks(bookId, status, favorite = false) {
    return apiClient.post(
      `/users/me/books/${bookId}?status=${status}&favorite=${favorite}`
    );
  },

  // Удалить книгу из всех коллекций (и из избранного)
  async removeFromUserBooks(bookId) {
    return apiClient.delete(`/users/me/books/${bookId}`);
  },
};
