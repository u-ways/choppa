import store from "@/config/store/store";
import Vue from "vue";
import VueRouter from "vue-router";

Vue.use(VueRouter);

const routes = [
  {
    name: "home",
    path: "/",
    component: () => import("@/pages/HomePage"),
    meta: { requiresAuthentication: false },
  },
  {
    name: "dashboard",
    path: "/dashboard",
    component: () => import("@/pages/DashboardPage"),
    meta: { requiresAuthentication: true },
  },
  {
    name: "my-tribes",
    path: "/my-tribes",
    component: () => import("@/pages/tribes/MyTribes"),
  },
  {
    name: "create-tribe",
    path: "/tribes/create",
    component: () => import("@/pages/tribes/EditTribePage"),
  },
  {
    name: "edit-tribe",
    path: "/tribes/:id/edit",
    component: () => import("@/pages/tribes/EditTribePage"),
    meta: { requiresAuthentication: true },
  },
  {
    name: "view-tribe",
    path: "/tribes/:id",
    component: () => import("@/pages/tribes/ViewTribePage"),
    meta: { requiresAuthentication: true },
  },
  {
    name: "create-chapter",
    path: "/chapters/create",
    component: () => import("@/pages/chapters/EditChapterPage"),
    meta: { requiresAuthentication: true },
  },
  {
    name: "edit-chapter",
    path: "/chapters/:id/edit",
    component: () => import("@/pages/chapters/EditChapterPage"),
    meta: { requiresAuthentication: true },
  },
  {
    name: "create-member",
    path: "/members/create",
    component: () => import("@/pages/members/EditMemberPage"),
    meta: { requiresAuthentication: true },
  },
  {
    name: "edit-member",
    path: "/members/:id/edit",
    component: () => import("@/pages/members/EditMemberPage"),
    meta: { requiresAuthentication: true },
  },
  {
    name: "create-squad",
    path: "/squads/create",
    component: () => import("@/pages/squads/EditSquadPage"),
    meta: { requiresAuthentication: true },
  },
  {
    name: "edit-squad",
    path: "/squads/:id/edit",
    component: () => import("@/pages/squads/EditSquadPage"),
    meta: { requiresAuthentication: true },
  },
  {
    name: "login",
    path: "/login",
    component: () => import("@/pages/auth/LoginPage"),
    meta: { requiresAuthentication: false },
  },
  {
    name: "welcome",
    path: "/welcome",
    component: () => import("@/pages/auth/WelcomePage"),
    meta: { requiresAuthentication: true },
  },
  {
    name: "404",
    path: "/404",
    component: () => import("@/pages/NotFoundPage"),
    meta: { requiresAuthentication: false },
  },
  {
    path: "/:pathMatch(.*)*",
    component: () => import("@/pages/NotFoundPage"),
    meta: { requiresAuthentication: false },
  },
];

const router = new VueRouter({
  mode: "history",
  routes,
});

router.beforeEach(async (to, from, next) => {
  // On first load we should load authentication status
  if (router.doneFirstLoad === undefined) {
    await store.dispatch("updateAuthenticationStatus");
    router.doneFirstLoad = true;
  }

  if (to.meta.requiresAuthentication === true) {
    if (store.getters.isAuthenticated === true) {
      next();
    } else {
      next({ name: "login" });
    }
  } else {
    next();
  }
});

export default router;
