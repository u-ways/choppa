<template>
  <Tag v-if="member.chapter" :style="{ 'border-left-color': member.chapter.color }"
       class="lowercase overflow-ellipsis overflow-hidden shadow-none border
                 dark:border-gray-600 border border-gray-200 dark:border-transparent">
    {{member.chapter.name}}
  </Tag>
</template>

<script>
import Member from "@/models/domain/member";
import Tag from "@/components/atoms/Tag";

export default {
  name: "ChapterTag",
  components: {
    Tag,
  },
  props: {
    member: {
      type: Member,
      required: true,
    },
  },
};
</script>
