import { describe, expect, it } from "@/tests/unit/test-imports";
import { isValidThemeSetting, themeSetting } from "@/enums/themeSetting";

describe("Theme Settings test", () => {
  it("Is Valid Theme Setting returns true if its a valid theme", () => {
    expect(isValidThemeSetting(themeSetting.LIGHT_THEME)).toBeTruthy();
  });

  it("Is Valid Theme Setting returns false if its a invalid theme", () => {
    expect(isValidThemeSetting("invalidTheme")).toBeFalsy();
  });
});
