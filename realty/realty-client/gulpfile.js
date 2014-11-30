/**
 * Плагины для галпа
 * @constructor
 */
var gulp = require('gulp');
function Plugins() {
    this.jshint = require('gulp-jshint');
    this.print = require('gulp-print');
    this.concat = require('gulp-concat');
    this.clean = require('gulp-clean');

    this.gulpBowerFiles = require('gulp-bower-files');
    this.browserify = require('browserify');
    this.source = require('vinyl-source-stream');
    this.reactify = require('reactify');

    this.open = require("gulp-open");
    this.webserver = require('gulp-webserver');
    //this.ngHtml2Js = require("gulp-ng-html2js");
    this.watch = require('gulp-watch');
    //this.watchify = require('watchify');
    this.livereload = require('gulp-livereload');
}

//Common variables
function Settings() {
    this.projectDir = './app/personal';
    this.buildDir = './build-js';
    this.mainJsFile = this.projectDir + '/app.js';

    this.nodeModules = './node_modules';
    this.bowerDir = this.nodeModules + '/bower';

    this.dist = [this.buildDir];
    this.indexHtml = this.projectDir + '/index.html';

    this.appFiles = [this.projectDir + '/*js'];
    this.cssFiles = [this.projectDir + '/**/*.css', this.projectDir + '/*.css'];
    this.images = [this.projectDir + 'img/**/*.jpg', this.projectDir + 'img/**/*.png', this.projectDir + 'img/**/*.gif'];

    this.jsHintFiles = this.projectDir + '/**/*.js';

    this.gulpFile = 'gulpfile.js';
    this.watchDirs = [this.projectDir + '/**/*.*'];
}

function Tasks() {
    this.bowerInstall = 'bowerInstall';
    this.clean = 'clean';
    this.jsHint = 'jsHint';
    this.files = 'files';
    this.watch = 'watch';
    this.build = 'build';
}

var plugins = new Plugins();
var settings = new Settings();
var tasks = new Tasks();

// ----------------------------------- Главные бизнес таски -------------------------------- //

/**
 * Наблюдение за исходниками и если они изменились, то производится пересборка проекта
 */
gulp.task(tasks.watch, function () {
    gulp.src(settings.watchDirs)
        .pipe(plugins.watch(settings.watchDirs, function (files) {
            gulp.start(tasks.build);
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
gulp.task(tasks.build, [tasks.clean], function () {
    build();
});

function build() {
    buildBrowserify();
    cssBuild();
    imgBuild();
    indexHtmlBuild();
}

function buildBrowserify() {
    var b = plugins.browserify();
    b.transform(plugins.reactify); // use the reactify transform
    b.add(settings.mainJsFile);
    return b.bundle()
        .pipe(plugins.source('main.js'))
        .pipe(gulp.dest(settings.buildDir));
}

function cssBuild() {
    gulp.src(settings.cssFiles)
        .pipe(plugins.concat('index.css'))
        .pipe(gulp.dest(settings.buildDir));
}

function imgBuild() {
    gulp.src(settings.images).pipe(gulp.dest(settings.buildDir + '/img'));
}

function indexHtmlBuild() {
    gulp.src(settings.indexHtml)
        .pipe(gulp.dest(settings.buildDir))
}

// ------------------------------------ малозначимые бизнес таски ------------------------------------------- //

/**
 * Очистка билдовой папки проекта
 */
gulp.task(tasks.clean, function () {
    return gulp.src(settings.dist, {read: false}).pipe(plugins.clean({force: true}));
});

/**
 * Проверка качества js кода. Наподобие джавашного findbugs-a
 */
gulp.task(tasks.jsHint, function () {
    return gulp.src(settings.jsHintFiles)
        .pipe(plugins.jshint())
        .pipe(plugins.jshint.reporter('default'));
});


// ---------------------------- системные (утилитные) таски ---------------------------- //

/**
 * Скачивание bower зависимостей. Сами зависимости можно посмотреть в bower.json
 * Для скачивания зависимостей у нас используется npmJs (см. package.json) то так как это нодовские зависимости,
 * то в них может не быть многих клиентских библиотек, например bootstrap. Для всех таких библиотек нам приходится
 * пользоваться bower-ом. Чтобы отличать боверские либы от npm-ных, таска кладет их в папку bower внутрь node_modules
 */
gulp.task(tasks.bowerInstall, function () {
    plugins.gulpBowerFiles().pipe(gulp.dest(settings.bowerDir));
});

/**
 * Print all javascript files
 */
gulp.task(tasks.files, function () {
    gulp.src(settings.app).pipe(print());
});