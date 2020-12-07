import Chapter from "@/models/domain/chapter";
import httpClient from "@/config/api/http-client";

export async function deserializeChapter(json) {
  return new Chapter({
    id: json.id,
    name: json.name,
    color: json.color,
    relations: {
      members: json.members,
    },
  });
}

export async function getChapter(config) {
  const url = Object.prototype.hasOwnProperty.call(config, "url") ? config.url : `chapters/${config.id}`;
  const response = await httpClient.get(url);

  return deserializeChapter(response.data);
}

export async function getChapters() {
  const response = await httpClient.get("chapters");
  return Promise.all(response.data.map((chapter) => deserializeChapter(chapter)));
}

export async function getChaptersByQuery(config) {
  try {
    const response = await httpClient.get(config.url);
    return Promise.all(response.data.map((chapter) => deserializeChapter(chapter)));
  } catch (error) {
    if (error.response.status === 404) {
      return [];
    }

    throw error;
  }
}

export async function saveChapter(config) {
  await httpClient.put(config.chapter.id, {
    id: config.chapter.id,
    name: config.chapter.name,
    color: config.chapter.color,
  });
}
