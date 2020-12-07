<template>
  <Tag :style="{ 'border-left-color': chapter.color }"
       class="lowercase overflow-ellipsis overflow-hidden shadow-none border
                 dark:border-gray-600 border border-gray-200 dark:border-transparent">
    {{chapter.name}}
  </Tag>
</template>

<script>
import Tag from "@/components/atoms/Tag";
import Chapter from "@/models/domain/chapter";

export default {
  name: "ChapterTag",
  components: {
    Tag,
  },
  props: {
    chapter: {
      type: Chapter,
      required: true,
    },
  },
};
</script>
