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
          <div class="col-md-9 p-0 px-3 pl-md-0">
            <div class="form-group">
              <label for="tribe-name">Tribe Name</label>
              <input class="form-control" id="tribe-name" v-model="tribe.name">
            </div>
          </div>
        </div>
        <div class="edit-tribe__chapters">
          <div class="row">
            <div class="col-md-3 edit-tribe__subheading pb-2 pb-md-0">
              Chapters
            </div>
            <div class="col-md-9">
              <div class="mb-4" v-for="chapter in tribe.allDistinctChapters()" :key="chapter.id">
                <div class="row">
                  <div class="col-8 p-0 pl-3 pl-md-0">
                    <label :for="chapterInputId(chapter.id)">Chapter Name</label>
                  </div>
                </div>
                <div class="row">
                  <div class="col-8 p-0 pl-3 pl-md-0">
                    <input class="form-control"
                           :id="chapterInputId(chapter.id)"
                           :value="chapter.name"
                           @change="onChapterNameChanged(chapter, $event)"
                    >
                  </div>
                  <div class="col-2 px-1 px-md-3">
                    <ColourSquareMolecule
                      :startingColour="chapter.colour"
                      @colourChanged="onChapterColourChanged"
                      :returnEvent="{ chapter: chapter }"
                    />
                  </div>
                  <div class="col-2 p-0">
                    <button type="button" class="btn btn-outline-dark">
                      <font-awesome-icon icon="trash"/>
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="edit-tribe__squads">
          <div class="row">
            <div class="col-12 col-md-3 edit-tribe__subheading pb-2 pb-md-0">
              Squads
            </div>
            <div class="col-12 col-md-9">
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
                <div class="row mt-md-3 justify-content-center">
                  <div class="col-12 p-md-0 text-secondary pt-3">
                    Members
                  </div>
                </div>
                <div class="row mt-2">
                  <div class="col-12 p-md-0 bg-white mb-2" v-for="member in squad.members" :key="member.id">
                    <EditTribeMemberRow :member="member"
                                        :possible-chapters="tribe.allDistinctChapters()"
                                        :possible-squads="tribe.squads"
                                        :current-squad="squad"
                                        @expanded="onMemberRowExpanded"
                                        @collapsed="onMemberRowCollapsed"
                    />
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
import EditTribeMemberRow from "@/components/molecules/EditTribeMemberRow";

export default {
  name: "EditTribePage",
  components: {
    EditTribeMemberRow,
    ColourSquareMolecule,
    FixedWidthWithNavbarTemplate,
  },
  data() {
    return {
      tribe: this.$root.$data.testTribeOne,
      currentlyExpandedMemberRow: undefined,
    };
  },
  methods: {
    nameInputId(squadId) {
      return `squad-name-${squadId}`;
    },
    chapterInputId(chapterId) {
      return `chapter-name-${chapterId}`;
    },
    memberDropdownSettingsId(memberId) {
      return `member-dropdown-setting-${memberId}`;
    },
    deleteSquad(squadId) {
      this.tribe.removeSquadById(squadId);
    },
    onSquadColourChanged(eventData) {
      this.tribe.findSquadById(eventData.squadId).colour = eventData.colour.hex;
    },
    onChapterColourChanged(eventData) {
      this.tribe.updateChapter(eventData.chapter.id, eventData.chapter.name, eventData.colour.hex);
    },
    onChapterNameChanged(chapter, eventData) {
      this.tribe.updateChapter(chapter.id, eventData.target.value, chapter.colour);
    },
    onMemberRowExpanded(memberRow) {
      if (this.currentlyExpandedMemberRow) {
        this.currentlyExpandedMemberRow.silentCollapse();
      }

      this.currentlyExpandedMemberRow = memberRow;
    },
    onMemberRowCollapsed(memberRow) {
      if (this.currentlyExpandedMemberRow === memberRow) {
        this.currentlyExpandedMemberRow = null;
      }
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

  &__chapters {
    border-top: 1px solid $white-border;
    margin-top: 1rem;
    padding-top: 0.3rem;
  }

  &__squads {
    border-top: 1px solid $white-border;
    margin-top: 1rem;
    padding-top: 0.3rem;
  }
}

</style>
