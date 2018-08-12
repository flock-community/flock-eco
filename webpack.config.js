const path = require('path')
const HtmlWebPackPlugin = require("html-webpack-plugin");

const root = 'eco-feature/eco-feature-users';

const htmlPlugin = new HtmlWebPackPlugin({
  template: path.join(__dirname, root, 'src/main/react/index.html'),
  filename: "./index.html"
});


module.exports = {

  entry: path.join(__dirname, root, 'src/main/react'),

  output: {
    path: path.join(__dirname, root, 'target/generated-resources')
  },

  module: {
    rules: [
      {
        test: /\.js|jsx$/,
        exclude: /node_modules/,
        use: {
          loader: "babel-loader",
          options: {
            presets: ['env', 'react']
          }
        }
      }
    ]
  },

  plugins: [htmlPlugin],

  devServer: {
    port: 3000,
    proxy: {
      '/api/**': 'http://localhost:8080'
    }
  }

};