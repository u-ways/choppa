import httpClient from "@/config/api/http-client";
import Squad from "@/models/squad";
import { getMember, saveMember } from "@/config/api/member.api";

async function deserializeSquad(config, json) {
  let members = [];
  if (!Object.prototype.hasOwnProperty.call(config, "loadMembers") || config.loadMembers) {
    members = await Promise.all(json.members.map((member) => getMember({ url: member, loadChapter: true })));
  }

  return new Squad({
    id: json.id,
    name: json.name,
    members,
    color: json.color,
  });
}

function serializeSquad(config) {
  return {
    id: config.squad.id,
    name: config.squad.name,
    color: config.squad.color,
    tribe: config.tribeId,
    members: config.squad.members.map((member) => member.id),
  };
}

export async function getSquads(config) {
  const response = await httpClient.get("squads");

  // TODO: Once API has been finished this should be implemented correctly with the api and support url and id
  return Promise.all(response.data.filter((squad) => squad.tribe === config.tribeId)
    .map((squad) => deserializeSquad(config, squad)));
}

async function saveExistingSquad(config) {
  await httpClient.put(config.squad.id, serializeSquad(config));
}

async function createNewSquad(config) {
  await httpClient.post("squads", serializeSquad(config));
}

export async function saveSquad(config) {
  if (config.squad.newlyCreated) {
    await createNewSquad(config);
  } else {
    await saveExistingSquad(config);
  }

  if (!Object.prototype.hasOwnProperty.call(config, "saveMembers") || config.saveMembers) {
    await config.squad.members.forEach((member) => saveMember({ member }));
  }
}

export async function deleteSquad(config) {
  await httpClient.delete(config.squad.id);
}
