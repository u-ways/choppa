<template>
  <button @click="() => this.$emit('click')" class="rounded-md p-2 inline-flex items-center justify-center
  bg-transparent hover:bg-gray-100 dark:hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-choppa-indigo-500">
    <span class="sr-only">{{ screenReaderText }}</span>
    <font-awesome-icon :icon="icon"
                       class="text-gray-600 dark:text-gray-200 hover:text-gray-700 dark:hover:text-gray-300"
    />
  </button>
</template>

<script>
export default {
  name: "IconButton",
  props: {
    screenReaderText: {
      type: String,
      required: true,
    },
    icon: {
      type: String,
      required: true,
    },
  },
};
</script>
