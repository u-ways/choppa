import { v4 as uuidv4 } from "uuid";
import { hasPropertyOrDefault } from "@/utils/hasPropertyOrDefault";

export default class Tribe {
  constructor(config) {
    this._id = hasPropertyOrDefault(config, "id", `tribes/${uuidv4()}`);
    this._name = hasPropertyOrDefault(config, "name", "");
    this._squads = hasPropertyOrDefault(config, "squads", []);
    this._relations = hasPropertyOrDefault(config, "relations", {});
  }

  get id() {
    return this._id;
  }

  get name() {
    return this._name;
  }

  set name(newName) {
    this._name = newName;
  }

  get squads() {
    return this._squads;
  }

  get relations() {
    return this._relations;
  }

  allDistinctMembers() {
    const uniques = {};
    this.squads
      .flatMap((squad) => squad.members)
      .forEach((member) => {
        uniques[member.id] = member;
      });

    return Object.values(uniques);
  }
}
