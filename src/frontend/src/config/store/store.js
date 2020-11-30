/* eslint-disable no-shadow, no-param-reassign */
import Vue from "vue";
import Vuex from "vuex";
import { isValidThemeSetting, themeSetting } from "@/enums/themeSetting";
import { UPDATE_CURRENT_THEME, UPDATE_PREFERRED_THEME } from "@/config/store/mutation-types";

Vue.use(Vuex);

const state = {
  currentTheme: undefined,
  preferredTheme: localStorage.preferredTheme,
};

const getters = {
  currentTheme: (state) => (state.currentTheme === undefined ? themeSetting.DARK_THEME : state.currentTheme),
  preferredTheme: (state) => (state.preferredTheme === undefined ? themeSetting.FOLLOW_OS : state.preferredTheme),
};

const mutations = {
  [UPDATE_CURRENT_THEME](state, newTheme) {
    if (isValidThemeSetting(newTheme) && newTheme !== themeSetting.FOLLOW_OS) {
      state.currentTheme = newTheme;
    } else {
      throw new Error(`Unsupported value ${newTheme}`);
    }
  },
  [UPDATE_PREFERRED_THEME](state, newTheme) {
    if (isValidThemeSetting(newTheme)) {
      state.preferredTheme = newTheme;
      localStorage.setItem("preferredTheme", newTheme);
    } else {
      throw new Error(`Unsupported value ${newTheme}`);
    }
  },
};

const actions = {
  updateTheme(context, newTheme) {
    context.commit(UPDATE_CURRENT_THEME, newTheme);
    context.commit(UPDATE_PREFERRED_THEME, newTheme);
  },
};

export default new Vuex.Store({
  state,
  getters,
  mutations,
  actions,
});
