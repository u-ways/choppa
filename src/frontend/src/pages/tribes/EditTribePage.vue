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
                        :link="`/${tribe.id}`">
            <div class="inline sm:pr-1">
              <font-awesome-icon icon="eye"/>
            </div>
            <div class="hidden sm:inline">View</div>
          </StyledButton>
        </div>
      </div>
    </template>
    <template v-slot:fixed-width v-if="tribe">
      <div class="px-3 py-5"> <!-- my-5 bg-white rounded-md shadow-md -->
        <section>
          <FormHeader>
            <template v-slot:heading>Tribe Settings</template>
            <template v-slot:subheading>Let's get started by filling in the information about the Tribe.</template>
          </FormHeader>
          <div class="pl-4 flex flex-col gap-2 mt-4">
            <StandardInputWithLabel id="tribe-name"
                                    label-text="Tribe Name"
                                    place-holder="Untitled"
                                    v-bind:value="tribe.name"
                                    v-on:input="tribe.name = $event.target.value"
                                    :validator="$v.tribe.name"
                                    :max-length="100"
            />
            <div class="self-end">
              <StyledButton type="button" variant="primary" css="px-2 pr-5 pl-4" @click="save">
                <font-awesome-icon icon="check"/>
                Save
              </StyledButton>
            </div>
          </div>
        </section>
        <section class="mt-5">
          <FormHeader>
            <template v-slot:heading>Squad Settings</template>
            <template v-slot:subheading>Now lets add some Squads.</template>
          </FormHeader>
          <div class="pl-4 pt-3">
            <div v-if="tribe.squads.length > 0" class="flex flex-col gap-2">
              <div class="grid grid-cols-1 md:grid-cols-2 gap-2">
                <router-link v-for="squad in tribe.squads" v-bind:key="squad.id" :to="`/${squad.id}`"
                             class="hover:ring-2 focus:ring-2 focus:outline-none ring-choppa-two rounded-sm">
                  <SquadCard :squad="squad"
                             :show-header-only="true"
                             :member-show-count="6"
                             :headerLinkLocation="squad.id"
                  />
                </router-link>
              </div>
              <div class="self-end">
                <StyledButton type="button" variant="secondary" css="px-2 pr-5 pl-4">
                  <font-awesome-icon icon="plus"/>
                  New Squad
                </StyledButton>
              </div>
            </div>
            <NoSquadsToShowAlert v-else />
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
import StandardInputWithLabel from "@/components/forms/StandardInputWithLabel";
import SquadCard from "@/components/squads/SquadCard";
import { getTribe, saveTribe } from "@/config/api/tribe.api";
import NoSquadsToShowAlert from "@/components/tribes/NoSquadsToShowAlert";
import { required, minLength, maxLength } from "vuelidate/lib/validators";
import { mapActions } from "vuex";
import { toastVariants } from "@/enums/toastVariants";
import ToastData from "@/models/toastData";

export default {
  name: "ViewTribePage",
  components: {
    NoSquadsToShowAlert,
    SquadCard,
    StandardInputWithLabel,
    FormHeader,
    StandardPageTemplate,
    StyledButton,
  },
  data() {
    return {
      tribe: undefined,
    };
  },
  validations: {
    tribe: {
      name: {
        required,
        minLength: minLength(3),
        maxLength: maxLength(100),
      },
    },
  },
  async mounted() {
    try {
      this.tribe = await getTribe({ id: this.$route.params.id });
    } catch (error) {
      await this.$router.replace("/not-found");
    }
  },
  computed: {
    tribeNameHeader() {
      return this.tribe.name ? this.tribe.name : "Untitled";
    },
  },
  methods: {
    ...mapActions(["newToast"]),
    save() {
      if (this.$v.$invalid) {
        return;
      }

      try {
        saveTribe({ tribe: this.tribe });
        this.$router.replace(`/${this.tribe.id}`);
        this.newToast(new ToastData({
          variant: toastVariants.SUCCESS,
          message: `Tribe ${this.tribe.name} has been updated`,
        }));
      } catch (error) {
        this.newToast(new ToastData({ variant: toastVariants.ERROR, message: "Save failed, please try again later" }));
        throw error;
      }
    },
  },
};
</script>
