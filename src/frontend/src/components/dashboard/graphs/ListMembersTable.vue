<template>
  <div class="h-full flex flex-col pt-1 pr-2 gap-2 overflow-y-auto">
    <div class="flex flex-row gap-2" v-for="member in members" :key="member.id">
      <Avatar :seed="member.avatarSeed" class="w-6 h-6"/>
      <router-link :to="{ name: 'edit-member', params: { id: member.path } }" class="font-semibold self-center">
        {{ member.name }}
      </router-link>
      <template v-if="member.active === true">
        <div class="flex-grow text-right text-green-500 font-semibold">Active</div>
      </template>
      <template v-else-if="member.active === false">
        <div class="flex-grow text-right text-red-500 font-semibold">Inactive</div>
      </template>
    </div>
  </div>
</template>

<script>
import Avatar from "@/components/member/Avatar";

export default {
  name: "ListMembersTable",
  components: {
    Avatar,
  },
  props: {
    members: {},
  },
};
</script>
