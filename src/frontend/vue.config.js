module.exports = {
  publicPath: process.env.BASE_URL,
  assetsDir: process.env.BASE_URL,
  outputDir: "../main/resources/public",
  devServer: {
    inline: true,
    hot: true,
    stats: "minimal",
    contentBase: __dirname,
    overlay: true,
    historyApiFallback: true,
    proxy: {
      "/api": {
        target: "http://localhost:8888",
        changeOrigin: true,
        secure: false,
      },
    },
  },
};
