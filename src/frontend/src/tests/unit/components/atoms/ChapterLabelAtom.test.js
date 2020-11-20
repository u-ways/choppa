import { describe, expect, it, shallowMount } from "@/tests/unit/test-imports";
import ChapterLabelAtom from "@/components/atoms/ChapterLabelAtom";
import Chapter from "@/data/types/chapter";

describe("Chapter Label Atom test", () => {
  it("Renders correctly", async () => {
    const wrapper = shallowMount(ChapterLabelAtom, {
      propsData: {
        chapter: new Chapter(1, "Chapter Test", "#FF00FF"),
      },
    });

    expect(wrapper.html()).toMatchSnapshot();
  });
});
