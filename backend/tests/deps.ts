/** Backend test dependencies */
import { Rhum } from "https://deno.land/x/rhum@v1.1.2/mod.ts";

const stubbed = (ToBeStubbed: any) => Rhum.stubbed(ToBeStubbed);

const mock = (ToBeMocked: any) => Rhum.mock(ToBeMocked);

const plan = (name: string, testFn: Function) => Rhum.testPlan(name, testFn);

const suite = (name: string, testFn: Function) => Rhum.testSuite(name, testFn);

const scenario = (name: string, testFn: Function) =>
  Rhum.testCase(name, testFn);

const assertEquals = (expected: any, actual: any) =>
  Rhum.asserts.assertEquals(expected, actual);

const runTests = () => Rhum.run();

export { stubbed, mock, plan, suite, scenario, assertEquals, runTests };
