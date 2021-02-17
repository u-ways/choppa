<template>
  <StyledButton
    type="button"
    :variant="variant"
    :css="css"
    @click="confirmationCheck">
    {{ buttonMessage }}
  </StyledButton>
</template>

<script>

import StyledButton from "@/components/atoms/buttons/StyledButton";

export default {
  name: "DoubleConfirmationButton",
  components: { StyledButton },
  props: {
    buttonMessage: {
      type: String,
      required: true,
    },
    css: {
      type: String,
      required: false,
    },
    variant: {
      required: true,
    },
  },
  data() {
    return {
      clicked: false,
    };
  },
  methods: {
    confirmationCheck() {
      if (this.clicked) {
        return this.$emit("click");
      }
      this.clicked = true;
      return this.$emit("next");
    },
  },
};
</script>
