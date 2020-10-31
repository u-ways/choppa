<template>
  <FullWidthWithNavbarTemplate>
    <div class="pt-3">
      <TribeOrganism :tribe="this.$root.$data.testTribeOne" />
    </div>
  </FullWidthWithNavbarTemplate>
</template>

<script>
import FullWidthWithNavbarTemplate from "@/components/templates/FullWidthWithNavbarTemplate";
import TribeOrganism from "@/components/organisms/TribeOrganism";

export default {
  name: "ViewTribePage",
  components: {
    TribeOrganism,
    FullWidthWithNavbarTemplate,
  },
};
</script>

<style scoped lang="scss">
</style>
