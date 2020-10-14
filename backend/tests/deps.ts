/** Backend test dependencies */
import {
  Rhum,
  Constructor,
  Stubbed,
  MockBuilder,
} from "https://deno.land/x/rhum@v1.1.2/mod.ts";

const plan = (name: string, testFn: Function) => Rhum.testPlan(name, testFn);

const suite = (name: string, testFn: Function) => Rhum.testSuite(name, testFn);

const scenario = (name: string, testFn: Function) => {
  return Rhum.testCase(name, testFn);
};

const assertEquals = (expected: any, actual: any) => {
  return Rhum.asserts.assertEquals(expected, actual);
};

const runTests = () => Rhum.run();

function stubbed<T>(obj: T): Stubbed<T> {
  return Rhum.stubbed(obj);
}

function mock<T>(constructorFn: Constructor<T>): MockBuilder<T> {
  return Rhum.mock(constructorFn);
}

export { stubbed, mock, plan, suite, scenario, assertEquals, runTests };
