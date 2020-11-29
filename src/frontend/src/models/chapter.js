import { v4 as uuidv4 } from "uuid";

const DEFAULT_CHAPTER_COLOR = "#3068c2";

export default class Chapter {
  constructor(config) {
    this._id = Object.prototype.hasOwnProperty.call(config, "id") ? config.id : `chapters/${uuidv4()}`;
    this._name = Object.prototype.hasOwnProperty.call(config, "name") ? config.name : "";
    this._color = Object.prototype.hasOwnProperty.call(config, "color") ? config.color : DEFAULT_CHAPTER_COLOR;
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
}
