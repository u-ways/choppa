import httpClient from "@/config/api/http-client";
import Chapter from "@/data/types/chapter";

function getUrlOrId(config) {
  return Object.prototype.hasOwnProperty.call(config, "url")
    ? config.url
    : `chapters/${config.id}`;
}

async function deserialiseChapter(config, json) {
  return new Chapter({
    id: json.id,
    name: json.name,
  });
}

async function getChapter(config) {
  const response = await httpClient.get(getUrlOrId(config));
  return deserialiseChapter(config, response.data);
}

export default getChapter;
