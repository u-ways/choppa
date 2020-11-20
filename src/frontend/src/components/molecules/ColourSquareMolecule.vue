<template>
  <div class="h-100" ref="target">
    <b-button class="colour-square" :style="reactiveStyle" />
    <b-popover v-if="this.$refs.target"
               :target="this.$refs.target"
               custom-class="colour-picker-popover"
               triggers="focus">
      <ColourPicker v-model="colour"/>
    </b-popover>
  </div>
</template>

<script>
import { Chrome } from "vue-color";

const STARTING_COLOUR = "#3068c2";

export default {
  name: "ColourSquareMolecule",
  components: {
    ColourPicker: Chrome,
  },
  props: {
    startingColour: String,
    returnEvent: {},
  },
  data() {
    return {
      colour: { hex: "" },
    };
  },
  mounted() {
    if (this.startingColour === undefined) {
      this.colour.hex = STARTING_COLOUR;
    } else {
      this.colour.hex = this.startingColour;
    }
  },
  computed: {
    reactiveStyle() {
      return {
        background: this.colour.hex,
      };
    },
  },
  watch: {
    colour(newColour) {
      this.$emit("colourChanged", {
        colour: newColour,
        ...this.returnEvent,
      });
    },
  },
};
</script>

<style lang="scss">
@import "../../assets/scss/colours";

.colour-square {
  width: 100%;
  height: 100%;
  border: 1px solid $white-border;
}

.popover-body {
  padding: 0;
}

</style>
