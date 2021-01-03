import { describe, expect, it } from "@/tests/unit/test-imports";
import Member from "@/models/domain/member";
import Chapter from "@/models/domain/chapter";

describe("Member test", () => {
  it("Constructor assigns properties correctly", () => {
    const chapter = new Chapter({
      id: 1,
      name: "Developer",
      color: "#FF00FF",
    });

    const member = new Member({
      id: 55,
      name: "Mr Choppa",
      chapter,
      relations: { relationTest: "test relation" },
    });

    expect(member.id).toBe(55);
    expect(member.name).toBe("Mr Choppa");
    expect(member.chapter).toBe(chapter);
    expect(member.relations.relationTest).toBe("test relation");
  });
});
