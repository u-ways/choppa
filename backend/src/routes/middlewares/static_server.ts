import type {Context} from "../../../deps.ts";

export const staticServer = async (ctx: Context, next: Function) => {
    await ctx.send({
        root: `${Deno.cwd()}/frontend/dist`,
        index: "index.html",
    });
    await next();
}