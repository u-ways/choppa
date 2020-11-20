import { describe, expect, it } from "@/tests/unit/test-imports";
import Squad from "@/data/types/squad";
import Tribe from "@/data/types/tribe";
import Member from "@/data/types/member";

describe("Tribe test", () => {
  it("Constructor assigns properties correctly", () => {
    const squadOne = new Squad(1, "Mr SquadOne", "#FFFFFF", []);
    const squadTwo = new Squad(2, "Mr SquadTwo", "#FFFFFF", []);
    const tribe = new Tribe(55, "Mr Tribe", [squadOne, squadTwo]);

    expect(tribe.id).toBe(55);
    expect(tribe.name).toBe("Mr Tribe");
    expect(tribe.squads).toEqual([squadOne, squadTwo]);
  });

  it("Distinct Members returns the correct Members", () => {
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

  it("Delete Squad By Id removes the correct Squad", () => {
    const squadOne = new Squad(1024, "Mr SquadOne", "#FFFFFF", []);
    const squadTwo = new Squad(2048, "Mr SquadTwo", "#FFFFFF", []);
    const tribe = new Tribe(4096, "Mr Tribe", [squadOne, squadTwo]);

    tribe.removeSquadById(1024);
    const remainingSquadIds = tribe.squads.map((squad) => squad.id);
    expect(remainingSquadIds).toIncludeAllMembers([2048]);
  });

  it("Delete Squad By Id handles a Squad that doesn't exist", () => {
    const squadOne = new Squad(1024, "Mr SquadOne", "#FFFFFF", []);
    const squadTwo = new Squad(2048, "Mr SquadTwo", "#FFFFFF", []);
    const tribe = new Tribe(4096, "Mr Tribe", [squadOne, squadTwo]);

    tribe.removeSquadById(9999);
    const remainingSquadIds = tribe.squads.map((squad) => squad.id);
    expect(remainingSquadIds).toIncludeAllMembers([1024, 2048]);
  });

  it("Find Squad By Id finds the correct Squad", () => {
    const squadOne = new Squad(1024, "Mr SquadOne", "#FFFFFF", []);
    const squadTwo = new Squad(2048, "Mr SquadTwo", "#FFFFFF", []);
    const tribe = new Tribe(4096, "Mr Tribe", [squadOne, squadTwo]);

    const squad = tribe.findSquadById(1024);
    expect(squad.id).toBe(1024);
  });

  it("Find Squad By Id throws an exception if the Squad isn't found", () => {
    const squadOne = new Squad(1024, "Mr SquadOne", "#FFFFFF", []);
    const squadTwo = new Squad(2048, "Mr SquadTwo", "#FFFFFF", []);
    const tribe = new Tribe(4096, "Mr Tribe", [squadOne, squadTwo]);

    expect(() => {
      tribe.findSquadById(9999);
    }).toThrowWithMessage(Error, "No squad found with id 9999");
  });
});
