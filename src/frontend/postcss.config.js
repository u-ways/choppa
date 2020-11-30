const autoprefixer = require("autoprefixer");
const postCssImport = require("postcss-import");
const tailwindcss = require("tailwindcss");

module.exports = {
  plugins: [
    // Order matters
    postCssImport,
    tailwindcss,
    autoprefixer,
  ],
};
