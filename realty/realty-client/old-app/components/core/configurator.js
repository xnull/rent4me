/**
 * Angular module configuration function
 * Created by null on 27.09.14.
 */
var validator = require('./validator.js');

function Configurator() {
    'use strict';

    var angularModule;
    var angularLogger;

    return {

        /**
         * Configure an angular module
         * @param cfg example:
         * {
         *      moduleName: 'rentApp.moduleNAme',
         *      moduleDependencies: ['ui.router', 'componentName/component-view.html'],
         *
         *      ctlName: 'ControllerName',
         *      serviceName: 'ServiceName',
         *      stateName: 'componentState',
         *      stateConfig: {
         *          url: '/componentName',
         *          templateUrl: 'componentName/component-view.html',
         *          controller: 'ControllerName'
         *      },
         *
         *       service: serviceFunction,
         *       controller: ctlFunction
         * }
         */
        configure: function configure(cfg) {
            commonConfiguration(cfg);
            checkUiCfg(cfg);
            configureUiRouter(cfg);
        },

        configureWithoutUi: function configureWithoutUi(cfg) {
            commonConfiguration(cfg);
        }
    };

    function commonConfiguration(cfg) {
        checkCfg(cfg);
        initAngularModule(cfg);
        initControllersAndServices(cfg);
    }

    function configureUiRouter(cfg) {
        angularModule.config(function ($stateProvider) {
            $stateProvider.state(cfg.stateName, cfg.stateConfig);
        });
    }

    function initAngularModule(cfg) {
        angularModule = angular.module(cfg.moduleName, cfg.moduleDependencies);
        angularLogger = angular.injector([cfg.moduleName, 'ng']).get('$log');
        angularLogger.debug('Loading "' + cfg.moduleName + '" module');
    }

    function initControllersAndServices(cfg) {
        angularModule.controller(cfg.ctlName, cfg.controller);
        angularModule.factory(cfg.serviceName, cfg.service);
    }

    function IllegalArgumentException(message) {
        this.name = 'IncorrectConfigObject';
        this.message = message;
    }

    function checkCfg(cfg) {
        //validator().checkUndefined(cfg);
        checkProperty(cfg, 'moduleName');
        checkProperty(cfg, 'moduleDependencies');
        checkProperty(cfg, 'ctlName');
        checkProperty(cfg, 'serviceName');
        checkProperty(cfg, 'service');
        checkProperty(cfg, 'controller');
    }

    function checkUiCfg(cfg) {
        checkProperty(cfg, 'stateName');
        checkProperty(cfg, 'stateConfig');
        checkProperty(cfg.stateConfig, 'url');
        checkProperty(cfg.stateConfig, 'templateUrl');
        checkProperty(cfg.stateConfig, 'controller');
    }

    function checkProperty(cfg, prop) {
        if (typeof cfg[prop] === 'undefined') {
            var error = 'Incorrect config. Module: ' + cfg.moduleName + ', missing property: ' + prop + '. Config: ' + JSON.stringify(cfg);
            console.log(error);
            throw new IllegalArgumentException(error);
        }
    }
}

module.exports = function () {
    return Configurator();
};