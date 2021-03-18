<template>
  <div class="h-full flex flex-col pt-1 pr-2 gap-2 overflow-y-auto">
    <div class="flex flex-row gap-1 justify-between border-l-4 border-choppa-two pl-2"
         v-for="squad in squads" :key="squad.id"
         :style="{ 'border-left-color': squad.color }">
      <router-link :to="{ name: 'edit-squad', params: { id: squad.path } }" class="font-semibold self-center">
        {{ squad.name }}
      </router-link>
      <div class="flex flex-col">
        <div class="text-right">{{ squad.members.length }} Members</div>
        <div class="flex flex-row-reverse gap-1">
          <Avatar v-for="member in squad.members" :key="member.id" :seed="member.avatarSeed"
                  class="inline -ml-1 w-5 h-5 text-xl self-center"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Avatar from "@/components/member/Avatar";

export default {
  name: "ListSquadsTable",
  components: {
    Avatar,
  },
  props: {
    squads: {},
  },
  computed: {
    limitedMembersAvatars(members) {
      return members.slice(0, 5);
    },
  },
};
</script>
