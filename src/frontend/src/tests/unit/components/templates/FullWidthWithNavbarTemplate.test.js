import { describe, expect, it, shallowMount } from "@/tests/unit/test-imports";
import FullWidthWithNavbarTemplate from "@/_old/components/templates/FullWidthWithNavbarTemplate";

describe("Full Width With Navbar Template test", () => {
  it("Renders correctly", async () => {
    const wrapper = shallowMount(FullWidthWithNavbarTemplate, {
      slots: {
        default: "Full Width With Navbar Template Test Slot",
      },
    });

    expect(wrapper.html()).toMatchSnapshot();
  });
});
