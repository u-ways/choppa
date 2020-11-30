import Vue from "vue";
import Vuex from "vuex";
import {
  darkModeActions,
  darkModeGetters,
  darkModeMutations,
  darkModeState,
} from "@/config/store/stores/darkmode.store";

Vue.use(Vuex);

const state = {
  ...darkModeState,
};

const getters = {
  ...darkModeGetters,
};

const mutations = {
  ...darkModeMutations,
};

const actions = {
  ...darkModeActions,
};

export default new Vuex.Store({
  state,
  getters,
  mutations,
  actions,
});
