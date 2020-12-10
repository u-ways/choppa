<template>
  <div :style="{ 'border-color': chapter.color }"
       class="bg-white dark:bg-gray-700 shadow rounded-sm ring-1 ring-black ring-opacity-5 px-3 py-4
                       border-l-4">
    {{ chapter.name }}
  </div>
</template>

<script>
import Chapter from "@/models/domain/chapter";

export default {
  name: "ChapterCard",
  props: {
    chapter: {
      type: Chapter,
      required: true,
    },
  },
};
</script>
