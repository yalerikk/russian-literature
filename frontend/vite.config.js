import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 3000,
    historyApiFallback: true,
    proxy: {
      "/api": "http://localhost:8080",
      "/users": "http://localhost:8080",
      "/books": "http://localhost:8080",
      "/genres": "http://localhost:8080",
      "/tags": "http://localhost:8080",
      "/authors": "http://localhost:8080",
    },
  },
});
