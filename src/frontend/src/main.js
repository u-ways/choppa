import { BootstrapVue } from "bootstrap-vue";
import { library } from "@fortawesome/fontawesome-svg-core";
import { faUserAlt, faHelicopter, faCog, faTrash, faUpload } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-vue/dist/bootstrap-vue.css";
import Vue from "vue";
import Tribe from "@/data/types/Tribe";
import Squad from "@/data/types/Squad";
import Member from "@/data/types/Member";
import Chapter from "@/data/types/Chapter";
import App from "./App.vue";
import router from "./router";

library.add(faUserAlt, faHelicopter, faCog, faTrash, faUpload);
Vue.component("font-awesome-icon", FontAwesomeIcon);

Vue.use(BootstrapVue);

Vue.config.productionTip = false;

const testTribeOne = new Tribe(1, "Game of Thrones", [
  new Squad(1, "Lannister", "#9F1830", [
    new Member(1, "Tywin Lannister", new Chapter("1", "Team Lead", "#AC190F")),
    new Member(2, "Jamie Lannister", new Chapter("2", "Tester", "#62A756")),
    new Member(3, "Cercei Lannister", new Chapter("3", "Developer", "#005391")),
    new Member(4, "Tyrion Lannister", new Chapter("4", "Intern", "#674DA4")),
  ]),

  new Squad(2, "Stark", "#FEF5CC", [
    new Member(5, "Eddard Stark", new Chapter("1", "Team Lead", "#AC190F")),
    new Member(6, "Catelyn Stark", new Chapter("5", "Business Analyst", "#F5C147")),
    new Member(7, "Rob Stark", new Chapter("3", "Developer", "#005391")),
    new Member(8, "Arya Stark", new Chapter("6", "Secretary", "#C7288E")),
    new Member(9, "Sansa Stark", new Chapter("6", "Secretary", "#C7288E")),
    new Member(10, "Bran Stark", new Chapter("4", "Intern", "#674DA4")),
    new Member(11, "Rickon Stark", new Chapter("4", "Intern", "#674DA4")),
  ]),

  new Squad(3, "Targaryen", "#101006", [
    new Member(12, "Danerys Targaryen", new Chapter("3", "Developer", "#005391")),
    new Member(13, "Viserys Targaryen", new Chapter("3", "Developer", "#005391")),
    new Member(14, "Aegon Targaryen (Jon Snow)", new Chapter("3", "Developer", "#005391")),
    new Member(100, "Viserion the Dragon", new Chapter("3", "Developer", "#005391")),
    new Member(101, "Drogon the Dragon", new Chapter("3", "Developer", "#005391")),
    new Member(102, "Rhaegar the Dragon", new Chapter("3", "Developer", "#005391")),
  ]),

  new Squad(4, "Baratheon", "#FCB600", [
    new Member(15, "Robert Baratheon", new Chapter("3", "Developer", "#005391")),
    new Member(16, "Joffery Baratheon", new Chapter("3", "Developer", "#005391")),
    new Member(17, "Stannis Baratheon", new Chapter("3", "Developer", "#005391")),
    new Member(18, "Renly Baratheon", new Chapter("3", "Developer", "#005391")),
    new Member(19, "Thomon Baratheon", new Chapter("3", "Developer", "#005391")),
  ]),

  new Squad(5, "Tully", "#002B4B", [
    new Member(20, "Brynden Tully", new Chapter("3", "Developer", "#005391")),
  ]),

  new Squad(6, "Greyjoy", "#FDD33A", [
    new Member(21, "Theon Greyjoy", new Chapter("3", "Developer", "#005391")),
    new Member(22, "Yara Greyjoy", new Chapter("3", "Developer", "#005391")),
    new Member(23, "Balon Greyjoy", new Chapter("3", "Developer", "#005391")),
  ]),

  new Squad(7, "Arryn", "#2B5DB2", [
    new Member(24, "Robyn Arryn", new Chapter("3", "Developer", "#005391")),
    new Member(25, "Lysa Arryn", new Chapter("3", "Developer", "#005391")),
    new Member(26, "Jon Arryn", new Chapter("3", "Developer", "#005391")),
  ]),

  new Squad(8, "Tyrell", "#4C860D", [
    new Member(27, "Olenna Tyrell", new Chapter("3", "Developer", "#005391")),
    new Member(28, "Mace Tyrell", new Chapter("3", "Developer", "#005391")),
    new Member(29, "Loras Tyrell", new Chapter("3", "Developer", "#005391")),
    new Member(30, "Margaery Tyrell", new Chapter("3", "Developer", "#005391")),
  ]),

  new Squad(9, "Martell", "#DD8A08", [
    new Member(31, "Oberyn Martell", new Chapter("3", "Developer", "#005391")),
    new Member(32, "Ellaria Martell", new Chapter("3", "Developer", "#005391")),
    new Member(33, "Obara Martell", new Chapter("3", "Developer", "#005391")),
    new Member(34, "Nymeria Martell", new Chapter("3", "Developer", "#005391")),
    new Member(35, "Tyene Martell", new Chapter("3", "Developer", "#005391")),
  ]),
]);

new Vue({
  router,
  render: (h) => h(App),
  data: {
    testTribeOne,
  },
}).$mount("#app");
