import { describe, it, expect } from "@/tests/unit/test-imports";
import Member from "@/data/types/Member";
import Squad from "@/data/types/Squad";

describe("Squad test", () => {
  it("Constructor assigns properties correctly", () => {
    const members = [
      new Member(7, "Member 1"),
      new Member(8, "Member 2"),
      new Member(9, "Member 3"),
    ];

    const squad = new Squad(21, "Choppa Squad", "#FF00FF", members);
    expect(squad.id).toBe(21);
    expect(squad.name).toBe("Choppa Squad");
    expect(squad.colour).toBe("#FF00FF");
    expect(squad.members).toBe(members);
  });
});
