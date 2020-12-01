<template>
<NoticePageTemplate>
  <div class="flex flex-col">
    <h1 class="text-2xl text-center">Choppa's Home Page</h1>
    <div class="text-center">
      <StyledLink link="/tribes/00000000-0000-0000-0000-000000000001">View Tribe</StyledLink> |
      <StyledLink link="/tribes/00000000-0000-0000-0000-000000000001/edit">Edit Tribe</StyledLink> |
      <StyledLink link="/tribes/00000000-0000-0000-0000-dsgds">Broken Tribe Link</StyledLink> |
      <StyledLink link="/not-found">404</StyledLink>
    </div>
  </div>
</NoticePageTemplate>
</template>

<script>
import NoticePageTemplate from "@/components/templates/NoticePageTemplate";
import StyledLink from "@/components/atoms/links/StyledLink";

export default {
  name: "HomePage",
  components: {
    StyledLink,
    NoticePageTemplate,
  },
};
</script>
