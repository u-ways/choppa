<template>
  <FixedWidthWithNavbarTemplate css="pt-3">
    <div class="edit-tribe">
      <div class="edit-tribe__header text-center">
        Edit Tribe
      </div>
      <div class="edit-tribe__body">
        <div class="row">
          <div class="col-md-3 edit-tribe__subheading pb-2 pb-md-0">
            Tribe
          </div>
          <div class="col-md-9 p-0 pl-3 pl-md-0">
            <div class="form-group">
              <label for="tribe-name">Tribe Name</label>
              <input class="form-control" id="tribe-name" v-model="tribe.name">
            </div>
          </div>
        </div>
        <div class="edit-tribe__squads">
          <div class="row">
            <div class="col-md-3 edit-tribe__subheading pb-2 pb-md-0">
              Squads
            </div>
            <div class="col-md-9">
              <div class="mb-4" v-for="squad in tribe.squads" :key="squad.id">
                <div class="row">
                  <div class="col-8 p-0 pl-3 pl-md-0">
                    <label :for="nameInputId(squad.id)">Squad Name</label>
                  </div>
                </div>
                <div class="row">
                  <div class="col-8 p-0 pl-3 pl-md-0">
                    <input class="form-control"
                           :id="nameInputId(squad.id)"
                           v-model="tribe.findSquadById(squad.id).name"
                    >
                  </div>
                  <div class="col-2 px-1 px-md-3">
                    <ColourSquareMolecule
                      :startingColour="tribe.findSquadById(squad.id).colour"
                      @colourChanged="onSquadColourChanged"
                      :returnEvent="{ squadId: squad.id }"
                    />
                  </div>
                  <div class="col-2 p-0">
                    <button type="button" class="btn btn-outline-dark" @click="deleteSquad(squad.id)">
                      <font-awesome-icon icon="trash"/>
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </FixedWidthWithNavbarTemplate>
</template>

<script>
import FixedWidthWithNavbarTemplate from "@/components/templates/FixedWidthWithNavbarTemplate";
import ColourSquareMolecule from "@/components/molecules/ColourSquareMolecule";

export default {
  name: "EditTribePage",
  components: {
    ColourSquareMolecule,
    FixedWidthWithNavbarTemplate,
  },
  data() {
    return {
      tribe: this.$root.$data.testTribeOne,
    };
  },
  methods: {
    nameInputId(squadId) {
      return `squad-name-${squadId}`;
    },
    deleteSquad(squadId) {
      this.tribe.removeSquadById(squadId);
    },
    onSquadColourChanged(eventData) {
      this.tribe.findSquadById(eventData.squadId).colour = eventData.colour.hex;
    },
  },
};
</script>

<style scoped lang="scss">
@import "src/assets/scss/colours";
@import "src/assets/scss/typography";

.edit-tribe {
  background: $white;
  border: 1px solid $white-border;
  padding: 1rem;

  &__header {
    font-size: 1.8rem;
    font-family: $font-family-emphasis;
  }

  &__subheading {
    font-family: $font-family-body;
    font-weight: $font-weight-body-bold;
  }

  &__body {
    margin-top: 1rem;
  }

  &__squads {
    border-top: 1px solid $white-border;
    margin-top: 1rem;
    padding-top: 0.3rem;
  }
}

</style>
