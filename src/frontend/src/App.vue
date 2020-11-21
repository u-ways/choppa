<template>
  <div class="h-100">
    <router-view/>
  </div>
</template>

<style lang="scss">
@import "src/assets/scss/colors";
@import "src/assets/scss/typography";

body {
  background: $bg-off-white !important;
  font-family: $font-family-body !important;
  font-weight: $font-weight-body-medium !important;
}

#app {
  height: 100%;
}
</style>
