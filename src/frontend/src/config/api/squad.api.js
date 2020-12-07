import httpClient from "@/config/api/http-client";
import Squad from "@/models/domain/squad";
import { deserializeMember } from "@/config/api/member.api";

async function deserializeSquad(json) {
  return new Squad({
    id: json.id,
    tribeId: json.tribe,
    name: json.name,
    members: await Promise.all(json.members.map((member) => deserializeMember(member))),
    color: json.color,
    relations: {
      tribe: json.tribe,
      chapters: json.chapters,
      iterations: json.iterations,
      history: json.history,
    },
  });
}

export async function getSquad(config) {
  const url = Object.prototype.hasOwnProperty.call(config, "url") ? config.url : `squads/${config.id}`;
  const response = await httpClient.get(url);

  return deserializeSquad(response.data);
}

export async function getSquadsByQuery(config) {
  try {
    const response = await httpClient.get(config.url);
    return Promise.all(response.data.map((squad) => deserializeSquad(squad)));
  } catch (error) {
    if (error.response.status === 404) {
      return [];
    }

    throw error;
  }
}

export async function saveSquad(config) {
  await httpClient.put(config.squad.id, {
    id: config.squad.id,
    tribe: config.squad.relations.tribe,
    name: config.squad.name,
    color: config.squad.color,
    members: config.squad.members.map((member) => member.id),
  });
}
