import { createApp } from "vue";
import App from "./App.vue";
import router from "./router"; 
import { useFavorites } from "./stores/favorites";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap";
import "@fortawesome/fontawesome-free/css/all.min.css";
import "./assets/css/main.css";

const app = createApp(App);

// Подключаем роутер
app.use(router);

// Глобальная переменная для API
app.config.globalProperties.$apiBaseUrl =
  import.meta.env.VITE_API_BASE_URL;

app.mount("#app");
