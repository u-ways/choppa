<template>
  <div>
    <StandardPageTemplate>
      <template v-slot:page-header>
        <div class="text-3xl font-bold">
          Dashboard
        </div>
      </template>
      <template v-slot:full-width>
        <div class="mx-3 my-5">
          <div class="grid grid-cols-1 gap-3" v-if="isLoaded">
            <router-link :to="{ name: 'view-tribe', params: { id: tribe.path } }"
                         v-for="tribe in tribes" :key="tribe.id"
                         class="hover:ring-2 focus:ring-2 focus:outline-none ring-choppa-two rounded-sm">
              <TribeCard :tribe="tribe" />
            </router-link>
          </div>
          <div class="grid grid-cols-1 gap-3" v-else>
            <TribeSkeleton/>
            <TribeSkeleton/>
            <TribeSkeleton class="hidden md:block"/>
          </div>
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

export default {
  name: "Dashboard",
  components: {
    TribeSkeleton,
    TribeCard,
    StandardPageTemplate,
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
      this.newToast(new ToastData({
        variant: toastVariants.ERROR,
        message: `Failed to load Dashboard, please try again later.`,
      }));

      throw error;
    }
  },
  methods: {
    ...mapActions(["newToast"]),
  },
};
</script>
