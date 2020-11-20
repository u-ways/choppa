import { createLocalVue, describe, expect, it, shallowMount } from "@/tests/unit/test-imports";
import VueRouter from "vue-router";
import ViewTribePage from "@/components/pages/tribe/ViewTribePage";
import Tribe from "@/data/types/tribe";
import Squad from "@/data/types/squad";
import Member from "@/data/types/member";

const tribe = new Tribe(4096, "Mr Tribe", [
  new Squad(1024, "Mr SquadOne", "#FFFFFF", [
    new Member(1, "Johnny"),
    new Member(2, "Robert"),
    new Member(3, "Peter"),
    new Member(10, "Steve"),
    new Member(11, "Johnny"),
  ]),
  new Squad(2048, "Mr SquadTwo", "#FFFFFF", [
    new Member(1, "Johnny"),
    new Member(2, "Robert"),
    new Member(3, "Peter"),
  ]),
]);

describe("View Tribe Page test", () => {
  it("Renders correctly", async () => {
    const localVue = createLocalVue();
    localVue.use(VueRouter);
    const router = new VueRouter();

    const wrapper = shallowMount(ViewTribePage, {
      localVue,
      router,
      data() {
        return {
          tribe,
        };
      },
    });

    expect(wrapper.html()).toMatchSnapshot();
  });
});
