/* eslint-disable no-param-reassign */

export function generateErrorMessagesFromValidator(label, validator) {
  const errorMessages = {};

  addIfExist(errorMessages, validator, label, "required", () => "is required");
  addIfExist(errorMessages, validator, label, "minLength", (param) => `must have at least ${param.min} characters`);
  addIfExist(errorMessages, validator, label, "maxLength", (param) => `must have no more than ${param.max} characters`);

  return errorMessages;
}

function addIfExist(errorMessages, validator, label, validationName, message) {
  if (validator[validationName]) {
    errorMessages[validationName] = `${label} ${message(validator.$params[validationName])}`;
  }
}
