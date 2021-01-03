<template>
  <div class="fixed z-20 top-0 right-0 bottom-0 w-full sm:w-96 pointer-events-none p-3 flex flex-col"
       v-bind:key="key">
    <div v-for="toast in toasts" v-bind:key="toast.id" class="mb-2">
      <Toast :toast="toast" class="pointer-events-auto"/>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import Toast from "@/components/toasts/Toast";

export default {
  name: "ToastDraw",
  components: {
    Toast,
  },
  computed: {
    ...mapGetters(["toasts"]),
  },
  data() {
    return {
      key: 0,
    };
  },
  watch: {
    toasts() {
      this.key += 1;
    },
  },
};
</script>
