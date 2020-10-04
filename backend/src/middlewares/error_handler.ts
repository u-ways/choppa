// deno-lint-ignore-file ban-types

import type {Context} from "../../deps.ts";
import {HttpError, red, bold, Status} from "../../deps.ts";

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
                        "status": e.status, "message": e.message
                    }
                };
            }
        } else if (e instanceof Error) {
            ctx.response.status = 500;
            ctx.response.body = {
                "error": {
                    "status": 500, "message": "Internal Server Error"
                }
            };
            console.error("Unhandled Error:", red(bold(e.message)));
            console.error(e.stack);
        }
    }
};