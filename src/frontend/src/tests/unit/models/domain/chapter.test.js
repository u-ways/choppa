import { describe, expect, it } from "@/tests/unit/test-imports";
import Chapter from "@/models/domain/chapter";

describe("Chapter test", () => {
  it("Constructor assigns properties correctly", () => {
    const chapter = new Chapter({
      id: 1,
      name: "Developer",
      color: "#FF00FF",
      relations: { relationTest: "test relation" },
    });

    expect(chapter.id).toBe(1);
    expect(chapter.name).toBe("Developer");
    expect(chapter.color).toBe("#FF00FF");
    expect(chapter.relations.relationTest).toBe("test relation");
  });
});
