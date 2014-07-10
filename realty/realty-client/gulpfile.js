// Includes
var gulp = require('gulp');
var jshint = require('gulp-jshint');
var print = require('gulp-print');
var concat = require('gulp-concat');
var clean = require('gulp-clean');
var gulpBowerFiles = require('gulp-bower-files');

var paths = {};
paths.app = 'app/**';
paths.jsHintFiles = 'app/components/**/*.js';
paths.dist = '../realty-web/src/main/webapp/view';

gulp.task('jsBuild', ['clean-dist'], function () {
    gulp.src(paths.jsHintFiles).pipe(jshint()).pipe(jshint.reporter('default'));
    gulpBowerFiles().pipe(gulp.dest('./app/vendor'));
    gulp.src(paths.app).pipe(gulp.dest(paths.dist));
});

gulp.task('clean-dist', function () {
    return gulp.src(paths.dist, {read: false}).pipe(clean({force: true}));
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