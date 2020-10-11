/**
 * The HTTP port for the server to listen on
 */
export const port = parseInt(Deno.env.get("PORT") || "8888");
