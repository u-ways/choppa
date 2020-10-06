import {plan, suite, scenario, assertEquals, runTests} from "../deps.ts";
import {Application, Context} from "../../deps.ts";
import {port} from "../../config/chopper.config.ts";
import {staticRestServer} from "../../src/middlewares/static_server.ts";

plan("http_module.ts", () => {
    suite("Oak",  () => {
        const HOST_NAME = "127.0.0.1";

        scenario("Can bind to Choppa port config", async () => {
            let bindedPort = 0;
            const app = new Application();
            const ac = generateAbortController();

            app.addEventListener("listen", ({port}) => {
                bindedPort = port
                ac.controller.abort();
            })

            app.use(async (ctx: Context, next: Function) => await next())

            await app.listen({hostname: HOST_NAME, port, signal: ac.signal})
            assertEquals(bindedPort, port);
        });
    });
});

function generateAbortController () {
    const controller = new AbortController();
    const { signal } = controller;
    return { controller, signal }
}

runTests();

