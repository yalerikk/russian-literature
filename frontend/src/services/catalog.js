import { apiClient } from "./api.js";

class CatalogService {
  async getCatalogPage() {
    return apiClient.get("/api/catalog/page");
  }
}

export const catalogService = new CatalogService();
