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

export async function getChaptersByQuery(config) {
  try {
    const response = await httpClient.get(config.url);
    return Promise.all(response.data.map((squad) => deserializeChapter(squad)));
  } catch (error) {
    if (error.response.status === 404) {
      return [];
    }

    throw error;
  }
}
