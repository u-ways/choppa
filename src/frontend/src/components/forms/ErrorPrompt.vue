<template>
  <p class="text-sm leading-6 text-red-600 dark:text-red-400">{{ errorText }}</p>
</template>

<script>
import { generateErrorMessagesFromValidator } from "@/utils/generateErrorMessagesFromValidator";

export default {
  name: "ErrorPrompt",
  props: {
    validator: {
      type: Object,
      required: true,
    },
    labelText: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      errorMessages: undefined,
      errorText: "",
    };
  },
  mounted() {
    this.errorMessages = generateErrorMessagesFromValidator(this.labelText, this.validator);
    this.updateErrorText();

    Object.keys(this.errorMessages).forEach((key) => this.$watch(`validator.${key}`, this.updateErrorText));
  },
  methods: {
    updateErrorText() {
      const appropriateErrorMessages = [];
      Object.keys(this.errorMessages).forEach((key) => {
        if (this.validator[key] === false) {
          appropriateErrorMessages.push(this.errorMessages[key]);
        }
      });

      this.errorText = appropriateErrorMessages.join(", ");
    },
  },
};
</script>
