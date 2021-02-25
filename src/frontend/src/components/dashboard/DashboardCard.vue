<template>
  <div class="shadow ring-1 ring-black ring-opacity-5 border-choppa-two dark:border-choppa-two border-t-4
  bg-white dark:bg-gray-700 dark:ring-bg-gray-700 py-2 p-3">
    <div class="flex flex-col h-full">
      <div class="text-md font-semibold">{{ title }}</div>
      <template v-if="isLoaded">
        <template v-if="noData === true">
          <div class="py-3 flex flex-col flex-grow items-center justify-items-center justify-center">
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
};
</script>
