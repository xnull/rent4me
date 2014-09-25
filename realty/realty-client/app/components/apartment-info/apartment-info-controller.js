/**
 * Created by null on 03.08.14.
 */
var apartmentService = require('./apartment-info-service.js');

var ApartmentInfoModule = (function () {
    'use strict';

    var cfg = {
        moduleName: 'rentApp.apartmentInfo',
        moduleDependencies: ['ui.router'],

        ctlName: 'ApartmentInfoController',
        serviceName: 'ApartmentInfoService',
        stateName: 'apartmentInfoState',
        stateConfig: {
            //https://github.com/angular-ui/ui-router/wiki/URL-Routing
            url: '/apartment-info/{apartmentId}',
            templateUrl: 'components/apartment-info/apartment-info-view.html',
            controller: 'ApartmentInfoController' //cfg.ctlName
        }
    };

    var angularModule;
    var angularLogger;

    function init() {
        angularModule = angular.module(cfg.moduleName, cfg.moduleDependencies);
        angularLogger = angular.injector([cfg.moduleName, 'ng']).get('$log');
        angularLogger.debug('Loading "' + cfg.moduleName + '" module');

        angularModule.config(function ($stateProvider) {
            $stateProvider.state(cfg.stateName, cfg.stateConfig);
        });

        angularModule.controller(cfg.ctlName, controller);
        angularModule.factory(cfg.serviceName, apartmentService(cfg.serviceName));
    }

    /**
     * dynamic pages https://egghead.io/lessons/angularjs-using-resource-for-data-models
     * @param $log
     * @param $scope
     * @param rentService
     * @param navigationService
     */
    function controller($log, $scope, $stateParams, navigationService, ApartmentInfoService) {
        $log.debug('Apartment info controller: execution');

        $scope.apartmentInfo = ApartmentInfoService.getApartmentById($stateParams.apartmentId);

        $(function () {
            var $carousel = $('.carousel');
            $carousel.carousel({
                interval: 15000,
                pause: "hover",
                wrap: true
            });

            $('.carousel-control').each(function () {
                var elem = $(this);
                elem.on('click', function () {
                    var dataSlideDirection = elem.attr('data-slide');
                    $carousel.carousel(dataSlideDirection);
                });
            });
        });
    }

    return {
        init: init,
        ctl: controller,
        srv: apartmentService(cfg.serviceName)
    };
})();

ApartmentInfoModule.init();
