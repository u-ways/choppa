import { Environment } from "../../../utilities/Environment";

describe("Environment test", () => {
  let originNodeEnv: string;

  beforeAll(() => {
    originNodeEnv = process.env.NODE_ENV;
  });

  afterAll(() => {
    process.env.NODE_ENV = originNodeEnv;
  });

  it("When in development mode isDevelopment returns true", () => {
    process.env.NODE_ENV = "development";
    expect(Environment.isDevelopment()).toBe(true);
  });

  it("When not in development mode isDevelopment returns false", () => {
    process.env.NODE_ENV = "not-development";
    expect(Environment.isDevelopment()).toBe(false);
  });
});