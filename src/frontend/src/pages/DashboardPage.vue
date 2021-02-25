<template>
  <div>
    <StandardPageTemplate>
      <template v-slot:page-header>
        <div class="text-3xl font-bold">
          Dashboard
        </div>
      </template>
      <template v-slot:full-width>
        <div class="py-3">
          <div class="px-3 grid grid-cols-2 md:grid-cols-4 gap-2">
            <DashboardCard title="Total Tribes" :is-loaded="totalTribeCount.length > 0">
              <div class="text-center w-full font-bold text-5xl md:text-7xl">{{totalTribeCount}}</div>
            </DashboardCard>
            <DashboardCard title="Total Squads" :is-loaded="totalSquadCount.length > 0">
              <div class="text-center w-full font-bold text-5xl md:text-7xl">{{totalSquadCount}}</div>
            </DashboardCard>
            <DashboardCard title="Total Chapters" :is-loaded="totalChapterCount.length > 0">
              <div class="text-center w-full font-bold text-5xl md:text-7xl">{{totalChapterCount}}</div>
            </DashboardCard>
            <DashboardCard title="Active Members"
                           :is-loaded="totalMemberCount.length > 0 && activeMemberCount.length > 0">
              <div class="text-center w-full font-bold text-5xl md:text-7xl">{{activeMemberCount}}</div>
              <div class="text-center w-full font-normal text-sm">{{ totalMemberCount }} Total Members</div>
            </DashboardCard>
          </div>
          <div class="pt-2 px-3 grid grid-cols-1 gap-2">
            <DashboardCard title="KSP Balance - Not Real KSP values"
                         :is-loaded="tribeKSPStatsLoaded"
                         :no-data="tribeKSPStats.length === 0">
            <KSPLineChart :tribeKspStats="tribeKSPStats[0]" class="max-h-72"/>
            </DashboardCard>
          </div>
          <div class="pt-2 px-3 grid grid-cols-1 md:grid-cols-2 gap-2">
            <DashboardCard title="Chapter Distribution"
                           :is-loaded="chapterStatsLoaded"
                           :no-data="chapterStats.length === 0">
              <ChapterPieChart :chapter-stats="chapterStats" class="max-h-72"/>
            </DashboardCard>
            <DashboardCard title="Latest Squad Changes"
                           :is-loaded="squadLatestChangesLoaded"
                           :no-data="squadLatestChanges.length === 0">
              <LatestChangesTable :latest-changes="squadLatestChanges" class="max-h-72"/>
            </DashboardCard>
            <DashboardCard title="Member Overview" :no-data="true" :is-loaded="true">
            </DashboardCard>
          </div>
        </div>
      </template>
    </StandardPageTemplate>
  </div>
</template>

<script>
import StandardPageTemplate from "@/components/templates/StandardPageTemplate";
import DashboardCard from "@/components/dashboard/DashboardCard";
import ChapterPieChart from "@/components/dashboard/graphs/ChapterPieChart";
import {
  chapterDistribution,
  memberStats,
  squadLatestChanges,
  tribeKnowledgeSharingPoints,
} from "@/config/api/stats.api";
import ToastData from "@/models/toastData";
import { toastVariants } from "@/enums/toastVariants";
import { mapActions } from "vuex";
import KSPLineChart from "@/components/dashboard/graphs/KSPLineChart";
import LatestChangesTable from "@/components/dashboard/graphs/LatestChangesTable";
import { getAllTribes } from "@/config/api/tribe.api";
import { getAllSquads } from "@/config/api/squad.api";
import { getAllChapters } from "@/config/api/chapter.api";

export default {
  name: "Dashboard",
  components: {
    LatestChangesTable,
    KSPLineChart,
    ChapterPieChart,
    DashboardCard,
    StandardPageTemplate,
  },
  data() {
    return {
      chapterStatsLoaded: false,
      chapterStats: [],
      tribeKSPStatsLoaded: false,
      tribeKSPStats: [],
      squadLatestChangesLoaded: false,
      squadLatestChanges: [],
      totalTribeCount: "",
      totalSquadCount: "",
      totalChapterCount: "",
      totalMemberCount: "",
      activeMemberCount: "",
    };
  },
  async mounted() {
    chapterDistribution().then((result) => {
      this.chapterStats = result;
      this.chapterStatsLoaded = true;
    }).catch((error) => {
      this.handleError(error);
    });

    tribeKnowledgeSharingPoints().then((result) => {
      this.tribeKSPStats = result;
      this.tribeKSPStatsLoaded = true;
    }).catch((error) => {
      this.handleError(error);
    });

    squadLatestChanges().then((result) => {
      this.squadLatestChanges = result;
      this.squadLatestChangesLoaded = true;
    }).catch((error) => {
      this.handleError(error);
    });

    getAllTribes().then((result) => {
      this.totalTribeCount = `${result.length}`;
    }).catch(() => {
      this.totalTribeCount = "0";
    });

    getAllSquads().then((result) => {
      this.totalSquadCount = `${result.length}`;
    }).catch(() => {
      this.totalSquadCount = "0";
    });

    getAllChapters().then((result) => {
      this.totalChapterCount = `${result.length}`;
    }).catch(() => {
      this.totalChapterCount = "0";
    });

    memberStats().then((result) => {
      this.totalMemberCount = `${result.total}`;
      this.activeMemberCount = `${Math.round(result.total * result.distribution.active)}`;
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
