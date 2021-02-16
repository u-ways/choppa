import { hasPropertyOrDefault } from "@/utils/hasPropertyOrDefault";

export default class Account {
  constructor(config) {
    this._id = hasPropertyOrDefault(config, "id", "");
    this._provider = hasPropertyOrDefault(config, "provider", "");
    this._providerId = hasPropertyOrDefault(config, "providerId", "");
    this._name = hasPropertyOrDefault(config, "name", "");
    this._organisationName = hasPropertyOrDefault(config, "organisationName", "");
    this._profilePicture = hasPropertyOrDefault(config, "profilePicture", "");
  }

  get id() {
    return this._id;
  }

  get provider() {
    return this._provider;
  }

  get providerId() {
    return this._providerId;
  }

  get name() {
    return this._name;
  }

  get shortName() {
    return this._name.substr(0, this._name.indexOf(" "));
  }

  get organisationName() {
    return this._organisationName;
  }

  set organisationName(newName) {
    this._organisationName = newName;
  }

  get profilePicture() {
    return this._profilePicture;
  }
  
  get avatarSeed() {
    return this._name;
  }
}
