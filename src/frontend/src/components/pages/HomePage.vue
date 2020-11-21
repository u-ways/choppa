<template>
  <div class="d-flex justify-content-center align-items-center p-2 h-100">
    <div class="d-flex flex-column align-items-center">
      <ChoppaLogoAtom css="h1" />
      <h1 class="h4">Choppa's Home Page</h1>
      <div>
        <router-link to="/tribe/00000000-0000-0000-0000-000000000001/">View Tribe Page</router-link> |
        <router-link to="/tribe/00000000-0000-0000-0000-000000000001/edit">Edit Tribe Page</router-link> |
        <router-link to="/not-found-404-test">404</router-link>
      </div>
    </div>
  </div>
</template>

<script>
import ChoppaLogoAtom from "@/components/atoms/ChoppaLogoAtom";

export default {
  name: "HomePage",
  components: { ChoppaLogoAtom },
};
</script>

<style scoped>

</style>
