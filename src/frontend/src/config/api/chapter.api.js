import Chapter from "@/models/chapter";

export async function deserializeChapter(json) {
  return new Chapter({
    id: json.id,
    name: json.name,
    color: json.color,
  });
}
