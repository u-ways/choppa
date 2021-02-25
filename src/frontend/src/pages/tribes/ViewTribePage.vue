<template>
  <StandardPageTemplate>
    <template v-slot:page-header>
      <template v-if="isLoaded">
        <div class="flex flex-row place-content-between place-items-center">
          <div class="truncate">
            <div class="text-3xl font-normal truncate">
              <span class="hidden sm:inline">Tribe </span>
              <span class="font-bold truncate">
                {{ tribe.name }}
              </span>
            </div>
            <div class="text-sm leading-7">
              <span>{{ tribe.squads.length }} Squads |</span>
              <span>{{ tribe.allDistinctMembers().length }} Distinct Members</span>
            </div>
          </div>
          <div class="flex-shrink-0" v-if="tribe">
            <StyledButton type="link"
                          variant="secondary"
                          css="px-2 sm:pr-5 sm:pl-4"
                          :link="{ name: 'edit-tribe', params: { id: tribe.path } }">
              <div class="inline sm:pr-1">
                <font-awesome-icon icon="pencil-alt"/>
              </div>
              <div class="hidden sm:inline">Edit</div>
            </StyledButton>
          </div>
        </div>
      </template>
      <template v-else>
        <div class="flex flex-col place-content-start gap-1">
          <div class="animate-pulse bg-gray-400 rounded w-72 h-9"></div>
          <div class="animate-pulse bg-gray-400 rounded w-52 h-7 pt-2"></div>
        </div>
      </template>
    </template>
    <template v-slot:full-width>
      <div class="mx-3 my-5">
        <div v-if="isLoaded">
          <div class="grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-3" v-if="tribe.squads.length > 0">
            <SquadCard v-for="squad in tribe.squads" :squad="squad" :key="squad.id"/>
          </div>
          <div v-else class="container mx-auto mt-10">
            <NoSquadsToShowAlert :tribe="tribe"/>
          </div>
        </div>
        <div class="grid grid-cols-1 lg:grid-cols-3 gap-3" v-else>
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
import StandardPageTemplate from "@/components/templates/StandardPageTemplate";
import SquadSkeleton from "@/components/squads/SquadSkeleton";
import SquadCard from "@/components/squads/SquadCard";
import StyledButton from "@/components/atoms/buttons/StyledButton";
import { getTribe } from "@/config/api/tribe.api";
import NoSquadsToShowAlert from "@/components/squads/NoSquadsToShowAlert";

export default {
  name: "ViewTribePage",
  components: {
    NoSquadsToShowAlert,
    StyledButton,
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
    try {
      this.tribe = await getTribe({ id: this.$route.params.id });
      this.isLoaded = true;
    } catch (error) {
      await this.$router.replace("/not-found");
    }
  },
};
</script>
