import { BootstrapVue } from "bootstrap-vue";
import { library } from "@fortawesome/fontawesome-svg-core";
import { faUserAlt, faHelicopter, faCog, faTrash } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-vue/dist/bootstrap-vue.css";
import Vue from "vue";
import Tribe from "@/data/types/Tribe";
import Squad from "@/data/types/Squad";
import Member from "@/data/types/Member";
import App from "./App.vue";
import router from "./router";

library.add(faUserAlt, faHelicopter, faCog, faTrash);
Vue.component("font-awesome-icon", FontAwesomeIcon);

Vue.use(BootstrapVue);

Vue.config.productionTip = false;

const testTribeOne = new Tribe(1, "Game of Thrones", [
  new Squad(1, "Lannister", "#9F1830", [
    new Member(1, "Tywin Lannister"),
    new Member(2, "Jamie Lannister"),
    new Member(3, "Cercei Lannister"),
    new Member(4, "Tyrion Lannister"),
  ]),

  new Squad(2, "Stark", "#FEF5CC", [
    new Member(5, "Eddard Stark"),
    new Member(6, "Catelyn Stark"),
    new Member(7, "Arya Stark"),
    new Member(8, "Sansa Stark"),
    new Member(9, "Rob Stark"),
    new Member(10, "Bran Stark"),
    new Member(11, "Rickon Stark"),
  ]),

  new Squad(3, "Targaryen", "#101006", [
    new Member(12, "Danerys Targaryen"),
    new Member(13, "Viserys Targaryen"),
    new Member(14, "Aegon Targaryen (Jon Snow)"),
    new Member(100, "Viserion the Dragon"),
    new Member(101, "Drogon the Dragon"),
    new Member(102, "Rhaegar the Dragon"),
  ]),

  new Squad(4, "Baratheon", "#FCB600", [
    new Member(15, "Robert Baratheon"),
    new Member(16, "Joffery Baratheon"),
    new Member(17, "Stannis Baratheon"),
    new Member(18, "Renly Baratheon"),
    new Member(19, "Thomon Baratheon"),
  ]),

  new Squad(5, "Tully", "#002B4B", [
    new Member(20, "Brynden Tully"),
  ]),

  new Squad(6, "Greyjoy", "#FDD33A", [
    new Member(21, "Theon Greyjoy"),
    new Member(22, "Yara Greyjoy"),
    new Member(23, "Balon Greyjoy"),
  ]),

  new Squad(7, "Arryn", "#2B5DB2", [
    new Member(24, "Robyn Arryn"),
    new Member(25, "Lysa Arryn"),
    new Member(26, "Jon Arryn"),
  ]),

  new Squad(8, "Tyrell", "#4C860D", [
    new Member(27, "Olenna Tyrell"),
    new Member(28, "Mace Tyrell"),
    new Member(29, "Loras Tyrell"),
    new Member(30, "Margaery Tyrell"),
  ]),

  new Squad(9, "Martell", "#DD8A08", [
    new Member(31, "Oberyn Martell"),
    new Member(32, "Ellaria Martell"),
    new Member(33, "Obara Martell"),
    new Member(34, "Nymeria Martell"),
    new Member(35, "Tyene Martell"),
  ]),
]);

new Vue({
  router,
  render: (h) => h(App),
  data: {
    testTribeOne,
  },
}).$mount("#app");
