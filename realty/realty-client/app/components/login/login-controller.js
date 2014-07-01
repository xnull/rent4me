var loginController = function ($scope, loginService, navigationService) {
    console.log('Login controller execution');
//    $scope.NavigationController = NavigationController;
    $scope.login = loginService.login;
    console.log("Nav service state:"+navigationService.getTab());
    $scope.navService = navigationService;
//    $scope.greeting = loginService.hello();
};

var loginService = function ($http) {
    "use strict";

    return {
        login: function () {
            $http({method: 'POST', url: 'login'}).
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

