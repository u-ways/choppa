<template>
  <div>
    <section id="hero" class="dark bg-choppa-two pb-6">
      <div class="container mx-auto max-w-screen-lg">
        <Navbar/>
      </div>
      <div class="text-white flex-grow px-3 mt-3 mx-auto lg:mt-6">
        <div class="container mx-auto max-w-screen-lg">
          <div class="flex flex-col lg:flex-row gap-1 place-items-center justify-center">
            <img :src="require('@/assets/svg/homepage/stacked-squads.svg')"
                 alt="Stacked squad" height="100%"
                 class="h-72"/>
            <div class="flex-shrink-0 order-first lg:order-last
              text-xl sm:text-2xl xl:text-3xl font-bold text-center">
              <p><span class="lg:inline hidden">Choppa Helps </span>Organise Your Team</p>
              <p>and</p>
              <p>Maximise Knowledge Sharing.</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section id="organisation">
      <div class="container px-3 py-3 mx-auto max-w-screen-lg">
        <div class="flex flex-col place-items-center lg:flex-row gap-1">
          <InformationBlock>
            <template v-slot:heading>Team Organisation</template>
            <template v-slot:body>Organise your team structure with our creation tool.</template>
          </InformationBlock>
          <div class="w-full pl-3 lg:w-2/3 py-6">
            <SquadCard v-bind:squad="tribe.squads[0]"/>
          </div>
        </div>
      </div>
    </section>

    <section id="Knowledge Sharing">
      <div class="container px-3 py-3 mx-auto max-w-screen-lg">
        <div class="flex flex-col place-items-center lg:flex-row gap-1">
          <div class="w-full lg:w-2/3 pr-3 py-6">
            <video width="100%" height="528" controls>
              <source src="@/assets/video/homeVideo.mp4" type="video/mp4">
              Your browser does not support the video tag.
            </video>
          </div>
          <InformationBlock>
            <template v-slot:heading>Knowledge Sharing</template>
            <template v-slot:body>
              Utilise several rotation strategies to maximise knowledge sharing within your team.
            </template>
          </InformationBlock>
        </div>
      </div>
    </section>

    <section id="Demonstration" class="bg-choppa-light dark:bg-choppa-dark py-6">
      <div class="container px-3 mx-auto max-w-screen-lg">
        <div class="flex flex-col pb-3 place-items-center gap-1">
          <div class="text-white w-full lg:w-1/3 text-center text-lg sm:text-xl lg:text-xl font-semibold">
            <p>Ready for your first test flight?</p>
            <p>Check out this handy demonstration!</p>
            <div class="pt-3">
              <StyledButton class="text-center" type="link"
                            link="/tribes/00000000-0000-0000-0000-000000000001/" :replace="true"
                            variant="primary">Demo
              </StyledButton>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section id="Glossary">
      <div class="container px-3 pt-12 mx-auto max-w-screen-lg">
        <div class="flex flex-col pb-3 place-items-center gap-1 lg:flex-row">
          <InformationCard>
            <template v-slot:icon>
              <font-awesome-icon icon="users" class="text-3xl text-white justify-self-center self-center"/>
            </template>
            <template v-slot:heading>Tribe</template>
            <template v-slot:body>
              Tribes are formed from multiple squads that work on the related feature area.
            </template>
          </InformationCard>
          <InformationCard>
            <template v-slot:icon>
              <font-awesome-icon icon="user-friends" class="text-3xl text-white justify-self-center self-center"/>
            </template>
            <template v-slot:heading>Squad</template>
            <template v-slot:body>
              Squads comprise a self-organising team that work on a specific aspect of the project.
            </template>
          </InformationCard>
          <InformationCard>
            <template v-slot:icon>
              <font-awesome-icon icon="tag" class="text-3xl text-white justify-self-center self-center"/>
            </template>
            <template v-slot:heading>Chapter</template>
            <template v-slot:body>
              Chapters are related members who are spread across multiple squads.
            </template>
          </InformationCard>
        </div>
      </div>
    </section>

    <div class="flex-none pt-6">
      <Footer/>
    </div>
  </div>
</template>

<script>
/* eslint-disable */
import Navbar from "@/components/navbar/Navbar";
import SquadCard from "@/components/squads/SquadCard";
import { getTribe } from "@/config/api/tribe.api";
import Footer from "@/components/footer/Footer";
import StyledButton from "@/components/atoms/buttons/StyledButton";
import InformationCard from "@/components/home/InformationCard";
import InformationBlock from "@/components/home/InformationBlock";

export default {
  name: "HomePage",
  components: {
    InformationBlock,
    StyledButton,
    Footer,
    SquadCard,
    Navbar,
    InformationCard,
  },
  data() {
    return {
      isLoaded: false,
      tribe: undefined,
    };
  },
  async mounted() {
    try {
      this.tribe = await getTribe({id: "00000000-0000-0000-0000-000000000001"});
      this.isLoaded = true;
    } catch {
      await this.$router.replace("/not-found");
    }
  },
};
</script>
