<template>
  <span class="badge badge-primary background" :style="cssVars">{{ chapter.name }}</span>
</template>

<script>
import Chapter from "@/data/types/chapter";

export default {
  name: "ChapterBadgeAtom",
  props: {
    chapter: Chapter,
  },
  computed: {
    cssVars() {
      return {
        "--bg-color": this.chapter.color,
      };
    },
  },
};
</script>

<style scoped>

.background {
  background-color: var(--bg-color);
}
</style>
