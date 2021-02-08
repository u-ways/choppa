<template>
  <div class="rounded-md p-2 text-sm font-semibold text-white hover:ring focus:ring
focus:outline-none block transform-gpu transition-transform transition-colors hover:-translate-y-0.5
focus:-translate-y-0.5 duration-100 motion-reduce:transition-none border-1 ring-white
bg-choppa-two">
    <router-link :to="link" :replace="linkReplace"
                 :append="linkAppend">
      <slot></slot>
    </router-link>
  </div>
</template>

<script>

export default {
  name: "StyledButton",
  props: {
    link: {
      type: String,
    },
    linkReplace: {
      default: false,
      type: Boolean,
    },
    linkAppend: {
      default: false,
      type: Boolean,
    },
  },
  data() {
    return {};
  },
  computed: {},
};
</script>
