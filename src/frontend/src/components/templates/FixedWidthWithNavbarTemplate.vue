<template>
  <div class="container-fluid w-100 m-0 p-0 mb-5 mh-100">
    <NavbarOrganism />
    <div class="container" :class="[ this.css ]">
      <slot></slot>
    </div>
  </div>
</template>

<script>
import NavbarOrganism from "@/components/organisms/NavbarOrganism";

export default {
  name: "FixedWidthWithNavbarTemplate",
  components: { NavbarOrganism },
  props: {
    css: String,
  },
};
</script>

<style scoped lang="scss">
</style>
