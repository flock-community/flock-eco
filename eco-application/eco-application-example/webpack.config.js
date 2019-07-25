const path = require('path')
const HtmlWebPackPlugin = require("html-webpack-plugin");

const htmlPlugin = new HtmlWebPackPlugin({
  template: path.resolve(__dirname, 'src/main/react/index.html'),
  filename: "./index.html"
});

module.exports = {

  entry: [
    'whatwg-fetch',
    path.resolve(__dirname, 'src/main/react')
  ],

  output: {
    path: path.resolve(__dirname, 'src/main/webapp')
  },

  module: {
    rules: [
      {
        test: /\.js|jsx$/,
        exclude: /node_modules/,
        use: {
          loader: "babel-loader",
          options: {
            "plugins": [
              "@babel/plugin-proposal-class-properties"
            ],
            "presets": [
              "@babel/preset-env",
              "@babel/preset-react"
            ]
          }
        }
      }
    ]
  },

  plugins: [htmlPlugin],

  devServer: {
    port: 3000,
    proxy: {
      '/api/**': 'http://localhost:8080',
      '/tasks/**': 'http://localhost:8080',
      '/login**': 'http://localhost:8080',
      '/configuration': 'http://localhost:8080',
      '/_ah/**': 'http://localhost:8080',
    }
  }

};
