import httpClient from "@/config/api/http-client";
import Chapter from "@/models/chapter";

function getUrlOrId(config) {
  return Object.prototype.hasOwnProperty.call(config, "url")
    ? config.url
    : `chapters/${config.id}`;
}

async function deserializeChapter(config, json) {
  return new Chapter({
    id: json.id,
    name: json.name,
    color: json.color,
  });
}

export async function getChapter(config) {
  const response = await httpClient.get(getUrlOrId(config));
  return deserializeChapter(config, response.data);
}
