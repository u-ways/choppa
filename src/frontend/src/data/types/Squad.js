import Chapter from "@/data/types/Chapter";

export default class Squad {
  constructor(id, name, colour, members) {
    this._id = id;
    this._name = name;
    this._colour = colour;
    this._members = members;
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
    this.members
      .map((member) => member.chapter)
      .filter((chapter) => chapter && chapter.id === id)
      .map((chapter) => new Chapter(chapter.id, name, colour))
      .forEach((chapter) => console.log(`${chapter.name} ${chapter.colour}`));
  }
}
