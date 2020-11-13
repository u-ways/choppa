import { describe, expect, it, shallowMount } from "@/tests/unit/test-imports";
import ChapterLabelAtom from "@/components/atoms/ChapterLabelAtom";

describe("Chapter Label Atom test", () => {
  it("Renders correctly", async () => {
    const wrapper = shallowMount(ChapterLabelAtom, {
      propsData: {
        css: "my-css-test",
      },
    });

    expect(wrapper.html()).toMatchSnapshot();
  });
});
