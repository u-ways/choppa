<template>
  <div class="p-5">
    <MemberMolecule :member="testMember"/>
  </div>
</template>

<script lang="ts">
  import { Options, Vue } from "vue-class-component";
  import MemberMolecule from "../molecules/MemberMolecule.vue";
  import { Member } from "../../data/types/Member";

  @Options({
    components: {
      MemberMolecule
    },
    data() {
      return {
        testMember: new Member("Chopper OG Person", "Developer")
      };
    }
  })
  export default class MemberPage extends Vue {}
</script>

<style scoped lang="scss">

</style>