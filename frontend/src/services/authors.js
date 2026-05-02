import { apiClient } from "./api.js";

class AuthorService {
  async getAllAuthors() {
    return apiClient.get("/authors");
  }

  async getAuthorById(id) {
    return apiClient.get(`/authors/${id}`);
  }

  async createAuthor(authorData) {
    return apiClient.post("/authors", authorData);
  }

  async updateAuthor(id, authorData) {
    return apiClient.put(`/authors/${id}`, authorData);
  }

  async deleteAuthor(id) {
    return apiClient.delete(`/authors/${id}`);
  }
}

export const authorService = new AuthorService();
