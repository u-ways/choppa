export default function hasMissingOrTrueProperty(object, propertyName) {
  return !Object.prototype.hasOwnProperty.call(object, propertyName) || object[propertyName];
}
