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
        exclude: /node_modules/,
        loader: 'babel-loader',
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
};