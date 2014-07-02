var indexController = function ($scope, indexService, navigationService) {
    console.log('Index controller execution');

    $(function () {
        navigationService.setHome();

        var $carousel = $('.carousel');
        $carousel.carousel({
            interval: 5000,
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
};

var indexService = function ($http) {
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

