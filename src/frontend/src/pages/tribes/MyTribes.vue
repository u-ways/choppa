<template>
  <div>
    <StandardPageTemplate>
      <template v-slot:page-header>
        <div class="text-3xl font-bold">
          My Tribes
        </div>
      </template>
      <template v-slot:full-width>
        <div class="mx-3 my-5">
          <div class="grid grid-cols-1 gap-3" v-if="isLoaded">
            <template v-if="tribes.length > 0">
              <router-link :to="{ name: 'view-tribe', params: { id: tribe.path } }"
                           v-for="tribe in tribes" :key="tribe.id"
                           class="hover:ring-2 focus:ring-2 focus:outline-none ring-choppa-two rounded-sm">
                <TribeCard :tribe="tribe" />
              </router-link>
            </template>
            <template v-else>
              <NoTribesToShowAlert/>
            </template>
          </div>
          <div class="grid grid-cols-1 gap-3" v-else>
            <TribeSkeleton/>
            <TribeSkeleton/>
            <TribeSkeleton class="hidden md:block"/>
          </div>
        </div>
        <div class="self-end" v-if="tribes.length > 0">
          <StyledButton type="link"
                        :link="{ name: 'create-tribe'}"
                        variant="secondary"
                        css="px-2 pr-5 pl-4">
            <font-awesome-icon icon="plus"/>
            New Tribe
          </StyledButton>
        </div>
      </template>
    </StandardPageTemplate>
  </div>
</template>

<script>
import StandardPageTemplate from "@/components/templates/StandardPageTemplate";
import { getAllTribes } from "@/config/api/tribe.api";
import { mapActions } from "vuex";
import ToastData from "@/models/toastData";
import { toastVariants } from "@/enums/toastVariants";
import TribeCard from "@/components/tribes/TribeCard";
import TribeSkeleton from "@/components/tribes/TribeSkeleton";
import StyledButton from "@/components/atoms/buttons/StyledButton";
import NoTribesToShowAlert from "@/components/tribes/NoTribesToShowAlert";

export default {
  name: "MyTribes",
  components: {
    NoTribesToShowAlert,
    TribeSkeleton,
    TribeCard,
    StandardPageTemplate,
    StyledButton,
  },
  data() {
    return {
      isLoaded: false,
      tribes: [],
    };
  },
  async mounted() {
    try {
      this.tribes = await getAllTribes();
      this.isLoaded = true;
    } catch (error) {
      if (error.response.status === 404) {
        this.isLoaded = true;
      } else {
        this.newToast(new ToastData({
          variant: toastVariants.ERROR,
          message: `Failed to load Dashboard, please try again later.`,
        }));
        throw error;
      }
    }
  },
  methods: {
    ...mapActions(["newToast"]),
  },
};
</script>
