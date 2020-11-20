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
import getTribe from "@/config/api/tribe.api";

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
  async mounted() {
    this.tribe = await getTribe({
      id: "00000000-0000-0000-0000-000000000001",
      loadSquads: true,
    });
  },
};
</script>

<style scoped lang="scss">
</style>
