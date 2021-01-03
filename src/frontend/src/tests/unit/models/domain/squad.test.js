import { describe, expect, it } from "@/tests/unit/test-imports";
import Member from "@/models/domain/member";
import Squad from "@/models/domain/squad";

describe("Squad test", () => {
  it("Constructor assigns properties correctly", () => {
    const members = [
      new Member({ id: 7, name: "Member 1" }),
      new Member({ id: 8, name: "Member 2" }),
      new Member({ id: 9, name: "Member 3" }),
    ];

    const squad = new Squad({
      id: 21,
      name: "Choppa Squad",
      color: "#FF00FF",
      members,
      relations: { relationTest: "test relation" },
    });

    expect(squad.id).toBe(21);
    expect(squad.name).toBe("Choppa Squad");
    expect(squad.color).toBe("#FF00FF");
    expect(squad.members).toBe(members);
    expect(squad.relations.relationTest).toBe("test relation");
  });
});
