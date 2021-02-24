<template>
  <div class="flex flex-col gap-2 text-center">
    <img :src="require('@/assets/svg/no-chapters.svg')" alt="This squad has no chapters" width="100%"
         height="100%" class="h-28 w-auto mx-auto"/>
    <p class="place-self-center font-semibold text-md dark:text-gray-300">
      Add your first Chapter.
    </p>
    <div class="inline-block mx-auto">
      <StyledButton type="link" :link="{ name: 'create-chapter', query: { tribe: tribe.path } }" variant="primary">
        Add Chapter
      </StyledButton>
    </div>
  </div>
</template>
<script>
import StyledButton from "@/components/atoms/buttons/StyledButton";
import Tribe from "@/models/domain/tribe";

export default {
  name: "NoChaptersToShowAlert",
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
