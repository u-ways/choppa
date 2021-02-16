<template>
  <footer class="h-20 flex flex-row bg-gray-100 dark:bg-gray-700 ring-1 ring-black ring-opacity-20">
    <div class="pt-3 container mx-auto grid justify-items-center text-gray-600 dark:text-gray-300">
      <div class="justify-self-center">
        Support Choppa's development at
        <a href="https://github.com/U-ways/choppa" class="font-bold hover:underline focus:underline
        active:outline-none focus:outline-none text-choppa-light hover:text-choppa-light-hover dark:text-choppa-dark
        dark:hover:text-choppa-dark-hover">
          Github
        </a>
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
import { themeSetting } from "@/enums/themeSetting";
import { mapActions } from "vuex";

export default {
  name: "Footer",
  components: {
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
