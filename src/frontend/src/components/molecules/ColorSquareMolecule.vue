<template>
  <div class="h-100" ref="target">
    <button class="color-square" :style="colourSquareStyle"/>
    <b-popover v-if="this.$refs.target"
               :target="this.$refs.target"
               custom-class="color-picker-popup"
               triggers="focus"
               ref="popup"
               :show.sync="showingColourPopover">
      <div class="color-picker">
        <button class="color-picker__color"
                v-for="hex in possibleColours"
                v-bind:key="hex"
                :style="cssVars(hex)"
                @click="onColourSelected(hex)"
        />
      </div>
    </b-popover>
  </div>
</template>

<script>
const COLOUR_HOVER_OPACITY = 64;
const COLORS = [
  "#2ecc70", "#27af60", "#3398db", "#2980b9",
  "#f03ab0", "#bf0f85", "#a463bf", "#8e43ad",
  "#f2c511", "#f39c19", "#e84b3c", "#c0382b",
  "#dde6e8", "#bdc3c8", "#3d556e", "#222f3d",
];

export default {
  name: "ColorSquareMolecule",
  props: {
    startingColor: String,
    returnEvent: {},
  },
  data() {
    return {
      possibleColours: COLORS,
      color: undefined,
      showingColourPopover: false,
    };
  },
  mounted() {
    if (this.startingColor === undefined) {
      this.color = COLORS.get(0);
    } else {
      this.color = this.startingColor;
    }
  },
  computed: {
    colourSquareStyle() {
      return {
        "--selected-color": this.color,
      };
    },
  },
  methods: {
    cssVars(hex) {
      return {
        "--bg-color": hex,
        "--bg-hover-color": `${hex}${COLOUR_HOVER_OPACITY}`,
      };
    },
    onColourSelected(hex) {
      this.color = hex;
      this.showingColourPopover = false;
      this.$emit("colorChanged", this.color);
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
  background-color: var(--selected-color);
  transition: background-color ease 0.25s;
}

.color-picker-popup {
  max-width: 207px;
}

.color-picker {
  padding: 0.25rem;

  &__color {
    width: 2.5rem;
    height: 2.5rem;
    border-radius: 50%;
    background: var(--bg-color);
    box-shadow: inset 0 0 2px rgba(0,0,0,.75);
    box-sizing: border-box;
    outline: none;
    border: 0;
    margin-right: 0.2rem;
    padding: 0;
    display: inline-block;
    transition: background-color ease 0.25s;

    &:hover,
    &:active {
      background: var(--bg-hover-color);
      outline: none;
    }
  }
}

</style>
