<template>
<div class="d-flex justify-content-center align-items-center p-2 h-100">
  <div class="d-flex flex-column align-items-center">
    <ChoppaLogoAtom css="h1" />
    <h1 class="h4">Choppa's Home Page</h1>
    <div>
      <router-link to="/member">Member</router-link> |
      <router-link to="/not-found-404-test">404</router-link>
    </div>
  </div>
</div>
</template>

<script lang="ts">
import ChoppaLogoAtom from "../atoms/ChoppaLogoAtom.vue";
import { Options, Vue } from "vue-class-component";

@Options({
  components: {
    ChoppaLogoAtom
  }
})
export default class HomePage extends Vue {}
</script>

<style scoped lang="scss">
</style>