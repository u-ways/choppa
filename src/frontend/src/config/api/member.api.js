import httpClient from "@/config/api/http-client";
import Member from "@/models/member";

function findChapter(config, chapterId) {
  if (config.chapters && chapterId) {
    const chapter = config.chapters.find((c) => c.id === chapterId);
    return chapter || null;
  }

  return null;
}

async function deserializeMember(config, json) {
  const potentialChapter = findChapter(config, json.chapter);

  return new Member({
    id: json.id,
    name: json.name,
    chapter: potentialChapter || null,
  });
}

function serializeMember(config) {
  return {
    id: config.member.id,
    name: config.member.name,
    chapter: config.member.chapter ? config.member.chapter.id : "",
  };
}

export async function getMembersByQuery(config) {
  try {
    const response = await httpClient.get(config.url);
    return Promise.all(response.data.map((member) => deserializeMember(config, member)));
  } catch (error) {
    if (error.response.status === 404) {
      return [];
    }

    throw error;
  }
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
