<template>
  <div class="grid grid-cols-1 md:grid-cols-2 gap-2">
    <router-link v-for="member in members" v-bind:key="member.id" :to="`/${member.id}`"
                 class="hover:ring-2 focus:ring-2 focus:outline-none ring-choppa-two rounded-sm">
      <MemberCard :member="member"/>
    </router-link>
  </div>
</template>

<script>
import MemberCard from "@/components/member/MemberCard";

export default {
  name: "MembersOverview",
  components: {
    MemberCard,
  },
  props: {
    members: {
      required: true,
    },
  },
};
</script>
