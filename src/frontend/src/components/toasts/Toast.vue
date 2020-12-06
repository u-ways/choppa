<template>
  <div :class="[ applyVariantCss, isFirstDisplay ? 'animate-fade-in' : '' ]"
       class="w-100 flex flex-row gap-2 px-2 py-2 items-center rounded-md shadow-lg border-l-4
       bg-white dark:bg-gray-800">
    <div class="mr-3">
      <div :class="[ applyLogoVariantCSS ]" class="rounded-full w-8 h-8 grid">
        <font-awesome-icon :icon="logo" class="text-2xl text-white justify-self-center self-center"/>
      </div>
    </div>
    <div class="flex-grow self-center">
      <p class="font-semibold">{{ toast.variant }}</p>
      <p class="text-sm">{{toast.message}}</p>
    </div>
    <div class="flex-shrink-0">
      <button class="p-2 rounded-md hover:bg-gray-100 dark:hover:bg-gray-700 focus:bg-gray-100 dark:focus:bg-gray-700
      focus:outline-none" @click="removeToast(toast)">
        <span class="sr-only">Close {{toast.message}} popup</span>
        <font-awesome-icon icon="times"/>
      </button>
    </div>
  </div>
</template>

<script>
import { toastVariants } from "@/enums/toastVariants";
import { mapActions } from "vuex";
import ToastData from "@/models/toastData";

const ERROR_CSS = "border-rose-500";
const ERROR_LOGO_CSS = "bg-rose-500";
const ERROR_LOGO = "times";

const SUCCESS_CSS = "border-emerald-500";
const SUCCESS_LOGO_CSS = "bg-emerald-500";
const SUCCESS_LOGO = "check";

const WARNING_CSS = "border-amber-400";
const WARNING_LOGO_CSS = "bg-amber-400";
const WARNING_LOGO = "exclamation";

const INFO_CSS = "border-choppa-two";
const INFO_LOGO_CSS = "bg-choppa-two";
const INFO_LOGO = "info";

export default {
  name: "Toast",
  components: {
  },
  props: {
    toast: {
      type: ToastData,
      required: true,
    },
  },
  computed: {
    applyVariantCss() {
      switch (this.toast.variant) {
        case toastVariants.ERROR:
          return ERROR_CSS;
        case toastVariants.WARNING:
          return WARNING_CSS;
        case toastVariants.SUCCESS:
          return SUCCESS_CSS;
        case toastVariants.INFO:
          return INFO_CSS;
        default:
          return "";
      }
    },
    logo() {
      switch (this.toast.variant) {
        case toastVariants.ERROR:
          return ERROR_LOGO;
        case toastVariants.WARNING:
          return WARNING_LOGO;
        case toastVariants.SUCCESS:
          return SUCCESS_LOGO;
        case toastVariants.INFO:
          return INFO_LOGO;
        default:
          return "";
      }
    },
    applyLogoVariantCSS() {
      switch (this.toast.variant) {
        case toastVariants.ERROR:
          return ERROR_LOGO_CSS;
        case toastVariants.WARNING:
          return WARNING_LOGO_CSS;
        case toastVariants.SUCCESS:
          return SUCCESS_LOGO_CSS;
        case toastVariants.INFO:
          return INFO_LOGO_CSS;
        default:
          return "";
      }
    },
  },
  data() {
    return {
      isFirstDisplay: this.toast.isFirstDisplay(),
    };
  },
  mounted() {
    this.toast.displayed();
  },
  methods: {
    ...mapActions(["removeToast"]),
  },
};
</script>
