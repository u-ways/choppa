<template>
  <div class="squad">
    <div class="squad__header" :style="{ 'border-top-color': squad.color }">
      <h1 class="squad__header-name m-0"><span class="squad__header-name--light">Squad</span> {{ squad.name }}</h1>
      <div class="row align-items-center m-0 pt-1">
        <div class="squad__header-member-count">
          {{ squad.members.length }} Members
        </div>
        <div class="squad__header-profile-pictures ml-1">
          <ProfilePictureStackMolecule :members="squad.members" :showCount="3"/>
        </div>
      </div>
    </div>
    <div class="squad__contents">
      <div class="row ml-0 mr-0 pb-1">
        <div class="col-1"></div>
        <div class="col-6">
          <span class="squad__contents--heading">Name</span>
        </div>
        <div class="col-5 col-md-3 text-center">
          <span class="squad__contents--heading">Chapter</span>
        </div>
      </div>
      <div class="row squad__contents-person-row" v-for="member in squad.members" v-bind:key="member.id">
        <div class="col-7 d-flex align-items-center squad__contents-person-cell">
          <div class="d-inline-block mr-3 squad__contents-person-picture">
            <ProfilePictureAtom :member="member"/>
          </div>
          <div>{{ member.name }}</div>
        </div>
        <div class="col-5 col-md-3 flex justify-content-center">
          <ChapterLabelAtom :chapter="member.chapter" v-if="member.chapter" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Squad from "@/data/types/squad";
import ProfilePictureStackMolecule from "@/components/molecules/ProfilePictureStackMolecule";
import ProfilePictureAtom from "@/components/atoms/ProfilePictureAtom";
import ChapterLabelAtom from "@/components/atoms/ChapterLabelAtom";

export default {
  name: "SquadOrganism",
  components: { ChapterLabelAtom, ProfilePictureAtom, ProfilePictureStackMolecule },
  props: {
    squad: Squad,
  },
};
</script>

<style scoped lang="scss">
@import "src/assets/scss/colors";
@import "../../assets/scss/typography";

.squad {
  width: 100%;
  height: 100%;
  box-shadow: 0 2px 6px 0 hsla(0, 0%, 0%, 0.2);
  background-color: $light-grey;

  &__header {
    border-top: 5px solid $accent;
    background-color: $white;
    padding: 1.25rem 1.25rem 0.8rem 1.25rem;
    max-width: 100%;
    font-family: $font-family-emphasis;

    &-name {
      font-size: 1.5rem;
      font-weight: $font-weight-emphasis-medium;
      overflow: hidden;
      max-width: 100%;

      &--light {
        font-weight: $font-weight-emphasis-light;
      }
    }

    &-member-count {
      font-family: $font-family-body;
      font-weight: $font-weight-body-light;
      font-size: 0.9rem;
      display: inline-block;
    }

    &-profile-pictures {
      display: inline-block;
    }
  }

  &__contents {
    padding: 0.7rem 0 1rem 0;
    max-width: 100%;

    &--heading {
      font-weight: $font-weight-body-light;
      font-size: 0.75rem;
    }

    &-person-row {
      max-width: 100%;
      margin: 0;
      padding: 0.4rem 0;
      font-weight: $font-weight-body-medium;
      font-size: 1rem;
      color: hsla(0, 0, 0, 0.7);
      border-top: 1px solid $white-border;
    }

    &-person-cell {
      overflow: hidden;
    }

    &-person-picture {
      min-width: 2rem;
      min-height: 2rem;
      max-width: 2rem;
      max-height: 2rem;
      width: 2rem;
      height: 2rem;
    }
  }
}
</style>
