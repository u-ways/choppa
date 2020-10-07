<template>
  <font-awesome icon="helicopter" :class="[ this.css ]" />
</template>

<script lang="ts">
  import { Options, Vue } from "vue-class-component";

  @Options({
    props: {
      css: String
    }
  })
  export default class ChoppaLogoAtom extends Vue {}
</script>

<style scoped lang="scss">

</style>