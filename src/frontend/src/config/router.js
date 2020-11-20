import Vue from "vue";
import VueRouter from "vue-router";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    component: () => import("../components/pages/HomePage"),
  },
  {
    path: "/tribe/:id",
    component: () => import("../components/pages/tribe/ViewTribePage"),
  },
  {
    path: "/tribe/:id/edit",
    component: () => import("../components/pages/tribe/EditTribePage"),
  },
  {
    path: "/:pathMatch(.*)*",
    component: () => import("../components/pages/NotFoundPage"),
  },
];

const router = new VueRouter({
  routes,
});

export default router;
