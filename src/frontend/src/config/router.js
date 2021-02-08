import Vue from "vue";
import VueRouter from "vue-router";

Vue.use(VueRouter);

const routes = [
  {
    name: "home",
    path: "/",
    component: () => import("@/pages/HomePage"),
  },
  {
    name: "dashboard",
    path: "/dashboard",
    component: () => import("@/pages/DashboardPage"),
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
  },
  {
    name: "view-tribe",
    path: "/tribes/:id",
    component: () => import("@/pages/tribes/ViewTribePage"),
  },
  {
    name: "create-chapter",
    path: "/chapters/create",
    component: () => import("@/pages/chapters/EditChapterPage"),
  },
  {
    name: "edit-chapter",
    path: "/chapters/:id/edit",
    component: () => import("@/pages/chapters/EditChapterPage"),
  },
  {
    name: "create-member",
    path: "/members/create",
    component: () => import("@/pages/members/EditMemberPage"),
  },
  {
    name: "edit-member",
    path: "/members/:id/edit",
    component: () => import("@/pages/members/EditMemberPage"),
  },
  {
    name: "create-squad",
    path: "/squads/create",
    component: () => import("@/pages/squads/EditSquadPage"),
  },
  {
    name: "edit-squad",
    path: "/squads/:id/edit",
    component: () => import("@/pages/squads/EditSquadPage"),
  },
  {
    name: "404",
    path: "/404",
    component: () => import("@/pages/NotFoundPage"),
  },
  {
    path: "/:pathMatch(.*)*",
    component: () => import("@/pages/NotFoundPage"),
  },
];

const router = new VueRouter({
  mode: "history",
  routes,
});

export default router;
