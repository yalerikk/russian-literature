import { createRouter, createWebHistory } from "vue-router";
import CatalogPage from "../pages/CatalogPage.vue";
import AuthorsPage from "../pages/AuthorsPage.vue";

const routes = [
  {
    path: "/",
    redirect: "/catalog",
  },
  {
    path: "/catalog",
    name: "Catalog",
    component: CatalogPage,
  },
  {
    path: "/authors",
    name: "Authors",
    component: AuthorsPage,
  },
  {
    path: "/authors/:id",
    name: "AuthorDetail",
    component: () => import("../views/AuthorDetailPage.vue"), // Будет создан позже
  },
  {
    path: "/books/:id",
    name: "BookDetail",
    component: () => import("../views/BookDetailPage.vue"),
    props: true,
  },
  {
    path: "/catalog/category/:code",
    name: "Category",
    component: () => import("../pages/CategoryPage.vue"),
    props: true,
  },
  // Добавьте другие маршруты позже
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
