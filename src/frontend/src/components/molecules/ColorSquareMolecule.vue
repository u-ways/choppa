<template>
  <div class="h-100" ref="target">
    <b-button class="color-square" :style="reactiveStyle" />
    <b-popover v-if="this.$refs.target"
               :target="this.$refs.target"
               custom-class="color-picker-popover"
               triggers="focus">
      <ColorPicker v-model="color"/>
    </b-popover>
  </div>
</template>

<script>
import { Chrome } from "vue-color";

const STARTING_COLOR = "#3068c2";

export default {
  name: "ColorSquareMolecule",
  components: {
    ColorPicker: Chrome,
  },
  props: {
    startingColor: String,
    returnEvent: {},
  },
  data() {
    return {
      color: { hex: "" },
    };
  },
  mounted() {
    if (this.startingColor === undefined) {
      this.color.hex = STARTING_COLOR;
    } else {
      this.color.hex = this.startingColor;
    }
  },
  computed: {
    reactiveStyle() {
      return {
        background: this.color.hex,
      };
    },
  },
  watch: {
    color(newColor) {
      this.$emit("colorChanged", {
        color: newColor,
        ...this.returnEvent,
      });
    },
  },
};
</script>

<style lang="scss">
@import "src/assets/scss/colors";

.color-square {
  width: 100%;
  height: 100%;
  border: 1px solid $white-border;
}

.popover-body {
  padding: 0;
}

</style>
