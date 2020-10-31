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
