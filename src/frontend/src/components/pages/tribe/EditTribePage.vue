<template>
  <FixedWidthWithNavbarTemplate css="mt-3 px-0">
    <div class="contents">
      Edit Tribe Page
    </div>
  </FixedWidthWithNavbarTemplate>
</template>

<script>
import FixedWidthWithNavbarTemplate from "@/components/templates/FixedWidthWithNavbarTemplate";

export default {
  name: "EditTribePage",
  components: { FixedWidthWithNavbarTemplate },
};
</script>

<style scoped lang="scss">
@import "src/assets/scss/colours";
@import "src/assets/scss/typography";

.contents {
  background: $white;
  border: 1px solid $white-border;
  padding: 1.5rem;
  font-size: 1.2rem;
  font-weight: $font-weight-body-light;
}
</style>
