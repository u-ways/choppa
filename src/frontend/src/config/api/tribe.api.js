import httpClient from "@/config/api/http-client";
import Tribe from "@/models/tribe";
import { deleteSquad, getSquadsByQuery, saveSquad } from "@/config/api/squad.api";
import hasMissingOrTrueProperty from "@/helpers/hasMissingOrTrueProperty";
import { getChaptersByQuery } from "@/config/api/chapter.api";

async function deserializeTribe(config, json) {
  let chapters = [];
  if (hasMissingOrTrueProperty(config, "loadChapters")) {
    chapters = await getChaptersByQuery({ url: json.chapters });
  }

  let squads = [];
  if (hasMissingOrTrueProperty(config, "loadSquads")) {
    squads = await getSquadsByQuery({ url: json.squads, loadMembers: true, chapters });
  }

  return new Tribe({
    id: json.id,
    name: json.name,
    squads,
  });
}

export async function getTribe(config) {
  const url = Object.prototype.hasOwnProperty.call(config, "url") ? config.url : `tribes/${config.id}`;
  const response = await httpClient.get(url);

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
