<template>
  <div>
    <section id="hero" class="dark bg-choppa-two pb-6">
      <Navbar/>
      <div class="text-white flex-grow px-3 mt-3 mx-auto lg:mt-6">
        <div class="container mx-auto max-w-screen-lg">
          <div class="flex flex-col lg:flex-row gap-1 place-items-center justify-center">
            <!-- TODO: Change stack to include simplified squad -->
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
      <div class="container px-3 text-center mx-auto">
        <div class="row align-items-center text-center pt-6">
          <span class="text-xl sm:text-2xl text-bold">Team Organisation</span> <br/>
          <span class="text-lg sm:text-xl text-semibold">Organise your team structure with our creation tool.</span>
        </div>
        <div>
          <!-- TODO: Place mocked SquadOrganism here. -->
          asdada
        </div>
      </div>
    </section>
    <section id="Knowledge Sharing">
      <div class="">
        <div class="container px-3 text-center mx-auto">
          <div class="row align-items-center text-center pt-6">
            <span class="text-xl sm:text-2xl text-bold">Knowledge Sharing</span> <br/>
            <span class="text-lg sm:text-xl text-semibold">
            Utilise several rotation strategies to <br/>
              maximise knowledge sharing within your team.
          </span>
          </div>
          <div>
            <!-- TODO: Rotation animation here. -->
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
/* eslint-disable */
import Navbar from "@/components/navbar/Navbar";
import SquadSkeleton from "@/components/squads/SquadSkeleton";

export default {
  name: "HomePage",
  components: {
    SquadSkeleton,
    Navbar,
  },
};
</script>
