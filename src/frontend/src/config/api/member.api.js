import Member from "@/models/domain/member";
import { deserializeChapter } from "@/config/api/chapter.api";

export async function deserializeMember(json) {
  return new Member({
    id: json.id,
    name: json.name,
    chapter: await deserializeChapter(json.chapter),
  });
}
