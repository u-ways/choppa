<template>
  <StandardPageTemplate>
    <div class="flex-grow mx-3 grid items-center">
      <div class="w-full max-w-3xl -mt-32 mx-auto text-center flex flex-col">
        <img :src="require('@/assets/svg/404.svg')" alt="404" width="100%" height="100%"
             class="max-h-20 md:max-h-28 w-auto mx-auto mb-4"
        />
        <p class="font-semibold text-lg sm:text-xl">You've taken the wrong flight</p>
        <p class="text-sm md:text-md">The page you're looking for doesn't exist.</p>
        <div class="mt-4 flex flex-row justify-center gap-1">
          <StyledButton type="button" variant="secondary" @click="goBack">Go Back</StyledButton>
          <StyledButton type="link" link="/" :replace="true" variant="primary">Home</StyledButton>
        </div>
      </div>
    </div>
  </StandardPageTemplate>
</template>

<script>
import StandardPageTemplate from "@/components/templates/StandardPageTemplate";
import StyledButton from "@/components/atoms/buttons/StyledButton";

export default {
  name: "NotFoundPage",
  components: {
    StyledButton,
    StandardPageTemplate,
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
  },
};
</script>
