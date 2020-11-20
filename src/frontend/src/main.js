import { BootstrapVue } from "bootstrap-vue";
import { library } from "@fortawesome/fontawesome-svg-core";
import { faUserAlt, faHelicopter, faCog, faTrash, faUpload, faPlus } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { v4 as uuidv4 } from "uuid";
import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-vue/dist/bootstrap-vue.css";
import Vue from "vue";
import Tribe from "@/data/types/Tribe";
import Squad from "@/data/types/Squad";
import Member from "@/data/types/Member";
import Chapter from "@/data/types/Chapter";
import App from "./App.vue";
import router from "./config/router";

library.add(faUserAlt, faHelicopter, faCog, faTrash, faUpload, faPlus);
Vue.component("font-awesome-icon", FontAwesomeIcon);

Vue.use(BootstrapVue);

Vue.config.productionTip = false;

const chapterTeamLead = new Chapter(uuidv4(), "Team Lead", "#AC190F");
const chapterTester = new Chapter(uuidv4(), "Tester", "#62A756");
const chapterDeveloper = new Chapter(uuidv4(), "Developer", "#005391");
const chapterIntern = new Chapter(uuidv4(), "Intern", "#674DA4");
const chapterBA = new Chapter(uuidv4(), "Business Analyst", "#F5C147");
const chapterSecretary = new Chapter(uuidv4(), "Secretary", "#C7288E");

const testTribeOne = new Tribe(uuidv4(), "Game of Thrones", [
  new Squad(uuidv4(), "Lannister", "#9F1830", [
    new Member(uuidv4(), "Tywin Lannister", chapterTeamLead),
    new Member(uuidv4(), "Jamie Lannister", chapterTester),
    new Member(uuidv4(), "Cercei Lannister", chapterDeveloper),
    new Member(uuidv4(), "Tyrion Lannister", chapterIntern),
  ]),

  new Squad(uuidv4(), "Stark", "#FEF5CC", [
    new Member(uuidv4(), "Eddard Stark", chapterTeamLead),
    new Member(uuidv4(), "Catelyn Stark", chapterBA),
    new Member(uuidv4(), "Rob Stark", chapterDeveloper),
    new Member(uuidv4(), "Arya Stark", chapterSecretary),
    new Member(uuidv4(), "Sansa Stark", chapterSecretary),
    new Member(uuidv4(), "Bran Stark", chapterIntern),
    new Member(uuidv4(), "Rickon Stark", chapterIntern),
  ]),

  new Squad(uuidv4(), "Targaryen", "#101006", [
    new Member(uuidv4(), "Danerys Targaryen", chapterDeveloper),
    new Member(uuidv4(), "Viserys Targaryen", chapterDeveloper),
    new Member(uuidv4(), "Aegon Targaryen (Jon Snow)", chapterDeveloper),
    new Member(uuidv4(), "Viserion the Dragon", chapterDeveloper),
    new Member(uuidv4(), "Drogon the Dragon", chapterDeveloper),
    new Member(uuidv4(), "Rhaegar the Dragon", chapterDeveloper),
  ]),

  new Squad(uuidv4(), "Baratheon", "#FCB600", [
    new Member(uuidv4(), "Robert Baratheon", chapterDeveloper),
    new Member(uuidv4(), "Joffery Baratheon", chapterDeveloper),
    new Member(uuidv4(), "Stannis Baratheon", chapterDeveloper),
    new Member(uuidv4(), "Renly Baratheon", chapterDeveloper),
    new Member(uuidv4(), "Thomon Baratheon", chapterDeveloper),
  ]),

  new Squad(uuidv4(), "Tully", "#002B4B", [
    new Member(uuidv4(), "Brynden Tully", chapterDeveloper),
  ]),

  new Squad(uuidv4(), "Greyjoy", "#FDD33A", [
    new Member(uuidv4(), "Theon Greyjoy", chapterDeveloper),
    new Member(uuidv4(), "Yara Greyjoy", chapterDeveloper),
    new Member(uuidv4(), "Balon Greyjoy", chapterDeveloper),
  ]),

  new Squad(uuidv4(), "Arryn", "#2B5DB2", [
    new Member(uuidv4(), "Robyn Arryn", chapterDeveloper),
    new Member(uuidv4(), "Lysa Arryn", chapterDeveloper),
    new Member(uuidv4(), "Jon Arryn", chapterDeveloper),
  ]),

  new Squad(uuidv4(), "Tyrell", "#4C860D", [
    new Member(uuidv4(), "Olenna Tyrell", chapterDeveloper),
    new Member(uuidv4(), "Mace Tyrell", chapterDeveloper),
    new Member(uuidv4(), "Loras Tyrell", chapterDeveloper),
    new Member(uuidv4(), "Margaery Tyrell", chapterDeveloper),
  ]),

  new Squad(uuidv4(), "Martell", "#DD8A08", [
    new Member(uuidv4(), "Oberyn Martell", chapterDeveloper),
    new Member(uuidv4(), "Ellaria Martell", chapterDeveloper),
    new Member(uuidv4(), "Obara Martell", chapterDeveloper),
    new Member(uuidv4(), "Nymeria Martell", chapterDeveloper),
    new Member(uuidv4(), "Tyene Martell", chapterDeveloper),
  ]),
]);

new Vue({
  router,
  render: (h) => h(App),
  data: {
    testTribeOne,
  },
}).$mount("#app");
