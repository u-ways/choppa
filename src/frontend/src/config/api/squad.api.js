import httpClient from "@/config/api/http-client";
import Squad from "@/models/domain/squad";
import { deserializeMember, serializeMember } from "@/config/api/member.api";

function serializeSquad(squad) {
  return {
    id: squad.id,
    tribe: squad.relations.tribeId,
    name: squad.name,
    color: squad.color,
    members: squad.members.map((member) => serializeMember(member)),
  };
}

async function deserializeSquad(json) {
  return new Squad({
    id: json.id,
    name: json.name,
    members: await Promise.all(json.members.map((member) => deserializeMember(member))),
    color: json.color,
    relations: {
      tribeId: json.tribe.replace("tribes/", ""),
      tribe: json.tribe,
      chapters: json.chapters,
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
  await httpClient.put(config.squad.id, serializeSquad(config.squad));
}

export async function createSquad(config) {
  await httpClient.post("squads", [serializeSquad(config.squad)]);
}

export async function deleteSquad(config) {
  await httpClient.delete(config.squad.id, { data: config.squad.id });
}
