<template>
  <FullWidthWithNavbarTemplate>
    <div class="pt-5 d-flex justify-content-center">
      <div class="w-100 mx-3">
        <div class="row">
          <div class="col-lg mr-lg-2 mb-2 mb-lg-0">
            <SquadOrganism :squad="this.testSquadOne"/>
          </div>
          <div class="col-lg mb-2 mb-lg-0">
            <SquadOrganism :squad="this.testSquadTwo"/>
          </div>
        </div>
      </div>
    </div>
  </FullWidthWithNavbarTemplate>
</template>>

<script>
import FullWidthWithNavbarTemplate from "@/components/templates/FullWidthWithNavbarTemplate";
import SquadOrganism from "@/components/organisms/SquadOrganism";
import Squad from "@/data/types/Squad";
import Member from "@/data/types/Member";

export default {
  name: "ViewTribePage",
  components: { SquadOrganism, FullWidthWithNavbarTemplate },
  data() {
    return {
      testSquadOne: new Squad(2, "Metropolitan", "#751056", [
        new Member(1, "Geralt Of Rivia"),
        new Member(2, "Ciri"),
        new Member(3, "Eskel"),
        new Member(4, "Yennefer of Vengerberg"),
        new Member(5, "Tissaia de Vries"),
        new Member(6, "Dandelion"),
      ]),
      testSquadTwo: new Squad(50, "District", "#005920", [
        new Member(20, "David Tennant"),
        new Member(21, "Matt Smith"),
        new Member(23, "Peter Capaldi"),
        new Member(24, "Jodie Whittaker"),
      ]),
    };
  },
};
</script>

<style scoped lang="scss">
</style>
