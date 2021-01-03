<template>
  <div class="bg-white dark:bg-gray-700 shadow rounded-sm flex flex-row items-center cursor-pointer
  hover:ring-2 ring-choppa-two">
    <input type="radio"
           :name="inputName"
           :value="value"
           :id="id"
           class="ml-4 flex-shrink-0 cursor-pointer"
           :checked="checked ? 'checked' : ''"
           @change="$emit('onChanged', $event)">
    <StandardLabel :label-text="label" :for-id="id" class="px-2 py-3 flex-grow cursor-pointer"/>
  </div>
</template>

<script>
import StandardLabel from "@/components/forms/inputs/StandardLabel";

export default {
  name: "StandardRadio",
  components: {
    StandardLabel,
  },
  props: {
    inputName: {
      type: String,
      required: true,
    },
    id: {
      type: String,
      required: true,
    },
    value: {
      type: String,
      required: true,
    },
    checked: {
      type: Boolean,
      required: true,
    },
    label: {
      type: String,
      required: true,
    },
  },
};
</script>
