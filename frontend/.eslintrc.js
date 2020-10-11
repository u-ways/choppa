module.exports = {
  root: true,
  env: {
    "node": true
  },
  extends: [
    "plugin:vue/vue3-essential",
    "eslint:recommended",
    "@vue/typescript",
    "plugin:import/errors",
    "plugin:import/warnings",
    "plugin:import/typescript",
    "plugin:jest/style"
  ],
  parserOptions: {
    "parser": "@typescript-eslint/parser"
  },
  plugins: [
    "@typescript-eslint"
  ],
  rules: {
    "semi": "error",
    "strict": "error",
    "quotes": ["error", "double", { "allowTemplateLiterals": true }],
    "object-curly-spacing": ["error", "always"],
    "brace-style": ["error", "1tbs"],
    "curly": ["error", "all"],
    "indent": "off",
    "vue/script-indent": ["error", 2, { "baseIndent": 0 }],
    "vue/html-indent": ["error", 2, { "baseIndent": 0 }],
    "vue/require-explicit-emits": ["error", { "allowProps": false }],
    "@typescript-eslint/explicit-function-return-type": ["error", { "allowExpressions": true }]
  },
  settings: {
    "import/resolver": {
      "node": {
        "paths": ["src"],
        "extensions": [".js", ".ts", ".vue"]
      }
    }
  }
};
