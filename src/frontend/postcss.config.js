module.exports = {
  plugins: [
    // Order matters
    require("postcss-import"),
    require("tailwindcss"),
    require("autoprefixer"),
  ],
};
