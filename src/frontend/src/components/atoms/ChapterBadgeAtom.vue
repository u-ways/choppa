<template>
  <span class="badge badge-primary background" :style="cssVars">{{ chapter.name }}</span>
</template>

<script>
import Chapter from "@/data/types/Chapter";

export default {
  name: "ChapterBadgeAtom",
  props: {
    chapter: Chapter,
  },
  computed: {
    cssVars() {
      return {
        "--bg-colour": this.chapter.colour,
      };
    },
  },
};
</script>

<style scoped>

.background {
  background-color: var(--bg-colour);
}
</style>
