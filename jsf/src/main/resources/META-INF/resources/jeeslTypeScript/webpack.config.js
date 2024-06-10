const path = require('path');
module.exports = {
  mode: process.env.NODE_ENV === 'development' ? 'development' : 'production',
  entry: {
    jeesl: './src/index.ts', // entry point for the application
    "jeesl.responsive": './src/jeesl.responsive.ts', // entry point for the mobile responsive code
    "jeesl.responsive.mobile": './src/jeesl.responsive.mobile.ts', // entry point for the mobile responsive code
  },
  resolve: {
    extensions: ['.js', '.jsx', '.ts', '.tsx'], // resolve these extensions
    alias: {
      'jquery': 'jquery/src/jquery'
    }
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
    splitChunks: {
      cacheGroups: {
        vendor: {
          test: /[\\/]node_modules[\\/]/, // select node_modules directory
          name: 'vendors', // name of the chunk
          chunks: 'all', // select all types of chunks
        }
      }
    }
  }
};