export default class Tribe {
  constructor(id, name, squads) {
    this._id = id;
    this._name = name;
    this._squads = squads;
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
    this._squads = this._squads.filter((squad) => squad.id !== squadId);
  }

  findSquadById(squadId) {
    const result = this._squads.filter((squad) => squad.id === squadId);
    if (!result[0]) {
      throw new Error(`No squad found with id ${squadId}`);
    }

    return result[0];
  }

  updateChapter(id, name, colour) {
    this.squads.forEach((squad) => squad.updateChapter(id, name, colour));
  }

  deleteChapter(id) {
    this.squads.forEach((squad) => squad.deleteChapter(id));
  }

  addSquad(squad) {
    this.squads.push(squad);
  }
}
