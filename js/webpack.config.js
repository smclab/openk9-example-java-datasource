const HtmlWebpackPlugin = require("html-webpack-plugin");
const path = require("path");

module.exports = {
  module: {
    rules: [
      {
        test: /\.tsx?$/,
        use: "ts-loader",
        exclude: /node_modules/,
      },
    ],
  },
  entry: { index: path.resolve(__dirname, "src", "index.ts") },
  output: {
    path: path.resolve(__dirname, "build"),
    library: "someLibName",
    libraryTarget: "commonjs-module",
  },
  // plugins: [
  //   new HtmlWebpackPlugin({
  //     template: path.resolve(__dirname, "mock", "index.html"),
  //   }),
  // ],
};
