import { describe, expect, it } from "@/tests/unit/test-imports";
import Member from "@/data/types/member";
import Chapter from "@/data/types/chapter";

describe("Member test", () => {
  it("Constructor assigns properties correctly", () => {
    const chapter = new Chapter(1, "Developer", "#FF00FF");

    const member = new Member(55, "Mr Choppa", chapter);
    expect(member.id).toBe(55);
    expect(member.name).toBe("Mr Choppa");
    expect(member.chapter).toBe(chapter);
  });
});
