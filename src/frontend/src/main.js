import { library } from "@fortawesome/fontawesome-svg-core";
import {
  faCog, faHelicopter, faPlus, faTrash, faUpload, faUserAlt, faTimes, faBars, faSun, faMoon, faPencilAlt, faEye,
  faCheck, faExclamation, faInfo, faTag, faUserFriends, faUsers, faSync,
} from "@fortawesome/free-solid-svg-icons";
import { faGithub } from "@fortawesome/free-brands-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { darkMode } from "@/mixins/darkMode";
import App from "@/App.vue";
import router from "@/config/router";
import store from "@/config/store/store";
import Vue from "vue";
import Vuelidate from "vuelidate";
import "./assets/style.css";

library.add(
  faUserAlt, faHelicopter, faCog, faTrash, faUpload, faPlus, faTimes, faBars, faSun, faMoon, faGithub, faPencilAlt,
  faEye, faCheck, faExclamation, faInfo, faTag, faUserFriends, faUsers, faSync,
);
Vue.component("font-awesome-icon", FontAwesomeIcon);

Vue.config.productionTip = false;

Vue.use(Vuelidate);

new Vue({
  mixins: [darkMode],
  store,
  router,
  render: (h) => h(App),
}).$mount("#app");
