'use strict';

module.exports = function(karma) {
    karma.set({
        // base path that will be used to resolve all patterns (eg. files, exclude)
        basePath: '../app',

        frameworks: [ 'jasmine', 'browserify' ],

        files: [
            '../test/spec/**/*-spec.js'
        ],

        reporters: [ 'dots' ],

        preprocessors: {
            '../test/spec/**/*-spec.js': [ 'browserify' ]
        },

        //browsers: [ 'PhantomJS' ],
        browsers: [ 'Firefox' ],

        logLevel: 'LOG_DEBUG',

        singleRun: true,
        autoWatch: false,

        // browserify configuration
        browserify: {
            debug: true,
            transform: [ 'brfs', 'browserify-shim', 'reactify' ],
            extensions: ['.js', '.jsx']
        }
    });
};