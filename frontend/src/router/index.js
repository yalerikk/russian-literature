import { createRouter, createWebHistory } from "vue-router";
import CatalogPage from "../pages/CatalogPage.vue";
import AuthorsPage from "../pages/AuthorsPage.vue";
// Admin
import AdminAuthorsPage from "../pages/admin/AdminAuthorsPage.vue";
import AdminUsersPage from "../pages/admin/AdminUsersPage.vue";
import AdminGenresPage from "../pages/admin/AdminGenresPage.vue";
import AdminTagsPage from "../pages/admin/AdminTagsPage.vue";
import AdminBooksPage from "../pages/admin/AdminBooksPage.vue";
import AdminCategoriesPage from "../pages/admin/AdminCategoriesPage.vue";
//
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
  {
    path: "/admin/users",
    name: "AdminUsers",
    component: AdminUsersPage,
    meta: { requiresAuth: true, requiresAdmin: true },
  },
  {
    path: "/admin/genres",
    name: "AdminGenres",
    component: AdminGenresPage,
    meta: { requiresAuth: true, requiresAdmin: true },
  },
  {
    path: "/admin/tags",
    name: "AdminTags",
    component: AdminTagsPage,
    meta: { requiresAuth: true, requiresAdmin: true },
  },
  {
    path: "/admin/books",
    name: "AdminBooks",
    component: AdminBooksPage,
    meta: { requiresAuth: true, requiresAdmin: true },
  },
  {
    path: "/admin/categories",
    name: "AdminCategories",
    component: AdminCategoriesPage,
    meta: { requiresAuth: true, requiresAdmin: true },
  },
  {
    path: "/reader/:id",
    name: "Reader",
    component: () => import("../pages/ReaderPage.vue"),
    meta: { requiresAuth: true, hideLayout: true },
  },
  // Добавьте другие маршруты позже
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth && !authService.isAuthenticated.value) {
    console.log("Router beforeEach:", to.path);
    next("/");
  } else if (to.meta.requiresAdmin) {
    const user = authService.getUserFromToken();
    if (user?.role !== "ROLE_ADMIN") {
      console.log("Router beforeEach:", to.path);
      next("/");
    } else {
      console.log("Router beforeEach:", to.path);
      next();
    }
  } else {
    console.log("Router beforeEach:", to.path);
    next();
  }
});

export default router;
