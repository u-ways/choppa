const DARK_THEME = "dark";
const LIGHT_THEME = "light";

function isDark() {
  return localStorage.theme === DARK_THEME;
}

export function applyTheme() {
  if (isDark()) {
    document.querySelector("html").classList.add("dark");
  } else {
    document.querySelector("html").classList.remove("dark");
  }
}

export function toggleTheme() {
  localStorage.theme = isDark() ? LIGHT_THEME : DARK_THEME;
  applyTheme();
}
