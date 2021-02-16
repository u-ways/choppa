<template>
  <div class="inline bg-white text-gray-500 ring-2 ring-opacity-5 ring-black dark:ring-gray-600 dark:ring-opacity-50
  rounded-full overflow-hidden grid">
    <img :src="imageUrlOverride" v-if="shouldUseImageOverride && rendered"
         class="w-full h-full object-cover">
    <svg v-show="!shouldUseImageOverride && rendered" ref="canvas" width="20" height="20" class="w-full h-full">
    </svg>
    <font-awesome-icon v-if="!rendered"
                       icon="user-alt"
                       class="justify-self-center self-center -mb-2"
    />
  </div>
</template>

<script>
import { updateSvg } from "jdenticon";

export default {
  name: "Avatar",
  props: {
    seed: {
      type: String,
      required: true,
    },
    imageUrlOverride: {
      type: String,
      required: false,
    },
  },
  data() {
    return {
      rendered: false,
    };
  },
  computed: {
    shouldUseImageOverride() {
      return this.imageUrlOverride && this.imageUrlOverride.length > 0;
    },
  },
  mounted() {
    if (this.shouldUseImageOverride) {
      this.rendered = true;
    } else {
      this.renderProfilePicture();
    }
  },
  methods: {
    renderProfilePicture() {
      this.$nextTick(() => {
        try {
          updateSvg(this.$refs.canvas, this.seed);
          this.rendered = true;
        } catch (error) {
          this.rendered = false;
        }
      });
    },
  },
};
</script>
