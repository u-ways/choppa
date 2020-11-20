module.exports = {
  root: true,
  env: {
    node: true,
    "jest/globals": true,
  },
  extends: [
    "@vue/standard",
    "airbnb-base",
    "plugin:vue/essential",
    "plugin:testing-library/recommended",
    "plugin:testing-library/vue",
  ],
  plugins: [
    "jest",
  ],
  parserOptions: {
    parser: "babel-eslint",
  },
  rules: {
    quotes: ["error", "double", { allowTemplateLiterals: true }],
    "max-len": ["error", { code: 120 }],
    "linebreak-style": "off",
    "no-trailing-spaces": "off",
    "no-underscore-dangle": "off",
    "object-curly-newline": ["error", { consistent: true }],
  },
};
