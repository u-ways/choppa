<template>
  <div>
    <router-link v-if="type === 'link'" :to="link" :class="[sharedCss, getVariantCss, css]" :replace="linkReplace"
                 :append="linkAppend">
      <slot></slot>
    </router-link>
    <button v-if="type === 'button'" @click="() => this.$emit('click')" :class="[sharedCss, getVariantCss, css]">
      <slot></slot>
    </button>
  </div>
</template>

<script>
const SHARED_CSS = `rounded-md p-2 text-sm font-semibold text-white hover:ring focus:ring
focus:outline-none ring-opacity-30 block transform-gpu transition-transform transition-colors hover:-translate-y-0.5
focus:-translate-y-0.5 duration-100 motion-reduce:transition-none border-1`;
const PRIMARY = "primary";
const PRIMARY_CSS = "bg-choppa-two ring-purple-600 border-transparent";
const SECONDARY_LIGHT = "secondary-light";
const SECONDARY_LIGHT_CSS = `bg-gray-100 text-gray-600 border-grey-200`;
const SECONDARY_DARK = "secondary-dark";
const SECONDARY_DARK_CSS = `dark:bg-gray-700 dark:text-gray-200 dark:border-transparent`;
const SECONDARY = "secondary";
const SECONDARY_CSS = `${SECONDARY_LIGHT_CSS} ${SECONDARY_DARK_CSS}`;
const CUSTOM = "custom";

export default {
  name: "StyledButton",
  props: {
    type: {
      required: true,
      validator: (value) => ["button", "link"].indexOf(value) !== -1,
    },
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
    variant: {
      required: true,
      validator: (value) => [PRIMARY, SECONDARY, SECONDARY_LIGHT, SECONDARY_DARK, CUSTOM].indexOf(value) !== -1,
    },
    css: {
      type: String,
      required: false,
    },
  },
  data() {
    return {
      sharedCss: SHARED_CSS,
    };
  },
  computed: {
    getVariantCss() {
      switch (this.variant) {
        case PRIMARY:
          return PRIMARY_CSS;
        case SECONDARY:
          return SECONDARY_CSS;
        case SECONDARY_LIGHT:
          return SECONDARY_LIGHT_CSS;
        case SECONDARY_DARK:
          return SECONDARY_DARK_CSS;
        case CUSTOM:
        default:
          return "";
      }
    },
  },
};
</script>
