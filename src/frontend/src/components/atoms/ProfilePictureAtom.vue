<template>
  <div class="profile-picture" :class="[ this.css ]">
    <canvas v-show="hasRenderedProfilePicture"
            width="100"
            height="100"
            ref="canvas"
            class="profile-picture__actual profile-picture__border"
    />
    <font-awesome-icon v-show="!hasRenderedProfilePicture"
                       icon="user-alt"
                       class="profile-picture__default-picture profile-picture__border"
    />
  </div>
</template>

<script>
import Member from "@/data/types/Member";
import { update } from "jdenticon";

export default {
  name: "ProfilePictureAtom",
  props: {
    member: {
      type: Member,
      required: true,
    },
    css: String,
  },
  data() {
    return {
      hasRenderedProfilePicture: false,
    };
  },
  mounted() {
    this.renderProfilePicture();
  },
  methods: {
    renderProfilePicture() {
      this.$nextTick(() => {
        update(this.$refs.canvas, `${this.member.id}${this.member.name}`);
        this.hasRenderedProfilePicture = true;
      });
    },
  },
};
</script>

<style scoped lang="scss">
@import "../../assets/scss/colours";
@import "../../assets/scss/typography";

.profile-picture {
  width: 100%;
  height: 100%;
  background: $white;
  border-radius: 50%;
  overflow: hidden;

  &__default-picture {
    width: 100%;
    height: 100%;
    color: hsla(0, 0, 0, 0.7);
  }

  &__actual {
    background: $white;
    width: 100%;
    height: 100%;
    overflow: hidden;;
  }

  &__border {
    border: 2px solid $white-border;
    border-radius: 50%;
  }
}
</style>
