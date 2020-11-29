import httpClient from "@/config/api/http-client";
import Member from "@/models/member";
import { getChapter } from "@/config/api/chapter.api";

function getUrlOrId(config) {
  return Object.prototype.hasOwnProperty.call(config, "url")
    ? config.url
    : `members/${config.id}`;
}

async function deserializeMember(config, json) {
  return new Member({
    id: json.id,
    name: json.name,
    chapter: !Object.prototype.hasOwnProperty.call(config, "loadChapter") || config.loadChapter
      ? await getChapter({ url: json.chapter }) : null,
  });
}

function serializeMember(config) {
  return {
    id: config.member.id,
    name: config.member.name,
    chapter: config.member.chapter ? config.member.chapter.id : "",
  };
}

export async function getMember(config) {
  const response = await httpClient.get(getUrlOrId(config));
  return deserializeMember(config, response.data);
}

async function saveExistingMember(config) {
  await httpClient.put(config.member.id, serializeMember(config));
}

async function createNewMember(config) {
  await httpClient.post("members", serializeMember(config));
}

export async function saveMember(config) {
  if (config.member.newlyCreated) {
    await createNewMember(config);
  } else {
    await saveExistingMember(config);
  }
}
