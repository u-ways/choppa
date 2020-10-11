import { assertEquals, plan, runTests, scenario, suite } from "../deps.ts";
import { Application, Context } from "../../deps.ts";
import { port } from "../../config/choppa.config.ts";
import { generateAbortController, HOSTNAME } from "../utils/http_utils.ts";

plan("http_module.ts", () => {
  suite("Oak()", () => {
    scenario("Can bind to Choppa port config", async () => {
      let bindedPort = 0;
      const app = new Application();
      const ac = generateAbortController();

      app.addEventListener("listen", ({ port }) => {
        bindedPort = port;
        ac.controller.abort();
      });

      app.use(async (ctx: Context, next: Function) => await next());

      await app.listen({ hostname: HOSTNAME, port, signal: ac.signal });
      assertEquals(bindedPort, port);
    });
  });
});

runTests();
