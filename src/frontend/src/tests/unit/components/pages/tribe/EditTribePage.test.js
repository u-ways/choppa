import { createLocalVue, describe, expect, it, shallowMount } from "@/tests/unit/test-imports";
import VueRouter from "vue-router";
import EditTribePage from "@/components/pages/tribe/EditTribePage";
import Member from "@/data/types/Member";
import Squad from "@/data/types/Squad";
import Tribe from "@/data/types/Tribe";

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

function mountComponent() {
  const localVue = createLocalVue();
  localVue.use(VueRouter);
  const router = new VueRouter();

  return shallowMount(EditTribePage, {
    localVue,
    router,
    data() {
      return {
        tribe,
      };
    },
    stubs: {
      "font-awesome-icon": true,
    },
  });
}

describe("Edit Tribe Page test", () => {
  it("nameInputId", () => {
    const wrapper = mountComponent();
    expect(wrapper.vm.nameInputId(5)).toBe("squad-name-5");
  });

  it("deleteSquad", async () => {
    const mock = jest.spyOn(Tribe.prototype, "removeSquadById");

    const wrapper = mountComponent();
    wrapper.vm.deleteSquad(5);
    expect(mock).toHaveBeenCalledTimes(1);
    expect(mock).toHaveBeenCalledWith(5);

    jest.restoreAllMocks();
  });

  it("onSquadColourChanged", async () => {
    const squad = new Squad("250", "Test Colour Squad", "old colour", []);
    jest.spyOn(Tribe.prototype, "findSquadById").mockReturnValue(squad);

    const wrapper = mountComponent();
    wrapper.vm.onSquadColourChanged({
      squadId: 5,
      colour: {
        hex: "new colour",
      },
    });

    expect(squad.colour).toEqual("new colour");
    jest.restoreAllMocks();
  });

  it("Renders correctly", async () => {
    const wrapper = mountComponent();
    expect(wrapper.html()).toMatchSnapshot();
  });
});
