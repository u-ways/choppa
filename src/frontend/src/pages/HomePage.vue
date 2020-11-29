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
      <button class="btn-link" @click="onToggleThemeClicked">Toggle Dark Theme</button>
    </div>
  </div>
</NoticePageTemplate>
</template>

<script>
import NoticePageTemplate from "@/components/templates/NoticePageTemplate";
import { toggleTheme } from "@/utils/darkMode";

export default {
  name: "HomePage",
  components: {
    NoticePageTemplate,
  },
  methods: {
    onToggleThemeClicked() {
      toggleTheme();
    },
  },
};
</script>
