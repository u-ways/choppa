import { describe, expect, it, shallowMount } from "@/tests/unit/test-imports";
import FixedWidthWithNavbarTemplate from "@/components/templates/FixedWidthWithNavbarTemplate";

describe("Fixed Width With Navbar Template test", () => {
  it("Renders correctly", async () => {
    const wrapper = shallowMount(FixedWidthWithNavbarTemplate, {
      slots: {
        default: "Fixed Width With Navbar Template Test Slot",
      },
    });

    expect(wrapper.html()).toMatchSnapshot();
  });
});
