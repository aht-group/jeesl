const webpack = require('webpack');
const path = require('path');
const CompressionPlugin = require("compression-webpack-plugin");
const TerserPlugin = require('terser-webpack-plugin');

module.exports = {
  mode:  'development',
  plugins: [
    new webpack.ProvidePlugin({
      $: "jquery",
      jQuery: "jquery",
//      hljs: "highlight.js",
    }),
    new CompressionPlugin({
      // Be very carefully with 'true', sometimes bug happens
      deleteOriginalAssets: false,
      algorithm: 'gzip',
      test: /\.(js|css|html)$/,
    }),
  ],
  entry: {
    jeesl: './src/index.ts', // entry point for the application
    "jeesl.responsive": './src/jeesl.responsive.ts', // entry point for the mobile responsive code
    "jeesl.responsive.mobile": './src/jeesl.responsive.mobile.ts', // entry point for the mobile responsive code
  },
  resolve: {
    extensions: ['.js', '.jsx', '.ts', '.tsx'], // resolve these extensions
    /*
    alias: {
      'jquery': 'jquery/src/jquery'
    }
      */
  },
  output: {
    filename: '[name].bundle.js',
    path: path.resolve(__dirname, './dist/')
    },
  module: {
    rules: [
      {
        test: /\.tsx?$/, // regex to select only .ts or .tsx files
        use: 'ts-loader', // use ts-loader to transpile TypeScript code to JavaScript
        exclude: /node_modules/, // exclude node_modules directory
      },
      {
        test: /\.js$/, // regex to select only .js files
        exclude: /node_modules/, // exclude node_modules directory
        include: path.resolve(__dirname, 'src/js'), // only include .js files from the src/js directory
        use: {
          loader: 'babel-loader',
          options: {
            presets: ['@babel/preset-env'] // use the @babel/preset-env preset
          }
        }
      }
    ]
  },
 
  devtool: 'source-map',
  optimization: {
    minimize: false, // disable minimization
    minimizer: [
      new TerserPlugin({
        parallel: true,
        test: /\.js(\?.*)?$/i,
        terserOptions: {
          format: {
            comments: false,
          },
        },
        extractComments: false,
      }),
    ],
    splitChunks: {
      cacheGroups: {
        vendor: {
          test: /[\\/]node_modules[\\/](?!tslib)/, // select node_modules directory excluding jquery, tslib, jquery-ui and primefaces
          name: 'vendors', // name of the chunk
          chunks: 'all', // only include initial chunks
          reuseExistingChunk: true,
        },
      }
    }
  }
};