/**
 * Created by null on 9/24/14.
 */
function RentService($resource, $log, $scope) {
    $log.debug('Rent service initialization');

    function putForRent() {
        $log.debug('Rent service: put for rent');

        var putResource = $resource('putForRent');
        putResource.save(angular.toJson($scope.rentData));

        putResource.query(function (response) {
            $scope.rentData = response;
        });
    }

    return {
        putForRent: putForRent
    };
}

module.exports = function () {
    return RentService;
};