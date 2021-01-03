<template>
  <footer class="h-20 flex flex-row bg-gray-100 dark:bg-gray-700 ring-1 ring-black ring-opacity-20">
    <div class="pt-3 container mx-auto grid justify-items-center text-gray-600 dark:text-gray-300">
      <div class="justify-self-center">
        Support Choppa's development at
        <StyledLink link="/external/github" class="pl-1">
          <font-awesome-icon :icon="['fab', 'github']"/> Github
        </StyledLink>
      </div>
      <div class="justify-self-center flex flex-row flex-none h-6">
        <span class="pr-2">Theme</span>
        <IconButton icon="sun"
                    class="hover:bg-gray-200 dark:hover:bg-choppa-dark"
                    screen-reader-text="Activate Light Theme"
                    @click="updateTheme(lightTheme)"
        />
        <IconButton icon="moon"
                    class="hover:bg-gray-200 dark:hover:bg-choppa-dark"
                    screen-reader-text="Activate Dark Theme"
                    @click="updateTheme(darkTheme)"
        />
      </div>
    </div>
  </footer>
</template>

<script>
import IconButton from "@/components/atoms/buttons/IconButton";
import StyledLink from "@/components/atoms/links/StyledLink";
import { themeSetting } from "@/enums/themeSetting";
import { mapActions } from "vuex";

export default {
  name: "Footer",
  components: {
    StyledLink,
    IconButton,
  },
  data() {
    return {
      lightTheme: themeSetting.LIGHT_THEME,
      darkTheme: themeSetting.DARK_THEME,
    };
  },
  methods: {
    ...mapActions(["updateTheme"]),
  },
};
</script>
