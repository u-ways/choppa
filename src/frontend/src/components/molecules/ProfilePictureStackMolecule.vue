<template>
  <div class="profile-picture-stack row m-0">
    <ProfilePictureAtom v-for="member in membersShowing"
                        v-bind:key="member.id"
                        :member="member"
                        css="profile-picture-stack__picture"
    />
    <div v-if="numberOfExtraMembers > 0" class="profile-picture-stack__not-showing-count">
      +{{ numberOfExtraMembers }}
    </div>
  </div>
</template>

<script>
import ProfilePictureAtom from "@/components/atoms/ProfilePictureAtom";

export default {
  name: "ProfilePictureStackMolecule",
  components: { ProfilePictureAtom },
  props: {
    members: Array,
    showCount: {
      type: Number,
      default: 3,
    },
  },
  computed: {
    membersShowing() {
      return this.members.slice(0, this.showCount);
    },
    numberOfExtraMembers() {
      return this.members.length - this.showCount;
    },
  },
};
</script>

<style scoped lang="scss">
@import "../../assets/scss/colours";
@import "../../assets/scss/typography";

.profile-picture-stack {

  & > div {
    margin-left: -0.55rem;
  }

  & > div:first-of-type {
    margin-left: 0;
  }

  &__column {
    display: flex;
  }

  &__picture {
    width: 1.5rem;
    height: 1.5rem;
    display: inline-block;
  }

  &__not-showing-count {
    width: 1.5rem;
    height: 1.5rem;
    line-height: 1.5rem;
    border-radius: 50%;
    background: $white;
    border: 1px solid $white-border;
    text-align: center;
    vertical-align: middle;
    color: $text-dark;
    font-family: $font-family-body;
    font-weight: $font-weight-body-medium;
    font-size: 0.75rem;
    z-index: 1;
  }
}
</style>
