<template>
  <div class="bg-white dark:bg-gray-700 shadow rounded-sm ring-1 ring-black ring-opacity-5 px-3 py-4">
    <div class="flex flex-row items-center gap-2">
      <Avatar class="w-8 h-8 flex-shrink-0" :member="member" />
      <div class="leading-2 flex-grow text-lg truncate" >{{ member.name }}</div>
      <ChapterTag v-if="member.chapter"
                  :chapter="member.chapter"
                  class="flex-shrink-0 w-16 md:w-20 lg:w-24 bg-gray-50 dark:bg-gray-600"
      />
    </div>
  </div>
</template>

<script>
import Member from "@/models/domain/member";
import Avatar from "@/components/member/Avatar";
import ChapterTag from "@/components/chapters/ChapterTag";

export default {
  name: "MemberCard",
  components: {
    ChapterTag,
    Avatar,
  },
  props: {
    member: {
      type: Member,
      required: true,
    },
  },
};
</script>
