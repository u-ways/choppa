<template>
  <div class="bg-white dark:bg-gray-700 shadow rounded-sm flex flex-row items-center cursor-pointer
  hover:ring-2 ring-choppa-two border-t-4 flex flex-row"
       :style="{ 'border-color': squad.color }"
       @click="toggleRadioButton">
    <input :id="squad.id"
           name="squad-selection"
           type="checkbox"
           class="ml-4 flex-shrink-0 cursor-pointer pointer-events-none"
           ref="checkbox"
    />
    <div class="flex flex-col">
      <StandardLabel :label-text="squad.name" :for-id="squad.id" class="px-2 pt-3 flex-grow cursor-pointer"/>
      <div class="flex flex-row px-2 pb-3">
        <Avatar v-for="member in limitedMembersAvatars(squad.members)" :key="member.id" :seed="member.avatarSeed"
                class="inline w-5 h-5 text-xl self-center"
        />
      </div>
    </div>
  </div>
</template>

<script>
import Squad from "@/models/domain/squad";
import StandardLabel from "@/components/forms/inputs/StandardLabel";
import Avatar from "@/components/member/Avatar";

export default {
  name: "SquadsCheckbox",
  components: {
    StandardLabel,
    Avatar,
  },
  props: {
    squad: Squad,
    defaultSelected: {
      type: Boolean,
      default: false,
    },
  },
  mounted() {
    this.$refs.checkbox.checked = this.defaultSelected;
  },
  methods: {
    limitedMembersAvatars(members) {
      return members.slice(0, this.memberShowCount);
    },
    toggleRadioButton() {
      this.$refs.checkbox.checked = this.$refs.checkbox.checked !== true;
      this.$emit("onChange", { squad: this.squad, selected: this.$refs.checkbox.checked });
    },
  },
};
</script>
