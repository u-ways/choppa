<template>
  <div class="h-full flex">
    <div class="m-auto flex flex-col sm:flex-row">
      <div class="mr-5 flex-grow-0 self-center">
        <ChoppaLogo css="place-self-center text-4xl"/>
      </div>
      <div class="flex-grow">
        <slot></slot>
      </div>
    </div>
  </div>
</template>

<script>
import ChoppaLogo from "@/components/atoms/ChoppaLogo";

export default {
  name: "NoticePageTemplate",
  components: {
    ChoppaLogo,
  },
};
</script>
