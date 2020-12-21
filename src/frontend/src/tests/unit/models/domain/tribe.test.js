import { describe, expect, it } from "@/tests/unit/test-imports";
import Squad from "@/models/domain/squad";
import Tribe from "@/models/domain/tribe";
import Member from "@/models/domain/member";

describe("Tribe test", () => {
  it("Constructor assigns properties correctly", () => {
    const squadOne = new Squad({ id: 1, name: "Mr SquadOne", color: "#FFFFFF", members: [] });
    const squadTwo = new Squad({ id: 2, name: "Mr SquadTwo", color: "#FFFFFF", members: [] });

    const tribe = new Tribe({
      id: 55,
      name: "Mr Tribe",
      squads: [squadOne, squadTwo],
      relations: { relationTest: "test relation" },
    });

    expect(tribe.id).toBe(55);
    expect(tribe.name).toBe("Mr Tribe");
    expect(tribe.squads).toEqual([squadOne, squadTwo]);
    expect(tribe.relations.relationTest).toBe("test relation");
  });

  it("Distinct Members returns the correct Members", () => {
    const squadOne = new Squad(1, "Mr SquadOne", "#FFFFFF", [
      new Member({ id: 1, name: "Johnny" }),
      new Member({ id: 2, name: "Robert" }),
      new Member({ id: 3, name: "Peter" }),
    ]);

    const squadTwo = new Squad({ id: 2,
      name: "Mr SquadTwo",
      color: "#FFFFFF",
      members: [
        new Member({ id: 1, name: "Johnny" }),
        new Member({ id: 10, name: "Steve" }),
        new Member({ id: 11, name: "Johnny" }),
      ] });

    const tribe = new Tribe({
      id: 55,
      name: "Mr Tribe",
      squads: [squadOne, squadTwo],
    });

    expect(tribe.allDistinctMembers().map((member) => member.id)).toIncludeAllMembers([1]);
  });
});
