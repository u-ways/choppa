import Vue from "vue";
import VueRouter from "vue-router";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    component: () => import("@/pages/HomePage"),
  },
  {
    path: "/tribes/:id/edit",
    component: () => import("@/pages/tribes/EditTribePage"),
  },
  {
    path: "/tribes/:id",
    component: () => import("@/pages/tribes/ViewTribePage"),
  },
  {
    path: "/chapters/:id",
    component: () => import("@/pages/chapters/EditChapterPage"),
  },
  {
    path: "/members/:id",
    component: () => import("@/pages/members/EditMemberPage"),
  },
  {
    path: "/squads/:id",
    component: () => import("@/pages/squads/EditSquadPage"),
  },
  {
    path: "/external/github",
    beforeEnter() { window.location.href = "https://github.com/u-ways/chopper"; },
  },
  {
    path: "/external/production",
    beforeEnter() { window.location.href = "https://www.choppa.app"; },
  },
  {
    path: "/external/staging",
    beforeEnter() { window.location.href = "https://choppa-staging.herokuapp.com/"; },
  },
  {
    path: "/:pathMatch(.*)*",
    component: () => import("@/pages/NotFoundPage"),
  },
];

const router = new VueRouter({
  routes,
});

export default router;
