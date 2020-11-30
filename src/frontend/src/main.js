import { library } from "@fortawesome/fontawesome-svg-core";
import {
  faCog, faHelicopter, faPlus, faTrash, faUpload, faUserAlt, faTimes, faBars, faSun, faMoon,
} from "@fortawesome/free-solid-svg-icons";
import { faGithub } from "@fortawesome/free-brands-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { darkMode } from "@/mixins/darkMode";
import App from "@/App.vue";
import router from "@/config/router";
import store from "@/config/store/store";
import Vue from "vue";
import "./assets/style.css";

library.add(faUserAlt, faHelicopter, faCog, faTrash, faUpload, faPlus, faTimes, faBars, faSun, faMoon, faGithub);
Vue.component("font-awesome-icon", FontAwesomeIcon);

Vue.config.productionTip = false;

new Vue({
  mixins: [darkMode],
  store,
  router,
  render: (h) => h(App),
}).$mount("#app");
