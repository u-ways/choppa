<template>
  <div class="member">
    <span class="member__icon">
      <font-awesome icon="user-alt" />
    </span>
    <span class="member__name">
      {{ member.name }}
    </span>
    <LabelAtom :text="this.member.chapter" :labelType="this.memberLabelType" />
  </div>
</template>

<script lang="ts">
  import { Options, Vue } from "vue-class-component";
  import { Member } from "@/data/types/Member";
  import LabelAtom from "../atoms/LabelAtom.vue";
  import {LabelType} from "@/data/enumerations/LabelType";

  @Options({
    components: {
      LabelAtom
    },
    props: {
      member: Member
    },
    data() {
      return {
        memberLabelType: LabelType.LIGHT_GREEN_LABEL
      };
    }
  })
  export default class MemberMolecule extends Vue {}
</script>

<style scoped lang="scss">
  @import "../../assets/scss/colours";

  .member {
    text-align: center;
    width: 100%;
    background: $light-gray;
    border: 1px solid $dark-gray;
    border-radius: 4px;
    padding: 10px;

    &__icon {
      padding-right: 5px;
    }

    &__name {
      padding-right: 5px;
    }

    &__chapter {
      font-weight: bold;
      font-size: x-small;
      border-radius: 4px;
      padding: 2px;
      background: $red;
      color: $light-gray;
    }
  }
</style>