<template>
  <div class="inline bg-white text-gray-500 ring-2 ring-opacity-5 ring-black dark:ring-gray-600 dark:ring-opacity-50
  rounded-full overflow-hidden grid">
    <svg v-show="rendered" ref="canvas" width="20" height="20" class="w-full h-full">
    </svg>
    <font-awesome-icon v-if="!rendered"
                       icon="user-alt"
                       class="justify-self-center self-center -mb-2"
    />
  </div>
</template>

<script>
import Member from "@/models/domain/member";
import { updateSvg } from "jdenticon";

export default {
  name: "Avatar",
  props: {
    member: {
      type: Member,
      required: true,
    },
  },
  data() {
    return {
      rendered: false,
    };
  },
  mounted() {
    this.renderProfilePicture();
  },
  methods: {
    renderProfilePicture() {
      this.$nextTick(() => {
        try {
          updateSvg(this.$refs.canvas, `${this.member.id}${this.member.name}`);
          this.rendered = true;
        } catch (error) {
          this.rendered = false;
        }
      });
    },
  },
};
</script>
