// deno-lint-ignore-file ban-types

import type { Context } from "../../deps.ts";
import { log } from "../utils/logger_utils.ts";
import { Color } from "../utils/color_utils.ts";

export const httpLogger = async (ctx: Context, next: Function) => {
  const timestamp = new Date().toISOString().replace("T", " ").substring(2, 19);
  const request = `%c${ctx.request.method} %c${ctx.request.url.pathname}`;
  const format = `%c${timestamp} | ${request}`;
  log.plain(
    `%c[HTTP] ${format}`,
    Color.gray,
    Color.white,
    Color.blue,
    Color.green,
  );
  await next();
};
