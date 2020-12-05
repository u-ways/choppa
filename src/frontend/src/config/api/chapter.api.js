import httpClient from "@/config/api/http-client";
import Chapter from "@/models/chapter";

async function deserializeChapter(config, json) {
  return new Chapter({
    id: json.id,
    name: json.name,
    color: json.color,
  });
}

export async function getChaptersByQuery(config) {
  const response = await httpClient.get(config.url);
  return Promise.all(response.data.map((chapter) => deserializeChapter(config, chapter)));
}
