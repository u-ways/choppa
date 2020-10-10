import { Member } from "../../../../data/types/Member";

describe("Member test", () => {

    it("Constructor assigns properties correctly", () => {
        const member: Member = new Member("test-name", "test-chapter");
        expect(member.name).toBe("test-name");
        expect(member.chapter).toBe("test-chapter");
    });

});