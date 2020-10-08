import {assertEquals, plan, runTests, scenario, suite} from "../deps.ts";
import {Application, resolve} from "../../deps.ts";
import {port} from "../../config/choppa.config.ts";
import {generateAbortController, HOSTNAME} from "../utils/http_utils.ts";
import {staticRestServer} from "../../src/middlewares/static_server.ts";
import {rwd} from "../../src/utils/path_utils.ts";

plan("static_server.ts", () => {
    const __dirname = rwd(import.meta);
    const __resources = resolve(__dirname, "../resources")

    suite("staticRestServer()", () => {
        scenario("Can serve static content from a REST endpoint", async () => {
            let result: Promise<string> = new Promise<string>(() => {
            })
            const app = new Application();
            const ac = generateAbortController();

            app.use(staticRestServer(__resources))

            app.addEventListener("listen", async ({hostname, port}) => {
                await fetch(`http://${hostname}:${port}/index.html`).then(res => result = res.text());
                ac.controller.abort();
            })

            await app.listen({hostname: HOSTNAME, port, signal: ac.signal})

            assertEquals(await result, "<h1>Test HTML Resource</h1>");
        });
    });
});

runTests();

