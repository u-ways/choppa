import httpClient from "@/config/api/http-client";
import Squad from "@/data/types/squad";
import getMember from "@/config/api/member.api";

function getUrlOrId(config) {
  return Object.prototype.hasOwnProperty.call(config, "url")
    ? config.url
    : `squads/${config.id}`;
}

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
  const response = await httpClient.get(getUrlOrId(config));

  return Promise.all(
    response.data.map((squad) => deserializeSquad(config, squad)),
  );
}

export default getSquads;
