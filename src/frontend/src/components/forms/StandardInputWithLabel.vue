<template>
  <div>
    <div>
      <StandardLabel :label-text="labelText" :for-id="id" :validator="validator"/>
    </div>
    <div class="mt-1">
      <StandardInput type="text"
                     :id="id"
                     v-bind="$attrs"
                     v-on="$listeners"
                     :place-holder="placeHolder"
                     :validator="validator"
                     :max-length="maxLength"
      />
      <ErrorPrompt v-if="validator" v-show="validator.$invalid" :validator="validator" :label-text="labelText" />
    </div>
  </div>
</template>

<script>
import StandardInput from "@/components/forms/inputs/StandardInput";
import StandardLabel from "@/components/forms/inputs/StandardLabel";
import ErrorPrompt from "@/components/forms/ErrorPrompt";

export default {
  name: "StandardInputWithLabel",
  components: {
    ErrorPrompt,
    StandardLabel,
    StandardInput,
  },
  props: {
    id: {
      type: String,
      required: true,
    },
    labelText: {
      type: String,
      required: true,
    },
    maxLength: Number,
    placeHolder: String,
    validator: Object,
  },
};
</script>
