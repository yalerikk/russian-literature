import { ref } from "vue";
import { apiClient } from "./api";

const TOKEN_KEY = "jwt_token";
export const isAuthenticated = ref(!!localStorage.getItem(TOKEN_KEY));

export const authService = {
  isAuthenticated, // ref
  async login(login, password) {
    const response = await apiClient.post("/users/login", { login, password });
    if (response.token) this.setToken(response.token);
    isAuthenticated.value = true;
    return response;
  },
  async register(username, email, password) {
    const response = await apiClient.post("/users/register", {
      username,
      email,
      password,
      role: "READER",
    });
    if (response.id) await this.login(username, password);
    return response;
  },
  logout() {
    this.removeToken();
    isAuthenticated.value = false;
  },
  setToken(token) {
    localStorage.setItem(TOKEN_KEY, token);
  },
  getToken() {
    return localStorage.getItem(TOKEN_KEY);
  },
  removeToken() {
    localStorage.removeItem(TOKEN_KEY);
  },
  getUserIdFromToken() {
    const token = this.getToken();
    if (!token) return null;
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.userId || payload.id || payload.sub;
    } catch (e) {
      console.error('Ошибка парсинга токена', e);
      return null;
    }
  },
  getUserFromToken() {
    const token = this.getToken();
    if (!token) return null;
    try {
      const payload = JSON.parse(atob(token.split(".")[1]));
      return {
        id: payload.userId || payload.id,
        username: payload.sub,
        role: payload.role,
      };
    } catch {
      return null;
    }
  },
};
