<template>
  <StandardRadio :style="{ 'border-color': chapter.color }"
                 :input-name="inputName"
                 :value="chapter.id"
                 :id="`${chapter.id}`"
                 :checked="isChecked"
                 :label="chapter.name"
                 class="border-l-4 lowercase"
                 @onChanged="$emit('onChapterChanged', $event)"
  />
</template>

<script>
import Chapter from "@/models/domain/chapter";
import StandardRadio from "@/components/forms/inputs/StandardRadio";

export default {
  name: "ChapterRadioButton",
  components: {
    StandardRadio,
  },
  props: {
    inputName: {
      type: String,
      required: true,
    },
    chapter: {
      type: Chapter,
      required: true,
    },
    selectedChapter: {
      type: Object,
    },
  },
  computed: {
    isChecked() {
      if (this.selectedChapter) {
        return this.selectedChapter.id === this.chapter.id;
      }

      return false;
    },
  },
};
</script>
