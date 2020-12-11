<template>
  <div>
    <div>
      <StandardLabel :label-text="labelText" :for-id="id" :validator="validator"/>
    </div>
    <div class="mt-1">
      <ColorPalette :current-color="currentColor"
                    @colorSelected="(color) => $emit('colorSelected', color)"
      />
      <ErrorPrompt v-if="validator" v-show="validator.$invalid" :validator="validator" :label-text="labelText" />
    </div>
  </div>
</template>

<script>
import StandardLabel from "@/components/forms/inputs/StandardLabel";
import ColorPalette from "@/components/forms/inputs/ColorPalette";
import ErrorPrompt from "@/components/forms/ErrorPrompt";

export default {
  name: "ColorPaletteWithLabel",
  components: {
    ErrorPrompt,
    ColorPalette,
    StandardLabel,
  },
  props: {
    id: {
      type: String,
      required: true,
    },
    labelText: {
      type: String,
      required: true,
    },
    currentColor: String,
    validator: Object,
  },
};
</script>
