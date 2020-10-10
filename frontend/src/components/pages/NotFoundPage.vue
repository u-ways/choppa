<template>
  <div class="d-flex justify-content-center align-items-center p-2 h-100">
    <div class="d-flex flex-column">
      <div class="d-flex flex-row justify-content-center font-weight-bold mt-2">
        <ChoppaLogoAtom css="h1"/>
      </div>
      <div class="d-flex flex-row">
        <h1 class="h5 justify-content-center mt-2 mb-0">
          404 - You've taken the wrong flight!
        </h1>
      </div>
      <div class="d-flex flex-row justify-content-center">
        <router-link to="/" class="d-flex mt-1">
          Fly home
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  import { Options, Vue } from "vue-class-component";
  import ChoppaLogoAtom from "../atoms/ChoppaLogoAtom.vue";

  @Options({
    components: {
      ChoppaLogoAtom
    }
  })
  export default class NotFoundPage extends Vue {}
</script>

<style scoped lang="scss">
  .icon {
    font-size: 4rem;
    text-align: center;
    width: 100%;
  }
</style>