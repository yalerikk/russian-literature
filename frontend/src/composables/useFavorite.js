import { ref } from "vue";
import { userBookService } from "../services/userBookService";

export function useFavorite() {
  const favoriteMap = ref({});

  async function loadFavoriteStatus(bookId) {
    if (!isAuthenticated()) return false;
    const { isFavorite } = await userBookService.getBookStatus(bookId);
    favoriteMap.value[bookId] = isFavorite;
    return isFavorite;
  }

  async function toggleFavorite(bookId) {
    if (!isAuthenticated()) return;
    const current = favoriteMap.value[bookId];
    await userBookService.addToUserBooks(bookId, null, !current);
    favoriteMap.value[bookId] = !current;
  }

  return { favoriteMap, loadFavoriteStatus, toggleFavorite };
}
