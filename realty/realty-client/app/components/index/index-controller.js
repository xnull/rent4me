var indexModule = (function () {
    'use strict';

    var cfg = {
        moduleName: 'rentApp.index',
        moduleDependencies: ['ui.router'],

        ctlName: 'IndexController',
        serviceName: 'indexService',
        stateName: 'indexState',
        stateConfig: {
            url: '/',
            templateUrl: 'index/index-view.html',
            controller: 'IndexController'
        }
    };

    var angularModule = angular.module(cfg.moduleName, cfg.moduleDependencies);
    var angularLogger = angular.injector([cfg.moduleName, 'ng']).get('$log');

    function init() {
        angularLogger.debug('Loading "' + cfg.moduleName + '" module');

        angularModule.config(function ($stateProvider) {
            $stateProvider.state(cfg.stateName, cfg.stateConfig);
        });

        angularModule.controller(cfg.ctlName, controller);
        angularModule.factory(cfg.serviceName, service);
    }

    function controller($scope, indexService, navigationService, $log) {
        $log.debug('Index controller execution');

        $scope.carouselData = [
            {
                img: 'components/index/images/1.jpg',
                header: 'Найдите своё лучшее жильё',
                text: 'Наша цель - помочь вам сэкономить деньги при поиске жилья.',
                isActive: 'active'
            },
            {
                img: 'components/index/images/2.jpg',
                header: 'Только проверенные объявления',
                text: 'Мы делаем акцент на том, чтобы защитить наших пользователей.',
                isActive: ''
            },
            {
                img: 'components/index/images/3.jpg',
                header: 'Мощная система модерации объявлений',
                text: 'Наши модераторы тщательно проверяют каждое добавленное объявление.',
                isActive: ''
            }
        ];

        $(function () {
            navigationService.setHome();

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

    function service($http, $log) {
        return {
            login: function () {
                $http({method: 'POST', url: 'login'}).
                    success(function (data, status, headers, config) {
                        // this callback will be called asynchronously
                        // when the response is available
                        $log.debug('Successful sending ajax request');
                    }).
                    error(function (data, status, headers, config) {
                        // called asynchronously if an error occurs
                        // or server returns response with an error status.
                        $log.debug('Error sending ajax request. Status: ' + status);
                        //$scope.greeting = "raz dva";
                    });
            }
        };
    }

    return {
        init: init,
        ctl: controller,
        srv: service
    };
})();

indexModule.init();
module.exports = indexModule;