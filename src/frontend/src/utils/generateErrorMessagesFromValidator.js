/* eslint-disable no-param-reassign */

export function generateErrorMessagesFromValidator(label, validator) {
  const errorMessages = {};

  addIfExist(errorMessages, validator, label, "required", () => "is required");
  addIfExist(errorMessages, validator, label, "minLength", (param) => `must have at least ${param.min} characters`);
  addIfExist(errorMessages, validator, label, "maxLength", (param) => `must have no more than ${param.max} characters`);
  addIfExist(errorMessages, validator, label, "minValue", (param) => `must be no less than ${param.min}`);
  addIfExist(errorMessages, validator, label, "maxValue", (param) => `must be no more than ${param.max}`);

  return errorMessages;
}

function addIfExist(errorMessages, validator, label, validationName, message) {
  if (validator[validationName] !== undefined) {
    errorMessages[validationName] = `${label} ${message(validator.$params[validationName])}`;
  }
}
