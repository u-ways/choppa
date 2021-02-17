<template>
  <StandardPageTemplate>
    <template v-slot:page-header v-if="member && chapters">
      <div class="text-3xl font-normal truncate">
        <span class="hidden sm:inline">Member </span>
        <span class="font-bold truncate">{{ memberNameHeader }}</span>
      </div>
    </template>
    <template v-slot:fixed-width v-if="member && chapters">
      <div class="px-3 py-5">
        <section>
          <FormHeader>
            <template v-slot:heading>Member Settings</template>
            <template v-slot:subheading>Let's get started by filling in the information about the Member.</template>
          </FormHeader>
          <div class="flex flex-col gap-2 mt-4">
            <StandardInputWithLabel id="member-name"
                                    label-text="Member Name"
                                    place-holder="Untitled"
                                    v-bind:value="member.name"
                                    v-on:input="member.name = $event.target.value"
                                    :validator="$v.member.name"
                                    :max-length="100"
            />
            <div>
              <StandardLabel for-id="member-chapter" label-text="Member Chapter" :validator="$v.member.chapter"/>
              <div class="mt-2 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-2">
                <ChapterRadioButton v-for="chapter in chapters"
                                    v-bind:key="chapter.id"
                                    :chapter="chapter"
                                    :selected-chapter="member.chapter"
                                    input-name="member-chapter"
                                    @onChapterChanged="onChapterChanged"/>
              </div>
              <ErrorPrompt label-text="Member Chapter" :validator="$v.member.chapter"/>
            </div>
            <div class="self-end flex flex-row gap-1">
              <StyledButton type="button" variant="secondary" css="px-2 pr-5 pl-4" @click="$router.go(-1)">
                <font-awesome-icon icon="times"/>
                Cancel
              </StyledButton>
              <StyledButton type="button" variant="primary" css="px-2 pr-5 pl-4" @click="save">
                <font-awesome-icon icon="check"/>
                Save
              </StyledButton>
            </div>
          </div>
        </section>
        <section class="mt-5" v-if="!creatingMember && squads">
          <FormHeader>
            <template v-slot:heading>Squads</template>
            <template v-slot:subheading>{{member.name}} belongs to the following Squads.</template>
          </FormHeader>
          <div class="pt-3">
            <SquadsOverview :squads="squads"/>
          </div>
        </section>
        <div class="px-3 py-5" v-if="!creatingMember">
          <section>
            <FormHeader>
              <template v-slot:heading>DANGER</template>
              <template v-slot:subheading>These features permanently affect this Member.</template>
            </FormHeader>
            <div class="flex flex-col gap-2 mt-4 text-center">
              <DoubleConfirmationButton
                buttonMessageOne="Delete Member"
                buttonMessageTwo="Confirm Deletion"
                variant="danger"
                css="px-2 pr-5 pl-4"
                @click="deleteMember" />
            </div>
          </section>
        </div>
      </div>
    </template>
  </StandardPageTemplate>
</template>

<script>
import StandardPageTemplate from "@/components/templates/StandardPageTemplate";
import { mapActions } from "vuex";
import { createMember, getMember, saveMember, deleteMember } from "@/config/api/member.api";
import { maxLength, minLength, required } from "vuelidate/lib/validators";
import FormHeader from "@/components/forms/FormHeader";
import StandardInputWithLabel from "@/components/forms/groups/StandardInputWithLabel";
import StyledButton from "@/components/atoms/buttons/StyledButton";
import ToastData from "@/models/toastData";
import { toastVariants } from "@/enums/toastVariants";
import StandardLabel from "@/components/forms/inputs/StandardLabel";
import SquadsOverview from "@/components/squads/SquadsOverview";
import { getChapters } from "@/config/api/chapter.api";
import { getSquad, getSquadsByQuery, saveSquad } from "@/config/api/squad.api";
import ChapterRadioButton from "@/components/chapters/ChapterRadioButton";
import Member from "@/models/domain/member";
import ErrorPrompt from "@/components/forms/ErrorPrompt";
import DoubleConfirmationButton from "@/components/atoms/buttons/DoubleConfirmationButton";

export default {
  name: "EditMemberPage",
  components: {
    ErrorPrompt,
    ChapterRadioButton,
    SquadsOverview,
    StandardLabel,
    StyledButton,
    StandardInputWithLabel,
    FormHeader,
    StandardPageTemplate,
    DoubleConfirmationButton,
  },
  computed: {
    memberNameHeader() {
      return this.member.name ? this.member.name : "Untitled";
    },
    saveOrCreateVerb() {
      return this.creatingMember ? "created" : "updated";
    },
  },
  data() {
    return {
      creatingMember: false,
      member: undefined,
      chapters: undefined,
      squads: undefined,
      selectedSquad: undefined,
      deleteConfirmation: false,
    };
  },
  validations: {
    member: {
      name: {
        required,
        minLength: minLength(3),
        maxLength: maxLength(100),
      },
      chapter: {
        required,
      },
    },
  },
  async mounted() {
    try {
      if (this.$route.query.squad) {
        this.creatingMember = true;
        this.member = new Member({});
        this.chapters = await getChapters();
        this.selectedSquad = await getSquad({ id: this.$route.query.squad });
      } else if (this.$route.params.id) {
        this.creatingMember = false;
        this.member = await getMember({ id: this.$route.params.id });
        this.chapters = await getChapters();
        this.squads = await getSquadsByQuery({ url: this.member.relations.squads });
      }
    } catch (error) {
      await this.$router.replace("/not-found");
    }
  },
  methods: {
    ...mapActions(["newToast"]),
    onChapterChanged(event) {
      const [selectedChapter] = this.chapters.filter((chapter) => chapter.id === event.target.value);
      this.member.chapter = selectedChapter;
    },
    async save() {
      if (this.$v.$invalid) {
        return;
      }

      try {
        if (this.creatingMember) {
          await createMember({ member: this.member });
          this.selectedSquad.members.push(this.member);
          await saveSquad({ squad: this.selectedSquad });
        } else {
          await saveMember({ member: this.member });
        }

        await this.$router.go(-1);
        this.newToast(new ToastData({
          variant: toastVariants.SUCCESS,
          message: `Member ${this.member.name} has been ${this.saveOrCreateVerb}`,
        }));
      } catch (error) {
        this.newToast(new ToastData({
          variant: toastVariants.ERROR,
          message: `${this.saveOrCreateVerb} failed, please try again later`,
        }));

        throw error;
      }
    },
    async deleteMember() {
      try {
        await deleteMember({ member: this.member });
        await this.$router.go(-1);
        this.newToast(new ToastData({
          variant: toastVariants.SUCCESS,
          message: `Member ${this.member.name} has been deleted`,
        }));
      } catch (error) {
        this.newToast(new ToastData({
          variant: toastVariants.ERROR,
          message: "Deletion failed, please try again later",
        }));

        throw error;
      }
    },
  },
};
</script>
