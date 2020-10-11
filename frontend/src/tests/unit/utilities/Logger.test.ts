import Logger from "../../../utilities/Logger";
import { Environment } from "../../../utilities/Environment";

describe("Logger test", () => {
  const consoleInfoSpy = jest.spyOn(console, "info").mockImplementation();
  const consoleLogSpy = jest.spyOn(console, "log").mockImplementation();
  const consoleWarnSpy = jest.spyOn(console, "warn").mockImplementation();
  const consoleErrorSpy = jest.spyOn(console, "error").mockImplementation();

  describe("When in development mode:", () => {
    beforeEach(() => {
      jest.spyOn(Environment, "isDevelopment").mockReturnValue(true);
      consoleInfoSpy.mockReset();
      consoleLogSpy.mockReset();
      consoleWarnSpy.mockReset();
      consoleErrorSpy.mockReset();
    });

    it("Logger.info will log to the console using info", () => {
      Logger.info("Development Logger.info test");
      expect(consoleInfoSpy).toHaveBeenCalledTimes(1);
    });

    it("Logger.log will log to the console using log", () => {
      Logger.log("Development Logger.log test");
      expect(consoleLogSpy).toHaveBeenCalledTimes(1);
    });

    it("Logger.warn will log to the console using warn", () => {
      Logger.warn("Development Logger.warn test");
      expect(consoleWarnSpy).toHaveBeenCalledTimes(1);
    });

    it("Logger.error will log to the console using error", () => {
      Logger.error("Development Logger.warn test");
      expect(consoleErrorSpy).toHaveBeenCalledTimes(1);
    });
  });

  describe("When not in development mode:", () => {
    beforeEach(() => {
      jest.spyOn(Environment, "isDevelopment").mockReturnValue(false);
      consoleInfoSpy.mockReset();
      consoleLogSpy.mockReset();
      consoleWarnSpy.mockReset();
      consoleErrorSpy.mockReset();
    });

    it("Logger.info will not log to the console using info", () => {
      Logger.info("Development Logger.info test");
      expect(consoleInfoSpy).toHaveBeenCalledTimes(0);
    });

    it("Logger.log will not log to the console using log", () => {
      Logger.log("Development Logger.log test");
      expect(consoleLogSpy).toHaveBeenCalledTimes(0);
    });

    it("Logger.warn will not log to the console using warn", () => {
      Logger.warn("Development Logger.warn test");
      expect(consoleWarnSpy).toHaveBeenCalledTimes(0);
    });

    it("Logger.error will not log to the console using error", () => {
      Logger.error("Development Logger.warn test");
      expect(consoleErrorSpy).toHaveBeenCalledTimes(0);
    });
  });
});