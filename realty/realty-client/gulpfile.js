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