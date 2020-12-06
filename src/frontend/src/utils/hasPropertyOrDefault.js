export function hasPropertyOrDefault(object, property, defaultValue) {
  return Object.prototype.hasOwnProperty.call(object, property) ? object[property] : defaultValue;
}
