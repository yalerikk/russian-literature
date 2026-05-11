import { reactive } from "vue";
import { apiClient } from "../services/api";
import { authService } from "../services/authService";

const state = reactive({
  favorites: new Set(),
});

let isLoading = false;

export const useFavorites = () => {
  const loadFavorites = async () => {
    if (!authService.isAuthenticated.value || isLoading) return;
    isLoading = true;
    try {
      const res = await apiClient.get(
        "/users/me/books/favorites?page=0&size=1000"
      );
      const books = res.content || [];
      state.favorites.clear();
      books.forEach((book) => state.favorites.add(book.id));
      console.log("[Favorites] загружено", state.favorites.size);
    } catch (e) {
      console.warn("Ошибка загрузки избранного", e);
    } finally {
      isLoading = false;
    }
  };

  const addFavorite = async (bookId) => {
    if (!authService.isAuthenticated.value) throw new Error("Unauthorized");
    await apiClient.post(`/users/me/books/${bookId}?favorite=true`);
    state.favorites.add(bookId);
  };

  const removeFavorite = async (bookId) => {
    if (!authService.isAuthenticated.value) throw new Error("Unauthorized");
    await apiClient.delete(`/users/me/books/${bookId}`);
    state.favorites.delete(bookId);
  };

  const clearFavorites = () => state.favorites.clear();

  const isFavorite = (bookId) => state.favorites.has(bookId);

  return {
    loadFavorites,
    addFavorite,
    removeFavorite,
    clearFavorites,
    isFavorite,
  };
};
