import { v4 as uuidv4 } from "uuid";
import { hasPropertyOrDefault } from "@/utils/hasPropertyOrDefault";
import { isValidToastVariant } from "@/enums/toastVariants";

const DEFAULT_TIME = 10 * 1000;

export default class ToastData {
  constructor(config) {
    if (!config.variant || !config.message) {
      throw new Error("Expected variant and message");
    } else if (!isValidToastVariant(config.variant)) {
      throw new Error(`Unsupported toast variant ${config.variant}`);
    }

    this._id = uuidv4();
    this._message = config.message;
    this._variant = config.variant;
    this._time = hasPropertyOrDefault(config, "time", DEFAULT_TIME);
    this._timer = undefined;
    this._firstDisplay = true;
  }

  get id() {
    return this._id;
  }

  get message() {
    return this._message;
  }

  get variant() {
    return this._variant;
  }

  get time() {
    return this._time;
  }

  get timer() {
    return this._timer;
  }

  set timer(newTimer) {
    this._timer = newTimer;
  }

  isFirstDisplay() {
    return this._firstDisplay;
  }

  displayed() {
    this._firstDisplay = false;
  }
}
