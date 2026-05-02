// HTTP клиент для работы с API
class ApiClient {
  constructor(baseURL) {
    this.baseURL = baseURL || "http://localhost:8080";
  }

  async request(endpoint, options = {}) {
    const url = `${this.baseURL}${endpoint}`;

    const defaultOptions = {
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
      },
      credentials: "include", // если будет авторизация
    };

    try {
      const response = await fetch(url, { ...defaultOptions, ...options });

      if (!response.ok) {
        const error = await response.json().catch(() => ({
          message: `HTTP ${response.status}: ${response.statusText}`,
        }));
        throw new Error(error.message || "Ошибка сети");
      }

      // Для DELETE запросов может не быть тела
      if (response.status === 204) {
        return null;
      }

      return await response.json();
    } catch (error) {
      console.error("API Request failed:", error);
      throw error;
    }
  }

  get(endpoint) {
    return this.request(endpoint, { method: "GET" });
  }

  post(endpoint, data) {
    return this.request(endpoint, {
      method: "POST",
      body: JSON.stringify(data),
    });
  }

  put(endpoint, data) {
    return this.request(endpoint, {
      method: "PUT",
      body: JSON.stringify(data),
    });
  }

  delete(endpoint) {
    return this.request(endpoint, { method: "DELETE" });
  }
}

// Создаем экземпляр клиента
export const apiClient = new ApiClient(
  import.meta.env.VITE_API_BASE_URL || "http://localhost:8080"
);
