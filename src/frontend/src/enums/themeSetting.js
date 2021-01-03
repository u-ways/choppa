export const themeSetting = Object.freeze({
  DARK_THEME: "dark",
  LIGHT_THEME: "light",
  FOLLOW_OS: "follow-os",
});

export function isValidThemeSetting(theme) {
  return Object.values(themeSetting).includes(theme);
}
