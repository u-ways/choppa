import httpClient from "@/config/api/http-client";
import Squad from "@/data/types/squad";
import getMember from "@/config/api/member.api";

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

async function getSquads(config) {
  const response = await httpClient.get("squads");

  // TODO: Once API has been finished this should be implemented correctly with the api and support url and id
  return Promise.all(response.data.filter((squad) => squad.tribe === config.tribeId)
    .map((squad) => deserializeSquad(config, squad)));
}

export default getSquads;
