// deno-lint-ignore-file ban-types

import type { Context } from "../../deps.ts";
import { HttpError, Status } from "../../deps.ts";
import { log } from "../utils/logger_utils.ts";

export const errorHandler = async (ctx: Context, next: Function) => {
  try {
    await next();
  } catch (e) {
    ctx.response.headers.set("Content-Type", "application/json");
    if (e instanceof HttpError) {
      ctx.response.status = e.status as Status;
      if (e.expose) {
        ctx.response.body = {
          "error": {
            "status": e.status,
            "message": e.message,
          },
        };
      }
    } else if (e instanceof Error) {
      ctx.response.status = 500;
      ctx.response.body = {
        "error": {
          "status": 500,
          "message": "Internal Server Error",
        },
      };
      log.error("HTTP", `Unhandled Error - ${e.message}\n${e.stack || ""}`);
    }
  }
};
