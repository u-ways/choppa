<template>
  <!-- eslint-disable -->
  <div class="w-full h-full">
    <svg class="w-full h-full" version="1.1" id="f2f1e99e-1651-46a6-b45f-af903ca31f8b"
         xmlns="http://www.w3.org/2000/svg" x="0px" y="0px" width="800px" height="800px" viewBox="0 0 405 405"
         xml:space="preserve">

  <g v-for="squad in squads">
    <rect fill="#7973dd" :x="squad.position.x" :y="squad.position.y" :width="squad.position.width"
          :height="squad.position.height" rx="15"/>
    <text text-anchor="middle" class="text-2xl font-bold fill-current text-white"
          :x="squad.position.x + (squad.position.width / 2)" :y="squad.position.y + (35)">{{ squad.name }}</text>
  </g>

      <g v-for="squadAndMember in orderedMembersAndSquad"
         :class="[squadAndMember.member.class ? squadAndMember.member.class : '']">
    <ellipse :fill="squadAndMember.member.type.color" :cx="squadAndMember.squad.position.x + squadAndMember.member.x"
             :cy="squadAndMember.squad.position.y + squadAndMember.member.y" rx="20" ry="20"/>
        <svg v-if="squadAndMember.member.type === tester" viewBox="0 0 640 512"
             :x="squadAndMember.squad.position.x + squadAndMember.member.x - 10"
             :y="squadAndMember.squad.position.y + squadAndMember.member.y - 10" width="20" height="20"><path :fill="squadAndMember.member.type.textColor" d="M18.32 255.78L192 223.96l-91.28 68.69c-10.08 10.08-2.94 27.31 11.31 27.31h222.7c-9.44-26.4-14.73-54.47-14.73-83.38v-42.27l-119.73-87.6c-23.82-15.88-55.29-14.01-77.06 4.59L5.81 227.64c-12.38 10.33-3.45 30.42 12.51 28.14zm556.87 34.1l-100.66-50.31A47.992 47.992 0 0 1 448 196.65v-36.69h64l28.09 22.63c6 6 14.14 9.37 22.63 9.37h30.97a32 32 0 0 0 28.62-17.69l14.31-28.62a32.005 32.005 0 0 0-3.02-33.51l-74.53-99.38C553.02 4.7 543.54 0 533.47 0H296.02c-7.13 0-10.7 8.57-5.66 13.61L352 63.96 292.42 88.8c-5.9 2.95-5.9 11.36 0 14.31L352 127.96v108.62c0 72.08 36.03 139.39 96 179.38-195.59 6.81-344.56 41.01-434.1 60.91C5.78 478.67 0 485.88 0 494.2 0 504 7.95 512 17.76 512h499.08c63.29.01 119.61-47.56 122.99-110.76 2.52-47.28-22.73-90.4-64.64-111.36zM489.18 66.25l45.65 11.41c-2.75 10.91-12.47 18.89-24.13 18.26-12.96-.71-25.85-12.53-21.52-29.67z"></path></svg>
        <svg v-if="squadAndMember.member.type === developer" viewBox="0 0 512 512"
             :x="squadAndMember.squad.position.x + squadAndMember.member.x - 10"
             :y="squadAndMember.squad.position.y + squadAndMember.member.y - 10" width="20" height="20"><path :fill="squadAndMember.member.type.textColor" d="M290.59 192c-20.18 0-106.82 1.98-162.59 85.95V192c0-52.94-43.06-96-96-96-17.67 0-32 14.33-32 32s14.33 32 32 32c17.64 0 32 14.36 32 32v256c0 35.3 28.7 64 64 64h176c8.84 0 16-7.16 16-16v-16c0-17.67-14.33-32-32-32h-32l128-96v144c0 8.84 7.16 16 16 16h32c8.84 0 16-7.16 16-16V289.86c-10.29 2.67-20.89 4.54-32 4.54-61.81 0-113.52-44.05-125.41-102.4zM448 96h-64l-64-64v134.4c0 53.02 42.98 96 96 96s96-42.98 96-96V32l-64 64zm-72 80c-8.84 0-16-7.16-16-16s7.16-16 16-16 16 7.16 16 16-7.16 16-16 16zm80 0c-8.84 0-16-7.16-16-16s7.16-16 16-16 16 7.16 16 16-7.16 16-16 16z"></path> </svg>
        <svg v-if="squadAndMember.member.type === intern" viewBox="0 0 640 512"
             :x="squadAndMember.squad.position.x + squadAndMember.member.x - 10"
             :y="squadAndMember.squad.position.y + squadAndMember.member.y - 10" width="20" height="20"><path :fill="squadAndMember.member.type.textColor" d="M544 32h-16.36C513.04 12.68 490.09 0 464 0c-44.18 0-80 35.82-80 80v20.98L12.09 393.57A30.216 30.216 0 0 0 0 417.74c0 22.46 23.64 37.07 43.73 27.03L165.27 384h96.49l44.41 120.1c2.27 6.23 9.15 9.44 15.38 7.17l22.55-8.21c6.23-2.27 9.44-9.15 7.17-15.38L312.94 384H352c1.91 0 3.76-.23 5.66-.29l44.51 120.38c2.27 6.23 9.15 9.44 15.38 7.17l22.55-8.21c6.23-2.27 9.44-9.15 7.17-15.38l-41.24-111.53C485.74 352.8 544 279.26 544 192v-80l96-16c0-35.35-42.98-64-96-64zm-80 72c-13.25 0-24-10.75-24-24 0-13.26 10.75-24 24-24s24 10.74 24 24c0 13.25-10.75 24-24 24z"></path></svg>
  </g>

</svg>
  </div>
</template>

<script>

import { memberType } from "@/enums/home/memberType";

export default {
  computed: {
    orderedMembersAndSquad() {
      return this.squads.map((squad) => squad.members.map((member) => ({ squad, member })))
        .flat()
        .sort((a, b) => {
          if (a.member.class && !b.member.class) {
            return 1;
          }

          if (!a.member.class && b.member.class) {
            return -1;
          }

          return 0;
        });
    },
  },

  data() {
    return {
      tester: memberType.TESTER,
      developer: memberType.DEVELOPER,
      intern: memberType.INTERN,
    };
  },

  props: {
    squads: {
      required: true,
    },
  },
};
</script>

<style scoped>
@keyframes clockwiseDBU {
  0%, 30%, 45% {
    transform: translateX(0px);
  }
  35% {
    transform: translateX(2px);
  }
  40% {
    transform: translateX(-2px);
  }
  70%, 100% {
    transform: translateX(202px);
  }
}

@keyframes clockwiseEKS {
  0%, 30%, 45% {
    transform: translate(0px);
  }
  35% {
    transform: translateX(2px);
  }
  40% {
    transform: translateX(-2px);
  }
  70%, 100% {
    transform: translateY(202px);
  }
}

@keyframes clockwiseWAI {
  0%, 30%, 45% {
    transform: translateX(0px);
  }
  35% {
    transform: translateX(2px);
  }
  40% {
    transform: translateX(-2px);
  }
  70%, 100% {
    transform: translateX(-202px);
  }
}

@keyframes clockwiseZED {
  0%, 30%, 45% {
    transform: translate(0px);
  }
  35% {
    transform: translateX(2px);
  }
  40% {
    transform: translateX(-2px);
  }
  70%, 100% {
    transform: translateY(-202px);
  }
}

.squadDBUClockwise {
  animation: clockwiseDBU 4s infinite;
}

.squadEKSClockwise {
  animation: clockwiseEKS 4s infinite;
}

.squadWAIClockwise {
  animation: clockwiseWAI 4s infinite;
}

.squadZEDClockwise {
  animation: clockwiseZED 4s infinite;
}

@keyframes antiClockwiseDBU {
  0%, 30%, 45% {
    transform: translateX(0px);
  }
  35% {
    transform: translateX(2px);
  }
  40% {
    transform: translateX(-2px);
  }
  70%, 100% {
    transform: translateY(202px);
  }
}

@keyframes antiClockwiseEKS {
  0%, 30%, 45% {
    transform: translate(0px);
  }
  35% {
    transform: translateX(2px);
  }
  40% {
    transform: translateX(-2px);
  }
  70%, 100% {
    transform: translateX(-202px);
  }
}

@keyframes antiClockwiseWAI {
  0%, 30%, 45% {
    transform: translateX(0px);
  }
  35% {
    transform: translateX(2px);
  }
  40% {
    transform: translateX(-2px);
  }
  70%, 100% {
    transform: translateY(-202px);
  }
}

@keyframes antiClockwiseZED {
  0%, 30%, 45% {
    transform: translate(0px);
  }
  35% {
    transform: translateX(2px);
  }
  40% {
    transform: translateX(-2px);
  }
  70%, 100% {
    transform: translateX(202px);
  }
}

.squadDBUAntiClockwise {
  animation: antiClockwiseDBU 4s infinite;
}

.squadEKSAntiClockwise {
  animation: antiClockwiseEKS 4s infinite;
}

.squadWAIAntiClockwise {
  animation: antiClockwiseWAI 4s infinite;
}

.squadZEDAntiClockwise {
  animation: antiClockwiseZED 4s infinite;
}

@keyframes randomDBU {
  0%, 30%, 45% {
    transform: translateX(0px);
  }
  35% {
    transform: translateX(2px);
  }
  40% {
    transform: translateX(-2px);
  }
  70%, 100% {
    transform: translate(154px, 202px);
  }
}

@keyframes randomEKS {
  0%, 30%, 45% {
    transform: translate(0px);
  }
  35% {
    transform: translateX(2px);
  }
  40% {
    transform: translateX(-2px);
  }
  70%, 100% {
    transform: translate(-106px, 154px);
  }
}

@keyframes randomWAI {
  0%, 30%, 45% {
    transform: translateX(0px);
  }
  35% {
    transform: translateX(2px);
  }
  40% {
    transform: translateX(-2px);
  }
  70%, 100% {

    transform: translateY(-154px);
  }
}

@keyframes randomZED {
  0%, 30%, 45% {
    transform: translate(0px);
  }
  35% {
    transform: translateX(2px);
  }
  40% {
    transform: translateX(-2px);
  }
  70%, 100% {
    transform: translate(-48px, -202px);
  }
}

.squadDBURandom {
  animation: randomDBU 4s infinite;
}

.squadEKSRandom {
  animation: randomEKS 4s infinite;
}

.squadWAIRandom {
  animation: randomWAI 4s infinite;
}

.squadZEDRandom {
  animation: randomZED 4s infinite;
}
</style>
