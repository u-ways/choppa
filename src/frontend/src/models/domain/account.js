import { hasPropertyOrDefault } from "@/utils/hasPropertyOrDefault";

export default class Account {
  constructor(config) {
    this._name = hasPropertyOrDefault(config, "name", "");
  }

  get name() {
    return this._name;
  }

  get shortName() {
    return this._name.substr(0, this._name.indexOf(" "));
  }

  get avatarSeed() {
    return this._name;
  }
}
