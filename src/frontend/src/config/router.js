import store from "@/config/store/store";
import Vue from "vue";
import VueRouter from "vue-router";

Vue.use(VueRouter);

const routes = [
  {
    name: "home",
    path: "/",
    component: () => import("@/pages/HomePage"),
    meta: { requiresAuthentication: false, firstLoginOnly: true },
  },
  {
    name: "dashboard",
    path: "/dashboard",
    component: () => import("@/pages/DashboardPage"),
    meta: { requiresAuthentication: true, firstLoginOnly: false },
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
    meta: { requiresAuthentication: true, firstLoginOnly: false },
  },
  {
    name: "view-tribe",
    path: "/tribes/:id",
    component: () => import("@/pages/tribes/ViewTribePage"),
    meta: { requiresAuthentication: true, firstLoginOnly: false },
  },
  {
    name: "create-chapter",
    path: "/chapters/create",
    component: () => import("@/pages/chapters/EditChapterPage"),
    meta: { requiresAuthentication: true, firstLoginOnly: false },
  },
  {
    name: "edit-chapter",
    path: "/chapters/:id/edit",
    component: () => import("@/pages/chapters/EditChapterPage"),
    meta: { requiresAuthentication: true, firstLoginOnly: false },
  },
  {
    name: "create-member",
    path: "/members/create",
    component: () => import("@/pages/members/EditMemberPage"),
    meta: { requiresAuthentication: true, firstLoginOnly: false },
  },
  {
    name: "edit-member",
    path: "/members/:id/edit",
    component: () => import("@/pages/members/EditMemberPage"),
    meta: { requiresAuthentication: true, firstLoginOnly: false },
  },
  {
    name: "create-squad",
    path: "/squads/create",
    component: () => import("@/pages/squads/EditSquadPage"),
    meta: { requiresAuthentication: true, firstLoginOnly: false },
  },
  {
    name: "edit-squad",
    path: "/squads/:id/edit",
    component: () => import("@/pages/squads/EditSquadPage"),
    meta: { requiresAuthentication: true, firstLoginOnly: false },
  },
  {
    name: "login",
    path: "/login",
    component: () => import("@/pages/auth/LoginPage"),
    meta: { requiresAuthentication: false, firstLoginOnly: false },
  },
  {
    name: "welcome",
    path: "/welcome",
    component: () => import("@/pages/auth/WelcomePage"),
    meta: { requiresAuthentication: true, firstLoginOnly: true },
  },
  {
    name: "404",
    path: "/404",
    component: () => import("@/pages/NotFoundPage"),
    meta: { requiresAuthentication: false, firstLoginOnly: false },
  },
  {
    path: "/:pathMatch(.*)*",
    component: () => import("@/pages/NotFoundPage"),
    meta: { requiresAuthentication: false, firstLoginOnly: false },
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

  if (to.meta.requiresAuthentication === true && store.getters.isAuthenticated === true) {
    authenticatedBeforeEach(to, from, next);
  } else if (to.meta.requiresAuthentication === true && store.getters.isAuthenticated === false) {
    next({ name: "login" });
  } else {
    next();
  }
});

function authenticatedBeforeEach(to, from, next) {
  if (store.getters.authenticatedAccount.isFirstLogin === true && to.meta.firstLoginOnly === true) {
    next();
  } else if (store.getters.authenticatedAccount.isFirstLogin === false && to.meta.firstLoginOnly === false) {
    next();
  } else if (store.getters.authenticatedAccount.isFirstLogin === true && to.meta.firstLoginOnly === false) {
    next({ name: "welcome" });
  } else if (store.getters.authenticatedAccount.isFirstLogin === false && to.meta.firstLoginOnly === true) {
    next({ name: "dashboard" });
  } else {
    throw new Error("Unexpected authentication routing");
  }
}

export default router;
