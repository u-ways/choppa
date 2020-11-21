<template>
  <FullWidthWithNavbarTemplate>
    <template v-if="isLoaded">
      <div class="pt-3">
        <TribeOrganism :tribe="tribe" />
      </div>
    </template>
    <template v-else>
      <div class="row pt-3 mx-0">
        <div class="col-12 text-center">
          <div class="spinner-border text-primary" role="status">
            <span class="sr-only">Loading...</span>
          </div>
        </div>
      </div>
    </template>
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
      isLoaded: false,
    };
  },
  async mounted() {
    this.tribe = await getTribe({
      id: this.$route.params.id,
      loadSquads: true,
    });

    this.isLoaded = true;
  },
};
</script>

<style scoped lang="scss">
</style>
