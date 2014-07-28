var indexModuleCfg = {
    ctlName: 'IndexController',
    serviceName: 'indexService',
    stateName: 'indexState',
    stateConfig: {
        url: '/',
        templateUrl: 'components/index/index-view.html',
        controller: 'IndexController'
    }
};

var indexModule = angular.module('rentApp.index', ['ui.router']);

indexModule.config(function ($stateProvider) {
    'use strict';
    $stateProvider.state(indexModuleCfg.stateName, indexModuleCfg.stateConfig);
});

indexModule.controller(indexModuleCfg.ctlName, function ($scope, indexService, navigationService, $log) {
    'use strict';

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
            text: 'Мы делаем акцент на том, что бы защитить наших пользователей.',
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
});

indexModule.factory(indexModuleCfg.serviceName, function ($http, $log) {
    "use strict";

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
});

