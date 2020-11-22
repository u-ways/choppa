const DEFAULT_SQUAD_COLOR = "#3068c2";

export default class Squad {
  constructor(config) {
    this._id = Object.prototype.hasOwnProperty.call(config, "id") ? config.id : "";
    this._name = Object.prototype.hasOwnProperty.call(config, "name") ? config.name : "";
    this._color = Object.prototype.hasOwnProperty.call(config, "color") ? config.color : DEFAULT_SQUAD_COLOR;
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

  get color() {
    return this._color;
  }

  set color(newColor) {
    this._color = newColor;
  }

  get members() {
    return this._members;
  }

  updateChapter(id, name, color) {
    this.members.forEach((member, index) => {
      if (member.chapter && member.chapter.id === id) {
        this.members[index].chapter.name = name;
        this.members[index].chapter.color = color;
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
