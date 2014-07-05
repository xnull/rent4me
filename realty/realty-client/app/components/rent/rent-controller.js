var rentController = function ($scope, rentService, navigationService) {
    //console.log('Rent controller execution');
    $scope.putForRent = rentService.putForRent;
    $scope.greeting = rentService.hello();

    $(function () {
        navigationService.setRent();
    });
};

var rentService = function ($http, $resource) {
    "use strict";

    return {
        hello: function () {
            return 'hey hello';
        },

        putForRent: function () {
            $resource('putForRent', {}, {
                query: {method: 'GET', params: {}, isArray: false}
            });
        }
    }
};


