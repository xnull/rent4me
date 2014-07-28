/**
 * Created by null on 06.07.14.
 */
var rentSearchModuleCfg = {
    ctlName: 'RentSearchController',
    serviceName: 'rentSearchService',
    stateName: 'rentSearchState',
    stateConfig: {
        url: '/rent-search',
        templateUrl: 'components/rent-search/rent-search-view.html'
        //controller: 'RentSearchController'
    }
};

var rentSearchModuleSubview = {
    stateName: 'rentSearchState.searchForm',
    //url: '/rent-search/search',
    stateConfig: {
        views: {
            "rentSearchForm": {
                templateUrl: 'rent-search-form.html'
            }
        }
    }
};

var rentSearchModule = angular.module('rentApp.rentSearch', ['ui.router']);

rentSearchModule.config(function ($stateProvider) {
    'use strict';
    $stateProvider.state(rentSearchModuleCfg.stateName, rentSearchModuleCfg.stateConfig);
});

/**
 * Google maps and angularjs http://www.simplecoding.org/google-maps-angularjs-pozicionirovanie-karty.html
 * @param $log
 * @param $scope
 * @param rentSearchService
 * @param navigationService
 */
rentSearchModule.controller(rentSearchModuleCfg.ctlName, function ($log, $scope, rentSearchService, navigationService) {
    "use strict";
    $log.debug('Rent search controller initialization');

    $scope.initializeGoogleMaps = rentSearchService.googleMapInitialization;

    $scope.apartment = {};
    $scope.search = function () {
        $log.debug($scope.apartment);
    };

    /* Dropdown menu see commented component on view
     $(function() {
     // Setup drop down menu
     $('.dropdown-toggle').dropdown();

     // Fix input element click problem
     $('.dropdown input, .dropdown label').click(function(e) {
     e.stopPropagation();
     });
     });*/

    $(function () {
        navigationService.setRentSearch();
    });
});

rentSearchModule.factory(rentSearchModuleCfg.serviceName, function ($log) {
    "use strict";

    var map;

    var testMapMarkers = {

        generateTestData: function () {
            var points = [
                {lat: 55.752020, lng: 37.617526}, //The Moscow cremlin
                {lat: 55.728253, lng: 37.601028}, //Park Gorkogo
                {lat: 55.703043, lng: 37.530714}, //MGU
                {lat: 55.708562, lng: 37.521573}, //Science park MGU
                {lat: 55.794082, lng: 37.588483}, //MEtro savolovskaya
                {lat: 55.807759, lng: 37.581531}, //Metro Dmitrovskaya
                {lat: 55.818586, lng: 37.578956}, //Metro Timiryazevskaya
                {lat: 55.836713, lng: 37.570073}, //Metro Petro-razumovskaya
                {lat: 55.831674, lng: 37.569448}, //Dmitrovskoe hosse 39
                {lat: 55.862149, lng: 37.604533}  //Otradnoe
            ];

            var clusterMarkers = [];
            for (var i = 0; i < points.length; i++) {
                clusterMarkers.push(new google.maps.Marker({
                    position: new google.maps.LatLng(points[i].lat, points[i].lng)
                }));
            }

            return clusterMarkers;
        }
    };

    /**
     * http://habrahabr.ru/post/28621/ clustering markers
     *
     * https://developers.google.com/maps/documentation/javascript/places?hl=ru
     *
     * https://developers.google.com/maps/documentation/javascript/examples/places-autocomplete-addressform
     */
    return {
        googleMapInitialization: function () {
            $log.debug('Initialize google maps');

            var markers = testMapMarkers.generateTestData();

            var myLatlng = new google.maps.LatLng(55.752020, 37.617526);
            var mapOptions = {
                center: myLatlng,
                zoom: 10
            };

            var mapContainer = document.getElementById('map-canvas');

            map = new google.maps.Map(mapContainer, mapOptions);

            var markerCluster = new MarkerClusterer(map, markers);

            //var inputElement = angular.element('addressInput');
            var inputElement = document.getElementById('addressInput');

            var autocomplete = new google.maps.places.Autocomplete(inputElement);
            autocomplete.bindTo('bounds', map);
        }
    };
});

