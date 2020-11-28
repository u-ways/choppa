import { v4 as uuidv4 } from "uuid";

export default class Member {
  constructor(config) {
    this._id = Object.prototype.hasOwnProperty.call(config, "id") ? config.id : `members/${uuidv4()}`;
    this._name = Object.prototype.hasOwnProperty.call(config, "name") ? config.name : "";
    this._chapter = Object.prototype.hasOwnProperty.call(config, "chapter") ? config.chapter : null;
    this._newlyCreated = Object.prototype.hasOwnProperty.call(config, "newlyCreated") ? config.newlyCreated : false;
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

  set chapter(newChapter) {
    this._chapter = newChapter;
  }

  get chapter() {
    return this._chapter;
  }

  get newlyCreated() {
    return this._newlyCreated;
  }
}
