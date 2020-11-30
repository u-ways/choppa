<template>
<NoticePageTemplate>
  <div class="flex flex-col">
    <h1 class="text-2xl text-center">Choppa's Home Page</h1>
    <div class="text-center">
      <router-link to="/tribes/1">View Tribe</router-link> |
      <router-link to="/tribes/1/edit">Edit Tribe</router-link> |
      <router-link to="/not-found">404</router-link>
    </div>
    <div class="text-center">
      <button class="btn-link" @click="onToggleThemeClicked">Swap To {{toggleThemeButtonText}} Theme</button>
      <template v-if="preferredTheme !== followOSTheme">
        |
        <button class="btn-link" @click="onRemoveThemePreferenceCLicked">Remove Theme Preference</button>
      </template>
    </div>
  </div>
</NoticePageTemplate>
</template>

<script>
import NoticePageTemplate from "@/components/templates/NoticePageTemplate";
import { mapActions, mapGetters } from "vuex";
import { themeSetting } from "@/enums/themeSetting";
import { UPDATE_PREFERRED_THEME } from "@/config/store/mutation-types";

export default {
  name: "HomePage",
  components: {
    NoticePageTemplate,
  },
  computed: {
    toggleThemeButtonText() {
      return this.currentTheme === themeSetting.DARK_THEME ? "Light" : "Dark";
    },
    ...mapGetters(["preferredTheme", "currentTheme"]),
  },
  data() {
    return {
      followOSTheme: themeSetting.FOLLOW_OS,
    };
  },
  methods: {
    onToggleThemeClicked() {
      this.updateTheme(
        this.currentTheme === themeSetting.DARK_THEME ? themeSetting.LIGHT_THEME : themeSetting.DARK_THEME,
      );
    },
    onRemoveThemePreferenceCLicked() {
      this.$store.commit(UPDATE_PREFERRED_THEME, themeSetting.FOLLOW_OS);
    },
    ...mapActions(["updateTheme"]),
  },
};
</script>
