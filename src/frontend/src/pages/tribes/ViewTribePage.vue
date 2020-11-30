<template>
  <StandardPageTemplate>
    <div class="text-center">
      <template v-if="isLoaded">
        <p class="text-3xl font-normal">Tribe <span class="font-semibold">{{ tribe.name }}</span></p>
        <p class="text-sm leading-7">
          {{ tribe.squads.length }} Squads | {{ tribe.allDistinctMembers().length }} Distinct Members
        </p>
      </template>
      <template v-else>
        <div class="flex flex-col gap-1">
          <div class="place-self-center animate-pulse bg-gray-400 rounded w-72 h-9"></div>
          <div class="place-self-center animate-pulse bg-gray-400 rounded w-52 h-7 pt-2"></div>
        </div>
      </template>
    </div>
    <div class="grid grid-cols-1 lg:grid-cols-2 mx-3 gap-3 my-5">
      <template v-if="isLoaded">
        <SquadCard v-for="squad in tribe.squads" :squad="squad" :key="squad.id"/>
      </template>
      <template v-else>
        <SquadSkeleton />
        <SquadSkeleton />
        <SquadSkeleton class="hidden md:block" />
        <SquadSkeleton class="hidden md:block" />
      </template>
    </div>
  </StandardPageTemplate>
</template>

<script>
import StandardPageTemplate from "@/components/templates/StandardPageTemplate";
import SquadSkeleton from "@/components/squads/SquadSkeleton";
import { getTribe } from "@/config/api/tribe.api";
import SquadCard from "@/components/squads/SquadCard";

export default {
  name: "ViewTribePage",
  components: {
    SquadCard,
    SquadSkeleton,
    StandardPageTemplate,
  },
  data() {
    return {
      isLoaded: false,
      tribe: undefined,
    };
  },
  async mounted() {
    // TODO: error handling
    this.tribe = await getTribe({
      id: this.$route.params.id,
      loadSquads: true,
    });

    this.isLoaded = true;
  },
};
</script>
