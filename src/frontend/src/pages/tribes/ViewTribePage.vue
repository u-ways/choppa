<template>
  <StandardPageTemplate>
    <div v-if="loadingErrored">
      bam errored
    </div>
    <template v-else>
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
      <div class="mx-3 my-5">
        <div v-if="isLoaded">
          <div class="grid grid-cols-1 lg:grid-cols-2 gap-3" v-if="tribe.squads.length > 0">
            <SquadCard v-for="squad in tribe.squads" :squad="squad" :key="squad.id"/>
          </div>
          <div v-else class="container mx-auto mt-10 sm:mt-20 flex flex-col gap-4">
            <img src="src/assets/svg/squad.svg" alt="A representation of a squad" width="100%" height="100%"
                 class="h-44 w-auto mx-auto"/>
            <p class="place-self-center font-semibold text-md dark:text-gray-300">
              Create your first Squad for this Tribe.
            </p>
            <router-link to="/edit/squad/create" class="rounded-md p-2 text-sm mx-auto font-semibold
            text-gray-50 bg-purple-600 hover:ring focus:ring focus:outline-none ring-purple-600 ring-opacity-30
            transform-gpu transition-transform transition-colors hover:-translate-y-0.5 focus:-translate-y-0.5
            duration-100 motion-reduce:transition-none">
              Create Squad
            </router-link>
          </div>
        </div>
        <div class="grid grid-cols-1 lg:grid-cols-2 gap-3" v-else>
          <SquadSkeleton />
          <SquadSkeleton />
          <SquadSkeleton class="hidden md:block" />
          <SquadSkeleton class="hidden md:block" />
        </div>
      </div>
    </template>
  </StandardPageTemplate>
</template>

<script>
/* eslint-disable */
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
      loadingErrored: false,
    };
  },
  async mounted() {
    // TODO: error handling
    try {
      this.tribe = await getTribe({
        id: this.$route.params.id,
        loadSquads: true,
      });

      this.isLoaded = true;
    } catch {
      this.isLoaded = false;
      this.loadingErrored = true;
    }
  },
};
</script>
