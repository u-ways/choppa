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

  const tribeAndSquads = await Promise.all(Object.keys(tribesKSPStats.knowledgeSharingPoints).map(async (tribeId) => ({
    tribe: await getTribe({ id: tribeId }),
    squads: await Promise.all(Object.keys(tribesKSPStats.knowledgeSharingPoints[tribeId]).map(async (squadId) => ({
      squad: await getSquad({ id: squadId }),
      ksp: tribesKSPStats.knowledgeSharingPoints[tribeId][squadId],
    }))),
  })));

  return tribeAndSquads.map((t) => ({
    ...t,
    tribeAverage: sumArrays(
      ...t.squads.map((s) => Object.values(s.ksp).map((k) => k.ksp).map((v) => Number.parseInt(v, 10))),
    ).map((avg) => avg / t.squads.length),
  }));
}

function sumArrays(...arrays) {
  const n = arrays.reduce((max, xs) => Math.max(max, xs.length), 0);
  const result = Array.from({ length: n });
  return result.map((_, i) => arrays.map((xs) => xs[i] || 0).reduce((sum, x) => sum + x, 0));
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
