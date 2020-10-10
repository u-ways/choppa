import { Environment } from "./Environment";

// TODO BT: Warn, Error should send the message the server?
export default class Logger {

    public static info(message: string) {
        if (Environment.isDevelopment()) {
            console.info(message);
        }
    }

    public static log(message: string) {
        if (Environment.isDevelopment()) {
            console.log(message);
        }
    }

    public static warn(message: string) {
        if (Environment.isDevelopment()) {
            console.warn(message);
        }
    }

    public static error(message: string) {
        if (Environment.isDevelopment()) {
            console.error(message);
        }
    }
}