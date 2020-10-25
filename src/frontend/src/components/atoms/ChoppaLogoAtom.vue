<template>
  <div>
    <font-awesome-icon icon="helicopter" :class="[ this.css ]" />
  </div>
</template>

<script>
export default {
  name: "ChoppaLogoAtom",
  props: {
    css: String,
  },
};
</script>

<style scoped>
</style>
