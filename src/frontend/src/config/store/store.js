import Vue from "vue";
import Vuex from "vuex";
import {
  darkModeActions, darkModeGetters, darkModeMutations, darkModeState,
} from "@/config/store/stores/darkmode.store";
import { toastActions, toastGetters, toastMutations, toastState } from "@/config/store/stores/toast.store";

Vue.use(Vuex);

const state = {
  ...darkModeState,
  ...toastState,
};

const getters = {
  ...darkModeGetters,
  ...toastGetters,
};

const mutations = {
  ...darkModeMutations,
  ...toastMutations,
};

const actions = {
  ...darkModeActions,
  ...toastActions,
};

export default new Vuex.Store({
  state,
  getters,
  mutations,
  actions,
});
