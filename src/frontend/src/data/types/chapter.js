export default class Chapter {
  constructor(config) {
    this._id = Object.prototype.hasOwnProperty.call(config, "id") ? config.id : "";
    this._name = Object.prototype.hasOwnProperty.call(config, "name") ? config.name : "";
    this._colour = "#FF00FF"; // TODO: COLOUR
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
}
