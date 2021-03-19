<template>
  <StandardPageTemplate>
    <template v-slot:page-header v-if="tribe">
      <div class="flex flex-row place-content-between place-items-center">
        <div class="text-3xl font-normal truncate">
          <span class="hidden sm:inline">Tribe </span>
          <span class="font-bold truncate">{{tribeNameHeader}}</span>
        </div>
        <div class="flex-shrink-0">
          <StyledButton type="link" variant="secondary" css="px-2 sm:pr-5 sm:pl-4"
                        :link="{ name: 'view-tribe', params: { id: tribe.path } }">
            <div class="inline sm:pr-1">
              <font-awesome-icon icon="eye"/>
            </div>
            <div class="hidden sm:inline">View</div>
          </StyledButton>
        </div>
      </div>
    </template>
    <template v-slot:fixed-width v-if="tribe">
      <div class="px-3 py-5">
        <section>
          <FormHeader variant="primary">
            <template v-slot:heading>Tribe Settings</template>
            <template v-slot:subheading>Let's get started by filling in the information about the Tribe.</template>
          </FormHeader>
          <div class="flex flex-col gap-2 mt-4">
            <StandardInputWithLabel id="tribe-name"
                                    label-text="Tribe Name"
                                    place-holder="Untitled"
                                    v-bind:value="tribe.name"
                                    v-on:input="tribe.name = $event.target.value"
                                    :validator="$v.tribe.name"
                                    :max-length="100"
            />
            <ColorPaletteWithLabel id="tribe-color"
                                   label-text="Tribe Color"
                                   :current-color="tribe.color"
                                   @colorSelected="(newColor) => tribe.color = newColor"
                                   :validator="$v.tribe.color"
            />
            <div class="self-end flex flex-row gap-1">
              <StyledButton type="button" variant="secondary" css="px-2 pr-5 pl-4" @click="$router.go(-1)">
                <font-awesome-icon icon="times"/>
                Cancel
              </StyledButton>
              <StyledButton type="button" variant="primary" css="px-2 pr-5 pl-4" @click="save">
                <font-awesome-icon icon="check"/>
                <template v-if="creatingTribe">
                  Create
                </template>
                <template v-else>
                  Save
                </template>
              </StyledButton>
            </div>
          </div>
        </section>
        <section class="mt-5" v-if="!creatingTribe && tribe">
          <FormHeader variant="primary">
            <template v-slot:heading>Squads</template>
            <template v-slot:subheading>Now lets add some Squads.</template>
          </FormHeader>
          <div class="pt-3">
            <div v-if="tribe.squads.length > 0" class="flex flex-col gap-2">
              <SquadsOverview :squads="tribe.squads"/>
              <div class="self-end">
                <StyledButton type="link"
                              :link="{ name: 'create-squad', query: { tribe: tribe.path } }"
                              variant="secondary"
                              css="px-2 pr-5 pl-4">
                  <font-awesome-icon icon="plus"/>
                  New Squad
                </StyledButton>
              </div>
            </div>
            <NoSquadsToShowAlert v-else :tribe="tribe" />
          </div>
        </section>
        <section class="mt-5" v-if="!creatingTribe">
          <FormHeader variant="primary">
            <template v-slot:heading>Chapters</template>
            <template v-slot:subheading>Now lets add some Chapters.</template>
          </FormHeader>
          <div class="pt-3">
            <div v-if="allChapters && allChapters.length > 0" class="flex flex-col gap-2">
              <ChapterOverview :chapters="allChapters"/>
              <div class="self-end">
                <StyledButton type="link"
                              :link="{ name: 'create-chapter', query: { tribe: tribe.path } }"
                              variant="secondary"
                              css="px-2 pr-5 pl-4">
                  <font-awesome-icon icon="plus"/>
                  New Chapter
                </StyledButton>
              </div>
            </div>
            <NoChaptersToShowAlert v-else :tribe="tribe"/>
          </div>
        </section>
        <section class="mt-5" v-if="tribe.allDistinctMembers().length > 0 && chaptersInUse.length > 0">
          <FormHeader variant="primary">
            <template v-slot:heading>Rotation</template>
            <template v-slot:subheading>Is it time to rotate the tribe?</template>
          </FormHeader>
          <div class="pt-3">
            <div class="flex flex-col gap-2">
              <StandardInputWithLabel id="rotation-amount"
                                      label-text="Amount of Members to rotate"
                                      type="number"
                                      v-bind:value="rotation.amount"
                                      v-on:input="rotation.amount = $event.target.value"
                                      :validator="$v.rotation.amount"
                                      place-holder="Amount of Members to rotate"
              />
              <div>
                <StandardLabel for-id="rotation-chapter" label-text="Chapter To Rotate"/>
                <div class="mt-2 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-2">
                  <ChapterRadioButton v-for="(chapter, i) in chaptersInUse"
                                      v-bind:key="chapter.id"
                                      :chapter="chapter"
                                      :is-checked="(rotation.chapter && rotation.chapter.id === chapter.id)
                                      || (!rotation.chapter && i === 0)"
                                      input-name="member-chapter"
                                      @onChapterChanged="onChapterChanged"/>
                </div>
              </div>
              <div>
                <StandardLabel for-id="rotation-filter" label-text="Filter"/>
                <div class="grid grid-cols-1 sm:grid-cols-3 gap-2">
                  <StandardRadio inputName="rotation-filter"
                                 id="rotation-filter-distributed"
                                 label="Distributed"
                                 :value="filters.distributed"
                                 :checked="rotation.filter === filters.distributed"
                                 @onChanged="(event) => rotation.filter = event.target.value"/>
                  <StandardRadio inputName="rotation-filter"
                                 id="rotation-filter-oldest"
                                 label="Oldest"
                                 :value="filters.oldest"
                                 :checked="rotation.filter === filters.oldest"
                                 @onChanged="(event) => rotation.filter = event.target.value"/>
                  <StandardRadio inputName="rotation-filter"
                                 id="rotation-filter-random"
                                 label="Random"
                                 :value="filters.random"
                                 :checked="rotation.filter === filters.random"
                                 @onChanged="(event) => rotation.filter = event.target.value"/>
                </div>
              </div>
              <div>
                <StandardLabel for-id="rotation-strategy" label-text="Strategy"/>
                <div class="grid grid-cols-1 sm:grid-cols-3 gap-2">
                  <StandardRadio inputName="rotation-strategy"
                                 id="rotation-strategy-clockwise"
                                 label="Clockwise"
                                 :value="strategies.clockwise"
                                 :checked="rotation.strategy === strategies.clockwise"
                                 @onChanged="(event) => rotation.strategy = event.target.value"/>
                  <StandardRadio inputName="rotation-strategy"
                                 id="rotation-strategy-anti-clockwise"
                                 label="Anti Clockwise"
                                 :value="strategies.antiClockwise"
                                 :checked="rotation.strategy === strategies.antiClockwise"
                                 @onChanged="(event) => rotation.strategy = event.target.value"/>
                  <StandardRadio inputName="rotation-strategy"
                                 id="rotation-strategy-random"
                                 label="Random"
                                 :value="strategies.random"
                                 :checked="rotation.strategy === strategies.random"
                                 @onChanged="(event) => rotation.strategy = event.target.value"/>
                </div>
              </div>
              <div class="self-end">
                <StyledButton type="button" @click="rotate" variant="primary" css="px-2 pr-5 pl-4">
                  <font-awesome-icon icon="sync"/>
                  Rotate Now
                </StyledButton>
              </div>
            </div>
          </div>
        </section>
      </div>
      <div class="px-3 py-5" v-if="!creatingTribe">
        <section>
          <FormHeader variant="danger">
            <template v-slot:heading>DANGER</template>
            <template v-slot:subheading>These features permanently affect this Tribe.</template>
          </FormHeader>
          <div class="flex flex-col gap-2 mt-4 text-center">
            <DoubleConfirmationButton
              buttonMessageOne="Delete Tribe"
              buttonMessageTwo="Confirm Deletion"
              variant="danger"
              css="px-2 pr-5 pl-4"
              @click="deleteTribe" />
          </div>
        </section>
      </div>
    </template>
  </StandardPageTemplate>
</template>

<script>
import StandardPageTemplate from "@/components/templates/StandardPageTemplate";
import StyledButton from "@/components/atoms/buttons/StyledButton";
import FormHeader from "@/components/forms/FormHeader";
import StandardInputWithLabel from "@/components/forms/groups/StandardInputWithLabel";
import { getTribe, rotateTribe, saveTribe, deleteTribe, createTribe } from "@/config/api/tribe.api";
import NoSquadsToShowAlert from "@/components/squads/NoSquadsToShowAlert";
import { required, minLength, maxLength, minValue, maxValue } from "vuelidate/lib/validators";
import { mapActions } from "vuex";
import { toastVariants } from "@/enums/toastVariants";
import ToastData from "@/models/toastData";
import SquadsOverview from "@/components/squads/SquadsOverview";
import { getAllChapters, getChaptersByQuery } from "@/config/api/chapter.api";
import ChapterRadioButton from "@/components/chapters/ChapterRadioButton";
import StandardLabel from "@/components/forms/inputs/StandardLabel";
import StandardRadio from "@/components/forms/inputs/StandardRadio";
import { rotationFilter } from "@/enums/rotationFilter";
import { rotationStrategy } from "@/enums/rotationStrategy";
import ChapterOverview from "@/components/chapters/ChapterOverview";
import NoChaptersToShowAlert from "@/components/chapters/NoChaptersToShowAlert";
import DoubleConfirmationButton from "@/components/atoms/buttons/DoubleConfirmationButton";
import Tribe from "@/models/domain/tribe";
import ColorPaletteWithLabel from "@/components/forms/groups/ColorPaletteWithLabel";

export default {
  name: "EditTribePage",
  components: {
    NoChaptersToShowAlert,
    ChapterOverview,
    StandardRadio,
    StandardLabel,
    ChapterRadioButton,
    SquadsOverview,
    NoSquadsToShowAlert,
    StandardInputWithLabel,
    FormHeader,
    StandardPageTemplate,
    StyledButton,
    DoubleConfirmationButton,
    ColorPaletteWithLabel,
  },
  computed: {
    tribeNameHeader() {
      return this.tribe.name ? this.tribe.name : "Untitled";
    },
    saveOrCreateVerb() {
      return this.creatingTribe ? "created" : "updated";
    },
  },
  data() {
    return {
      creatingTribe: false,
      tribe: undefined,
      allChapters: [],
      chaptersInUse: [],
      rotation: {
        amount: 1,
        chapter: undefined,
        filter: rotationFilter.DISTRIBUTED,
        strategy: rotationStrategy.CLOCKWISE,
      },
      filters: {
        distributed: rotationFilter.DISTRIBUTED,
        random: rotationFilter.RANDOM,
        oldest: rotationFilter.OLDEST,
      },
      strategies: {
        clockwise: rotationStrategy.CLOCKWISE,
        antiClockwise: rotationStrategy.ANTI_CLOCKWISE,
        random: rotationStrategy.RANDOM,
      },
    };
  },
  validations: {
    tribe: {
      name: {
        required,
        minLength: minLength(3),
        maxLength: maxLength(100),
      },
      color: {
        required,
      },
    },
    rotation: {
      amount: {
        required,
        minValue: minValue(1),
        maxValue: maxValue(20),
      },
    },
  },
  async mounted() {
    try {
      if (this.$route.params.id) {
        this.creatingTribe = false;
        this.tribe = await getTribe({ id: this.$route.params.id });
        this.allChapters = await getAllChapters();
        this.chaptersInUse = await getChaptersByQuery({ url: this.tribe.relations.chapters });
      } else {
        this.creatingTribe = true;
        this.tribe = new Tribe({ });
        this.allChapters = await getAllChapters();
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
        if (this.creatingTribe) {
          await createTribe({ tribe: this.tribe });
        } else {
          await saveTribe({ tribe: this.tribe });
        }
        await this.$router.go(-1);
        this.newToast(new ToastData({
          variant: toastVariants.SUCCESS,
          message: `Tribe ${this.tribe.name} has been ${this.saveOrCreateVerb}`,
        }));
      } catch (error) {
        this.newToast(new ToastData({ variant: toastVariants.ERROR, message: "Save failed, please try again later" }));
        throw error;
      }
    },
    onChapterChanged(event) {
      const [selectedChapter] = this.chaptersInUse.filter((chapter) => chapter.id === event.target.value);
      this.rotation.chapter = selectedChapter;
    },
    async rotate() {
      try {
        await rotateTribe({ tribe: this.tribe, ...this.rotation });
        this.$router.go(-1);
        this.newToast(new ToastData({
          variant: toastVariants.SUCCESS,
          message: `Tribe ${this.tribe.name} has been rotated`,
        }));
      } catch (error) {
        this.newToast(new ToastData({
          variant: toastVariants.ERROR,
          message: "Rotation failed, please try again later",
        }));

        throw error;
      }
    },
    async deleteTribe() {
      try {
        await deleteTribe({ tribe: this.tribe });
        await this.$router.push({ name: "dashboard" });
        this.newToast(new ToastData({
          variant: toastVariants.SUCCESS,
          message: `Tribe ${this.tribe.name} has been deleted`,
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
