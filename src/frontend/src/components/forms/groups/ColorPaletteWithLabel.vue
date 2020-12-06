<template>
  <div>
    <div>
      <StandardLabel :label-text="labelText" :for-id="id"/>
    </div>
    <div class="mt-1">
      <ColorPalette :current-color="currentColor"
                    @colorSelected="(color) => $emit('colorSelected', color)"
      />
    </div>
  </div>
</template>

<script>
import StandardLabel from "@/components/forms/inputs/StandardLabel";
import ColorPalette from "@/components/forms/inputs/ColorPalette";

export default {
  name: "ColorPaletteWithLabel",
  components: {
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
  },
};
</script>
