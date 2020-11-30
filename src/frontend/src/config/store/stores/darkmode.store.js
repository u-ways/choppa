/* eslint-disable no-shadow, no-param-reassign */
import { isValidThemeSetting, themeSetting } from "@/enums/themeSetting";
import { UPDATE_CURRENT_THEME, UPDATE_PREFERRED_THEME } from "@/config/store/mutation-types";

export const darkModeState = {
  currentTheme: undefined,
  preferredTheme: localStorage.preferredTheme,
};

export const darkModeGetters = {
  currentTheme: (state) => (state.currentTheme === undefined ? themeSetting.DARK_THEME : state.currentTheme),
  preferredTheme: (state) => (state.preferredTheme === undefined ? themeSetting.FOLLOW_OS : state.preferredTheme),
};

export const darkModeMutations = {
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

export const darkModeActions = {
  updateTheme(context, newTheme) {
    context.commit(UPDATE_PREFERRED_THEME, newTheme);
  },
};
