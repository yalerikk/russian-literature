import { apiClient } from "./api.js";

class TagService {
  async getAllTags() {
    return apiClient.get("/book-tags");
  }

  async getTagById(id) {
    return apiClient.get(`/book-tags/${id}`);
  }

  async getTagsByType(type) {
    return apiClient.get(`/book-tags/by-type?type=${type}`);
  }

  async createTag(tagData) {
    return apiClient.post("/book-tags", tagData);
  }

  async updateTag(id, tagData) {
    return apiClient.put(`/book-tags/${id}`, tagData);
  }

  async deleteTag(id) {
    return apiClient.delete(`/book-tags/${id}`);
  }
}

export const tagService = new TagService();
