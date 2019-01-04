
const path = require('path');
module.exports = {
  entry: {
    client_hydrate: './client_hydrate.js',
    client_render: './client_render.js',
  },
  mode: process.env.NODE_ENV || 'development',
  output: {
    path: path.resolve(__dirname, 'build'),
    filename: '[name].js',
    publicPath: '/',
  },
  module: {
    rules: [
      {
        test: /\.js$/,
        loader: 'babel-loader',
        exclude: /node_modules/,
        options: {
          "presets": [
            "@babel/preset-env",
            "@babel/preset-react"
          ],
          "plugins": [
            "@babel/plugin-syntax-dynamic-import",
            "@babel/plugin-syntax-import-meta",
            [
              "@babel/plugin-proposal-class-properties",
              {
                "loose": true
              }
            ],
            "@babel/plugin-proposal-json-strings",
            "@babel/plugin-proposal-function-sent",
            "@babel/plugin-proposal-export-namespace-from",
            "@babel/plugin-proposal-numeric-separator",
            "@babel/plugin-proposal-throw-expressions"
          ]
        }
      },
    ],
  },
  devServer: {
    port: 4000,
    host: '0.0.0.0',
    proxy: {
      '/api/**': 'http://localhost:8080',
      '/login': 'http://localhost:8080',
    },
  },
  resolve: {
    symlinks: true
  }
};