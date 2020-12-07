import Member from "@/models/domain/member";
import { deserializeChapter } from "@/config/api/chapter.api";
import httpClient from "@/config/api/http-client";

export async function deserializeMember(json) {
  return new Member({
    id: json.id,
    name: json.name,
    chapter: await deserializeChapter(json.chapter),
    relations: {
      squads: json.squads,
      iterations: json.iterations,
      history: json.history,
    },
  });
}

export async function getMember(config) {
  const url = Object.prototype.hasOwnProperty.call(config, "url") ? config.url : `members/${config.id}`;
  const response = await httpClient.get(url);

  return deserializeMember(response.data);
}

export async function getMembersByQuery(config) {
  try {
    const response = await httpClient.get(config.url);
    return Promise.all(response.data.map((member) => deserializeMember(member)));
  } catch (error) {
    if (error.response.status === 404) {
      return [];
    }

    throw error;
  }
}

export async function saveMember(config) {
  await httpClient.put(config.member.id, {
    id: config.member.id,
    name: config.member.name,
    chapter: config.member.chapter ? config.member.chapter.id : "",
  });
}
