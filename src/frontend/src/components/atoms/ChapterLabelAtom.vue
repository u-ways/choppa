<template>
  <div class="label text-center" :style="cssVars">
    {{ chapter.name }}
  </div>
</template>

<script>
import Chapter from "@/data/types/chapter";

export default {
  name: "ChapterLabelAtom",
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

.label {
  color: white;
  border: 3px solid black;
  border-radius: 5px 0 5px 0;
  padding: 0.15rem;
  background-color: var(--bg-colour);
  width: 100%;
}
</style>
