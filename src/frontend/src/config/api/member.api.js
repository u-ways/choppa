import httpClient from "@/config/api/http-client";
import Member from "@/data/types/member";
import getChapter from "@/config/api/chapter.api";

function getUrlOrId(config) {
  return Object.prototype.hasOwnProperty.call(config, "url")
    ? config.url
    : `members/${config.id}`;
}

async function deserialiseMember(config, json) {
  return new Member({
    id: json.id,
    name: json.name,
    chapter: !Object.prototype.hasOwnProperty.call(config, "loadChapter") || config.loadChapter
      ? await getChapter({ url: json.chapter }) : null,
  });
}

async function getMember(config) {
  const response = await httpClient.get(getUrlOrId(config));
  return deserialiseMember(config, response.data);
}

export default getMember;
