var rentController = function ($scope, rentService, navigationService) {
    //console.log('Rent controller execution');
    $scope.putForRent = rentService.putForRent;
    $scope.greeting = rentService.hello();

    $(function () {
        navigationService.setRent();
    });
};

var rentService = function ($http) {
    "use strict";

    return {
        hello: function () {
            return 'hey hello';
        },

        putForRent: function () {
            $http({method: 'GET', url: 'putForRent'}).
                success(function (data, status, headers, config) {
                    // this callback will be called asynchronously
                    // when the response is available
                    console.log('Successful sending ajax request');
                }).
                error(function (data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    console.log('Error sending ajax request. Status: ' + status);
                    //$scope.greeting = "raz dva";
                });
        }
    };
};

