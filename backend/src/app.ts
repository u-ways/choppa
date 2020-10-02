import {Application, green} from "../deps.ts";

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

app.addEventListener("listen", ({ hostname, port }) =>
    console.log(green(`Server started listening at ${hostname}:${port}`))
);

await app.listen({
    port: parseInt(Deno.env.get("PORT") || "8080")
});