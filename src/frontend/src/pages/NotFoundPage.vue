<template>
  <NoticePageTemplate>
    <div class="flex flex-col">
      <h1 class="text-2xl text-center">404 - You've taken the wrong flight!</h1>
      <span class="text-center"><StyledLink link="/">Edit Tribe</StyledLink></span>
    </div>
  </NoticePageTemplate>
</template>

<script>
import NoticePageTemplate from "@/components/templates/NoticePageTemplate";
import StyledLink from "@/components/atoms/StyledLink";

export default {
  name: "NotFoundPage",
  components: {
    NoticePageTemplate,
    StyledLink,
  },
};
</script>
