<template>
  <div class="mt-2 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-2">
    <router-link :to="{ name: 'edit-chapter', params: { id: chapter.path } }"
                 v-for="chapter in chapters" v-bind:key="chapter.id"
                 class="hover:ring-2 focus:ring-2 focus:outline-none ring-choppa-two rounded-sm">
      <ChapterCard :chapter="chapter"/>
    </router-link>
  </div>
</template>

<script>
import ChapterCard from "@/components/chapters/ChapterCard";

export default {
  name: "ChapterOverview",
  components: {
    ChapterCard,
  },
  props: {
    chapters: {
      required: true,
    },
  },
};
</script>
