<template>
  <div class="h-full flex flex-col pt-2 pr-2 gap-2 overflow-y-auto">
    <div class="grid grid-cols-4 font-bold text-sm">
      <div class="text-left">Member</div>
      <div class="text-center">Operation</div>
      <div class="text-center">Squad</div>
      <div class="text-right">Time</div>
    </div>
    <div class="flex-grow">
      <div v-for="(change, i) in latestChanges" :key="i">
        <div class="grid grid-cols-4 py-3">
          <div class="text-left flex flex-row gap-2 items-center">
            <Avatar :seed="change.member.avatarSeed" class="w-6 h-6"/>
            <div class="font-semibold">{{ change.member.name }}</div>
          </div>
          <div class="text-center">
            {{ change.revisionType }}
          </div>
          <div class="text-center">
            {{ change.squad.name }}
          </div>
          <div class="text-right">
            {{ printPrettyDate(change.date) }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Avatar from "@/components/member/Avatar";

export default {
  name: "LatestChangesTable",
  components: {
    Avatar,
  },
  props: {
    latestChanges: {},
  },
  methods: {
    printPrettyDate(timestamp) {
      const date = new Date(timestamp);
      const ye = new Intl.DateTimeFormat("en", { year: "numeric" }).format(date);
      const mo = new Intl.DateTimeFormat("en", { month: "2-digit" }).format(date);
      const da = new Intl.DateTimeFormat("en", { day: "2-digit" }).format(date);
      return `${da}/${mo}/${ye}`;
    },
  },
};
</script>
