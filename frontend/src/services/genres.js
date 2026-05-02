import { apiClient } from "./api.js";

class GenreService {
  async getAllGenres() {
    return apiClient.get("/genres");
  }

  async getGenreById(id) {
    return apiClient.get(`/genres/${id}`);
  }

  async createGenre(genreData) {
    return apiClient.post("/genres", genreData);
  }

  async updateGenre(id, genreData) {
    return apiClient.put(`/genres/${id}`, genreData);
  }

  async deleteGenre(id) {
    return apiClient.delete(`/genres/${id}`);
  }
}

export const genreService = new GenreService();
