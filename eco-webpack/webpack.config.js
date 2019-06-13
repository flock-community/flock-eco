const path = require('path')
const HtmlWebPackPlugin = require('html-webpack-plugin')


const htmlPlugin = new HtmlWebPackPlugin({
  template: path.join(process.cwd(),'src/main/react/index.html'),
  filename: './index.html',
})

module.exports = {
  entry: path.join(process.cwd(),'src/main/react'),

  output: {
    path: path.join(process.cwd(), 'target/generated-resources'),
  },

  devtool: 'eval-source-map',
  module: {
    rules: [
      {
        test: /\.js|jsx$/,
        exclude: /node_modules/,
        use: {
          loader: 'babel-loader',
          options: {
            presets: ['env', 'react', 'stage-2'],
          },
        },
      },
    ],
  },

  plugins: [htmlPlugin],

  devServer: {
    port: 3000,
    host: 'localhost',
    proxy: {
      '/api/**': 'http://localhost:8080',
      '/login': 'http://localhost:8080',
    },
  },
}
