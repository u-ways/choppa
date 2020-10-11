module.exports = {
  preset: "ts-jest",
  testEnvironment: "jsdom",
  moduleFileExtensions: [
    "js",
    "ts",
    "vue"
  ],
  transform: {
    "^.+\\.ts?$": "ts-jest",
    ".*\\.(vue)$": "vue-jest"
  },
};