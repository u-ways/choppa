<template>
  <div class="grid grid-cols-1 md:grid-cols-2 gap-2">
    <router-link v-for="squad in squads" v-bind:key="squad.id" :to="`/${squad.id}`"
                 class="hover:ring-2 focus:ring-2 focus:outline-none ring-choppa-two rounded-sm">
      <SquadCard :squad="squad"
                 :show-header-only="true"
                 :member-show-count="6"
                 :headerLinkLocation="squad.id"
      />
    </router-link>
  </div>
</template>

<script>
import SquadCard from "@/components/squads/SquadCard";

export default {
  name: "SquadsOverview",
  components: {
    SquadCard,
  },
  props: {
    squads: {
      required: true,
    },
  },
};
</script>
