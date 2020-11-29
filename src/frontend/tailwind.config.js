const colors = require("tailwindcss/colors"); // eslint-disable-line import/no-extraneous-dependencies

module.exports = {
  purge: [
    "./public/**/*.html",
    "./src/**/*.vue",
  ],
  darkMode: "class",
  theme: {
    extend: {
      colors: {
        "choppa-light": {
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
  },
  variants: {
    extend: {
      fontStyle: ["focus"],
      outline: ["active", "focus"],
    },
  },
  plugins: [],
};
