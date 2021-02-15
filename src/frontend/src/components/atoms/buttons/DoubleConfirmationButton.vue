<template>
  <StyledButton
    type="button"
    :variant="variant"
    :css="css"
    @click="() => this.$emit('click')">
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
};
</script>

<style scoped>

</style>
