/** Backend test dependencies */
import { Rhum } from "https://deno.land/x/rhum@v1.1.2/mod.ts";

const plan = (a: string, b: Function) => Rhum.testPlan(a, b)
const suite = (a: string, b: Function) => Rhum.testSuite(a, b)
const scenario = (a: string, b: Function) => Rhum.testCase(a, b)
const assertEquals = (a: any, b: any) => Rhum.asserts.assertEquals(a, b)
const runTests = () => Rhum.run()

export { plan, suite, scenario, assertEquals, runTests }