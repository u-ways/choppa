<template>
  <div :class="[sharedCss, getVariantCss, css]">
    <p class="text-2xl font-bold text-gray-800 dark:text-gray-200">
      <slot name="heading"/>
    </p>
    <p class="pl-1 font-normal text-md text-gray-700 dark:text-gray-300">
      <slot name="subheading"/>
    </p>
  </div>
</template>
<script>
const SHARED_CSS = `border-l-4 pl-2`;
const PRIMARY = "primary";
const PRIMARY_CSS = "border-choppa-two";
const DANGER = "danger";
const DANGER_CSS = "border-red-600";
const CUSTOM = "custom";

export default {
  props: {
    variant: {
      required: true,
      validator: (value) => [PRIMARY, DANGER, CUSTOM].indexOf(value) !== -1,
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
        case DANGER:
          return DANGER_CSS;
        case CUSTOM:
        default:
          return "";
      }
    },
  },
};
</script>
