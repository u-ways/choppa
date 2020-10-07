import type Member from "./Member.ts";

export default interface Squad {
    readonly id: string;
    readonly name: string;
    readonly members: [Member];
}