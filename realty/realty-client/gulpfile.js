/**
 *
 */
"use strict";

// Includes
var gulp = require('gulp');
var jshint = require('gulp-jshint');
var print = require('gulp-print');

// Lint Task
gulp.task('lint', function () {
    return gulp.src('app/components/**/*.js')
        .pipe(jshint())
        .pipe(jshint.reporter('default'));
});

/**
 * Print all javascript files
 */
gulp.task('print', function () {
    gulp.src('app/components/**/*.js').pipe(print());
});