import { createRouter, createWebHistory } from "vue-router";
import CatalogPage from "../pages/CatalogPage.vue";
import AuthorsPage from "../pages/AuthorsPage.vue";
import AdminAuthorsPage from "../pages/admin/AdminAuthorsPage.vue";
import { authService } from "../services/authService";

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
  {
    path: "/search",
    name: "Search",
    component: () => import("../pages/SearchPage.vue"),
    props: (route) => ({ query: route.query.q }),
  },
  {
    path: "/profile",
    name: "Profile",
    component: () => import("../pages/ProfilePage.vue"),
    meta: { requiresAuth: true },
  },
  {
    path: "/profile/my-books",
    name: "MyBooks",
    component: () => import("../pages/MyBooks.vue"),
    meta: { requiresAuth: true },
  },
  {
    path: "/profile/books/:status",
    name: "Collection",
    component: () => import("../pages/CollectionPage.vue"),
    meta: { requiresAuth: true },
    props: true,
  },
  {
    path: "/profile/favorites",
    name: "Favorites",
    component: () => import("../pages/Favorites.vue"),
    meta: { requiresAuth: true },
  },
  {
    path: "/profile/edit",
    name: "ProfileEdit",
    component: () => import("../pages/ProfileEdit.vue"),
    meta: { requiresAuth: true },
  },
  {
    path: "/admin/authors",
    name: "AdminAuthors",
    component: AdminAuthorsPage,
    meta: { requiresAuth: true, requiresAdmin: true },
  },
  // Добавьте другие маршруты позже
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth && !authService.isAuthenticated.value) {
    next("/");
  } else if (to.meta.requiresAdmin) {
    const user = authService.getUserFromToken();
    if (user?.role !== "ROLE_ADMIN") {
      next("/");
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router;
