var configurator = require('../core/configurator.js');
var validator = require('../core/validator.js');

function IndexCtl($scope, indexService, navigationService, $log) {
    $log.debug('Index controller execution');

    validator().checkUndefinedBatch([$scope, indexService, navigationService, $log]);

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

function IndexService($http, $log) {
    validator().checkUndefinedBatch($http, $log);

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

var cfg = {
    moduleName: 'rentApp.index',
    moduleDependencies: ['ui.router', 'index/index-view.html'],

    ctlName: 'IndexController',
    serviceName: 'indexService',

    stateName: 'indexState',
    stateConfig: {
        url: '/',
        templateUrl: 'index/index-view.html',
        controller: 'IndexController'
    },

    service: IndexService,
    controller: IndexCtl
};

configurator().configure(cfg);