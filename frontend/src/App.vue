<template>
  <router-view />
</template>

<script lang="ts">
  import { Options, Vue } from "vue-class-component";
  import MemberMolecule from "@/components/molecules/MemberMolecule.vue";

  @Options({
    components: {
      MemberMolecule
    }
  })
  export default class App extends Vue {}
</script>

<style lang="scss">
</style>
