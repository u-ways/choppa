<template>
  <div class="shadow ring-1 ring-black ring-opacity-5 border-choppa-light dark:border-choppa-dark border-t-4
  bg-gray-100 dark:bg-gray-600 dark:ring-bg-gray-700 flex flex-col" :style="{ 'border-top-color': squad.color }">
    <div class="bg-white dark:bg-gray-700 p-5 flex-shrink-0">
      <p class="text-xl font-normal">Squad <span class="font-semibold">{{ squad.name }}</span></p>
      <div class="text-sm leading-8 flex flex-row gap-1">
        <div class="self-center">{{squad.members.length}} Members</div>
        <div class="self-center flex -space-x-1 overflow-hidden">
          <Avatar v-for="member in limitedMembersAvatars(squad.members)" :key="member.id" :member="member"
                  class="inline w-8 h-8 text-xl self-center"
          />
          <div v-if="squad.members.length > memberShowCount" class="bg-white dark:bg-gray-700 text-gray-500
          dark:text-gray-300 border border-gray-200 dark:border-gray-600 rounded-full grid self-center w-8 h-8">
            <div class="justify-self-center self-center font-bold text-sm">
              +{{ squad.members.length - memberShowCount }}
            </div>
          </div>
        </div>
      </div>
    </div>
    <template v-if="!showHeaderOnly">
      <div v-if="squad.members.length > 0">
        <div class="grid grid-cols-4 text-sm leading-8 px-7 dark:text-gray-200 text-gray-500
        bg-gray-100 dark:bg-gray-666">
          <div class="ml-10 col-span-3">Name</div>
          <div>Chapter</div>
        </div>
        <div v-for="member in squad.members" :key="member.id" class="py-3 px-4 sm:px-7 grid grid-cols-4
        bg-gray-100 even:bg-gray-50 dark:bg-gray-666 dark:even:bg-gray-600">
          <div class="self-center flex flex-row gap-3 col-span-3">
            <Avatar class="self-center flex-shrink-0 w-8 h-8 text-2xl" :member="member"/>
            <div class="self-center flex-grow text-lg font-normal leading-8">
              {{member.name}}
            </div>
          </div>
          <div class="self-center grid">
            <ChapterTag v-if="member.chapter"
                        :chapter="member.chapter"
                        class="w-16 md:w-20 lg:w-24 bg-gray-50 dark:bg-gray-700"
            />
          </div>
        </div>
      </div>
      <div v-else class="py-5 flex-grow flex flex-col content-center place-content-center">
        <NoMembersToShowAlert :squad="squad"/>
      </div>
    </template>
  </div>
</template>

<script>
import Squad from "@/models/domain/squad";
import Avatar from "@/components/member/Avatar";
import NoMembersToShowAlert from "@/components/member/NoMembersToShowAlert";
import ChapterTag from "@/components/chapters/ChapterTag";

export default {
  name: "SquadCard",
  components: {
    ChapterTag,
    NoMembersToShowAlert,
    Avatar,
  },
  props: {
    squad: {
      type: Squad,
      required: true,
    },
    showHeaderOnly: {
      type: Boolean,
      default: false,
    },
    memberShowCount: {
      type: Number,
      default: 3,
      validator: (value) => value > 0,
    },
  },
  methods: {
    limitedMembersAvatars(members) {
      return members.slice(0, this.memberShowCount);
    },
    addMemberUrl(squad) {
      // TO:DO maybe add member here should be a popup? What about mobile?
      return `/${squad.id}`;
    },
  },
};
</script>
