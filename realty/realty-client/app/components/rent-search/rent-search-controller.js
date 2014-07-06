/**
 * Created by null on 06.07.14.
 */

/**
 * Google maps and angularjs http://www.simplecoding.org/google-maps-angularjs-pozicionirovanie-karty.html
 * @param $log
 * @param $scope
 * @param rentSearchService
 * @param navigationService
 */
var rentSearchController = function ($log, $scope, rentSearchService, navigationService) {
    "use strict";

    $log.debug('Rent search controller initialization');

    $scope.initializeGoogleMaps = function () {
        $log.debug('Initialize google maps');
        var map;
        var myLatlng = new google.maps.LatLng(55.752020, 37.617526);
        var mapOptions = {
            center: myLatlng,
            zoom: 10
        };

        var mapContainer = document.getElementById('map-canvas');

        map = new google.maps.Map(mapContainer, mapOptions);

        /*$log.debug('infobox');
         var infoBox = new google.maps.InfoWindow();
         infoBox.setPosition(myLatlng);
         infoBox.open(map);
         map.setCenter(myLatlng);*/
    };

    //google.maps.event.addDomListener(window, 'load', initializeGoogleMaps);

    $(function () {
        navigationService.setRentSearch();
    });
};

var rentSearchService = function () {
    "use strict";
};