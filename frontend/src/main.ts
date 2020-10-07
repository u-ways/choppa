import { createApp } from "vue";
import App from "./App.vue";
import { library } from "@fortawesome/fontawesome-svg-core";
import { faUserAlt, faHelicopter } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import router from "@/router";

library.add(faUserAlt, faHelicopter);

createApp(App)
    .component("font-awesome", FontAwesomeIcon)
    .use(router)
    .mount("#app");
