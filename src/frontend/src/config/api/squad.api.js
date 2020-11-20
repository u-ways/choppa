import httpClient from "@/config/api/http-client";
import Squad from "@/data/types/squad";
import getMember from "@/config/api/member.api";

function getUrlOrId(config) {
  return Object.prototype.hasOwnProperty.call(config, "url")
    ? config.url
    : `squads/${config.id}`;
}

async function deserialiseSquad(config, json) {
  let members = [];
  if (!Object.prototype.hasOwnProperty.call(config, "loadMembers") || config.loadMembers) {
    members = await Promise.all(json.members.map((member) => getMember({ url: member, loadChapter: true })));
  }

  return new Squad({
    id: json.id,
    name: json.name,
    members,
  });
}

async function getSquads(config) {
  const response = await httpClient.get(getUrlOrId(config));

  return Promise.all(
    response.data.map((squad) => deserialiseSquad(config, squad)),
  );
}

export default getSquads;
