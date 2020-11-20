<template>
  <div class="tribe">
    <div class="row ml-0 mr-0">
      <div class="col">
        <div class="tribe__header">
          <div class="row m-0">
            <div class="col tribe__header-name text-center p-0">
              <span class="tribe__header--light">Tribe</span> {{ tribe.name }}
            </div>
          </div>
          <div class="row m-0">
            <div class="col tribe__header-underneath text-center p-0">
              {{ tribe.squads.length }} Squads
              | {{ tribe.allDistinctMembers().length }} Distinct Members
              | <router-link to="/tribe/1/edit">Edit</router-link>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="row mt-3 ml-0 mr-0">
      <div class="col-lg-6 mb-3" v-for="squad in tribe.squads" :key="squad.id">
        <SquadOrganism :squad="squad"/>
      </div>
    </div>
  </div>
</template>

<script>
import Tribe from "@/data/types/tribe";
import SquadOrganism from "@/components/organisms/SquadOrganism";

export default {
  name: "TribeOrganism",
  components: { SquadOrganism },
  props: {
    tribe: Tribe,
  },
};

</script>

<style scoped lang="scss">
@import "../../assets/scss/colours";
@import "../../assets/scss/typography";

.tribe {

  &__header {
    font-size: 1.8rem;
    font-family: $font-family-emphasis;

    &--light {
      font-weight: $font-weight-emphasis-light;
    }

    &-name {
      font-size: 1.8rem;
      font-weight: $font-weight-emphasis-medium;
      text-overflow: ellipsis;
      overflow: hidden;
      max-width: 100%;
    }

    &-underneath {
      font-family: $font-family-body;
      font-size: 0.75rem;
      font-weight: $font-weight-body-light;
    }

    &-edit {
      font-size: 1.2rem;
      line-height: 2rem;
      vertical-align: middle;
      color: red;
    }
  }
}
</style>
