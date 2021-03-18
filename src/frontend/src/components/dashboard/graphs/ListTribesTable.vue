<template>
  <div class="h-full flex flex-col pt-1 pr-2 gap-2 overflow-y-auto">
    <div class="flex flex-row gap-1 justify-between border-l-4 border-choppa-two pl-2"
         v-for="tribe in tribes" :key="tribe.id"
         :style="{ 'border-left-color': tribe.color }">
      <router-link :to="{ name: 'view-tribe', params: { id: tribe.path } }" class="font-semibold self-center">
        {{ tribe.name }}
      </router-link>
      <div class="flex flex-col">
        <div class="text-right">{{ tribe.squads.length }} Squads</div>
        <div class="text-right">{{ tribe.allDistinctMembers().length }} Members</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ListTribeTable",
  props: {
    tribes: {},
  },
};
</script>
