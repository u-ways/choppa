<template>
  <section id="Knowledge Sharing" class="bg-choppa-two">
    <div class="container px-3 py-6 mx-auto max-w-screen-lg">
      <div class="flex flex-col place-items-center lg:flex-row gap-1">
        <div class="h-80 md:h-96 lg:h-120 w-full my-3">
          <!-- <SimplifiedTribe :squads="squadsRandom"/> -->
          <Carousel>
            <template>
              <SimplifiedTribe :squads="squadsClockwise"/>
              <SimplifiedTribe :squads="squadsAnticlockwise"/>
              <SimplifiedTribe :squads="squadsRandom"/>
            </template>
          </Carousel>
        </div>
        <InformationBlock class="text-white order-first lg:order-last">
          <template v-slot:heading>Knowledge Sharing</template>
          <template v-slot:body>
            Utilise several rotation strategies to maximise knowledge sharing within your team.
          </template>
        </InformationBlock>
      </div>
    </div>
  </section>
</template>

<script>
import Carousel from "@/components/home/Carousel";
import InformationBlock from "@/components/home/InformationBlock";
import SimplifiedTribe from "@/components/home/SimplifiedTribe";
import { memberType } from "@/enums/home/memberType";

export default {
  name: "KnowledgeSharing",
  components: {
    Carousel,
    InformationBlock,
    SimplifiedTribe,
  },
  data() {
    return {
      tester: memberType.TESTER,
      developer: memberType.DEVELOPER,
      intern: memberType.INTERN,

      squadsClockwise: [
        {
          name: "Squad ZED",
          position: { x: 1, y: 203, width: 200, height: 200 },
          members: [
            { type: memberType.TESTER, x: 27, y: 73 },
            { type: memberType.TESTER, x: 75, y: 73 },
            { type: memberType.TESTER, x: 123, y: 73 },
            { type: memberType.TESTER, x: 171, y: 73 },
            { type: memberType.DEVELOPER, x: 27, y: 123 },
            { type: memberType.DEVELOPER, x: 75, y: 123 },
            { type: memberType.DEVELOPER, x: 123, y: 123, class: "squadZEDClockwise" },
            { type: memberType.DEVELOPER, x: 171, y: 123 },
            { type: memberType.DEVELOPER, x: 27, y: 171 },
            { type: memberType.DEVELOPER, x: 75, y: 171 },
            { type: memberType.INTERN, x: 123, y: 171 },
          ],
        },
        {
          name: "Squad WAI",
          position: { x: 203, y: 203, width: 200, height: 200 },
          members: [
            { type: memberType.TESTER, x: 27, y: 73 },
            { type: memberType.TESTER, x: 75, y: 73 },
            { type: memberType.TESTER, x: 123, y: 73 },
            { type: memberType.DEVELOPER, x: 171, y: 73 },
            { type: memberType.DEVELOPER, x: 27, y: 123 },
            { type: memberType.DEVELOPER, x: 75, y: 123 },
            { type: memberType.DEVELOPER, x: 123, y: 123, class: "squadWAIClockwise" },
            { type: memberType.DEVELOPER, x: 171, y: 123 },
            { type: memberType.DEVELOPER, x: 27, y: 171 },
            { type: memberType.DEVELOPER, x: 75, y: 171 },
            { type: memberType.INTERN, x: 123, y: 171 },
            { type: memberType.INTERN, x: 171, y: 171 },
          ],
        },
        {
          name: "Squad EKS",
          position: { x: 203, y: 1, width: 200, height: 200 },
          members: [
            { type: memberType.TESTER, x: 27, y: 73 },
            { type: memberType.TESTER, x: 75, y: 73 },
            { type: memberType.TESTER, x: 123, y: 73 },
            { type: memberType.TESTER, x: 171, y: 73 },
            { type: memberType.TESTER, x: 27, y: 123 },
            { type: memberType.DEVELOPER, x: 75, y: 123 },
            { type: memberType.DEVELOPER, x: 171, y: 123 },
            { type: memberType.DEVELOPER, x: 123, y: 123, class: "squadEKSClockwise" },
            { type: memberType.DEVELOPER, x: 27, y: 171 },
            { type: memberType.DEVELOPER, x: 75, y: 171 },
            { type: memberType.INTERN, x: 123, y: 171 },
            { type: memberType.INTERN, x: 171, y: 171 },
          ],
        },
        {
          name: "Squad DBU",
          position: { x: 1, y: 1, width: 200, height: 200 },
          members: [
            { type: memberType.TESTER, x: 27, y: 73 },
            { type: memberType.TESTER, x: 75, y: 73 },
            { type: memberType.TESTER, x: 123, y: 73 },
            { type: memberType.TESTER, x: 171, y: 73 },
            { type: memberType.DEVELOPER, x: 27, y: 123 },
            { type: memberType.DEVELOPER, x: 75, y: 123 },
            { type: memberType.DEVELOPER, x: 123, y: 123, class: "squadDBUClockwise" },
            { type: memberType.DEVELOPER, x: 171, y: 123 },
            { type: memberType.DEVELOPER, x: 27, y: 171 },
            { type: memberType.DEVELOPER, x: 75, y: 171 },
            { type: memberType.INTERN, x: 123, y: 171 },
            { type: memberType.INTERN, x: 171, y: 171 },
          ],
        },
      ],

      squadsAnticlockwise: [
        {
          name: "Squad ZED",
          position: { x: 1, y: 203, width: 200, height: 200 },
          members: [
            { type: memberType.TESTER, x: 27, y: 73 },
            { type: memberType.TESTER, x: 75, y: 73, class: "squadZEDAntiClockwise" },
            { type: memberType.TESTER, x: 123, y: 73 },
            { type: memberType.TESTER, x: 171, y: 73 },
            { type: memberType.DEVELOPER, x: 27, y: 123 },
            { type: memberType.DEVELOPER, x: 75, y: 123 },
            { type: memberType.DEVELOPER, x: 123, y: 123 },
            { type: memberType.DEVELOPER, x: 171, y: 123 },
            { type: memberType.DEVELOPER, x: 27, y: 171 },
            { type: memberType.DEVELOPER, x: 75, y: 171 },
            { type: memberType.INTERN, x: 123, y: 171 },
          ],
        },
        {
          name: "Squad WAI",
          position: { x: 203, y: 203, width: 200, height: 200 },
          members: [
            { type: memberType.TESTER, x: 27, y: 73 },
            { type: memberType.TESTER, x: 75, y: 73, class: "squadWAIAntiClockwise" },
            { type: memberType.TESTER, x: 123, y: 73 },
            { type: memberType.DEVELOPER, x: 171, y: 73 },
            { type: memberType.DEVELOPER, x: 27, y: 123 },
            { type: memberType.DEVELOPER, x: 75, y: 123 },
            { type: memberType.DEVELOPER, x: 123, y: 123 },
            { type: memberType.DEVELOPER, x: 171, y: 123 },
            { type: memberType.DEVELOPER, x: 27, y: 171 },
            { type: memberType.DEVELOPER, x: 75, y: 171 },
            { type: memberType.INTERN, x: 123, y: 171 },
            { type: memberType.INTERN, x: 171, y: 171 },
          ],
        },
        {
          name: "Squad EKS",
          position: { x: 203, y: 1, width: 200, height: 200 },
          members: [
            { type: memberType.TESTER, x: 27, y: 73 },
            { type: memberType.TESTER, x: 75, y: 73, class: "squadEKSAntiClockwise" },
            { type: memberType.TESTER, x: 123, y: 73 },
            { type: memberType.TESTER, x: 171, y: 73 },
            { type: memberType.TESTER, x: 27, y: 123 },
            { type: memberType.DEVELOPER, x: 75, y: 123 },
            { type: memberType.DEVELOPER, x: 171, y: 123 },
            { type: memberType.DEVELOPER, x: 123, y: 123 },
            { type: memberType.DEVELOPER, x: 27, y: 171 },
            { type: memberType.DEVELOPER, x: 75, y: 171 },
            { type: memberType.INTERN, x: 123, y: 171 },
            { type: memberType.INTERN, x: 171, y: 171 },
          ],
        },
        {
          name: "Squad DBU",
          position: { x: 1, y: 1, width: 200, height: 200 },
          members: [
            { type: memberType.TESTER, x: 27, y: 73 },
            { type: memberType.TESTER, x: 75, y: 73, class: "squadDBUAntiClockwise" },
            { type: memberType.TESTER, x: 123, y: 73 },
            { type: memberType.TESTER, x: 171, y: 73 },
            { type: memberType.DEVELOPER, x: 27, y: 123 },
            { type: memberType.DEVELOPER, x: 75, y: 123 },
            { type: memberType.DEVELOPER, x: 123, y: 123 },
            { type: memberType.DEVELOPER, x: 171, y: 123 },
            { type: memberType.DEVELOPER, x: 27, y: 171 },
            { type: memberType.DEVELOPER, x: 75, y: 171 },
            { type: memberType.INTERN, x: 123, y: 171 },
            { type: memberType.INTERN, x: 171, y: 171 },
          ],
        },
      ],

      squadsRandom: [
        {
          name: "Squad ZED",
          position: { x: 1, y: 203, width: 200, height: 200 },
          members: [
            { type: memberType.TESTER, x: 27, y: 73 },
            { type: memberType.TESTER, x: 75, y: 73 },
            { type: memberType.TESTER, x: 123, y: 73 },
            { type: memberType.TESTER, x: 171, y: 73 },
            { type: memberType.DEVELOPER, x: 27, y: 123 },
            { type: memberType.DEVELOPER, x: 75, y: 123 },
            { type: memberType.DEVELOPER, x: 123, y: 123, class: "squadZEDRandom" },
            { type: memberType.DEVELOPER, x: 171, y: 123 },
            { type: memberType.DEVELOPER, x: 27, y: 171 },
            { type: memberType.DEVELOPER, x: 75, y: 171 },
            { type: memberType.INTERN, x: 123, y: 171 },
          ],
        },
        {
          name: "Squad WAI",
          position: { x: 203, y: 203, width: 200, height: 200 },
          members: [
            { type: memberType.TESTER, x: 27, y: 73 },
            { type: memberType.TESTER, x: 75, y: 73 },
            { type: memberType.TESTER, x: 123, y: 73 },
            { type: memberType.DEVELOPER, x: 171, y: 73 },
            { type: memberType.DEVELOPER, x: 27, y: 123, class: "squadWAIRandom" },
            { type: memberType.DEVELOPER, x: 75, y: 123 },
            { type: memberType.DEVELOPER, x: 123, y: 123 },
            { type: memberType.DEVELOPER, x: 171, y: 123 },
            { type: memberType.DEVELOPER, x: 27, y: 171 },
            { type: memberType.DEVELOPER, x: 75, y: 171 },
            { type: memberType.INTERN, x: 123, y: 171 },
            { type: memberType.INTERN, x: 171, y: 171 },
          ],
        },
        {
          name: "Squad EKS",
          position: { x: 203, y: 1, width: 200, height: 200 },
          members: [
            { type: memberType.TESTER, x: 27, y: 73 },
            { type: memberType.TESTER, x: 75, y: 73 },
            { type: memberType.TESTER, x: 123, y: 73 },
            { type: memberType.TESTER, x: 171, y: 73 },
            { type: memberType.TESTER, x: 27, y: 123 },
            { type: memberType.DEVELOPER, x: 75, y: 123 },
            { type: memberType.DEVELOPER, x: 171, y: 123 },
            { type: memberType.DEVELOPER, x: 123, y: 123 },
            { type: memberType.DEVELOPER, x: 27, y: 171, class: "squadEKSRandom" },
            { type: memberType.DEVELOPER, x: 75, y: 171 },
            { type: memberType.INTERN, x: 123, y: 171 },
            { type: memberType.INTERN, x: 171, y: 171 },
          ],
        },
        {
          name: "Squad DBU",
          position: { x: 1, y: 1, width: 200, height: 200 },
          members: [
            { type: memberType.TESTER, x: 27, y: 73 },
            { type: memberType.TESTER, x: 75, y: 73 },
            { type: memberType.TESTER, x: 123, y: 73 },
            { type: memberType.TESTER, x: 171, y: 73 },
            { type: memberType.DEVELOPER, x: 27, y: 123 },
            { type: memberType.DEVELOPER, x: 75, y: 123, class: "squadDBURandom" },
            { type: memberType.DEVELOPER, x: 123, y: 123 },
            { type: memberType.DEVELOPER, x: 171, y: 123 },
            { type: memberType.DEVELOPER, x: 27, y: 171 },
            { type: memberType.DEVELOPER, x: 75, y: 171 },
            { type: memberType.INTERN, x: 123, y: 171 },
            { type: memberType.INTERN, x: 171, y: 171 },
          ],
        },
      ],
    };
  },
};

</script>

<style scoped>

</style>
