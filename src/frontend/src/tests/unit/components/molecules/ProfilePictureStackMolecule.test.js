import { describe, expect, it, shallowMount } from "@/tests/unit/test-imports";
import ProfilePictureStackMolecule from "@/_old/components/molecules/ProfilePictureStackMolecule";
import Member from "@/data/types/member";

const memberJohnny = new Member("1", "Johnny");
const memberRobert = new Member("2", "Robert");
const memberLucie = new Member("3", "Lucie");
const memberPeter = new Member("4", "Peter");
const memberKelly = new Member("5", "Kelly");
const members = [memberJohnny, memberRobert, memberLucie, memberPeter, memberKelly];

describe("Profile Picture Stack Molecule test", () => {
  it("Shows the correct amount of members", () => {
    expect(ProfilePictureStackMolecule.computed.membersShowing.call({
      members,
      showCount: 2,
    })).toIncludeAllMembers([memberJohnny, memberRobert]);

    expect(ProfilePictureStackMolecule.computed.membersShowing.call({
      members,
      showCount: 3,
    })).toIncludeAllMembers([memberJohnny, memberRobert, memberLucie]);

    expect(ProfilePictureStackMolecule.computed.membersShowing.call({
      members,
      showCount: 0,
    })).toBeArrayOfSize(0);
  });

  it("Renders correctly", async () => {
    const wrapper = shallowMount(ProfilePictureStackMolecule, {
      propsData: {
        members,
        showCount: 3,
      },
    });

    expect(wrapper.html()).toMatchSnapshot();
  });
});
