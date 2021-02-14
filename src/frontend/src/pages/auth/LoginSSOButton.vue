<template>
  <a :href='loginLink' class="bg-white dark:bg-gray-700 shadow rounded-sm ring-1 ring-black ring-opacity-5 px-1 py-2
  font-bold font-sm flex flex-row gap-4">
    <img :src="require(`@/assets/img/sso/${imageName}.png`)" :alt="name" height="100%" />
    <p class="flex-grow leading-8">{{ name }}</p>
  </a>
</template>

<script>
export default {
  name: "LoginSSOButton",
  props: {
    name: String,
    imageName: String,
    loginEndpoint: String,
  },
  computed: {
    loginLink() {
      const authPrefix = process.env.VUE_APP_AUTH_PREFIX || "";
      return `${authPrefix}oauth2/authorization/${this.loginEndpoint}`;
    },
  },
};
</script>

<style scoped>

</style>
