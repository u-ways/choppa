import { getAllChapters } from "@/config/api/chapter.api";
import httpClient from "@/config/api/http-client";
import { getTribe } from "@/config/api/tribe.api";
import { getSquad } from "@/config/api/squad.api";
import Member from "@/models/domain/member";
import Squad from "@/models/domain/squad";

export async function chapterDistribution() {
  const [chapters, chapterStats] = await Promise.all([
    getAllChapters(),
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

export async function tribeKnowledgeSharingPoints() {
  const tribesKSPStats = (await httpClient.get("/tribes/stats")).data;

  return Promise.all(Object.keys(tribesKSPStats.knowledgeSharingPoints).map(async (tribeId) => ({
    tribe: await getTribe({ id: tribeId }),
    squads: await Promise.all(Object.keys(tribesKSPStats.knowledgeSharingPoints[tribeId]).map(async (squadId) => ({
      squad: await getSquad({ id: squadId }),
      ksp: tribesKSPStats.knowledgeSharingPoints[tribeId][squadId],
    }))),
  })));
}

export async function squadLatestChanges() {
  const squadChanges = (await httpClient.get("/squads/stats")).data;

  return Object.values(squadChanges.latestChanges).map((value) => ({
    date: value.createDate,
    member: new Member({
      id: value.member.id,
      name: value.member.name,
    }),
    revisionType: value.revisionType,
    squad: new Squad({
      id: value.squad.id,
      name: value.squad.name,
    }),
  }));
}

export async function memberStats() {
  return (await httpClient.get("/members/stats")).data;
}
