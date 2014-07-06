/**
 *
 */
"use strict";

// Includes
var gulp = require('gulp');
var jshint = require('gulp-jshint');
var print = require('gulp-print');
var concat = require('gulp-concat');

var paths = {
    scripts: ['app/components/**/*.js', 'app/app.js']
    //images: 'client/img/**/*'
};

gulp.task('jsBuild', function () {
    gulp.src(paths.scripts)
        .pipe(jshint()).pipe(jshint.reporter('default'))
        .pipe(concat('all.js'))
        .pipe(gulp.dest('./dist'));

    //concat and copy all css files to destination
    //copy all html files to destination
    //copy all images, eventually all files exclude js
});

// Lint Task
gulp.task('lint', function () {
    return gulp.src(paths.scripts)
        .pipe(jshint())
        .pipe(jshint.reporter('default'));
});

/**
 * Print all javascript files
 */
gulp.task('print', function () {
    gulp.src(paths.scripts).pipe(print());
});