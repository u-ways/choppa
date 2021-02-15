<template>
  <div>
    <div>
      <StandardLabel :label-text="labelText" :for-id="id" :validator="validator" :class="[ labelTextCSS ]"/>
    </div>
    <div class="mt-1">
      <StandardInput :type="type"
                     :id="id"
                     v-bind="$attrs"
                     v-on="$listeners"
                     :place-holder="placeHolder"
                     :validator="validator"
                     :max-length="maxLength"
                     :isErroredState="shouldShowError"
      />
      <ErrorPrompt v-if="validator" v-show="shouldShowError" :validator="validator" :label-text="labelText" />
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
    labelTextCSS: String,
    maxLength: Number,
    placeHolder: String,
    validator: Object,
    customShowError: Function,
    type: {
      type: String,
      default: "text",
    },
  },
  computed: {
    shouldShowError() {
      if (this.customShowError) {
        return this.customShowError(this.validator);
      }

      return this.validator.$invalid;
    },
  },
};
</script>
