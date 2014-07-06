/**
 * dynamic pages https://egghead.io/lessons/angularjs-using-resource-for-data-models
 * @param $log
 * @param $scope
 * @param rentService
 * @param navigationService
 */
var rentController = function ($log, $scope, rentService, navigationService) {
    "use strict";
    $log.debug('Rent controller: execution');

    $scope.putForRent = function () {
        $log.debug(angular.toJson($scope.rentData));
        rentService.putForRent($scope);
        //$scope.$watch('rentData', rentService.putForRent);
    };

    $(function () {
        navigationService.setRent();
    });
};

var rentService = function ($resource, $log) {
    "use strict";

    return {
        hello: function () {
            return 'hey hello';
        },

        putForRent: function ($scope) {
            $log.debug('Rent service: put for rent');

            var putResource = $resource('putForRent');
            putResource.save(angular.toJson($scope.rentData));

            putResource.query(function (response) {
                $scope.rentData = response;
            });
        }
    };
};
