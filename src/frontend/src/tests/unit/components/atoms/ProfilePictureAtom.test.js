/* eslint-disable import/first */
jest.mock("jdenticon"); // eslint-disable-line no-undef

import * as jdenticon from "jdenticon";
import { describe, expect, it, shallowMount } from "@/tests/unit/test-imports";
import ProfilePictureAtom from "@/components/atoms/ProfilePictureAtom";
import Member from "@/data/types/member";

const member = new Member(1, "Taylor");

describe("Profile Picture Atom test", () => {
  it("Render Profile Picture updates the canvas and sets the flag", async () => {
    const wrapper = shallowMount(ProfilePictureAtom, {
      propsData: {
        member,
        css: "my-css-class-test",
      },
      stubs: {
        "font-awesome-icon": true,
      },
    });

    await wrapper.vm.$nextTick();
    jdenticon.update.mockClear();
    await wrapper.setData({ hasRenderedProfilePicture: false });

    wrapper.vm.renderProfilePicture();
    await wrapper.vm.$nextTick();
    expect(jdenticon.update).toHaveBeenCalledTimes(1);
    expect(wrapper.vm.$data.hasRenderedProfilePicture).toBeTruthy();
  });

  it("Renders correctly with fallback image", async () => {
    const wrapper = shallowMount(ProfilePictureAtom, {
      propsData: {
        member,
        css: "my-css-class-test",
      },
      stubs: {
        "font-awesome-icon": true,
      },
    });

    expect(wrapper.html()).toMatchSnapshot();
  });

  it("Renders correctly with user's icon", async () => {
    const wrapper = shallowMount(ProfilePictureAtom, {
      propsData: {
        member,
        css: "my-css-class-test",
      },
      stubs: {
        "font-awesome-icon": true,
      },
    });

    await wrapper.setData({ hasRenderedProfilePicture: true });
    expect(wrapper.html()).toMatchSnapshot();
  });
});
