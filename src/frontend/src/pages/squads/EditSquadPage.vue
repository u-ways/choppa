<template>
  <StandardPageTemplate>
    <template v-slot:page-header v-if="squad">
      <div class="flex flex-row place-content-between place-items-center">
        <div class="text-3xl font-normal truncate">
          <span class="hidden sm:inline">Squad </span>
          <span class="font-bold truncate">{{squadNameHeader}}</span>
        </div>
        <div class="flex-shrink-0">
          <StyledButton type="link" variant="secondary"
                        css="px-2 sm:pr-5 sm:pl-4"
                        :link="{ name: 'view-tribe', params: { id: squad.relations.tribeId } }">
            <div class="inline sm:pr-1">
              <font-awesome-icon icon="eye"/>
            </div>
            <div class="hidden sm:inline">View</div>
          </StyledButton>
        </div>
      </div>
    </template>
    <template v-slot:fixed-width v-if="squad">
      <div class="px-3 py-5">
        <section>
          <FormHeader>
            <template v-slot:heading>Squad Settings</template>
            <template v-slot:subheading>Let's get started by filling in the information about the Squad.</template>
          </FormHeader>
          <div class="flex flex-col gap-2 mt-4">
            <StandardInputWithLabel id="squad-name"
                                    label-text="Squad Name"
                                    place-holder="Untitled"
                                    v-bind:value="squad.name"
                                    v-on:input="squad.name = $event.target.value"
                                    :validator="$v.squad.name"
                                    :max-length="100"
            />
            <ColorPaletteWithLabel id="squad-color"
                                   label-text="Squad Color"
                                   :current-color="squad.color"
                                   @colorSelected="(newColor) => squad.color = newColor"
                                   :validator="$v.squad.color"
            />
            <div class="self-end flex flex-row gap-1">
              <StyledButton type="button" variant="secondary" css="px-2 pr-5 pl-4" @click="$router.go(-1)">
                <font-awesome-icon icon="times"/>
                Cancel
              </StyledButton>
              <StyledButton type="button" variant="primary" css="px-2 pr-5 pl-4" @click="save">
                <font-awesome-icon icon="check"/>
                <template v-if="creatingSquad">
                  Create
                </template>
                <template v-else>
                  Save
                </template>
              </StyledButton>
            </div>
          </div>
        </section>
        <section class="mt-5" v-if="!creatingSquad">
          <FormHeader>
            <template v-slot:heading>Members</template>
            <template v-slot:subheading>Now lets add some Members.</template>
          </FormHeader>
          <div class="pt-3">
            <div v-if="squad.members.length > 0" class="flex flex-col gap-2">
              <MembersOverview :members="squad.members"/>
              <div class="self-end">
                <StyledButton type="link"
                              :link="{ name: 'create-member', query: { squad: squad.path } }"
                              variant="secondary"
                              css="px-2 pr-5 pl-4">
                  <font-awesome-icon icon="plus"/>
                  New Member
                </StyledButton>
              </div>
            </div>
            <NoMembersToShowAlert v-else :squad="squad"/>
          </div>
        </section>
        <section class="mt-5" v-if="!creatingSquad && chapters">
          <FormHeader>
            <template v-slot:heading>Chapters</template>
            <template v-slot:subheading>Now lets add some Chapters.</template>
          </FormHeader>
          <div class="pt-3">
            <div v-if="chapters.length > 0" class="flex flex-col gap-2">
              <ChapterOverview :chapters="chapters"/>
              <div class="self-end">
                <StyledButton type="link"
                              :link="{ name: '404' }"
                              variant="secondary"
                              css="px-2 pr-5 pl-4">
                  <font-awesome-icon icon="plus"/>
                  New Chapter
                </StyledButton>
              </div>
            </div>
            <NoChaptersToShowAlert v-else/>
          </div>
        </section>
        <section class="mt-5" v-if="!creatingSquad && tribe && allSquadsExceptOneBeingEdited.length > 0">
          <FormHeader>
            <template v-slot:heading>Related Squads</template>
            <template v-slot:subheading>Squads belonging to Tribe {{tribe.name}}.</template>
          </FormHeader>
          <div class="pt-3">
            <SquadsOverview :squads="allSquadsExceptOneBeingEdited"/>
          </div>
        </section>
      </div>
    </template>
  </StandardPageTemplate>
</template>

<script>
import StandardPageTemplate from "@/components/templates/StandardPageTemplate";
import FormHeader from "@/components/forms/FormHeader";
import StandardInputWithLabel from "@/components/forms/groups/StandardInputWithLabel";
import StyledButton from "@/components/atoms/buttons/StyledButton";
import { createSquad, getSquad, saveSquad } from "@/config/api/squad.api";
import { maxLength, minLength, required } from "vuelidate/lib/validators";
import { mapActions } from "vuex";
import ToastData from "@/models/toastData";
import { toastVariants } from "@/enums/toastVariants";
import ColorPaletteWithLabel from "@/components/forms/groups/ColorPaletteWithLabel";
import { getTribe } from "@/config/api/tribe.api";
import SquadsOverview from "@/components/squads/SquadsOverview";
import NoMembersToShowAlert from "@/components/member/NoMembersToShowAlert";
import { getChaptersByQuery } from "@/config/api/chapter.api";
import NoChaptersToShowAlert from "@/components/chapters/NoChaptersToShowAlert";
import ChapterOverview from "@/components/chapters/ChapterOverview";
import MembersOverview from "@/components/member/MembersOverview";
import Squad from "@/models/domain/squad";

export default {
  name: "EditSquadPage",
  components: {
    MembersOverview,
    ChapterOverview,
    NoChaptersToShowAlert,
    NoMembersToShowAlert,
    SquadsOverview,
    ColorPaletteWithLabel,
    StandardPageTemplate,
    FormHeader,
    StandardInputWithLabel,
    StyledButton,
  },
  computed: {
    squadNameHeader() {
      return this.squad.name ? this.squad.name : "Untitled";
    },
    allSquadsExceptOneBeingEdited() {
      return this.tribe.squads.filter((otherSquad) => otherSquad.id !== this.squad.id);
    },
    saveOrCreateVerb() {
      return this.creatingSquad ? "created" : "updated";
    },
  },
  data() {
    return {
      creatingSquad: false,
      tribe: undefined,
      squad: undefined,
      chapters: undefined,
    };
  },
  validations: {
    squad: {
      name: {
        required,
        minLength: minLength(3),
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
        this.creatingSquad = true;
        this.squad = new Squad({ relations: { tribeId: this.$route.query.tribe } });
        this.tribe = await getTribe({ id: this.$route.query.tribe });
      } else if (this.$route.params.id) {
        this.creatingSquad = false;
        this.squad = await getSquad({ id: this.$route.params.id });
        this.tribe = await getTribe({ url: this.squad.relations.tribe });
        this.chapters = await getChaptersByQuery({ url: this.squad.relations.chapters });
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
        if (this.creatingSquad) {
          await createSquad({ squad: this.squad });
        } else {
          await saveSquad({ squad: this.squad });
        }

        await this.$router.go(-1);
        this.newToast(new ToastData({
          variant: toastVariants.SUCCESS,
          message: `Squad ${this.squad.name} has been ${this.saveOrCreateVerb}`,
        }));
      } catch (error) {
        this.newToast(new ToastData({
          variant: toastVariants.ERROR,
          message: `${this.saveOrCreateVerb} failed, please try again later`,
        }));

        throw error;
      }
    },
  },
};
</script>
