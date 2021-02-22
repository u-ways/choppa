import { getChapters } from "@/config/api/chapter.api";
import httpClient from "@/config/api/http-client";

export async function chapterDistribution() {
  const [chapters, chapterStats] = await Promise.all([
    getChapters(),
    httpClient.get("/chapters/stats"),
  ]);

  if (chapters.length !== chapterStats.data.total) {
    throw new Error("Expected chapters and chapterStats to be the same length");
  }

  return chapters.map((chapter) => ({
    chapter,
    percentage: chapterStats.data.distribution[chapter.name],
  }));
}
