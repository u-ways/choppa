import { hasPropertyOrDefault } from "@/utils/hasPropertyOrDefault";

export default class Account {
  constructor(config) {
    this._name = hasPropertyOrDefault(config, "name", "");
  }

  get name() {
    return this._name;
  }
}
