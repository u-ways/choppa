// deno-lint-ignore-file ban-types

import type {Context} from "../../deps.ts";

export const staticRestServer = async (ctx: Context, next: Function) => {
    await ctx.send({
        root: `../chopper-frontend/dist`,
        index: "index.html",
    });
    await next();
}