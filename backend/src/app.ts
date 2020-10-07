import {Application, blue, gray, green, red, yellow} from "../deps.ts";

import {port} from "../config/choppa.config.ts";
import {GraphQLAllowedMethods, GraphQLRoutes} from "./graphql/entrypoint.graphql.ts";

import {httpLogger} from "./middlewares/http_logger.ts";
import {staticRestServer} from "./middlewares/static_server.ts";
import {errorHandler} from "./middlewares/error_handler.ts";
import {dbConnection} from "./utils/db_utils.ts";

console.info(gray("[Choppa] ") + yellow(`Starting...`))

await dbConnection();

const app = new Application();

app.use(errorHandler);
app.use(httpLogger);

app.use(GraphQLRoutes, GraphQLAllowedMethods);

app.use(staticRestServer());

console.info(gray("[Choppa] ") + green("Binding server to listen on port ") + blue(`${port}`))

await app.listen({port}).catch(error => {
    console.error(gray("[Choppa] ") + red(`Error - ${error}`))
    throw error;
})