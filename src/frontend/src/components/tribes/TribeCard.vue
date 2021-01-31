<template>
  <div class="shadow ring-1 ring-black ring-opacity-5 border-choppa-two dark:border-choppa-two border-t-4
  bg-white dark:bg-gray-700 dark:ring-bg-gray-700 p-5">
    <div class="flex flex-col">
      <div class="text-xl font-normal">Tribe <span class="font-semibold">{{ tribe.name }}</span></div>
      <div class="text-sm leading-8">
        {{ tribe.squads.length }} Squads
        {{ tribe.allDistinctMembers().length }} Members
      </div>
    </div>
  </div>
</template>

<script>
import Tribe from "@/models/domain/tribe";

export default {
  name: "TribeCard",
  props: {
    tribe: Tribe,
  },
};
</script>
