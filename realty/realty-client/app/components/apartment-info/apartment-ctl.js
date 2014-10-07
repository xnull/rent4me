/**
 * Created by null on 03.08.14.
 */

/**
 * dynamic pages https://egghead.io/lessons/angularjs-using-resource-for-data-models
 * @param $log
 * @param $scope
 * @param rentService
 * @param navigationService
 */
var validator = require('../core/validator.js');

function controller($log, $scope, $stateParams, navigationService, ApartmentInfoService) {
    $log.debug('Apartment info controller: execution');

    //validator().checkUndefinedBatch([$log, $scope, $stateParams, navigationService, ApartmentInfoService]);

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

module.exports = function () {
    return controller;
};