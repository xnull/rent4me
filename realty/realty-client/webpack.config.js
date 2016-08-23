const webpack = require('webpack');
const path = require('path');

function isProduction() {
  let production = false;
  for(var arg of process.argv) {
    if(arg == '-p') {
      production = true;
      break;
    }
  }
  return production;
}

module.exports = {
    entry: path.join(__dirname, "src/entry.js"),
    output: {
      path: path.join(__dirname, 'build'),
      filename: 'bundle.js'
    },
    module: {
        loaders: [
            { test: /\.css$/, loader: "style!css" },
            { test: /\.js$/, exclude: /node_modules/, loader: "babel-loader" }
        ]
    },
    plugins: [
      new webpack.DefinePlugin({
        'process.env': {
          'NODE_ENV': JSON.stringify(isProduction() ? 'production' : 'development')//TODO: check is production
        }
      })
    ],

    //enable for development when 404 fallback to /index.html so that react-router browserHistory will work
    devServer: {
      historyApiFallback: true
    }
};
