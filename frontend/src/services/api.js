import { authService } from "./authService";

class ApiClient {
  constructor(baseURL) {
    this.baseURL = baseURL;
  }

  async request(endpoint, options = {}) {
    const url = `${this.baseURL}${endpoint}`;
    const token = localStorage.getItem("jwt_token");

    const headers = {
      "Content-Type": "application/json",
      Accept: "application/json; charset=utf-8",
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

      // Пытаемся прочитать тело ответа (для любых статусов)
      let data = null;
      let rawText = null;
      const contentType = response.headers.get("content-type");
      const isJson = contentType && contentType.includes("application/json");

      if (isJson) {
        try {
          data = await response.json();
          console.log(`[API] Тело ответа (JSON):`, data);
        } catch (jsonError) {
          console.warn("Ошибка парсинга JSON", jsonError);
        }
      } else {
        rawText = await response.text();
        console.log(`[API] Тело ответа (текст):`, rawText);
      }

      // Если ответ неуспешен — формируем ошибку с человеческим сообщением
      if (!response.ok) {
        let errorMessage;
        if (data && data.message) {
          errorMessage = data.message; // берём именно message (детали)
        } else if (data && data.error) {
          errorMessage = data.error;
        } else if (rawText) {
          errorMessage = rawText;
        } else {
          errorMessage = `HTTP ${response.status}: ${response.statusText}`;
        }
        console.error("[API] Ошибка с бэкенда:", errorMessage);

        const error = new Error(errorMessage);
        error.response = response;
        error.data = data;
        throw error;
      }

      if (response.status === 204) return null;
      return data;
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

  patch(endpoint, data) {
    return this.request(endpoint, {
      method: "PATCH",
      body: data !== undefined ? JSON.stringify(data) : undefined,
    });
  }

  delete(endpoint) {
    return this.request(endpoint, { method: "DELETE" });
  }

  async uploadFile(endpoint, file) {
    const formData = new FormData();
    formData.append("file", file);
    const token = localStorage.getItem("jwt_token");
    const response = await fetch(endpoint, {
      method: "POST",
      headers: {
        Authorization: token ? `Bearer ${token}` : undefined,
      },
      body: formData,
    });
    if (!response.ok) {
      const error = await response
        .json()
        .catch(() => ({ message: "Upload failed" }));
      throw new Error(error.message || "Upload failed");
    }
    return response.json();
  }
}

// Создаем экземпляр клиента
export const apiClient = new ApiClient("");
