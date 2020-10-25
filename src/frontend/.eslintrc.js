module.exports = {
  root: true,
  env: {
    node: true,
  },
  extends: [
    "@vue/standard",
    "airbnb-base",
    "plugin:vue/essential",
  ],
  parserOptions: {
    parser: "babel-eslint",
  },
  rules: {
    quotes: ["error", "double", { allowTemplateLiterals: true }],
    "max-len": ["error", { "code": 120 }],
  },
};
