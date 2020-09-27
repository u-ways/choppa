import {Application} from "../deps.ts";

import tribeRouter from "./routes/controllers/tribe_controller.ts";
import {httpLogger} from "./routes/middlewares/http_logger.ts";
import {staticServer} from "./routes/middlewares/static_server.ts";
import {errorHandler} from "./routes/middlewares/error_handler.ts";

const app = new Application();

app.use(errorHandler);
app.use(httpLogger);

app.use(tribeRouter.allowedMethods())
app.use(tribeRouter.routes());

app.use(staticServer);

await app.listen({
    hostname: Deno.env.get("CHOPPER_HOSTNAME") || "localhost",
    port: parseInt(Deno.env.get("CHOPPER_PORT") || "8888")
});
