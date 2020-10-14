import { LogLevelStrings } from "../../config/logger.config.ts";

export enum LogLevel {
  OFF,
  ERROR,
  WARN,
  INFO,
  ALL,
}

export class Log {
  private readonly level: LogLevel;
  private fmt = (s: string) => {`[${s}]`};

  constructor(level: LogLevel) {
    this.level = level;
  }

  info(source: string, data: string): void {
    if (this.level >= LogLevel.INFO) {
      console.info(`%c${this.fmt(source)} %c${data}`, "color: gray;", "color: blue;");
    }
  }

  warn(source: string, data: string): void {
    if (this.level >= LogLevel.WARN) {
      console.warn(`%c${this.fmt(source)} %c${data}`, "color: gray;", "color: yellow;");
    }
  }

  error(source: string, data: string): void {
    if (this.level >= LogLevel.ERROR) {
      console.error(`%c${this.fmt(source)} %c${data}`, "color: gray;", "color: red;");
    }
  }

  success(source: string, data: string): void {
    if (this.level >= LogLevel.ALL) {
      console.info(`%c${this.fmt(source)} %c${data}`, "color: gray;", "color: green;");
    }
  }

  plain(source: string, data: string): void {
    if (this.level >= LogLevel.INFO) {
      console.info(`${this.fmt(source)} ${data}`);
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
 * - `data`:   The data you want to log.
 */
export const log = new Log(LogLevelStrings);
