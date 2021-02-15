<template>
  <NavbarElement>
    <router-link :to="url"
                 class="text-sm font-semibold pb-2 border-b-4 border-choppa-light-extra dark:hover:border-choppa-dark
                 dark:focus:border-choppa-dark border-opacity-0 hover:border-opacity-100 focus:border-opacity-100
                 outline-none"
                 :class="[ css,
                 isOnHomePage ?
                 'text-gray-200 dark:hover:text-gray-100' :
                 'text-gray-800 hover:text-gray-900 dark:text-gray-200 dark:hover:text-gray-100'
                 ]">
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
      type: Object,
      required: true,
    },
    css: {
      type: String,
      required: false,
    },
    isOnHomePage: {
      type: Boolean,
      required: false,
      default: false,
    },
  },
};
</script>
