<template>
  <div class="min-h-screen font-open-sans text-gray-800 bg-gray-50 dark:text-gray-200 dark:bg-gray-800">
    <ToastDraw/>
    <router-view :key="$route.fullPath"/>
  </div>
</template>

<script>
import ToastDraw from "@/components/toasts/ToastDraw";
import { mapActions } from "vuex";

export default {
  components: {
    ToastDraw,
  },
  async mounted() {
    await this.updateAuthenticationStatus();
  },
  methods: {
    ...mapActions(["updateAuthenticationStatus"]),
  },
};
</script>
