import { describe, expect, it } from "@/tests/unit/test-imports";
import { stripAlphaFromHex } from "@/utils/stripAlphaFromHex";

describe("Strip Alpha From Hex test", () => {
  it("Strip Alpha From Hex returns the hex code unedited if an alpha code was not present", () => {
    expect(stripAlphaFromHex("#FF00FF")).toBe("#FF00FF");
  });

  it("Strip Alpha From Hex returns the hex code without the alpha code if an alpha code was present", () => {
    expect(stripAlphaFromHex("#FF00FF55")).toBe("#FF00FF");
  });
});
