import { v4 as uuidv4 } from "uuid";
import { hasPropertyOrDefault } from "@/utils/hasPropertyOrDefault";

export default class Member {
  constructor(config) {
    this._id = hasPropertyOrDefault(config, "id", `members/${uuidv4()}`);
    this._name = hasPropertyOrDefault(config, "name", "");
    this._chapter = hasPropertyOrDefault(config, "chapter", null);
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

  set chapter(newChapter) {
    this._chapter = newChapter;
  }

  get chapter() {
    return this._chapter;
  }

  get relations() {
    return this._relations;
  }
}
