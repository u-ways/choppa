const colors = require("tailwindcss/colors");
const tailWindCssDebugScreens = require("tailwindcss-debug-screens");

module.exports = {
  purge: [
    "./public/**/*.html",
    "./src/**/*.vue",
    "./src/**/*.css",
  ],
  darkMode: "class",
  theme: {
    extend: {
      colors: {
        gray: {
          "666": "#444e5d",
          ...colors.coolGray
        },
        "choppa-light": {
          extra: colors.emerald["500"],
          DEFAULT: colors.emerald["700"],
          hover: colors.emerald["800"],
        },
        "choppa-dark": {
          DEFAULT: colors.emerald["500"],
          hover: colors.emerald["600"],
        },
      },
      fontFamily: {
        "open-sans": ["Open\\ Sans", "sans-serif"],
      },
    },
    container: {
      screens: {
        "sm": "2640px",
        "md": "768px",
        "lg": "1024px",
        "xl": "1280px",
      },
    },
  },
  variants: {
    extend: {
      backgroundColor: ["even"],
      fontStyle: ["focus"],
      outline: ["active", "focus"],
      ringWidth: ["hover", "focus", "active"],
    },
  },
  plugins: [
    tailWindCssDebugScreens,
  ],
};
