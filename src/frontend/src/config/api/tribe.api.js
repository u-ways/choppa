import httpClient from "@/config/api/http-client";
import Tribe from "@/models/domain/tribe";
import { getSquadsByQuery } from "@/config/api/squad.api";

async function deserializeTribe(config, json) {
  return new Tribe({
    id: json.id,
    name: json.name,
    squads: await getSquadsByQuery({ url: json.squads }),
    relations: {
      chapters: json.chapters,
      members: json.members,
      squads: json.squads,
      iterations: json.iterations,
      history: json.history,
    },
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
}

export async function rotateTribe(config) {
  await httpClient.post(`${config.tribe.id}:rotate`, {
    amount: config.amount,
    chapter: config.chapter.id,
    filter: config.filter,
    strategy: config.strategy,
  });
}
