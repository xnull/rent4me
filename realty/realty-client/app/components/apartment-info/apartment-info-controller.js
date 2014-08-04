/**
 * Created by null on 03.08.14.
 */
var ApartmentInfoModule = (function () {
    'use strict';

    var cfg = {
        moduleName: 'rentApp.apartmentInfo',
        moduleDependencies: ['ui.router'],

        ctlName: 'ApartmentInfoController',
        serviceName: 'ApartmentInfoService',
        stateName: 'apartmentInfoState',
        stateConfig: {
            url: '/apartment-info',
            templateUrl: 'components/apartment-info/apartment-info-view.html',
            controller: 'ApartmentInfoController' //cfg.ctlName
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
        angularModule.factory(cfg.serviceName, ApartmentInfoService);
    }

    /**
     * dynamic pages https://egghead.io/lessons/angularjs-using-resource-for-data-models
     * @param $log
     * @param $scope
     * @param rentService
     * @param navigationService
     */
    function controller($log, $scope, navigationService, ApartmentInfoService) {
        $log.debug('Apartment info controller: execution');

        $scope.apartmentInfo = ApartmentInfoService.getApartmentById(123);

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

    function ApartmentInfoService($resource, $log) {

        /**
         * @type {Object}
         */
        var apartmentInfoDto = {
            id: 1,
            ownerId: 123,
            floor: 7,
            rooms: 1,
            square: 52,
            price: 50000,
            address: 'Москва, улица 800-летия Москвы, 4к2',
            features: {
                balcony: true,
                furniture: true,
                refrigerator: true,
                tv: true,
                conditioner: true //etc
            },
            images: [
                {
                    img: 'components/apartment-info/images/photo1.jpg',
                    isActive: 'active'
                },
                {
                    img: 'components/apartment-info/images/photo2.jpg',
                    isActive: ''
                }
            ],
            description: 'Комната в центре столицы, с хорошими соседями, в историческом доме - крепче современных.' +
                'До Чистых Прудов 5 минут пешком.' +
                'Все удовольствия культурной жизни в шаговой доступности, театры, кафе, антикафе, кинотеатры - ' +
                'мест, где можно общаться множество. В квартире есть WiFI.' +
                'В качестве жильца хочется видеть одного, адекватного человека. Не домоседа), ' +
                'без вредных привычек, аккуратного. Въехать можно уже 4-5 августа.'

        };

        /**
         * Get info about a apartment
         * @param apartmentId
         * @returns {Object} apartmentInfoDto
         * @param $resource
         */
        function getApartmentById(apartmentId) {
            $log.debug('Get apartment info by id: ' + apartmentId);
            //$log.debug($resource);
            return apartmentInfoDto;
        }

        /**
         *
         * @param address
         * @returns apartmentInfoDto
         */
        function getApartmentByAddress(address) {

        }

        return {
            getApartmentById: getApartmentById
        };
    }

    return {
        init: init,
        ctl: controller,
        srv: ApartmentInfoService
    };
})();

ApartmentInfoModule.init();
