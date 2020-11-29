import httpClient from "@/config/api/http-client";
import Tribe from "@/models/tribe";
import { deleteSquad, getSquads, saveSquad } from "@/config/api/squad.api";

function getUrlOrId(config) {
  return Object.prototype.hasOwnProperty.call(config, "url")
    ? config.url
    : `tribes/${config.id}`;
}

async function deserializeTribe(config, json) {
  let squads = [];
  if (!Object.prototype.hasOwnProperty.call(config, "loadSquads") || config.loadSquads) {
    squads = await getSquads({ tribeId: json.id, loadMembers: true });
  }

  return new Tribe({
    id: json.id,
    name: json.name,
    squads,
  });
}

export async function getTribe(config) {
  const response = await httpClient.get(getUrlOrId(config));
  return deserializeTribe(config, response.data);
}

export async function saveTribe(config) {
  await httpClient.put(config.tribe.id, {
    id: config.tribe.id,
    name: config.tribe.name,
  });

  if (!Object.prototype.hasOwnProperty.call(config, "saveSquads") || config.saveSquads) {
    await config.tribe.squads.forEach((squad) => saveSquad({
      squad,
      tribeId: config.tribe.id,
      saveMembers: true,
    }));
  }

  if (!Object.prototype.hasOwnProperty.call(config, "deleteSquads") || config.deleteSquads) {
    await config.tribe.deletedSquads.filter((squad) => !squad.newlyCreated)
      .forEach((squad) => deleteSquad({ squad }));
  }
}
