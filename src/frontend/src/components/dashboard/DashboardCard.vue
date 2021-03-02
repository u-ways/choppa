<template>
  <div class="shadow ring-1 ring-black ring-opacity-5 border-choppa-two dark:border-choppa-two border-t-4
  bg-white dark:bg-gray-700 dark:ring-bg-gray-700 py-2 p-3 focus:outline-none focus:ring-4 focus:ring-choppa-two"
       tabindex="0"
  >
    <div class="flex flex-col h-full">
      <div class="flex flex-row justify-between">
        <div class="text-md font-semibold">{{ title }}</div>
        <div class="flex flex-row gap-2" v-if="tabs.length > 1">
          <button v-for="(tab, i) in tabs" :key="i"
                  class="p-1 focus:outline-none shadow border-l-4 border-choppa-two
                  focus:ring-2 ring-choppa-two"
                  :class="[ tab.getIsEnabled() ? 'font-semibold' : '' ]"
                  :style="{ 'border-left-color': tab.getBorderColor() }"
                  @click="enableTab(tab)">
            {{ tab.$options.propsData.tabName }}
          </button>
        </div>
      </div>
      <template v-if="isLoaded">
        <template v-if="noData === true">
          <div class="py-2 flex flex-col flex-grow items-center justify-items-center justify-center">
            <span class="font-semibold text-1xl md:text-2xl">No data</span>
          </div>
        </template>
        <template v-else>
          <slot></slot>
        </template>
      </template>
      <template v-else>
        <div class="py-3 flex flex-row flex-grow items-center">
          <img :src="require('@/assets/svg/dashboard/loading.svg')" alt="Loading" width="100%"
          height="100%" class="h-10 w-auto mx-auto animate-spin self-center justify-self-center"/>
        </div>
      </template>
    </div>
  </div>
</template>

<script>
export default {
  name: "DashboardCard",
  components: {
  },
  props: {
    title: String,
    isLoaded: Boolean,
    noData: Boolean,
  },
  data() {
    return {
      tabs: [],
      selectedTab: undefined,
    };
  },
  beforeMount() {
    this.$on("registeredTab", (component) => {
      if (component.$options.name === "DashboardTab") {
        this.registerNewTab(component);
      }
    });
  },
  methods: {
    registerNewTab(tab) {
      if (this.tabs.length === 0) {
        this.enableTab(tab);
      }

      this.tabs.push(tab);
    },
    enableTab(tab) {
      if (this.selectedTab !== undefined) {
        this.selectedTab.setEnabled(false);
      }

      tab.setEnabled(true);
      this.selectedTab = tab;
    },
  },
};
</script>
