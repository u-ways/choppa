import { describe, expect, it, shallowMount } from "@/tests/unit/test-imports";
import ChoppaLogoAtom from "@/_old/components/atoms/ChoppaLogoAtom";

describe("Choppa Logo Atom test", () => {
  it("Renders correctly", async () => {
    const wrapper = shallowMount(ChoppaLogoAtom, {
      propsData: {
        css: "my-css-test",
      },
      stubs: {
        "font-awesome-icon": true,
      },
    });

    expect(wrapper.html()).toMatchSnapshot();
  });
});
