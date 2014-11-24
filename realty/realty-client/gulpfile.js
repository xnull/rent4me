/**
 * Плагины для галпа
 * @constructor
 */
function Plugins() {
    this.gulp = require('gulp');
    this.jshint = require('gulp-jshint');
    this.print = require('gulp-print');
    this.concat = require('gulp-concat');
    this.clean = require('gulp-clean');
    this.gulpBowerFiles = require('gulp-bower-files');
    this.browserify = require('gulp-browserify');
    this.source = require('vinyl-source-stream');
    this.open = require("gulp-open");
    this.webserver = require('gulp-webserver');
    this.ngHtml2Js = require("gulp-ng-html2js");
    this.watch = require('gulp-watch');
    //this.watchify = require('watchify');
    this.livereload = require('gulp-livereload');
}

//Common variables
function Settings() {
    this.projectDir = 'app';
    this.buildDir = './build-js';

    this.nodeModules = 'node_modules';
    this.bowerDir = this.nodeModules + '/bower';

    this.dist = [this.buildDir];
    this.appFiles = [this.projectDir + '/*js'];
    this.jsHintFiles = this.projectDir + '/**/*.js';

    this.gulpFile = 'gulpfile.js';
}

function Tasks() {
    this.bowerFiles = 'bower-files';
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
plugins.gulp.task(tasks.watch, function () {
    plugins.gulp.src(['./app/**/*.*', this.gulpFile])
        .pipe(watch(['./app/**/*.*', this.gulpFile], function (files) {
            plugins.gulp.start(tasks.build);
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
plugins.gulp.task(tasks.build, [tasks.clean], function () {
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
    plugins.gulp.src('app/app.js')
        .pipe(browserify({
            insertGlobals: true,
            debug: true
        }))
        .pipe(plugins.gulp.dest(paths.buildDir));
}

function buildHtmlFiles() {
    plugins.gulp.src("./app/components/**/*.html")
        .pipe(ngHtml2Js({
            moduleName: '',
            prefix: ''
        }))
        .pipe(concat("html-templates.js"))
        .pipe(plugins.gulp.dest("./build-js/"));
}

function cssBuild() {
    plugins.gulp.src(['app/components/**/*.css', 'app/*.css'])
        .pipe(concat('index.css'))
        .pipe(plugins.gulp.dest(paths.buildDir));
}

function imgBuild() {
    plugins.gulp.src([
        './app/components/**/*.jpg',
        './app/components/**/*.png',
        './app/components/**/*.gif'
    ])
        .pipe(plugins.gulp.dest(paths.buildDir + '/components'));
}

function indexHtmlBuild() {
    plugins.gulp.src('./app/index.html')
        .pipe(plugins.gulp.dest(paths.buildDir))
}

// ------------------------------------ малозначимые бизнес таски ------------------------------------------- //

/**
 * Очистка билдовой папки проекта
 */
plugins.gulp.task(tasks.clean, function () {
    return plugins.gulp.src(paths.dist, {read: false}).pipe(clean({force: true}));
});

/**
 * Проверка качества js кода. Наподобие джавашного findbugs-a
 */
plugins.gulp.task(tasks.jsHint, function () {
    return plugins.gulp.src(paths.jsHintFiles)
        .pipe(jshint())
        .pipe(jshint.reporter('default'));
});


// ---------------------------- системные (утилитные) таски ---------------------------- //

/**
 * Скачивание bower зависимостей. Сами зависимости можно посмотреть в bower.json
 * Для скачивания зависимостей у нас используется npmJs (см. package.json) то так как это нодовские зависимости,
 * то в них может не быть многих клиентских библиотек, например bootstrap. Для всех таких библиотек нам приходится
 * пользоваться bower-ом. Чтобы отличать боверские либы от npm-ных, таска кладет их в папку bower внутрь node_modules
 */
plugins.gulp.task(tasks.bowerFiles, function () {
    plugins.gulpBowerFiles().pipe(plugins.gulp.dest(settings.bowerDir));
});

/**
 * Print all javascript files
 */
plugins.gulp.task(tasks.files, function () {
    plugins.gulp.src(paths.app).pipe(print());
});