<template>
  <div>
    <StandardPageTemplate>
      <template v-slot:page-header>
        <div class="text-3xl font-bold">
          Dashboard
        </div>
      </template>
      <template v-slot:full-width>
        <div class="p-3 grid grid-cols-1 md:grid-cols-2 gap-2">
          <DashboardCard title="Chapters" :is-loaded="chapterStatsLoaded">
            <ChapterPieChart :chapter-stats="chapterStats"/>
          </DashboardCard>
          <DashboardCard title="KSP Balance">
            test 2
          </DashboardCard>
          <DashboardCard title="Latest Changes">
            test 3
          </DashboardCard>
          <DashboardCard title="Members">
            test 4
          </DashboardCard>
        </div>
      </template>
    </StandardPageTemplate>
  </div>
</template>

<script>
import StandardPageTemplate from "@/components/templates/StandardPageTemplate";
import DashboardCard from "@/components/dashboard/DashboardCard";
import ChapterPieChart from "@/components/dashboard/graphs/ChapterPieChart";
import { chapterDistribution } from "@/config/api/stats.api";
import ToastData from "@/models/toastData";
import { toastVariants } from "@/enums/toastVariants";
import { mapActions } from "vuex";

export default {
  name: "Dashboard",
  components: {
    ChapterPieChart,
    DashboardCard,
    StandardPageTemplate,
  },
  data() {
    return {
      chapterStatsLoaded: false,
      chapterStats: [],
    };
  },
  async mounted() {
    chapterDistribution().then((result) => {
      this.chapterStats = result;
      this.chapterStatsLoaded = true;
    }).catch((error) => {
      this.handleError(error);
    });
  },
  methods: {
    ...mapActions(["newToast"]),
    handleError(error) {
      this.newToast(new ToastData({
        variant: toastVariants.ERROR,
        message: "The Dashboard can not be loaded at this moment, please try again later.",
      }));

      throw error;
    },
  },
};
</script>
