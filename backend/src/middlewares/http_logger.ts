// deno-lint-ignore-file ban-types

import type { Context } from "../../deps.ts";
import { cyan, green } from "../../deps.ts";
import { log } from "../utils/logger_utils.ts";

export const httpLogger = async (ctx: Context, next: Function) => {
  const timestamp = new Date().toISOString().replace("T", " ").substring(2, 19);
  const format = `${timestamp} | ${green(ctx.request.method)} ${
    cyan(ctx.request.url.pathname)
  }`;
  log.plain("HTTP", format);
  await next();
};
