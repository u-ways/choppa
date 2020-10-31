import { describe, expect, it } from "@/tests/unit/test-imports";
import Member from "@/data/types/Member";

describe("Member test", () => {
  it("Constructor assigns properties correctly", () => {
    const member = new Member(55, "Mr Choppa");
    expect(member.id).toBe(55);
    expect(member.name).toBe("Mr Choppa");
  });
});
