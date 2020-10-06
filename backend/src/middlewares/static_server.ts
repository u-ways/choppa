// deno-lint-ignore-file ban-types

import type {Context} from "../../deps.ts";

export function staticRestServer(root: string = `../frontend/dist`, index: string = "index.html") {
    return async (ctx: Context, next: Function) => {
        await ctx.send({ root: root, index: index });
        await next();
    };
}