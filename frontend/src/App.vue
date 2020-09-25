<template>
  <img alt="Vue logo" src="./assets/logo.png" />
  <example msg="Chopper's Vue.js component!" />
</template>

<script lang="ts">
import { defineComponent } from "vue";
import example from "./components/example.vue";

export default defineComponent({
  name: "App",
  components: {
    example
  }
});
</script>

<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
