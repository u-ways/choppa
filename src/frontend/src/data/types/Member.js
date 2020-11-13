export default class Member {
  constructor(id, name, chapter) {
    this._id = id;
    this._name = name;
    this._chapter = chapter;
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

  get chapter() {
    return this._chapter;
  }
}
