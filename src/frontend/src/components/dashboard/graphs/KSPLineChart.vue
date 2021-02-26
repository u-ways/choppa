<template>
  <canvas ref="canvas"/>
</template>

<script>
import Chart from "chart.js";
import { mapGetters } from "vuex";
import { themeSetting } from "@/enums/themeSetting";
import { stripAlphaFromHex } from "@/utils/stripAlphaFromHex";

export default {
  name: "KSPLineChart",
  props: {
    tribeKspStats: {},
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
    gridLineColor() {
      return this.currentTheme === themeSetting.DARK_THEME ? "rgba(229,231,235, 0.2)" : "rgba(55,65,81, 0.2)";
    },
  },
  mounted() {
    this.chart = new Chart(this.$refs.canvas.getContext("2d"), {
      type: "line",
      data: {
        labels: ["T1", "T2", "T3", "T4", "T5", "T6", "T7"],
        datasets: [
          {
            data: this.tribeKspStats.tribeAverage,
            label: "Tribe Average",
            borderColor: "#6762D950",
            backgroundColor: "#6762D950",
          },
          ...this.tribeKspStats.squads.map((squadKspPair) => ({
            data: Object.values(squadKspPair.ksp).map((ksp) => ksp.ksp),
            label: squadKspPair.squad.name,
            borderColor: stripAlphaFromHex(squadKspPair.squad.color),
            backgroundColor: stripAlphaFromHex(squadKspPair.squad.color),
            fill: false,
          })),
        ],
      },
      options: {
        legend: {
          labels: {
            fontColor: this.legendFontColor,
          },
        },
        maintainAspectRatio: false,
        scales: {
          xAxes: [{
            gridLines: {
              display: false,
            },
            ticks: {
              fontColor: this.legendFontColor,
            },
          }],
          yAxes: [{
            gridLines: {
              display: true,
              color: this.gridLineColor,
              drawBorder: false,
            },
            ticks: {
              fontColor: this.legendFontColor,
            },
          }],
        },
      },
    });
  },
  watch: {
    currentTheme() {
      this.chart.options.legend.labels.fontColor = this.legendFontColor;
      this.chart.options.scales.xAxes[0].ticks.fontColor = this.legendFontColor;
      this.chart.options.scales.yAxes[0].ticks.fontColor = this.legendFontColor;
      this.chart.options.scales.yAxes[0].gridLines.color = this.gridLineColor;
      this.chart.update();
    },
  },
};
</script>
