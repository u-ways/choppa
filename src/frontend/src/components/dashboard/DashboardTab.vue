<template>
  <div v-show="isEnabled" class="h-full">
    <template v-if="noData">
      <div class="flex flex-col h-full">
        <div class="h-full py-2 flex flex-col flex-grow items-center justify-items-center justify-center">
          <span class="font-semibold text-1xl md:text-2xl">No data</span>
        </div>
      </div>
    </template>
    <template v-else>
      <slot>
      </slot>
    </template>
  </div>
</template>

<script>
export default {
  name: "DashboardTab",
  props: {
    tabName: String,
    borderColor: {
      type: String,
      required: false,
    },
    noData: {
      type: Boolean,
      required: false,
      default: false,
    },
  },
  data() {
    return {
      isEnabled: false,
    };
  },
  mounted() {
    this.$parent.$emit("registeredTab", this);
  },
  methods: {
    getBorderColor() {
      return this.borderColor === undefined ? "#6762D9" : this.borderColor;
    },
    getIsEnabled() {
      return this.isEnabled;
    },
    setEnabled(enabled) {
      this.isEnabled = enabled;

      if (enabled === true) {
        this.$emit("onEnabled");
      }
    },
  },
};
</script>
