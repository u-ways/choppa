<template>
  <a :href='loginLink' class="bg-white dark:bg-gray-600 shadow rounded-sm ring-1 ring-black ring-opacity-5 px-1 py-2
  text-xs flex flex-row hover:bg-gray-200 dark:hover:bg-gray-500 text-gray-600 dark:text-gray-300 font-bold
  leading-6 justify-center">
    <img :src="require(`@/assets/img/sso/${imageName}.png`)" :alt="name" class="w-6 h-6 mr-1" />
    <p class="flex-grow">{{ name }}</p>
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
