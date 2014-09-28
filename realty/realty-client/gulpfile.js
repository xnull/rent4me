//Common variables
var paths = {};
paths.app = ['app/components/**/*.js', 'app/*js'];
paths.jsHintFiles = 'app/components/**/*.js';
paths.buildDir = './build-js';
paths.dist = [paths.buildDir, '../realty-web/src/main/webapp/view'];

// Includes
var gulp = require('gulp');
var jshint = require('gulp-jshint');
var print = require('gulp-print');
var concat = require('gulp-concat');
var clean = require('gulp-clean');
var gulpBowerFiles = require('gulp-bower-files');
var browserify = require('gulp-browserify');
var source = require('vinyl-source-stream');
var open = require("gulp-open");
var webserver = require('gulp-webserver');
var ngHtml2Js = require("gulp-ng-html2js");
var watch = require('gulp-watch');
//var watchify = require('watchify');
var livereload = require('gulp-livereload');

gulp.task('bower-files', function () {
    gulpBowerFiles().pipe(gulp.dest('./node_modules/client'));
});
gulp.task('cleanDist', function () {
    return gulp.src(paths.dist, {read: false}).pipe(clean({force: true}));
});

// Lint Task
gulp.task('lint', function () {
    return gulp.src(paths.jsHintFiles)
        .pipe(jshint())
        .pipe(jshint.reporter('default'));
});

/**
 * Print all javascript files
 */
gulp.task('files', function () {
    gulp.src(paths.app).pipe(print());
});

gulp.task('watch', function () {
    gulp.src(['./app/**/*.*', 'gulpfile.js'])
        .pipe(watch(['./app/**/*.*', 'gulpfile.js'], function (files) {
            gulp.start('jsBuild');
        }));
});

/**
 * Build distribution with browserify
 *
 * http://markgoodyear.com/2014/01/getting-started-with-gulp/
 * https://www.npmjs.org/package/gulp-ng-html2js - html files minification and saving to js
 * live reload http://mindthecode.com/lets-build-an-angularjs-app-with-browserify-and-gulp/
 *
 * live reload rus: http://frontender.info/getting-started-with-gulp-2/
 */
gulp.task('jsBuild', ['cleanDist'], function () {
    jsBuild();
});

function jsBuild() {
    buildHtmlFiles();
    buildBrowserify();
    cssBuild();
    imgBuild();
    indexHtmlBuild();
}

function buildBrowserify() {
    gulp.src('app/app.js')
        .pipe(browserify({
            insertGlobals: true,
            debug: true
        }))
        .pipe(gulp.dest(paths.buildDir));
}

function buildHtmlFiles() {
    gulp.src("./app/components/**/*.html")
        .pipe(ngHtml2Js({
            moduleName: '',
            prefix: ''
        }))
        .pipe(concat("html-templates.js"))
        .pipe(gulp.dest("./build-js/"));
}

function cssBuild() {
    gulp.src(['app/components/**/*.css', 'app/*.css'])
        .pipe(concat('index.css'))
        .pipe(gulp.dest(paths.buildDir));
}

function imgBuild() {
    gulp.src([
        './app/components/**/*.jpg',
        './app/components/**/*.png',
        './app/components/**/*.gif'
    ])
        .pipe(gulp.dest(paths.buildDir + '/components'));
}

function indexHtmlBuild() {
    gulp.src('./app/index.html')
        .pipe(gulp.dest(paths.buildDir))
}