import { authService } from "./authService"; 

// HTTP клиент для работы с API
class ApiClient {
  constructor(baseURL) {
    this.baseURL = baseURL;
  }

  async request(endpoint, options = {}) {
    const url = `${this.baseURL}${endpoint}`;
    const token = localStorage.getItem("jwt_token");

    const headers = {
      "Content-Type": "application/json",
      "Accept": "application/json; charset=utf-8",
      "Accept-Charset": "utf-8",
      ...(token && { Authorization: `Bearer ${token}` }),
      ...options.headers,
    };

    try {
      console.log(
        `[API] ${options.method || "GET"} ${url}`,
        options.body ? JSON.parse(options.body) : ""
      );
      const response = await fetch(url, { ...options, headers });
      console.log(`[API] Ответ ${url}`, response.status, response.statusText);

      if (response.status === 401) {
        console.warn("[API] 401 Unauthorized – выполняю logout");
        authService.logout();
        window.location.reload();
        throw new Error("Сессия истекла");
      }

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

      const contentType = response.headers.get('content-type');
      if (contentType && contentType.includes('application/json')) {
        const data = await response.json();
        console.log(
          `[API] Тело ответа (первые 200 символов):`,
          JSON.stringify(data).slice(0, 200)
        );
        return data;
      }
      return null;
    } catch (error) {
      console.error("API Request failed:", error);
      throw error;
    }
  }

  get(endpoint, options = {}) {
    let url = endpoint;
    if (options.params) {
      const query = new URLSearchParams(options.params).toString();
      url += `?${query}`;
    }
    return this.request(url, { method: "GET" });
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
export const apiClient = new ApiClient('');
