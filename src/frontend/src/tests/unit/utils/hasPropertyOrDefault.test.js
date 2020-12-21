import { describe, expect, it } from "@/tests/unit/test-imports";
import { hasPropertyOrDefault } from "@/utils/hasPropertyOrDefault";

const configTest = {
  property: "testValue",
};

describe("Has Property Or Default test", () => {
  it("Has Property Or Default returns the property if it exist", () => {
    expect(
      hasPropertyOrDefault(configTest, "property", "defaultValue"),
    ).toBe("testValue");
  });

  it("Has Property Or Default returns the default if it doesn't exist", () => {
    expect(
      hasPropertyOrDefault(configTest, "fakeProperty", "defaultValue"),
    ).toBe("defaultValue");
  });
});
