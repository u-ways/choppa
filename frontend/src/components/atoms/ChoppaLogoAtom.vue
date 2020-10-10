<template>
  <font-awesome icon="helicopter" :class="[ this.css ]" />
</template>

<script lang="ts">
  import { defineComponent } from "vue";

  export default defineComponent({
    name: "ChoppaLogoAtom",
    props: {
      css: String
    }
  });
</script>

<style scoped lang="scss">

</style>