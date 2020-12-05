<template>
  <StandardPageTemplate>
    <template v-slot:page-header>
      <template v-if="isLoaded">
        <div class="flex flex-row place-content-between place-items-center">
          <div>
            <p class="text-3xl font-normal">
              <span class="hidden sm:inline">Tribe </span>
              <span class="inline font-bold">
                {{ tribe.name }}
              </span>
            </p>
            <p class="text-sm leading-7">
              <span>{{ tribe.squads.length }} Squads |</span>
              <span>{{ tribe.allDistinctMembers().length }} Distinct Members</span>
            </p>
          </div>
          <div>
            <StyledButton type="link" variant="secondary-light" css="pr-5 pl-4" link="edit" :link-append="true">
              <div class="inline pr-1">
                <font-awesome-icon icon="pencil-alt"/>
              </div>
              Edit
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
          <div class="grid grid-cols-1 lg:grid-cols-2 gap-3" v-if="tribe.squads.length > 0">
            <SquadCard v-for="squad in tribe.squads" :squad="squad" :key="squad.id"/>
          </div>
          <div v-else class="container mx-auto mt-10 flex flex-col gap-4">
            <img :src="require('@/assets/svg/squad.svg')" alt="A representation of a squad" width="100%"
                 height="100%" class="h-44 w-auto mx-auto"/>
            <p class="place-self-center font-semibold text-md dark:text-gray-300">
              Create your first Squad for this Tribe.
            </p>
            <div class="inline-block mx-auto">
              <StyledButton type="link" link="/edit/squad/create" variant="primary">
                Create Squad
              </StyledButton>
            </div>
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
import StandardPageTemplate from "@/components/templates/StandardPageTemplate";
import SquadSkeleton from "@/components/squads/SquadSkeleton";
import SquadCard from "@/components/squads/SquadCard";
import StyledButton from "@/components/atoms/buttons/StyledButton";
import { getTribe } from "@/config/api/tribe.api";

export default {
  name: "ViewTribePage",
  components: {
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
      console.log(error);
      await this.$router.replace("/not-found");
    }
  },
};
</script>
