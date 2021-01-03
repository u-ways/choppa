export const toastVariants = Object.freeze({
  SUCCESS: "Success",
  WARNING: "Warning",
  ERROR: "Error",
  INFO: "Info",
});

export function isValidToastVariant(toastVariant) {
  return Object.values(toastVariants).includes(toastVariant);
}
