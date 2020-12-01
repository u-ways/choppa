<template>
  <router-link :to="link" class="hover:underline focus:underline active:outline-none focus:outline-none
  text-choppa-light hover:text-choppa-light-hover dark:text-choppa-dark dark:hover:text-choppa-dark-hover">
    <slot></slot>
  </router-link>
</template>

<script>
export default {
  name: "StyledLink",
  props: {
    link: {
      type: String,
      required: true,
    },
  },
};
</script>
