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
            <template v-slot:body>
              Organise your team structure with our agile creation tool, based on the Spotify model.
            </template>
          </InformationBlock>
          <div class="w-full pl-3 lg:w-2/3 py-6">
            <SquadCard :squad="squad"/>
          </div>
        </div>
      </div>
    </section>

    <section id="Glossary" class="bg-gray-100 dark:bg-gray-700">
      <div class="container px-3 py-9 mx-auto max-w-screen-lg">
        <div class="flex flex-col place-items-center gap-1 lg:flex-row">
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

    <KnowledgeSharing/>

    <section id="Demonstration" class="py-16">
      <div class="container px-3 mx-auto max-w-screen-lg">
        <div class="flex flex-col pb-3 place-items-center gap-1">
          <InformationBlock>
            <template slot="heading">
              Time for your first test flight!
            </template>
            <template slot="body">
              Check out this handy demonstration!
            </template>
          </InformationBlock>
          <div class="pt-12 w-full lg:w-1/2">
            <StyledButton class ="text-center" type="link"
                          :link="{ name: 'view-tribe', params: { id: '00000000-0000-0000-0000-000000000001' } }"
                          variant="primary">I'm Ready!</StyledButton>
          </div>
        </div>
      </div>
    </section>

    <div class="flex-none pt-6">
      <Footer/>
    </div>

  </div>
</template>

<script>
import Navbar from "@/components/navbar/Navbar";
import SquadCard from "@/components/squads/SquadCard";
import Footer from "@/components/footer/Footer";
import InformationCard from "@/components/home/InformationCard";
import InformationBlock from "@/components/home/InformationBlock";
import Squad from "@/models/domain/squad";
import Member from "@/models/domain/member";
import Chapter from "@/models/domain/chapter";
import KnowledgeSharing from "@/components/home/KnowledgeSharing";
import StyledButton from "@/components/atoms/buttons/StyledButton";

export default {
  name: "HomePage",
  components: {
    InformationBlock,
    Footer,
    SquadCard,
    Navbar,
    InformationCard,
    KnowledgeSharing,
    StyledButton,
  },
  data() {
    return {
      isLoaded: false,
      tribe: undefined,
      squad: new Squad({
        name: "Stark",
        color: "#FACC15",
        members: [
          new Member({
            name: "Eddard",
            chapter: new Chapter({
              name: "lead",
              color: "#DC2626",
            }),
          }),
          new Member({
            name: "Arya",
            chapter: new Chapter({
              name: "tester",
              color: "#16A34A",
            }),
          }),
          new Member({
            name: "Bran",
            chapter: new Chapter({
              name: "dev",
              color: "#2563EB",
            }),
          }),
          new Member({
            name: "Rob",
            chapter: new Chapter({
              name: "dev",
              color: "#2563EB",
            }),
          }),
          new Member({
            name: "Sansa",
            chapter: new Chapter({
              name: "ba",
              color: "#F5C147",
            }),
          }),
          new Member({
            name: "Rickon",
            chapter: new Chapter({
              name: "intern",
              color: "#9333EA",
            }),
          }),
          new Member({
            name: "Tony",
            chapter: new Chapter({
              name: "intern",
              color: "#9333EA",
            }),
          }),
        ],
      }),
    };
  },
  methods: {},
};
</script>
