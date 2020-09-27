import { Rhum } from "../deps.ts";

let value = false;

function run() {
    return true;
}

async function close() {
    value = true;
    return value;
}

// 1. Define your test plan (usually the test file's name)
// 2. Define your test suites (usually methods being tested)
// 3. Define your test cases with assertions
Rhum.testPlan("config_test.ts", () => {
    Rhum.testSuite("run()", () => {
        Rhum.testCase("Returns true", () => {
            const result = run();
            Rhum.asserts.assertEquals(true, result);
        });
    });
    Rhum.testSuite("close()", () => {
        Rhum.testCase("Returns true", async () => {
            const result = await close();
            Rhum.asserts.assertEquals(true, result);
        });
    });
});

Rhum.run(); // <-- make sure to include this so that your tests run via `deno test`
