import { library } from "@fortawesome/fontawesome-svg-core";
import { faCog, faHelicopter, faPlus, faTrash, faUpload, faUserAlt } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import Vue from "vue";
import App from "./App.vue";
import router from "./config/router";
import "./assets/_style.css";

library.add(faUserAlt, faHelicopter, faCog, faTrash, faUpload, faPlus);
Vue.component("font-awesome-icon", FontAwesomeIcon);

Vue.config.productionTip = false;

new Vue({
  router,
  render: (h) => h(App),
}).$mount("#app");
