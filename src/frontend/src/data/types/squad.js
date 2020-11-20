import Chapter from "@/data/types/chapter";

export default class Squad {
  constructor(config) {
    this._id = Object.prototype.hasOwnProperty.call(config, "id") ? config.id : "";
    this._name = Object.prototype.hasOwnProperty.call(config, "name") ? config.name : "";
    this._colour = "#FF00FF"; // TODO: COLOUR
    this._members = Object.prototype.hasOwnProperty.call(config, "members") ? config.members : [];
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

  get colour() {
    return this._colour;
  }

  set colour(newColour) {
    this._colour = newColour;
  }

  get members() {
    return this._members;
  }

  updateChapter(id, name, colour) {
    this.members.forEach((member, index) => {
      if (member.chapter && member.chapter.id === id) {
        this.members[index].chapter = new Chapter(member.chapter.id, name, colour);
      }
    });
  }

  deleteChapter(id) {
    this.members.forEach((member, index) => {
      if (member.chapter && member.chapter.id === id) {
        this.members[index].chapter = undefined;
      }
    });
  }

  removeMember(member) {
    this._members = this.members.filter((m) => m.id !== member.id);
  }

  addMember(member) {
    this._members.push(member);
  }
}
