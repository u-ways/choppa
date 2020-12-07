<template>
      <div class="text-center pt-6 pl-4 w-full lg:w-1/3">
        <p class="text-xl sm:text-2xl lg:text-3xl font-bold ulDark">
          <slot name="heading"/>
        </p>
        <div class="text-lg sm:text-xl lg:text-2xl">
          <slot name="body"/>
          <slot name="extBody"/>
        </div>
      </div>
</template>

<script>
export default {
  name: "InformationBlock",
};
</script>

<style scoped>

</style>
