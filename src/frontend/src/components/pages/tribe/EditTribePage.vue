<template>
  <FixedWidthWithNavbarTemplate css="mt-3 px-0">
    <div class="edit-tribe-page">
      <div class="edit-tribe-page__header">
        Edit Tribe Page
      </div>
    </div>
  </FixedWidthWithNavbarTemplate>
</template>

<script>
import FixedWidthWithNavbarTemplate from "@/components/templates/FixedWidthWithNavbarTemplate";

export default {
  name: "EditTribePage",
  components: {
    FixedWidthWithNavbarTemplate,
  },
};
</script>

<style scoped lang="scss">
@import "src/assets/scss/colours";
@import "src/assets/scss/typography";

.edit-tribe-page {
  padding: 1.5rem;

  &__header {
    color: $text-dark;
    font-size: 1.2rem;
    font-family: $font-family-emphasis;
    font-weight: $font-weight-emphasis-bold;
    padding-bottom: 0.25rem;
    border-bottom: 1px solid $white-border;
  }
}
</style>
