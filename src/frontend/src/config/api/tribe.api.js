import httpClient from "@/config/api/http-client";
import Tribe from "@/data/types/tribe";
import getSquads from "@/config/api/squad.api";

function getUrlOrId(config) {
  return Object.prototype.hasOwnProperty.call(config, "url")
    ? config.url
    : `tribes/${config.id}`;
}

async function deserialiseTribe(config, json) {
  let squads = [];
  if (!Object.prototype.hasOwnProperty.call(config, "loadSquads") || config.loadSquads) {
    squads = await getSquads({ url: json.squads, loadMembers: true });
  }

  return new Tribe({
    id: json.id,
    name: json.name,
    squads,
  });
}

async function getTribe(config) {
  const response = await httpClient.get(getUrlOrId(config));
  return deserialiseTribe(config, response.data);
}

export default getTribe;
