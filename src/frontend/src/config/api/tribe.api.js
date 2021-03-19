import httpClient from "@/config/api/http-client";
import Tribe from "@/models/domain/tribe";
import { getSquadsByQuery } from "@/config/api/squad.api";

function serializeTribe(tribe) {
  return {
    id: tribe.id,
    name: tribe.name,
    color: tribe.color,
  };
}

async function deserializeTribe(json) {
  return new Tribe({
    id: json.id,
    name: json.name,
    color: json.color,
    squads: await getSquadsByQuery({ url: json.squads }),
    relations: {
      chapters: json.chapters,
      members: json.members,
      squads: json.squads,
    },
  });
}

export async function getAllTribes() {
  try {
    const response = await httpClient.get("tribes");
    return Promise.all(response.data.map((tribe) => deserializeTribe(tribe)));
  } catch (error) {
    if (error.response.status === 404) {
      return [];
    }

    throw error;
  }
}

export async function getTribe(config) {
  const url = Object.prototype.hasOwnProperty.call(config, "url") ? config.url : `tribes/${config.id}`;
  const response = await httpClient.get(url);

  return deserializeTribe(response.data);
}

export async function createTribe(config) {
  await httpClient.post("tribes", [serializeTribe(config.tribe)]);
}

export async function saveTribe(config) {
  await httpClient.put(config.tribe.id, serializeTribe(config.tribe));
}

export async function rotateTribeSmart(config) {
  await httpClient.post(`${config.tribe.id}:smr`, {
    amount: config.amount,
    chapter: config.chapter.id,
    filter: config.filter,
    strategy: config.strategy,
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

export async function deleteTribe(config) {
  await httpClient.delete(config.tribe.id, { data: config.tribe.id });
}
