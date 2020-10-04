export const hostname = Deno.env.get("HEROKU_APP_NAME") || Deno.env.get("CHOPPER_HOSTNAME") || "www.localhost";
export const port = parseInt(Deno.env.get("CHOPPER_PORT") || "8888");