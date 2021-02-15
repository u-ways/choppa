<template>
  <input type="text" :id="id" v-bind="$attrs" v-on="$listeners" :placeholder="placeHolder" :maxlength="maxLength"
         class="p-2 focus:ring-1 focus:outline-none block w-full shadow-sm text-md sm:text-sm border rounded-md
         bg-white dark:bg-gray-700"
         :class="[ validator && isErroredState ?
            'focus:ring-red-600 border-red-600 focus:border-red-600 ' +
             ' dark:focus:ring-red-400 dark:border-red-400 dark:focus:border-red-400' :
            'focus:ring-choppa-two border-gray-300 dark:border-gray-800 focus:border-choppa-two ' +
             ' dark:focus:border-choppa-two'
         ]"
  />
</template>

<script>
export default {
  name: "StandardInput",
  props: {
    id: {
      type: String,
      required: true,
    },
    placeHolder: String,
    validator: Object,
    maxLength: Number,
    isErroredState: Boolean,
  },
};
</script>
