<template>
  <StyledButton
    type="button"
    :variant="variant"
    :css="css"
    @click="confirmationCheck">
    {{ clicked ? buttonMessageTwo : buttonMessageOne }}
  </StyledButton>
</template>

<script>

import StyledButton from "@/components/atoms/buttons/StyledButton";

export default {
  name: "DoubleConfirmationButton",
  components: { StyledButton },
  props: {
    buttonMessageOne: String,
    buttonMessageTwo: String,
    css: {
      type: String,
      required: false,
    },
    variant: String,
  },
  data() {
    return {
      clicked: false,
    };
  },
  methods: {
    confirmationCheck() {
      if (this.clicked) {
        this.$emit("click");
      } else {
        this.clicked = true;
      }
    },
  },
};
</script>
