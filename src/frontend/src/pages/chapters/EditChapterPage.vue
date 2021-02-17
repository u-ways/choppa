<template>
  <StandardPageTemplate>
    <template v-slot:page-header v-if="chapter && members">
      <div class="text-3xl font-normal truncate">
        <span class="hidden sm:inline">Chapter </span>
        <span class="font-bold truncate">{{ chapterNameHeader }}</span>
      </div>
    </template>
    <template v-slot:fixed-width v-if="chapter">
      <div class="px-3 py-5">
        <section>
          <FormHeader>
            <template v-slot:heading>Chapter Settings</template>
            <template v-slot:subheading>Let's get started by filling in the information about the Chapter.</template>
          </FormHeader>
          <div class="flex flex-col gap-2 mt-4">
            <StandardInputWithLabel id="chapter-name"
                                    label-text="Chapter Name"
                                    place-holder="Untitled"
                                    v-bind:value="chapter.name"
                                    v-on:input="chapter.name = $event.target.value"
                                    :validator="$v.chapter.name"
                                    :max-length="100"
            />
            <ColorPaletteWithLabel id="chapter-color"
                                   label-text="Chapter Color"
                                   :current-color="chapter.color"
                                   @colorSelected="(newColor) => chapter.color = newColor"
                                   :validator="$v.chapter.color"
            />
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
        <section class="mt-5" v-if="members && members.length > 0">
          <FormHeader>
            <template v-slot:heading>Related Members</template>
            <template v-slot:subheading>These members belong to Chapter {{chapterNameHeader}}.</template>
          </FormHeader>
          <div class="pt-3">
            <MembersOverview :members="members"/>
          </div>
        </section>
        <div class="px-3 py-5" v-if="!creatingChapter">
          <section>
            <FormHeader>
              <template v-slot:heading>DANGER</template>
              <template v-slot:subheading>These features permanently affect this Chapter.</template>
            </FormHeader>
            <div class="flex flex-col gap-2 mt-4 text-center">
              <DoubleConfirmationButton
                :buttonMessage="deleteMessage"
                variant="danger"
                css="px-2 pr-5 pl-4"
                @next="deleteConfirmation = true"
                @click="deleteChapter" />
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
import { createChapter, deleteChapter, getChapter, saveChapter } from "@/config/api/chapter.api";
import FormHeader from "@/components/forms/FormHeader";
import StandardInputWithLabel from "@/components/forms/groups/StandardInputWithLabel";
import { maxLength, minLength, required } from "vuelidate/lib/validators";
import StyledButton from "@/components/atoms/buttons/StyledButton";
import ColorPaletteWithLabel from "@/components/forms/groups/ColorPaletteWithLabel";
import ToastData from "@/models/toastData";
import { toastVariants } from "@/enums/toastVariants";
import { getMembersByQuery } from "@/config/api/member.api";
import MembersOverview from "@/components/member/MembersOverview";
import Chapter from "@/models/domain/chapter";
import DoubleConfirmationButton from "@/components/atoms/buttons/DoubleConfirmationButton";

export default {
  name: "EditSquadPage",
  components: {
    MembersOverview,
    ColorPaletteWithLabel,
    StyledButton,
    StandardInputWithLabel,
    FormHeader,
    StandardPageTemplate,
    DoubleConfirmationButton,
  },
  computed: {
    chapterNameHeader() {
      return this.chapter.name ? this.chapter.name : "Untitled";
    },
    deleteMessage() {
      return this.deleteConfirmation ? "Confirm Deletion" : "Delete Chapter";
    },
    saveOrCreateVerb() {
      return this.creatingChapter ? "created" : "updated";
    },
  },
  data() {
    return {
      creatingChapter: false,
      chapter: undefined,
      members: undefined,
      deleteConfirmation: false,
    };
  },
  validations: {
    chapter: {
      name: {
        required,
        minLength: minLength(2),
        maxLength: maxLength(100),
      },
      color: {
        required,
      },
    },
  },
  async mounted() {
    try {
      if (this.$route.query.tribe) {
        this.creatingChapter = true;
        this.chapter = new Chapter({});
      } else if (this.$route.params.id) {
        this.creatingChapter = false;
        this.chapter = await getChapter({ id: this.$route.params.id });
        this.members = await getMembersByQuery({ url: this.chapter.relations.members });
      }
    } catch (error) {
      await this.$router.replace("/not-found");
    }
  },
  methods: {
    ...mapActions(["newToast"]),
    async save() {
      if (this.$v.$invalid) {
        return;
      }

      try {
        if (this.creatingChapter) {
          await createChapter({ chapter: this.chapter });
        } else {
          await saveChapter({ chapter: this.chapter });
        }

        await this.$router.go(-1);
        this.newToast(new ToastData({
          variant: toastVariants.SUCCESS,
          message: `Chapter ${this.chapter.name} has been ${this.saveOrCreateVerb}`,
        }));
      } catch (error) {
        this.newToast(new ToastData({
          variant: toastVariants.ERROR,
          message: `${this.saveOrCreateVerb} failed, please try again later`,
        }));

        throw error;
      }
    },
    async deleteChapter() {
      try {
        await deleteChapter({ chapter: this.chapter });
        await this.$router.go(-1);
        this.newToast(new ToastData({
          variant: toastVariants.SUCCESS,
          message: `Chapter ${this.chapter.name} has been deleted`,
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
