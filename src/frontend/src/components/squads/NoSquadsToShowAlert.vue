<template>
  <div class="flex flex-col gap-4 text-center">
    <img :src="require('@/assets/svg/no-squad.svg')" alt="A representation of a squad" width="100%"
         height="100%" class="h-44 w-auto mx-auto"/>
    <p class="place-self-center font-semibold text-md dark:text-gray-300">
      Create your first Squad for this Tribe.
    </p>
    <div class="inline-block mx-auto">
      <StyledButton type="link" :link="{ name: 'create-squad', query: { tribe: tribe.path } }" variant="primary">
        Create Squad
      </StyledButton>
    </div>
  </div>
</template>

<script>
import StyledButton from "@/components/atoms/buttons/StyledButton";
import Tribe from "@/models/domain/tribe";

export default {
  name: "NoSquadsToShowAlert",
  components: {
    StyledButton,
  },
  props: {
    tribe: {
      type: Tribe,
      required: true,
    },
  },
};
</script>
