import { LogLevel } from "../src/utils/logger_utils.ts";

function extractLogLevel(input: string): LogLevel {
  switch (input) {
    case "OFF":
      return 0;
    case "ERROR":
      return 1;
    case "WARN":
      return 2;
    case "INFO":
      return 3;
    default:
      return 4;
  }
}

/**
 * The log level to set Choppa to log at:
 * - OFF
 * - ERROR
 * - WARN
 * - INFO
 * - ALL (DEFAULT)
 */
export const LogLevelStrings: LogLevel = extractLogLevel(
  Deno.env.get("LOG_LEVEL") || "ALL",
);
