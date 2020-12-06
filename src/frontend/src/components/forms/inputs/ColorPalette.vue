<template>
  <div class="flex flex-row flex-wrap gap-1">
    <button v-for="color in this.colors" v-bind:key="color"
            :style="{ background: color }"
            @click="$emit('colorSelected', color)"
            class="w-8 h-8 sm:w-10 sm:h-10 flex-shrink-0 rounded-full hover:outline-none focus:outline-none
            hover:ring focus:ring ring-choppa-two transform-gpu transition-transform transition-colors
            hover:-translate-y-0.5 focus:-translate-y-0.5 duration-100 motion-reduce:transition-none">
      <div v-if="color.toLowerCase() === currentColorNoAlpha.toLowerCase()" class="grid">
        <font-awesome-icon icon="check" class="text-2xl text-white justify-self-center self-center"/>
      </div>
    </button>
  </div>
</template>

<script>
import { stripAlphaFromHex } from "@/utils/stripAlphaFromHex";

export default {
  name: "ColorPalette",
  props: {
    colors: {
      default: () => [
        "#F87171", "#DC2626",
        "#FB923C", "#EA580C",
        "#FBBF24", "#D97706",
        "#FACC15", "#EAB308",
        "#A3E635", "#65A30D",
        "#4ADE80", "#16A34A",
        "#34D399", "#059669",
        "#2DD4BF", "#0D9488",
        "#22D3EE", "#0891B2",
        "#38BDF8", "#0284C7",
        "#60A5FA", "#2563EB",
        "#818CF8", "#4F46E5",
        "#A78BFA", "#7C3AED",
        "#C084FC", "#9333EA",
        "#E879F9", "#C026D3",
        "#F472B6", "#DB2777",
        "#FB7185", "#E11D48",
      ],
    },
    currentColor: String,
  },
  computed: {
    currentColorNoAlpha() {
      return stripAlphaFromHex(this.currentColor);
    },
  },
};
</script>
