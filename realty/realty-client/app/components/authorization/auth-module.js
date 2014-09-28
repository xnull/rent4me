/**
 * Created by null on 28.09.14.
 */
var authService = require('./auth-service.js');
var authCtl = require('./auth-ctl.js');
var configurator = require('../core/configurator.js');

var cfg = {
    moduleName: 'rentApp.auth',
    moduleDependencies: [],

    ctlName: 'AuthCtl',
    serviceName: 'AuthService',

    service: authService(),
    controller: authCtl()
};

configurator().configureWithoutUi(cfg);