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

const chapterTeamLead = new Chapter(1, "Team Lead", "#AC190F");
const chapterTester = new Chapter(2, "Tester", "#62A756");
const chapterDeveloper = new Chapter(3, "Developer", "#005391");
const chapterIntern = new Chapter(4, "Intern", "#674DA4");
const chapterBA = new Chapter(5, "Business Analyst", "#F5C147");
const chapterSecretary = new Chapter(6, "Secretary", "#C7288E");

const testTribeOne = new Tribe(1, "Game of Thrones", [
  new Squad(1, "Lannister", "#9F1830", [
    new Member(1, "Tywin Lannister", chapterTeamLead),
    new Member(2, "Jamie Lannister", chapterTester),
    new Member(3, "Cercei Lannister", chapterDeveloper),
    new Member(4, "Tyrion Lannister", chapterIntern),
  ]),

  new Squad(2, "Stark", "#FEF5CC", [
    new Member(5, "Eddard Stark", chapterTeamLead),
    new Member(6, "Catelyn Stark", chapterBA),
    new Member(7, "Rob Stark", chapterDeveloper),
    new Member(8, "Arya Stark", chapterSecretary),
    new Member(9, "Sansa Stark", chapterSecretary),
    new Member(10, "Bran Stark", chapterIntern),
    new Member(11, "Rickon Stark", chapterIntern),
  ]),

  new Squad(3, "Targaryen", "#101006", [
    new Member(12, "Danerys Targaryen", chapterDeveloper),
    new Member(13, "Viserys Targaryen", chapterDeveloper),
    new Member(14, "Aegon Targaryen (Jon Snow)", chapterDeveloper),
    new Member(100, "Viserion the Dragon", chapterDeveloper),
    new Member(101, "Drogon the Dragon", chapterDeveloper),
    new Member(102, "Rhaegar the Dragon", chapterDeveloper),
  ]),

  new Squad(4, "Baratheon", "#FCB600", [
    new Member(15, "Robert Baratheon", chapterDeveloper),
    new Member(16, "Joffery Baratheon", chapterDeveloper),
    new Member(17, "Stannis Baratheon", chapterDeveloper),
    new Member(18, "Renly Baratheon", chapterDeveloper),
    new Member(19, "Thomon Baratheon", chapterDeveloper),
  ]),

  new Squad(5, "Tully", "#002B4B", [
    new Member(20, "Brynden Tully", chapterDeveloper),
  ]),

  new Squad(6, "Greyjoy", "#FDD33A", [
    new Member(21, "Theon Greyjoy", chapterDeveloper),
    new Member(22, "Yara Greyjoy", chapterDeveloper),
    new Member(23, "Balon Greyjoy", chapterDeveloper),
  ]),

  new Squad(7, "Arryn", "#2B5DB2", [
    new Member(24, "Robyn Arryn", chapterDeveloper),
    new Member(25, "Lysa Arryn", chapterDeveloper),
    new Member(26, "Jon Arryn", chapterDeveloper),
  ]),

  new Squad(8, "Tyrell", "#4C860D", [
    new Member(27, "Olenna Tyrell", chapterDeveloper),
    new Member(28, "Mace Tyrell", chapterDeveloper),
    new Member(29, "Loras Tyrell", chapterDeveloper),
    new Member(30, "Margaery Tyrell", chapterDeveloper),
  ]),

  new Squad(9, "Martell", "#DD8A08", [
    new Member(31, "Oberyn Martell", chapterDeveloper),
    new Member(32, "Ellaria Martell", chapterDeveloper),
    new Member(33, "Obara Martell", chapterDeveloper),
    new Member(34, "Nymeria Martell", chapterDeveloper),
    new Member(35, "Tyene Martell", chapterDeveloper),
  ]),
]);

new Vue({
  router,
  render: (h) => h(App),
  data: {
    testTribeOne,
  },
}).$mount("#app");
