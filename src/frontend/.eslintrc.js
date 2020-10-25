module.exports = {
  root: true,
  env: {
    node: true,
  },
  extends: [
    "@vue/standard",
    "airbnb-base",
    "plugin:vue/essential",
    "plugin:testing-library/recommended",
    "plugin:testing-library/vue",
  ],
  parserOptions: {
    parser: "babel-eslint",
  },
  rules: {
    quotes: ["error", "double", { allowTemplateLiterals: true }],
    "max-len": ["error", { code: 120 }],
  },
};
