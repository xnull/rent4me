'use strict';

var webpack = require('webpack'),
    HtmlWebpackPlugin = require('html-webpack-plugin'),
    path = require('path'),
    srcPath = path.join(__dirname, 'app');
var CopyWebpackPlugin = require('copy-webpack-plugin');

module.exports = {
    target: 'web',
    cache: true,
    context: srcPath,
    entry: {
        module: path.join(srcPath, 'personal/app.js'),
        common: [
            'jquery', 'react', 'react-router', 'bootstrap', 'react-bootstrap',
            'underscore', 'validator.js', 'JSON2', 'dropzone', 'react-tools', 'through', 'flux', 'events',
            'object-assign', 'moment', 'accounting', 'sockjs-client', 'websocket-multiplex'
        ]
    },
    resolve: {
        root: srcPath,
        extensions: ['', '.js'],
        modulesDirectories: ['node_modules']
    },
    output: {
        path: path.join(__dirname, 'tmp'),
        publicPath: '',
        filename: '[name].js',
        library: ['Example', '[name]'],
        pathInfo: true
    },

    module: {
        loaders: [
            {test: /\.js?$/, exclude: /node_modules/, loader: 'babel?cacheDirectory'},
            {test: /\.css$/, loader: 'style-loader!css-loader'},
            {test: /\.(png|jpg|jpeg|gif)$/, loader: "url-loader?prefix=image"}
        ]
    },
    plugins: [
        new CopyWebpackPlugin([
            {from: 'personal/index.css', to: 'css/index.css'},
            {from: 'start/images', to: 'images'},
            {from: 'personal/images', to: 'images'}
        ]),
        new webpack.ProvidePlugin({
            $: "jquery",
            jQuery: "jquery"
        }),
        new webpack.optimize.CommonsChunkPlugin('common', 'common.js'),
        new HtmlWebpackPlugin({
            inject: true,
            template: 'app/personal/index.html'
        }),
        new webpack.NoErrorsPlugin()
    ],

    debug: true,
    devtool: 'eval-cheap-module-source-map',
    devServer: {
        contentBase: './tmp',
        historyApiFallback: true
    }
};
