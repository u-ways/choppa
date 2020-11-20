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
import axiosInstance from "@/config/axios";

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
    const result = await axiosInstance.get("/tribes/00000000-0000-0000-0000-000000000001");
    console.log(result.data);
  },
};
</script>

<style scoped lang="scss">
</style>
