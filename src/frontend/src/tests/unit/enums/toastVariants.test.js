import { describe, expect, it } from "@/tests/unit/test-imports";
import { isValidToastVariant, toastVariants } from "@/enums/toastVariants";

describe("Toast Variants test", () => {
  it("Is Valid Toast Variant returns true if its a valid variant", () => {
    expect(isValidToastVariant(toastVariants.SUCCESS)).toBeTruthy();
  });

  it("Is Valid Toast Variant returns false if its a invalid variant", () => {
    expect(isValidToastVariant("invalidToastVariant")).toBeFalsy();
  });
});
