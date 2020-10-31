import { createLocalVue, describe, expect, it, shallowMount } from "@/tests/unit/test-imports";
import VueRouter from "vue-router";
import TribeOrganism from "@/components/organisms/TribeOrganism";
import Tribe from "@/data/types/Tribe";
import Squad from "@/data/types/Squad";
import Member from "@/data/types/Member";

const testTribe = new Tribe(1, "Test Tribe", [
  new Squad(5, "Squad One", "#FF00FF", [new Member(10, "Johnny")]),
  new Squad(6, "Squad Two", "#00FFFF", [new Member(10, "Johnny"), new Member(11, "Robert")]),
]);

describe("Tribe Organism test", () => {
  it("Renders correctly", async () => {
    const localVue = createLocalVue();
    localVue.use(VueRouter);
    const router = new VueRouter();

    const wrapper = shallowMount(TribeOrganism, {
      localVue,
      router,
      propsData: {
        tribe: testTribe,
      },
    });

    expect(wrapper.html()).toMatchSnapshot();
  });
});
