import { apiClient } from "./api.js";

class BookService {
  // Получить все книги
  async getAllBooks() {
    return apiClient.get("/books");
  }

  // Получить книгу по ID
  async getBookById(id) {
    return apiClient.get(`/books/${id}`);
  }

  // Получить книги автора
  async getBooksByAuthor(authorId) {
    return apiClient.get(`/books/author/${authorId}`);
  }

  // Получить книги по жанру
  async getBooksByGenre(genreId) {
    return apiClient.get(`/books/genre/${genreId}`);
  }

  // Получить книги по тегу
  async getBooksByTag(tagId) {
    return apiClient.get(`/books/tag/${tagId}`);
  }

  // Создать книгу
  async createBook(bookData) {
    return apiClient.post("/books", bookData);
  }

  // Обновить книгу
  async updateBook(id, bookData) {
    return apiClient.put(`/books/${id}`, bookData);
  }

  // Удалить книгу
  async deleteBook(id) {
    return apiClient.delete(`/books/${id}`);
  }
}

export const bookService = new BookService();
