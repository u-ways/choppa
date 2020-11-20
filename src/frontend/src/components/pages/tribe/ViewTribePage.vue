<template>
  <FullWidthWithNavbarTemplate>
    <div class="pt-3">
      <TribeOrganism :tribe="tribe" />
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
  data() {
    return {
      tribe: this.$root.$data.testTribeOne,
    };
  },
};
</script>

<style scoped lang="scss">
</style>
