import httpClient from "@/config/api/http-client";
import Squad from "@/models/squad";
import { getMembersByQuery, saveMember } from "@/config/api/member.api";
import hasMissingOrTrueProperty from "@/helpers/hasMissingOrTrueProperty";

async function deserializeSquad(config, json) {
  let members = [];
  if (hasMissingOrTrueProperty(config, "loadMembers")) {
    members = await getMembersByQuery({
      url: `members?squad=${json.id.replace("squads/", "")}`,
      chapters: config.chapters,
    });
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

export async function getSquadsByQuery(config) {
  const response = await httpClient.get(config.url);
  return Promise.all(response.data.map((squad) => deserializeSquad(config, squad)));
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
