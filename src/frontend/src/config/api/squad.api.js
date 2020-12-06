import httpClient from "@/config/api/http-client";
import Squad from "@/models/domain/squad";
import { deserializeMember } from "@/config/api/member.api";

async function deserializeSquad(json) {
  return new Squad({
    id: json.id,
    name: json.name,
    members: await Promise.all(json.members.map((member) => deserializeMember(member))),
    color: json.color,
  });
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
