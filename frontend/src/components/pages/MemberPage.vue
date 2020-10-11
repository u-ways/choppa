<template>
<div class="p-5">
  <MemberMolecule :member="testMember" />
</div>
</template>

<script lang="ts">
import MemberMolecule from "../molecules/MemberMolecule.vue";
import { Member } from "../../data/types/Member";
import { Options, Vue } from "vue-class-component";

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
