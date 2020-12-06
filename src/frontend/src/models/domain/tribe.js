import { v4 as uuidv4 } from "uuid";
import { hasPropertyOrDefault } from "@/utils/hasPropertyOrDefault";

export default class Tribe {
  constructor(config) {
    this._id = hasPropertyOrDefault(config, "id", `tribes/${uuidv4()}`);
    this._name = hasPropertyOrDefault(config, "name", "");
    this._squads = hasPropertyOrDefault(config, "squads", []);
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

  allDistinctChapters() {
    const uniques = {};
    this.squads
      .flatMap((squad) => squad.members)
      .filter((member) => member.chapter)
      .flatMap((member) => member.chapter)
      .forEach((chapter) => {
        uniques[chapter.id] = chapter;
      });

    return Object.values(uniques);
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

  removeSquadById(squadId) {
    this._squads = this.squads.filter((squad) => squad.id !== squadId);
  }

  findSquadById(squadId) {
    const result = this.squads.filter((squad) => squad.id === squadId);
    if (!result[0]) {
      throw new Error(`No squad found with id ${squadId}`);
    }

    return result[0];
  }

  updateChapter(id, name, color) {
    this.squads.forEach((squad) => squad.updateChapter(id, name, color));
  }

  deleteChapter(id) {
    this.squads.forEach((squad) => squad.deleteChapter(id));
  }

  addSquad(squad) {
    this.squads.push(squad);
  }
}
