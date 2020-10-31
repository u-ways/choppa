import { describe, expect, it } from "@/tests/unit/test-imports";
import Squad from "@/data/types/Squad";
import Tribe from "@/data/types/Tribe";
import Member from "@/data/types/Member";

describe("Tribe test", () => {
  it("Constructor assigns properties correctly", () => {
    const squadOne = new Squad(1, "Mr SquadOne", "#FFFFFF", []);
    const squadTwo = new Squad(2, "Mr SquadTwo", "#FFFFFF", []);
    const tribe = new Tribe(55, "Mr Tribe", [squadOne, squadTwo]);

    expect(tribe.id).toBe(55);
    expect(tribe.name).toBe("Mr Tribe");
    expect(tribe.squads).toEqual([squadOne, squadTwo]);
  });

  it("Distinct members returns the correct members", () => {
    const squadOne = new Squad(1, "Mr SquadOne", "#FFFFFF", [
      new Member(1, "Johnny"),
      new Member(2, "Robert"),
      new Member(3, "Peter"),
    ]);

    const squadTwo = new Squad(2, "Mr SquadTwo", "#FFFFFF", [
      new Member(1, "Johnny"),
      new Member(10, "Steve"),
      new Member(11, "Johnny"),
    ]);

    const tribe = new Tribe(55, "Mr Tribe", [squadOne, squadTwo]);
    expect(tribe.allDistinctMembers()).toIncludeAllMembers([
      new Member(1, "Johnny"),
      new Member(2, "Robert"),
      new Member(3, "Peter"),
      new Member(10, "Steve"),
      new Member(11, "Johnny"),
    ]);
  });
});
