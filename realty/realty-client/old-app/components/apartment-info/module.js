/**
 * Created by null on 28.09.14.
 */
var apartmentService = require('./apartment-service.js');
var apartmentController = require('./apartment-ctl.js');
var configurator = require('../core/configurator.js');

var cfg = {
    moduleName: 'rentApp.apartmentInfo',
    moduleDependencies: ['ui.router', 'apartment-info/apartment-info-view.html'],

    ctlName: 'ApartmentInfoController',
    serviceName: 'ApartmentInfoService',
    stateName: 'apartmentInfoState',
    stateConfig: {
        //https://github.com/angular-ui/ui-router/wiki/URL-Routing
        url: '/apartment-info/{apartmentId}',
        templateUrl: 'apartment-info/apartment-info-view.html',
        controller: 'ApartmentInfoController' //cfg.ctlName
    },

    service: apartmentService(),
    controller: apartmentController()
};

configurator().configure(cfg);