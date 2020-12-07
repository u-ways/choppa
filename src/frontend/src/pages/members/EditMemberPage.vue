<template>
  <StandardPageTemplate>
    <template v-slot:page-header v-if="isReady">
      <div class="text-3xl font-normal truncate">
        <span class="hidden sm:inline">Member </span>
        <span class="font-bold truncate">{{ memberNameHeader }}</span>
      </div>
    </template>
    <template v-slot:fixed-width v-if="isReady">
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
              <StandardLabel for-id="member-chapter" label-text="Member Chapter"/>
              <div class="mt-2 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-2">
                <div v-for="chapter in tribeChapters"
                     v-bind:key="chapter.id"
                     class="bg-white dark:bg-gray-700 shadow rounded-sm flex
                     flex-row items-center border-l-4 cursor-pointer hover:ring-2 ring-choppa-two"
                     :style="{ 'border-color': chapter.color }">
                  <input type="radio" name="member-chapter"
                         :value="chapter.id"
                         :id="`${chapter.id}`"
                         class="ml-4 flex-shrink-0 cursor-pointer"
                         :checked=" member.chapter && member.chapter.id === chapter.id ? 'checked' : ''"
                         @change="onChapterChanged">
                  <StandardLabel :label-text="chapter.name"
                                 :for-id="`${chapter.id}`"
                                 class="px-2 py-3 flex-grow cursor-pointer"
                  />
                </div>
              </div>
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
        <section class="mt-5" v-if="isReady">
          <FormHeader>
            <template v-slot:heading>Squads</template>
            <template v-slot:subheading>{{ memberNameHeader }} belongs to the following Squads.</template>
          </FormHeader>
          <div class="pt-3">
            <SquadsOverview :squads="involvedSquads"/>
          </div>
        </section>
      </div>
    </template>
  </StandardPageTemplate>
</template>

<script>
import StandardPageTemplate from "@/components/templates/StandardPageTemplate";
import { mapActions } from "vuex";
import { getMember, saveMember } from "@/config/api/member.api";
import { maxLength, minLength, required } from "vuelidate/lib/validators";
import FormHeader from "@/components/forms/FormHeader";
import StandardInputWithLabel from "@/components/forms/groups/StandardInputWithLabel";
import StyledButton from "@/components/atoms/buttons/StyledButton";
import ToastData from "@/models/toastData";
import { toastVariants } from "@/enums/toastVariants";
import StandardLabel from "@/components/forms/inputs/StandardLabel";
import { getSquad, getSquadsByQuery } from "@/config/api/squad.api";
import { getChaptersByQuery } from "@/config/api/chapter.api";
import { getTribe } from "@/config/api/tribe.api";
import SquadsOverview from "@/components/squads/SquadsOverview";

export default {
  name: "EditMemberPage",
  components: {
    SquadsOverview,
    StandardLabel,
    StyledButton,
    StandardInputWithLabel,
    FormHeader,
    StandardPageTemplate,
  },
  computed: {
    memberNameHeader() {
      return this.member.name ? this.member.name : "Untitled";
    },
    isReady() {
      return this.member && this.squad && this.tribe && this.tribeChapters;
    },
  },
  data() {
    return {
      member: undefined,
      squad: undefined,
      tribe: undefined,
      tribeChapters: undefined,
      involvedSquads: undefined,
    };
  },
  validations: {
    member: {
      name: {
        required,
        minLength: minLength(3),
        maxLength: maxLength(100),
      },
    },
  },
  async mounted() {
    try {
      // TODO BT: Needs reworking - making too many queries right now.
      this.member = await getMember({ id: this.$route.params.memberId });
      this.squad = await getSquad({ id: this.$route.params.squadId });
      this.involvedSquads = await getSquadsByQuery({ url: this.member.relations.squads });
      this.tribe = await getTribe({ url: this.squad.relations.tribe });
      this.tribeChapters = await getChaptersByQuery({ url: this.tribe.relations.chapters });
    } catch (error) {
      await this.$router.replace("/not-found");
    }
  },
  methods: {
    ...mapActions(["newToast"]),
    onChapterChanged(event) {
      const [selectedChapter] = this.tribeChapters.filter((chapter) => chapter.id === event.target.value);
      this.member.chapter = selectedChapter;
    },
    save() {
      if (this.$v.$invalid) {
        return;
      }

      try {
        saveMember({ member: this.member });
        this.$router.go(-1);
        this.newToast(new ToastData({
          variant: toastVariants.SUCCESS,
          message: `Member ${this.member.name} has been updated`,
        }));
      } catch (error) {
        this.newToast(new ToastData({ variant: toastVariants.ERROR, message: "Save failed, please try again later" }));
        throw error;
      }
    },
  },
};
</script>
