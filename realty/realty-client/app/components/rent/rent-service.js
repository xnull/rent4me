/**
 * Created by null on 9/24/14.
 */
module.exports = function RentService($resource, $log, $scope) {
    function hello () {
        return 'hey hello';
    }

    function putForRent() {
        $log.debug('Rent service: put for rent');

        var putResource = $resource('putForRent');
        putResource.save(angular.toJson($scope.rentData));

        putResource.query(function (response) {
            $scope.rentData = response;
        });
    }

    return {
        hello: hello,
        putForRent: putForRent
    };
};