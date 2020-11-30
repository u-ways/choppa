<template>
<NoticePageTemplate>
  <div class="flex flex-col">
    <h1 class="text-2xl text-center">Choppa's Home Page</h1>
    <div class="text-center">
      <StyledLink link="/tribes/1">View Tribe</StyledLink> |
      <StyledLink link="/tribes/1/edit">Edit Tribe</StyledLink> |
      <StyledLink link="/not-found">404</StyledLink> |
    </div>
    <div class="text-center">
      <LinkButton @click="onToggleThemeClicked">Swap To {{toggleThemeButtonText}} Theme</LinkButton>
      <template v-if="preferredTheme !== followOSTheme">
        | <LinkButton @click="onRemoveThemePreferenceCLicked">Remove Theme Preference</LinkButton>
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
import StyledLink from "@/components/atoms/StyledLink";
import LinkButton from "@/components/atoms/LinkButton";

export default {
  name: "HomePage",
  components: {
    LinkButton,
    StyledLink,
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
