import { v4 as uuidv4 } from "uuid";
import { hasPropertyOrDefault } from "@/utils/hasPropertyOrDefault";

export default class Squad {
  constructor(config) {
    this._id = hasPropertyOrDefault(config, "id", `squads/${uuidv4()}`);
    this._name = hasPropertyOrDefault(config, "name", "");
    this._color = hasPropertyOrDefault(config, "color", "");
    this._members = hasPropertyOrDefault(config, "members", []);
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

  get color() {
    return this._color;
  }

  set color(newColor) {
    this._color = newColor;
  }

  get members() {
    return this._members;
  }

  get relations() {
    return this._relations;
  }

  get path() {
    return this._id.replace("squads/", "");
  }

  addMember(member) {
    if (this.members.includes(member) === false) {
      this.members.push(member);
    }
  }

  removeMember(member) {
    this._members = this.members.filter((m) => m.id !== member.id);
  }
}
