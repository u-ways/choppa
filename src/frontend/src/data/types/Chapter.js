export default class Chapter {
  constructor(id, name, colour) {
    this._id = id;
    this._name = name;
    this._colour = colour;
  }

  get id() {
    return this._id;
  }

  get name() {
    return this._name;
  }

  get colour() {
    return this._colour;
  }
}
