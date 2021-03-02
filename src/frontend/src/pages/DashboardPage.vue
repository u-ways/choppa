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
            <DashboardCard title="Total Tribes" :is-loaded="loadedAllTribes">
              <div class="text-center w-full font-bold text-5xl md:text-7xl">{{allTribes.length}}</div>
            </DashboardCard>
            <DashboardCard title="Total Squads" :is-loaded="loadedAllSquads">
              <div class="text-center w-full font-bold text-5xl md:text-7xl">{{allSquads.length}}</div>
            </DashboardCard>
            <DashboardCard title="Total Chapters" :is-loaded="loadedAllChapters">
              <div class="text-center w-full font-bold text-5xl md:text-7xl">{{allChapters.length}}</div>
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
                           :no-data="tribeKSPStats.length === 0"
                           class="min-h-72">
              <DashboardTab v-for="(stats, index) in tribeKSPStats"
                            :key="index"
                            :tab-name="stats.tribe.name"
                            :border-color="stats.tribe.color"
                            @onEnabled="swapKspStatsData(index)"/>
              <KSPLineChart class="max-h-72" ref="kspChart" :starting-data="tribeKSPStats[0]"/>
            </DashboardCard>
          </div>
          <div class="pt-2 px-3 grid grid-cols-1 lg:grid-cols-2 gap-2">
            <DashboardCard title="Chapter Distribution"
                           :is-loaded="chapterStatsLoaded"
                           :no-data="chapterStats.length === 0"
                           class="min-h-72">
              <ChapterPieChart :chapter-stats="chapterStats" class="max-h-72"/>
            </DashboardCard>
            <DashboardCard title="Latest Squad Changes"
                           :is-loaded="squadLatestChangesLoaded"
                           :no-data="squadLatestChanges.length === 0"
                           class="min-h-72">
              <LatestChangesTable :latest-changes="squadLatestChanges" class="max-h-72"/>
            </DashboardCard>
          </div>
          <div class="pt-2 px-3 grid grid-cols-1 lg:grid-cols-3 gap-2">
            <DashboardCard title="Tribes Overview"
                           :no-data="allTribes.length === 0"
                           :is-loaded="loadedAllTribes"
                           class="h-72 min-h-72">
              <ListTribeTable :tribes="allTribes"/>
            </DashboardCard>
            <DashboardCard title="Squads Overview"
                           :no-data="allSquads.length === 0"
                           :is-loaded="loadedAllSquads"
                           class="h-72 min-h-72">
              <ListSquadsTable :squads="allSquads"/>
            </DashboardCard>
            <DashboardCard title="Member Overview"
                           :no-data="false"
                           :is-loaded="loadedAllMembers"
                           class="h-72 min-h-72">
              <DashboardTab tab-name="All"
                            class="max-h-60 pt-2"
                            :no-data="allMembers.length === 0">
                <ListMembersTable class="" :members="allMembers"/>
              </DashboardTab>
              <DashboardTab tab-name="Active"
                            class="max-h-60 pt-2"
                            :no-data="allActiveMembers && allActiveMembers.length === 0">
                <ListMembersTable class="" :members="allActiveMembers"/>
              </DashboardTab>
              <DashboardTab tab-name="Inactive"
                            class="max-h-60 pt-2"
                            :no-data="allInactiveMembers && allInactiveMembers.length === 0">
                <ListMembersTable class="" :members="allInactiveMembers"/>
              </DashboardTab>
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
import DashboardTab from "@/components/dashboard/DashboardTab";
import ListMembersTable from "@/components/dashboard/graphs/ListMembersTable";
import { getAllMembers } from "@/config/api/member.api";
import ListTribeTable from "@/components/dashboard/graphs/ListTribesTable";
import ListSquadsTable from "@/components/dashboard/graphs/ListSquadsTable";

export default {
  name: "Dashboard",
  components: {
    ListSquadsTable,
    ListTribeTable,
    ListMembersTable,
    DashboardTab,
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
      loadedAllTribes: false,
      allTribes: [],
      loadedAllSquads: false,
      allSquads: [],
      loadedAllMembers: false,
      allMembers: [],
      loadedAllChapters: false,
      allChapters: [],
      totalMemberCount: "",
      activeMemberCount: "",
    };
  },
  computed: {
    allActiveMembers() {
      return this.allMembers.filter((member) => member.active === true);
    },
    allInactiveMembers() {
      return this.allMembers.filter((member) => member.active === false);
    },
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
      this.allTribes = result;
      this.loadedAllTribes = true;
    }).catch(() => {
      this.allTribes = [];
    });

    getAllSquads().then((result) => {
      this.allSquads = result;
      this.loadedAllSquads = true;
    }).catch(() => {
      this.allSquads = [];
    });

    getAllMembers().then((result) => {
      this.allMembers = result.sort((a, b) => {
        if (a.name.toLowerCase() < b.name.toLowerCase()) { return -1; }
        if (a.name.toLowerCase() > b.name.toLowerCase()) { return 1; }
        return 0;
      });
      this.loadedAllMembers = true;
    }).catch(() => {
      this.allMembers = [];
    });

    getAllChapters().then((result) => {
      this.allChapters = result;
      this.loadedAllChapters = true;
    }).catch(() => {
      this.allChapters = [];
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
    swapKspStatsData(index) {
      this.$refs.kspChart.setData(this.tribeKSPStats[index]);
    },
  },
};
</script>
