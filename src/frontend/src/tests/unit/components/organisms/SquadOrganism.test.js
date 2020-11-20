import { describe, expect, it, shallowMount } from "@/tests/unit/test-imports";
import Squad from "@/data/types/squad";
import Member from "@/data/types/member";
import SquadOrganism from "@/components/organisms/SquadOrganism";

const testSquad = new Squad(6, "Squad Two", "#00FFFF", [
  new Member(10, "Johnny"),
  new Member(11, "Robert"),
]);

describe("Squad Organism test", () => {
  it("Renders correctly", async () => {
    const wrapper = shallowMount(SquadOrganism, {
      propsData: {
        squad: testSquad,
      },
    });

    expect(wrapper.html()).toMatchSnapshot();
  });
});
