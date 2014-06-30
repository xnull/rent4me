var fbLoginController = function ($scope, fbLoginService) {
    console.log('Rent controller execution');
    $scope.fbAsyncInit = fbLoginService.fbAsyncInit;
//    $scope.greeting = fbLoginService.hello();
};

var fbLoginService = function ($http) {
    "use strict";

    return {
        fbAsyncInit: function() {
            FB.init({
                appId      : '270007246518198',
                xfbml      : true,
                version    : 'v2.0'
            });
        },

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

