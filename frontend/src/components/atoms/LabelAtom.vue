<template>
  <span class="label" :class="[ labelType.backgroundColourCss ]">
    <span class="label__text" :class="[ labelType.textColourCss ]">
      {{text}}
    </span>
  </span>
</template>

<script lang="ts">
  import { Options, Vue } from "vue-class-component";
  import { LabelType } from "../../data/enumerations/LabelType";

  @Options({
    props: {
      text: String,
      labelType: LabelType
    }
  })
  export default class LabelAtom extends Vue {}
</script>

<style scoped lang="scss">
  @import "../../assets/scss/colours";
  @import "../../assets/scss/labels";

  .label {
    border-radius: 4px;
    padding: 2px;

    &__text {
      font-weight: bold;
      font-size: x-small;
    }
  }
</style>