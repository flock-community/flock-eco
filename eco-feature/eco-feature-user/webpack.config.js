const path = require('path')
const HtmlWebPackPlugin = require('html-webpack-plugin')

const root =
  process.env.BASE_DIR || `../eco-feature/eco-feature-${process.env.FEATURE}/`

const htmlPlugin = new HtmlWebPackPlugin({
  template: path.join(__dirname, 'src/main/frontend/index.html'),
  filename: './index.html',
})

module.exports = {
  entry: path.join(__dirname, 'src/main/frontend'),

  output: {
    path: path.join(__dirname, 'target/generated-resources'),
  },

  devtool: 'eval-source-map',

  resolve: {
    extensions: [ '.ts', '.tsx','.js', '.jsx',  '.json'],
  },

  module: {
    rules: [
      {
        test: /\.json$/,
        exclude: /node_modules/,
        use: {
          loader: './loaders/test_loader.js',
        },
      },
      {
        test: /\.ts|tsx|js|jsx$/,
        exclude: /node_modules/,
        use: {
          loader: 'babel-loader',
          options: {
            plugins: [
              "@babel/plugin-proposal-class-properties",
            ],
            presets: [
              "@babel/preset-env",
              "@babel/preset-react",
              "@babel/preset-typescript",
            ],
          },
        },
      },
    ],
  },

  plugins: [htmlPlugin],

  devServer: {
    port: 3000,
    host: '0.0.0.0',
    proxy: {
      '/api/**': 'http://localhost:8080',
      '/login': 'http://localhost:8080',
    },
  },
}
