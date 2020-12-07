import { v4 as uuidv4 } from "uuid";
import { hasPropertyOrDefault } from "@/utils/hasPropertyOrDefault";

const DEFAULT_SQUAD_COLOR = "#3068c2";

export default class Squad {
  constructor(config) {
    this._id = hasPropertyOrDefault(config, "id", `squads/${uuidv4()}`);
    this._name = hasPropertyOrDefault(config, "name", "");
    this._color = hasPropertyOrDefault(config, "color", DEFAULT_SQUAD_COLOR);
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
}
