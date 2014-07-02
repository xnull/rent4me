var registerController = function ($scope, registerService, navigationService) {
    //console.log('Register controller execution');
    $scope.register = registerService.register;

    $(function () {
        navigationService.setRegister();
    });
//    $scope.greeting = registerService.hello();
};

var registerService = function ($http) {
    "use strict";

    return {
        register: function () {
            $http({method: 'POST', url: 'register'}).
                success(function (data, status, headers, config) {
                    // this callback will be called asynchronously
                    // when the response is available
                    //console.log('Successful sending ajax request');
                }).
                error(function (data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    //console.log('Error sending ajax request. Status: ' + status);
                    //$scope.greeting = "raz dva";
                });
        }
    };
};

