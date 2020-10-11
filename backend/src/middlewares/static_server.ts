// deno-lint-ignore-file ban-types

import type { Context } from "../../deps.ts";

export function staticRestServer(
  root = `../frontend/dist`,
  index = "index.html",
) {
  return async (ctx: Context, next: Function) => {
    await ctx.send({ root: root, index: index });
    await next();
  };
}
