export function stripAlphaFromHex(color) {
  return color.length === 9 ? color.substring(0, 7) : color;
}
