<template>
  <NoticePageTemplate>
    <div class="flex flex-col">
      <h1 class="text-2xl text-center">404 - You've taken the wrong flight!</h1>
      <router-link to="/" class="text-center">Fly home</router-link>
    </div>
  </NoticePageTemplate>
</template>

<script>
import NoticePageTemplate from "@/components/templates/NoticePageTemplate";

export default {
  name: "NotFoundPage",
  components: {
    NoticePageTemplate,
  },
};
</script>
