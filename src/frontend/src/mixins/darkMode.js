import { themeSetting } from "@/enums/themeSetting";
import { mapGetters } from "vuex";
import { UPDATE_CURRENT_THEME } from "@/config/store/mutation-types";

export const darkMode = {
  beforeMount() {
    this.calculateCurrentTheme();
    this.applyOrRemoveListenerAsNecessary();
  },
  computed: {
    ...mapGetters(["preferredTheme", "currentTheme"]),
  },
  methods: {
    calculateCurrentTheme() {
      const isInDarkMode = this.preferredTheme === themeSetting.DARK_THEME
        || (this.preferredTheme === themeSetting.FOLLOW_OS && this.isOSInDarkMode());

      this.$store.commit(UPDATE_CURRENT_THEME, isInDarkMode ? themeSetting.DARK_THEME : themeSetting.LIGHT_THEME);
    },
    getOSColorSchemeMedia() {
      return window.matchMedia("(prefers-color-scheme: dark)");
    },
    isOSInDarkMode() {
      return this.getOSColorSchemeMedia().matches;
    },
    applyTheme() {
      if (this.currentTheme === themeSetting.DARK_THEME) {
        document.querySelector("html").classList.add("dark");
      } else {
        document.querySelector("html").classList.remove("dark");
      }
    },
    applyOrRemoveListenerAsNecessary() {
      if (this.preferredTheme === themeSetting.FOLLOW_OS) {
        this.getOSColorSchemeMedia().addEventListener("change", () => this.calculateCurrentTheme(), { passive: true });
      } else {
        this.getOSColorSchemeMedia().removeEventListener("change", () => this.calculateCurrentTheme());
      }
    },
  },
  watch: {
    preferredTheme() {
      this.applyOrRemoveListenerAsNecessary();
      this.calculateCurrentTheme();
    },
    currentTheme() {
      this.applyTheme();
    },
  },
};
