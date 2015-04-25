/**
 * Плагины для галпа
 * @constructor
 */
var gulp = require('gulp');
var substituter = require('gulp-substituter');

function __guid() {
    function s4() {
        return Math.floor((1 + Math.random()) * 0x10000)
            .toString(16)
            .substring(1);
    }
    return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
        s4() + '-' + s4() + s4() + s4();
}

var argv = require('yargs').argv;

var buildNumber = argv.buildNumber || __guid();

console.log('Starting:' + buildNumber);

function Plugins() {
    this.gutil = require('gulp-util');
    this.jshint = require('gulp-jshint');
    this.print = require('gulp-print');
    this.concat = require('gulp-concat');
    this.clean = require('gulp-clean');
    this.uglify = require('gulp-uglify');
    this.replace = require('gulp-replace');
    this.stripDebug = require('gulp-strip-debug');

    this.gulpBowerFiles = require('gulp-bower-files');
    this.browserify = require('browserify');
    this.to5ify = require("6to5ify");
    this.source = require('vinyl-source-stream');
    this.reactify = require('reactify');

    this.open = require("gulp-open");
    this.webserver = require('gulp-webserver');
    //this.ngHtml2Js = require("gulp-ng-html2js");
    this.watch = require('gulp-watch');
    //this.watchify = require('watchify');
    this.livereload = require('gulp-livereload');
    this.gulpConnect = require('gulp-connect');
}

//Common variables
function Settings() {
    this.projectBaseDir = './app';

    this.projectSubModules = [
        '/personal',
        '/start',
        '/vk_auth_return_page'
    ];

    this.projectBuildSubPaths = {
        '/start': '/'
    };

    this.moduleInfo = {};

    this.projectDir = this.projectBaseDir;
    this.buildDir = './build-js';
    this.mainJsFile = this.projectBaseDir + '/app.js';

    this.nodeModules = './node_modules';
    this.bowerDir = this.nodeModules + '/bower';

    this.dist = [this.buildDir];
    this.indexHtml = this.projectBaseDir + '/index.html';

    this.appFiles = [this.projectBaseDir + '/*js'];
    this.cssFiles = [this.projectBaseDir + '/**/*.css', this.projectBaseDir + '/*.css'];
    this.images = [this.projectBaseDir + 'img/**/*.jpg', this.projectBaseDir + 'img/**/*.png', this.projectBaseDir + 'img/**/*.gif'];

    this.jsHintFiles = this.projectBaseDir + '/**/*.js';

    this.gulpFile = 'gulpfile.js';
    this.watchDirs = [this.projectBaseDir + '/**/*.*'];

    //init module info
    var len = this.projectSubModules.length;
    for (var i = 0; i < len; i++) {
        var moduleName = this.projectSubModules[i];
        console.log('Initializing module ' + moduleName);
        this.moduleInfo[moduleName] = {};
    }

    for (var i = 0; i < len; i++) {
        var moduleName = this.projectSubModules[i];
        var projectDir = this.projectBaseDir + moduleName;
        this.moduleInfo[moduleName]['projectDir'] = projectDir;
        this.moduleInfo[moduleName]['buildDir'] = this.buildDir + (this.projectBuildSubPaths[moduleName] || moduleName);
        this.moduleInfo[moduleName]['mainJsFile'] = projectDir + '/app.js';
        this.moduleInfo[moduleName]['dist'] = [this.moduleInfo[moduleName]['buildDir']];
        this.moduleInfo[moduleName]['indexHtml'] = projectDir + '/index.html';
        this.moduleInfo[moduleName]['appFiles'] = [projectDir + '/*js'];
        this.moduleInfo[moduleName]['cssFiles'] = [projectDir + '/**/*.css', projectDir + '/*.css'];
        this.moduleInfo[moduleName]['fontFiles'] = [projectDir + '/fonts/*.*'];
//        this.moduleInfo[moduleName]['images'] = [projectDir + 'images/**/*.jpg', projectDir + 'images/**/*.png', projectDir + 'images/**/*.gif'];
        this.moduleInfo[moduleName]['images'] = [projectDir + '/images/**/*.*'];
        this.moduleInfo[moduleName]['jsHintFiles'] = projectDir + '/**/*.js';
        this.moduleInfo[moduleName]['watchDir'] = projectDir + '/**/*.*';
    }
}

function Tasks() {
    this.bowerInstall = 'bowerInstall';
    this.clean = 'clean';
    this.jsHint = 'jsHint';
    this.files = 'files';
    this.watch = 'watch';
    this.build = 'build';
    this.reloadServer = 'reload-server';
    this.connectServer = 'connect-server'
}

var plugins = new Plugins();
var settings = new Settings();
var tasks = new Tasks();

// ----------------------------------- Главные бизнес таски -------------------------------- //

/**
 * Наблюдение за исходниками и если они изменились, то производится пересборка проекта
 */
gulp.task(tasks.watch, [tasks.connectServer], function () {
    gulp.src(settings.watchDirs)
        .pipe(plugins.watch(settings.watchDirs, function (files) {
                buildNumber = __guid();
                gulp.start(tasks.build);
                gulp.start(tasks.reloadServer)
            }
        ))
        .on('error', plugins.gutil.log);
});

gulp.task(tasks.connectServer, function () {
    plugins.gulpConnect.server({
        root: './build-js',
        port: 9999,
        livereload: true
    });
});

gulp.task(tasks.reloadServer, function () {
    gulp.src('./build-js').pipe(plugins.gulpConnect.reload());
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

gulp.task('compressDev', [], function () {

    var len = settings.projectSubModules.length;
    for (var i = 0; i < len; i++) {
        var moduleName = settings.projectSubModules[i];
        console.log('Building: ' + moduleName);
        var moduleInfo = settings.moduleInfo[moduleName];
        console.log('Compressing js for: ' + moduleName);
        if (moduleName != '/start') {
            var jsToCompress = moduleInfo['buildDir'] + "/js/main-"+buildNumber+".js";
            console.log('Js will be compressed and replaced:' + jsToCompress);

            gulp.src(jsToCompress)
                .pipe(plugins.stripDebug())
                .pipe(plugins.uglify({mangle: false, compress: true}))
                .pipe(gulp.dest(moduleInfo['buildDir'] + "/js"));
        } else {
            var jsToCompress = moduleInfo['buildDir'] + "js/main-"+buildNumber+".js";
            console.log('Js will be compressed and replaced:' + jsToCompress);

            gulp.src(jsToCompress)
                .pipe(plugins.stripDebug())
                .pipe(plugins.uglify({mangle: false, compress: true}))
                .pipe(gulp.dest(moduleInfo['buildDir'] + "/js"));
        }
    }

});

gulp.task('compressProd', [], function () {

    var len = settings.projectSubModules.length;
    for (var i = 0; i < len; i++) {
        var moduleName = settings.projectSubModules[i];
        console.log('Building: ' + moduleName);
        var moduleInfo = settings.moduleInfo[moduleName];
        console.log('Compressing js for: ' + moduleName);
        if (moduleName != '/start') {
            var jsToCompress = moduleInfo['buildDir'] + "/js/main-"+buildNumber+".js";
            console.log('Js will be compressed and replaced:' + jsToCompress);

            gulp.src(jsToCompress)
                .pipe(plugins.stripDebug())
                .pipe(plugins.uglify({mangle: false, compress: true}))
                .pipe(gulp.dest(moduleInfo['buildDir'] + "/js"));
        } else {
            var jsToCompress = moduleInfo['buildDir'] + "js/main-"+buildNumber+".js";
            console.log('Js will be compressed and replaced:' + jsToCompress);

            gulp.src(jsToCompress)
                .pipe(plugins.stripDebug())
                .pipe(plugins.uglify({mangle: false, compress: true}))
                .pipe(gulp.dest(moduleInfo['buildDir'] + "/js"));
        }
    }

});

function build() {
    var len = settings.projectSubModules.length;
    for (var i = 0; i < len; i++) {
        var moduleName = settings.projectSubModules[i];
        console.log('Building: ' + moduleName);
        var moduleInfo = settings.moduleInfo[moduleName];
        if (moduleName != '/start') {
            buildBrowserify(moduleInfo);
            cssBuild(moduleInfo);
            imgBuild(moduleInfo);
            indexHtmlBuild(moduleInfo);
            fontsBuild(moduleInfo);
        } else {
            buildStart(moduleInfo);
        }
    }
}

function buildStart(moduleInfo) {
    buildBrowserify(moduleInfo);//build js stuff
    //indexHtmlBuild(moduleInfo);
    fontsBuild(moduleInfo);

    //simply copy all html files
    gulp.src([moduleInfo['projectDir'] + "/**/*.html"])
        .pipe(substituter({
            buildNumber: buildNumber
        }))
        .pipe(gulp.dest(moduleInfo['buildDir']));

    //and xml files
    gulp.src([moduleInfo['projectDir'] + "/**/*.xml"])
        .pipe(gulp.dest(moduleInfo['buildDir']));

    //and txt files
    gulp.src([moduleInfo['projectDir'] + "/**/*.txt"])
        .pipe(gulp.dest(moduleInfo['buildDir']));

    //simply copy all images
    gulp.src([moduleInfo['projectDir'] + "/images/**/*.*"])
        .pipe(gulp.dest(moduleInfo['buildDir'] + "/images"));

//    imgBuild(moduleInfo);
    //simply copy all content of js
    gulp.src([moduleInfo['projectDir'] + "/css/**/*.*"])
        .pipe(gulp.dest(moduleInfo['buildDir'] + "/css"));
    //simply copy all content of js
    gulp.src([moduleInfo['projectDir'] + "/js/*.*"])
        .pipe(gulp.dest(moduleInfo['buildDir'] + "/js"));
}

function buildBrowserify(moduleInfo) {
    var b = plugins.browserify();
    b
        .transform(plugins.to5ify)
        .transform(plugins.reactify); // use the reactify transform
    b.add(moduleInfo['mainJsFile']);
    b.bundle()
        .pipe(plugins.source('main-'+buildNumber+'.js'))
        .pipe(gulp.dest(moduleInfo['buildDir'] + "/js"));
}

function cssBuild(moduleInfo) {
    gulp.src(moduleInfo['cssFiles'])
        .pipe(plugins.concat('index.css'))
        .pipe(gulp.dest(moduleInfo['buildDir'] + "/css"));
}

function imgBuild(moduleInfo) {
    console.log("Image build for: ");
    console.log(moduleInfo['images']);
    gulp.src(moduleInfo['images'])
        .pipe(gulp.dest(moduleInfo['buildDir'] + '/images'));
}

function indexHtmlBuild(moduleInfo) {
    gulp.src(moduleInfo['indexHtml'])
        .pipe(substituter({
            buildNumber: buildNumber
        }))
        .pipe(gulp.dest(moduleInfo['buildDir']))
}

function fontsBuild(moduleInfo) {
    gulp.src(moduleInfo['fontFiles'])
        .pipe(gulp.dest(moduleInfo['buildDir'] + '/fonts'))
}

// ------------------------------------ малозначимые бизнес таски ------------------------------------------- //

/**
 * Очистка билдовой папки проекта
 */
gulp.task(tasks.clean, function () {
    return gulp.src(settings.dist, {read: false})
        .pipe(plugins.clean({force: true}));
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