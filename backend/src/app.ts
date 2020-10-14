import { Application } from "../deps.ts";

import { port } from "../config/choppa.config.ts";
import { gqlMethods, gqlRoutes } from "./graphql/entrypoint.graphql.ts";

import { httpLogger } from "./middlewares/http_logger.ts";
import { staticRestServer } from "./middlewares/static_server.ts";
import { errorHandler } from "./middlewares/error_handler.ts";
import { dbConnection } from "./utils/db_utils.ts";
import { log } from "./utils/logger_utils.ts";

log.warn("Choppa", "Starting...");

await dbConnection();

const app = new Application();

app.use(errorHandler);
app.use(httpLogger);

app.use(gqlRoutes, gqlMethods);

app.use(staticRestServer());

log.warn("HTTP", `Starting HTTP server`);
log.success("HTTP", `Server is listening on port: ${port.toString()}`);

await app.listen({ port }).catch((error) => {
  log.error("Choppa", error);
  throw error;
});
