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
var minifyCSS = require('gulp-minify-css');
var open = require("gulp-open");
var webserver = require('gulp-webserver');
var ngHtml2Js = require("gulp-ng-html2js");

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

/**
 * Build distribution with browserify
 *
 * http://markgoodyear.com/2014/01/getting-started-with-gulp/
 * https://www.npmjs.org/package/gulp-ng-html2js - html files minification and saving to js
 * live reload http://mindthecode.com/lets-build-an-angularjs-app-with-browserify-and-gulp/
 */
gulp.task('jsBuild', ['cleanDist'], function () {
    gulp.src("./app/components/**/*.html")
        .pipe(ngHtml2Js({
            moduleName: "html.templates",
            prefix: ''
        }))
        .pipe(concat("html-templates.js"))
        .pipe(gulp.dest("./build-js/"));

    gulp.src('app/app.js')
        .pipe(browserify({
            insertGlobals: true,
            debug: true
        }))
        .pipe(gulp.dest(paths.buildDir));

    gulp.src(['app/components/**/*.css', 'app/*.css'])
        .pipe(concat('index.css'))
        .pipe(gulp.dest(paths.buildDir));

    gulp.src(['./app/components/**/*.jpg', './app/components/**/*.png', './app/components/**/*.gif'])
        .pipe(gulp.dest(paths.buildDir + '/components'));

    gulp.src('./app/index.html')
        .pipe(gulp.dest(paths.buildDir))

});

gulp.task("browser", ['webserver'], function () {
    gulp.src(paths.buildDir)
        .pipe(open("<%file.path%>", {app: "google-chrome"}));
});

//http://habrahabr.ru/post/224825/
gulp.task('webserver', ['jsBuild'], function () {
    gulp.src(paths.buildDir)
        .pipe(webserver({
            port: 12345,
            livereload: true,
            directoryListing: true,
            open: true
        }));
});