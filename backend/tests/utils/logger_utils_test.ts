import { assertEquals, plan, runTests, scenario, suite } from "../deps.ts";
import { Log, LogLevel } from "../../src/utils/logger_utils.ts";
import { Rhum } from "https://deno.land/x/rhum@v1.1.4/mod.ts";

plan("logger_utils.ts", () => {
  suite("log()", () => {
    scenario(
      "Setting LOG_LEVEL env variable to OFF will not produce any logs.",
      () => {
        console = Rhum.mock(FakeConsole).create();
        const logger = Rhum.mock(Log).withConstructorArgs(LogLevel.OFF)
          .create();
        logger.error("Test", "#1");
        logger.warn("Test", "#2");
        logger.info("Test", "#3");
        logger.success("Test", "#4");

        // @ts-ignore
        assertEquals(console.calls.error, 0);
        // @ts-ignore
        assertEquals(console.calls.warn, 0);
        // @ts-ignore
        assertEquals(console.calls.info, 0);
      },
    );
    scenario(
      "Setting LOG_LEVEL env variable to ERROR will only produce error logs.",
      () => {
        console = Rhum.mock(FakeConsole).create();

        const logger = Rhum.mock(Log).withConstructorArgs(LogLevel.ERROR)
          .create();
        logger.error("Test", "#1");
        logger.warn("Test", "#2");
        logger.info("Test", "#3");
        logger.success("Test", "#4");

        // @ts-ignore
        assertEquals(console.calls.error, 1);
        // @ts-ignore
        assertEquals(console.calls.warn, 0);
        // @ts-ignore
        assertEquals(console.calls.info, 0);
      },
    );
    scenario(
      "Setting LOG_LEVEL env variable to WARN will only produce [error, warn] logs.",
      () => {
        console = Rhum.mock(FakeConsole).create();

        const logger = Rhum.mock(Log).withConstructorArgs(LogLevel.WARN)
          .create();
        logger.error("Test", "#1");
        logger.warn("Test", "#2");
        logger.info("Test", "#3");
        logger.success("Test", "#4");

        // @ts-ignore
        assertEquals(console.calls.error, 1);
        // @ts-ignore
        assertEquals(console.calls.warn, 1);
        // @ts-ignore
        assertEquals(console.calls.info, 0);
      },
    );
    scenario(
      "Setting LOG_LEVEL env variable to INFO will only produce [error, warn, info] logs.",
      () => {
        console = Rhum.mock(FakeConsole).create();

        const logger = Rhum.mock(Log).withConstructorArgs(LogLevel.INFO)
          .create();
        logger.error("Test", "#1");
        logger.warn("Test", "#2");
        logger.info("Test", "#3");
        logger.success("Test", "#4");

        // @ts-ignore
        assertEquals(console.calls.error, 1);
        // @ts-ignore
        assertEquals(console.calls.warn, 1);
        // @ts-ignore
        assertEquals(console.calls.info, 1);
      },
    );
    scenario(
      "Setting LOG_LEVEL env variable to ALL will produce all logs.",
      () => {
        console = Rhum.mock(FakeConsole).create();

        const logger = Rhum.mock(Log).withConstructorArgs(LogLevel.ALL)
          .create();
        logger.error("Test", "#1");
        logger.warn("Test", "#2");
        logger.info("Test", "#3");
        logger.success("Test", "#4");

        // @ts-ignore
        assertEquals(console.calls.error, 1);
        // @ts-ignore
        assertEquals(console.calls.warn, 1);
        // @ts-ignore
        assertEquals(console.calls.info, 2);
      },
    );
  });
});
runTests();

class FakeConsole implements Console {
  memory: any;

  assert(condition?: boolean, ...data: any[]): void {
  }

  clear(): void {
  }

  count(label?: string): void {
  }

  countReset(label?: string): void {
  }

  debug(...data: any[]): void {
  }

  dir(item?: any, options?: any): void {
  }

  dirxml(...data: any[]): void {
  }

  error(...data: any[]): void {
  }

  exception(message?: string, ...optionalParams: any[]): void {
  }

  group(...data: any[]): void {
  }

  groupCollapsed(...data: any[]): void {
  }

  groupEnd(): void {
  }

  info(...data: any[]): void {
  }

  log(...data: any[]): void {
  }

  table(tabularData?: any, properties?: string[]): void {
  }

  time(label?: string): void {
  }

  timeEnd(label?: string): void {
  }

  timeLog(label?: string, ...data: any[]): void {
  }

  timeStamp(label?: string): void {
  }

  trace(...data: any[]): void {
  }

  warn(...data: any[]): void {
  }
}
