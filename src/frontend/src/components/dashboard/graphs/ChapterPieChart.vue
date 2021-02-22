<template>
  <canvas ref="canvas"/>
</template>

<script>
import Chart from "chart.js";
import { stripAlphaFromHex } from "@/utils/stripAlphaFromHex";
import { mapGetters } from "vuex";
import { themeSetting } from "@/enums/themeSetting";

export default {
  name: "ChapterPieChart",
  props: {
    chapterStats: {},
  },
  data() {
    return {
      chart: {},
    };
  },
  computed: {
    ...mapGetters(["currentTheme"]),
    legendFontColor() {
      return this.currentTheme === themeSetting.DARK_THEME ? "#E5E7EB" : "#374151";
    },
  },
  mounted() {
    this.chart = new Chart(this.$refs.canvas.getContext("2d"), {
      type: "doughnut",
      data: {
        labels: this.chapterStats.map((chapterStat) => chapterStat.chapter.name),
        datasets: [{
          data: this.chapterStats.map((chapterStat) => chapterStat.percentage),
          backgroundColor: this.chapterStats.map((chapterStat) => `${stripAlphaFromHex(chapterStat.chapter.color)}`),
          borderColor: this.chapterStats.map((chapterStat) => `${stripAlphaFromHex(chapterStat.chapter.color)}`),
          borderWidth: 1,
        }],
      },
      options: {
        legend: {
          labels: {
            fontColor: this.legendFontColor,
          },
        },
      },
    });
  },
  watch: {
    currentTheme() {
      this.chart.options.legend.labels.fontColor = this.legendFontColor;
      this.chart.update();
    },
  },
};
</script>
