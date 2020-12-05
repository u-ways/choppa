import httpClient from "@/config/api/http-client";
import Tribe from "@/models/tribe";
import { getSquadsByQuery } from "@/config/api/squad.api";

async function deserializeTribe(config, json) {
  return new Tribe({
    id: json.id,
    name: json.name,
    squads: await getSquadsByQuery({ url: json.squads }),
  });
}

export async function getTribe(config) {
  const url = Object.prototype.hasOwnProperty.call(config, "url") ? config.url : `tribes/${config.id}`;
  const response = await httpClient.get(url);

  return deserializeTribe(config, response.data);
}
