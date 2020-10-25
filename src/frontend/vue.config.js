module.exports = {
  publicPath: "./",
  devServer: {
    inline: true,
    hot: true,
    stats: "minimal",
    contentBase: __dirname,
    overlay: true,
    historyApiFallback: true,
    proxy: {
      "*": {
        target: "http://localhost:8888",
        changeOrigin: true,
        secure: false,
      },
    },
  },
};
