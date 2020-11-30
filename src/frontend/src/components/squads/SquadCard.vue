<template>
  <div class="shadow ring-1 ring-black ring-opacity-5 border-choppa-light dark:border-choppa-dark border-t-4
  bg-gray-100 dark:bg-blue-gray-600 dark:ring-bg-blue-gray-700" :style="{ 'border-top-color': squad.color }">
    <div class="bg-white dark:bg-blue-gray-700 p-5">
      <p class="text-xl font-normal">Squad <span class="font-semibold">{{ squad.name }}</span></p>
      <div class="text-sm leading-8 flex flex-row gap-1">
        <div class="self-center">{{squad.members.length}} Members</div>
        <div class="self-center flex -space-x-1 overflow-hidden">
          <Avatar v-for="member in limitedMembersAvatars(squad.members)" :key="member.id" :member="member"
                  class="inline w-8 h-8 text-xl self-center"
          />
          <div v-if="squad.members.length > 3" class="bg-white dark:bg-gray-700 text-gray-500 dark:text-gray-300
          border border-gray-200 dark:border-gray-600 rounded-full grid self-center w-8 h-8">
            <div class="justify-self-center self-center font-bold text-sm">
              +{{ squad.members.length - 3 }}
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="pt-3 pb-2">
      <div class="y-3 px-7 grid grid-cols-4 text-sm pb-1 dark:text-gray-200 text-gray-500">
        <div class="ml-10 col-span-3">Name</div>
        <div>Chapter</div>
      </div>
      <div v-for="member in squad.members" :key="member.id" class="py-3 px-4 sm:px-7 grid grid-cols-4 border-t
      border-gray-200 dark:border-gray-700">
        <div class="self-center flex flex-row gap-3 col-span-3">
          <Avatar class="self-center flex-shrink-0 w-8 h-8 text-2xl" :member="member"/>
          <div class="self-center flex-grow text-lg font-normal leading-8">
            {{member.name}}
          </div>
        </div>
        <div class="self-center">
          <div v-if="member.chapter" :style="{ 'background': member.chapter.color }" class="inline-block text-sm
          bg-red-500 px-2 py-1 rounded overflow-ellipsis overflow-hidden lowercase text-gray-200
          text-center w-16 md:w-20 lg:w-24 shadow border border-gray-200 dark:border-gray-700">
            {{member.chapter.name}}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
/* eslint-disable */
import Squad from "@/models/squad";
import Avatar from "@/components/member/Avatar";

export default {
  name: "SquadCard",
  components: {
    Avatar,
  },
  props: {
    squad: {
      type: Squad,
      required: true,
    },
  },
  methods: {
    limitedMembersAvatars(members) {
      return members.slice(0, 3);
    }
  },
};
</script>
