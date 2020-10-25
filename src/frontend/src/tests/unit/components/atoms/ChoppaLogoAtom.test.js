import {
  describe, it, render, screen, expect,
} from "@/tests/unit/test-imports";
import ChoppaLogoAtom from "@/components/atoms/ChoppaLogoAtom";

describe("Choppa Logo Atom test", () => {
  it("Applies the CSS prop onto the icon", async () => {
    render(ChoppaLogoAtom, {
      props: {
        css: "my-css-test",
      },
      stubs: {
        "font-awesome-icon": { template: `<span data-testid="testing-icon"></span>` },
      },
    });

    expect(screen.getByTestId("testing-icon")).toHaveClass("my-css-test");
  });
});
