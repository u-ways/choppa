import { blue, gray, green, red, yellow } from "../../deps.ts";
import { LogLevelStrings } from "../../config/logger.config.ts";

export enum LogLevel {
  OFF,
  ERROR,
  WARN,
  INFO,
  ALL,
}

class Log {
  private readonly level: LogLevel;
  private fmt = (s: string) => `${gray("[" + s + "]")}`;

  constructor(level: LogLevel) {
    this.level = level;
  }

  info(source: string, data: string): void {
    if (this.level >= LogLevel.INFO) {
      console.info(this.fmt(source), blue(data));
    }
  }

  warn(source: string, data: string): void {
    if (this.level >= LogLevel.WARN) {
      console.warn(this.fmt(source), yellow(data));
    }
  }

  error(source: string, data: string): void {
    if (this.level >= LogLevel.ERROR) {
      console.error(this.fmt(source), red(data));
    }
  }

  success(source: string, data: string): void {
    if (this.level >= LogLevel.ALL) {
      console.info(this.fmt(source), green(data));
    }
  }

  plain(source: string, data: string): void {
    if (this.level >= LogLevel.INFO) {
      console.info(this.fmt(source), data);
    }
  }
}

/**
 * Standard internal logger, format:
 * ```
 * log.[type]([source],[data])
 * ```
 * Where:
 * - `type`:   [info|warn|success|error]
 * - `source`: Which service did the log came from. (i.e. Database, HTTP (Choppa), GraphQL...etc.)
 * - `data`:   The data you want to log.\
 */
export const log = new Log(LogLevelStrings);
