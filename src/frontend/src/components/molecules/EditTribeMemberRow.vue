<template>
  <div class="row align-items-center">
    <template v-if="isExpanded">
      <div class="col-11">
        <div class="card mb-3">
          <div class="card-body">
            <div class="form-group">
              <label :for="nameInputId">Name</label>
              <input class="form-control form-control-sm" :id="nameInputId" v-model="member.name">
            </div>
            <div class="form-group">
              <label :for="chapterSelectId">Chapter</label>
              <select class="form-control form-control-sm" :id="chapterSelectId" @change="onChapterSelectedChanged">
                <option :value="null">Select a Chapter...</option>
                <option v-for="chapter in possibleChapters"
                        :selected="member.chapter ? chapter.id === member.chapter.id : false"
                        :key="chapter.id"
                        :value="chapter.id">
                  {{chapter.name}}
                </option>
              </select>
            </div>
            <div class="form-group">
              <label :for="squadSelectId">Move to new Squad</label>
              <div class="input-group mb-3">
                <select class="form-control form-control-sm" :id="chapterSelectId" @change="onSquadMoveSelectedChanged">
                  <option v-for="squad in possibleSquads"
                          :key="squad.id"
                          :value="squad.id"
                          :selected="squad.id === currentSquad.id">
                    {{ squad.name }}
                  </option>
                </select>
                <div class="input-group-append">
                  <button class="btn btn-sm btn-outline-secondary"
                          type="button"
                          :disabled="!currentSelectedSquadToMoveTo"
                          @click="onSquadMovedClicked">
                    Move
                  </button>
                </div>
              </div>
            </div>
            <div class="form-group">
              <div class="btn-group float-right">
                <button type="button" class="btn btn-sm btn-outline-secondary" @click="collapse">
                  Finish Editing
                </button>
                <button type="button" class="btn btn-sm btn-outline-danger" @click="onDeleteMemberClicked">
                  Delete Member
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </template>
    <template v-else>
      <div class="col-10 pl-3">
        <div>
          <ChapterBadgeAtom :chapter="member.chapter" v-if="member.chapter" />
        </div>
        {{ member.name }}
      </div>
      <div class="col-2 p-0">
        <button type="button" class="btn btn-sm btn-outline-dark" @click="expand">
          <font-awesome-icon icon="cog"/>
        </button>
      </div>
    </template>
  </div>
</template>

<script>
import Member from "@/data/types/Member";
import ChapterBadgeAtom from "@/components/atoms/ChapterBadgeAtom";
import Squad from "@/data/types/Squad";

export default {
  name: "EditTribeMemberRow",
  components: {
    ChapterBadgeAtom,
  },
  props: {
    member: Member,
    possibleChapters: Array,
    possibleSquads: Array,
    currentSquad: Squad,
  },
  data() {
    return {
      isExpanded: false,
      currentSelectedSquadToMoveTo: undefined,
    };
  },
  computed: {
    nameInputId() {
      return `member-name-${this.member.id}`;
    },
    chapterSelectId() {
      return `member-chapter-${this.member.id}`;
    },
    squadSelectId() {
      return `member-squad-${this.member.id}`;
    },
    squadSelectHelpTextId() {
      return `member-squad-help-${this.member.id}`;
    },
    chapterSelectOptions() {
      const options = [];

      this.possibleChapters.forEach((chapter) => {
        options.push({ text: chapter.name, value: chapter.id });
      });

      return options;
    },
  },
  methods: {
    expand() {
      this.isExpanded = true;
      this.$emit("expanded", this);
    },
    collapse() {
      this.$emit("collapsed", this);
      this.silentCollapse();
    },
    silentCollapse() {
      this.isExpanded = false;
    },
    onChapterSelectedChanged(eventData) {
      if (eventData.target.value) {
        const selectedChapter = this.possibleChapters.filter((c) => c.id === Number(eventData.target.value))[0];
        if (selectedChapter) {
          this.member.chapter = selectedChapter;
        }
      }
    },
    onSquadMoveSelectedChanged(eventData) {
      if (eventData.target.value) {
        const selectedSquad = this.possibleSquads.filter((s) => s.id === Number(eventData.target.value))[0];
        if (selectedSquad && selectedSquad.id !== this.currentSquad.id) {
          this.currentSelectedSquadToMoveTo = selectedSquad;
        } else {
          this.currentSelectedSquadToMoveTo = null;
        }
      }
    },
    onSquadMovedClicked() {
      if (this.currentSelectedSquadToMoveTo) {
        this.currentSquad.removeMember(this.member);
        this.currentSelectedSquadToMoveTo.addMember(this.member);
      }
    },
    onDeleteMemberClicked() {
      this.currentSquad.removeMember(this.member);
    },
  },
};
</script>

<style lang="scss">
</style>
