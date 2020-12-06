<template>
  <StandardPageTemplate>
    <template v-slot:page-header v-if="squad">
      <div class="flex flex-row place-content-between place-items-center">
        <div class="text-3xl font-normal truncate">
          <span class="hidden sm:inline">Squad </span>
          <span class="font-bold truncate">{{squadNameHeader}}</span>
        </div>
        <div class="flex-shrink-0">
          <StyledButton type="link" variant="secondary" css="px-2 sm:pr-5 sm:pl-4"
                        :link="`/${squad.tribeId}`">
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
                                   @colorSelected="onColorChanged"
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
        <section class="mt-5">
          <FormHeader>
            <template v-slot:heading>Members</template>
            <template v-slot:subheading>Now lets add some Members.</template>
          </FormHeader>
        </section>
        <section class="mt-5" v-if="squad && tribe && allSquadsExceptOneBeingEdited.length > 0">
          <FormHeader>
            <template v-slot:heading>Other Squads</template>
            <template v-slot:subheading>Other Squads that belong to the same tribe.</template>
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
import { getSquad, saveSquad } from "@/config/api/squad.api";
import { maxLength, minLength, required } from "vuelidate/lib/validators";
import { mapActions } from "vuex";
import ToastData from "@/models/toastData";
import { toastVariants } from "@/enums/toastVariants";
import ColorPaletteWithLabel from "@/components/forms/groups/ColorPaletteWithLabel";
import { getTribe } from "@/config/api/tribe.api";
import SquadsOverview from "@/components/squads/SquadsOverview";

export default {
  name: "EditSquadPage",
  components: {
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
  },
  data() {
    return {
      tribe: undefined,
      squad: undefined,
    };
  },
  validations: {
    squad: {
      name: {
        required,
        minLength: minLength(3),
        maxLength: maxLength(100),
      },
    },
  },
  async mounted() {
    try {
      this.squad = await getSquad({ id: this.$route.params.id });
      this.tribe = await getTribe({ url: this.squad.tribeId });
    } catch (error) {
      await this.$router.replace("/not-found");
    }
  },
  methods: {
    ...mapActions(["newToast"]),
    onColorChanged(newColor) {
      this.squad.color = newColor;
    },
    save() {
      if (this.$v.$invalid) {
        return;
      }

      try {
        saveSquad({ squad: this.squad });
        this.$router.go(-1);
        this.newToast(new ToastData({
          variant: toastVariants.SUCCESS,
          message: `Squad ${this.squad.name} has been updated`,
        }));
      } catch (error) {
        this.newToast(new ToastData({ variant: toastVariants.ERROR, message: "Save failed, please try again later" }));
        throw error;
      }
    },
  },
};
</script>
