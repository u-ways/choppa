// deno-lint-ignore-file ban-types

import type {Context} from "../../deps.ts";
import {green, cyan} from "../../deps.ts";

export const httpLogger = async (ctx: Context, next: Function) => {
    const timestamp = new Date().toISOString().replace("T", " ").substring(2, 19);
    const format = `${timestamp} | ${green(ctx.request.method)} ${cyan(ctx.request.url.pathname)}`;
    console.log(format);
    await next();
};