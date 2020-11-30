<template>
  <NavbarElement>
    <router-link :to="url" :class="css" class="text-lg sm:text-sm font-semibold lowercase text-gray-800
    hover:text-gray-900 dark:text-gray-200 dark:hover:text-gray-100 border-b-2 border-choppa-light
    dark:hover:border-choppa-dark dark:focus:border-choppa-dark border-opacity-0 hover:border-opacity-100
    focus:border-opacity-100 outline-none">
      <slot></slot>
    </router-link>
  </NavbarElement>
</template>

<script>
import NavbarElement from "@/components/navbar/NavbarElement";

export default {
  components: {
    NavbarElement,
  },
  props: {
    url: {
      type: String,
      required: true,
    },
    css: {
      type: String,
      required: false,
    },
  },
};
</script>
