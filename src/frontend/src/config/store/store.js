import Vue from "vue";
import Vuex from "vuex";
import {
  darkModeActions, darkModeGetters, darkModeMutations, darkModeState,
} from "@/config/store/stores/darkmode.store";
import { toastActions, toastGetters, toastMutations, toastState } from "@/config/store/stores/toast.store";
import { authActions, authGetters, authMutations, authState } from "@/config/store/stores/auth.store";

Vue.use(Vuex);

const state = {
  ...darkModeState,
  ...toastState,
  ...authState,
};

const getters = {
  ...darkModeGetters,
  ...toastGetters,
  ...authGetters,
};

const mutations = {
  ...darkModeMutations,
  ...toastMutations,
  ...authMutations,
};

const actions = {
  ...darkModeActions,
  ...toastActions,
  ...authActions,
};

export default new Vuex.Store({
  state,
  getters,
  mutations,
  actions,
});
