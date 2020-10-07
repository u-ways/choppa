import {createRouter, createWebHashHistory} from "vue-router";
import MemberPage from "@/components/pages/MemberPage.vue";
import HomePage from "@/components/pages/HomePage.vue";
import NotFoundPage from "@/components/pages/NotFoundPage.vue";

const router = createRouter({
    history: createWebHashHistory(),
    routes: [
        {
            path: "/",
            component: HomePage
        },
        {
            path: "/member",
            component: MemberPage
        },
        {
            path: "/:pathMatch(.*)*",
            component: NotFoundPage
        }
    ]
});

export default router;