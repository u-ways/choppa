<template>
  <label :for="forId" class="font-semibold"
         :class="[ validator && validator.$invalid ?
            'text-red-600 dark:text-red-400' :
            'text-gray-700 dark:text-gray-300'
         ]"
  >
    {{ labelText }}
  </label>
</template>

<script>
export default {
  name: "StandardLabel",
  props: {
    forId: {
      type: String,
      required: true,
    },
    labelText: {
      type: String,
      required: true,
    },
    validator: Object,
  },
};
</script>
