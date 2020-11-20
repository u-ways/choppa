import { describe, expect, it } from "@/tests/unit/test-imports";
import Chapter from "@/data/types/chapter";

describe("Chapter test", () => {
  it("Constructor assigns properties correctly", () => {
    const chapter = new Chapter(1, "Developer", "#FF00FF");
    expect(chapter.id).toBe(1);
    expect(chapter.name).toBe("Developer");
    expect(chapter.colour).toBe("#FF00FF");
  });
});
