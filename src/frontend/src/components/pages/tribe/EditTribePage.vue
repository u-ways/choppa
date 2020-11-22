<template>
  <FixedWidthWithNavbarTemplate css="pt-3">
    <div class="edit-tribe mb-5" v-if="isLoaded">
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
        <div class="edit-tribe__section">
          <div class="row">
            <div class="col-md-3 edit-tribe__subheading pb-2 pb-md-0">
              Chapters
            </div>
            <div class="col-md-9">
              <div class="mb-4" v-for="chapter in currentChapters" :key="chapter.id">
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
                    <ColorSquareMolecule
                      :startingColor="chapter.color"
                      @colorChanged="onChapterColorChanged(chapter, $event)"
                    />
                  </div>
                  <div class="col-2 p-0">
                    <button type="button" class="btn btn-outline-dark" @click="onChapterDeleted(chapter.id)">
                      <font-awesome-icon icon="trash"/>
                    </button>
                  </div>
                </div>
              </div>
              <div class="row mt-2">
                <div class="col-12 pl-3 pl-md-0">
                  <button type="button" class="btn btn-outline-dark" @click="addNewChapter">
                    <font-awesome-icon icon="plus"/>
                    Add New Chapter
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="edit-tribe__section">
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
                    <ColorSquareMolecule
                      :startingColor="tribe.findSquadById(squad.id).color"
                      @colorChanged="onSquadColorChanged(squad, $event)"
                    />
                  </div>
                  <div class="col-2 p-0">
                    <button type="button" class="btn btn-outline-dark" @click="deleteSquad(squad.id)">
                      <font-awesome-icon icon="trash"/>
                    </button>
                  </div>
                </div>
                <div class="row mt-md-3 justify-content-center">
                  <div class="col-12 p-md-0 text-secondary pt-3 d-flex align-items-center">
                    <span>Members</span>
                  </div>
                </div>
                <div class="row mt-2">
                  <div class="col-12 p-md-0 bg-white mb-2" v-for="member in squad.members" :key="member.id">
                    <EditTribeMemberRow :member="member"
                                        :possible-chapters="currentChapters"
                                        :possible-squads="tribe.squads"
                                        :current-squad="squad"
                                        @expanded="onMemberRowExpanded"
                                        @collapsed="onMemberRowCollapsed"
                    />
                  </div>
                </div>
                <div class="row mt-2">
                  <div class="col-12 pl-3 pl-md-0">
                    <button type="button" class="btn btn-sm btn-outline-dark" @click="addNewMember(squad)">
                      <font-awesome-icon icon="plus"/>
                      Add New Member
                    </button>
                  </div>
                </div>
              </div>
              <div class="row mt-2">
                <div class="col-12 pl-3 pl-md-0">
                  <button type="button" class="btn btn-outline-dark" @click="addNewSquad">
                    <font-awesome-icon icon="plus"/>
                    Add New Squad
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="edit-tribe__section">
          <div class="row">
            <div class="col-12 col-md-3 pb-2 pb-md-0">
              <p class="edit-tribe__subheading">Rotation</p>
              <p class="text-muted">You can manually trigger a rotation here.</p>
            </div>
            <div class="col-12 col-md-9 d-flex align-items-center">
              <button class="btn btn-block btn-secondary">Trigger Rotation</button>
            </div>
          </div>
        </div>
        <div class="edit-tribe__section">
          <div class="row">
            <div class="col-12 col-md-3 pb-2 pb-md-0">
              <p class="edit-tribe__subheading">Save changes</p>
              <p class="text-muted">Save your changes before leaving this page.</p>
            </div>
            <div class="col-12 col-md-9 d-flex align-items-center">
              <button class="btn btn-block btn-primary">Save</button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="" v-else>
      <div class="row pt-3 mx-0">
        <div class="col-12 text-center">
          <div class="spinner-border text-primary" role="status">
            <span class="sr-only">Loading...</span>
          </div>
        </div>
      </div>
    </div>
  </FixedWidthWithNavbarTemplate>
</template>

<script>
import FixedWidthWithNavbarTemplate from "@/components/templates/FixedWidthWithNavbarTemplate";
import ColorSquareMolecule from "@/components/molecules/ColorSquareMolecule";
import EditTribeMemberRow from "@/components/molecules/EditTribeMemberRow";
import { v4 as uuidv4 } from "uuid";
import Member from "@/data/types/member";
import Squad from "@/data/types/squad";
import Chapter from "@/data/types/chapter";
import getTribe from "@/config/api/tribe.api";

export default {
  name: "EditTribePage",
  components: {
    EditTribeMemberRow,
    ColorSquareMolecule,
    FixedWidthWithNavbarTemplate,
  },
  data() {
    return {
      tribe: undefined,
      currentlyExpandedMemberRow: undefined,
      currentChapters: [],
      isLoaded: false,
    };
  },
  async mounted() {
    this.tribe = await getTribe({
      id: this.$route.params.id,
      loadSquads: true,
    });

    this.currentChapters = this.tribe.allDistinctChapters();
    this.isLoaded = true;
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
    onSquadColorChanged(squad, newColor) {
      this.tribe.findSquadById(squad.id).color = newColor;
    },
    getLocalChapter(chapterId) {
      return this.currentChapters.filter((c) => c.id === chapterId)[0];
    },
    onChapterColorChanged(chapter, newColor) {
      this.tribe.updateChapter(chapter.id, chapter.name, newColor);
      this.getLocalChapter(chapter.id).color = newColor;
    },
    onChapterNameChanged(chapter, eventData) {
      this.tribe.updateChapter(chapter.id, eventData.target.value, chapter.color);
      this.getLocalChapter(chapter.id).name = eventData.target.value;
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
    onChapterDeleted(chapterId) {
      this.currentChapters = this.currentChapters.filter((c) => c.id !== chapterId);
      this.tribe.deleteChapter(chapterId);
    },
    addNewChapter() {
      this.currentChapters.push(new Chapter({
        name: "New Chapter",
        color: "#ff00ff",
      }));
    },
    addNewMember(squad) {
      squad.addMember(new Member({
        name: "New Member",
        chapter: null,
      }));
    },
    addNewSquad() {
      this.tribe.addSquad(new Squad({
        name: "New Squad",
        color: "#ff00ff",
        members: [],
      }));
    },
  },
};
</script>

<style scoped lang="scss">
@import "src/assets/scss/colors";
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

  &__section {
    border-top: 1px solid $white-border;
    margin-top: 1rem;
    padding-top: 0.3rem;
  }
}

</style>
