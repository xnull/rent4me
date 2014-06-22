/**
 * angular module initialization and route setup
 */

var map;
var markers = [];

function initialize() {
    var myLatlng = new google.maps.LatLng(55.882109, 37.556684)
    var mapOptions = {
        center: myLatlng,
        zoom: 18
    };
    map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);

    var marker = new google.maps.Marker({
        position: myLatlng,
        map: map,
        title: 'ДУБНИНСКАЯ МАТЬ ЕЁ!'
    });

    markers.push(marker);

    var contentString = '<div id="content">' +
        '<div id="siteNotice">' +
        '</div>' +
        '<h1 id="firstHeading" class="firstHeading">Uluru</h1>' +
        '<div id="bodyContent">' +
        '<p><b>Uluru</b>, also referred to as <b>Ayers Rock</b>, is a large ' +
        'sandstone rock formation in the southern part of the ' +
        'Northern Territory, central Australia. It lies 335&#160;km (208&#160;mi) ' +
        'south west of the nearest large town, Alice Springs; 450&#160;km ' +
        '(280&#160;mi) by road. Kata Tjuta and Uluru are the two major ' +
        'features of the Uluru - Kata Tjuta National Park. Uluru is ' +
        'sacred to the Pitjantjatjara and Yankunytjatjara, the ' +
        'Aboriginal people of the area. It has many springs, waterholes, ' +
        'rock caves and ancient paintings. Uluru is listed as a World ' +
        'Heritage Site.</p>' +
        '<p>Attribution: Uluru, <a href="http://en.wikipedia.org/w/index.php?title=Uluru&oldid=297882194">' +
        'http://en.wikipedia.org/w/index.php?title=Uluru</a> ' +
        '(last visited June 22, 2009).</p>' +
        '</div>' +
        '</div>';

    var infowindow = new google.maps.InfoWindow({
        content: contentString
    });

    google.maps.event.addListener(marker, 'click', function () {
        infowindow.open(map, marker);
    });
}

google.maps.event.addDomListener(window, 'load', initialize);

function setAllMap(map) {
    for (var i = 0; i < markers.length; i++) {
        markers[i].setMap(map);
    }
}

// Removes the markers from the map, but keeps them in the array.
function clearMarkers() {
    setAllMap(null);
}

// Shows any markers currently in the array.
function showMarkers() {
    setAllMap(map);
}


/**
 * ajax request
 */
angular.module("realtyapp", []).controller("RealtyController", function ($scope, $http) {
    $scope.myData = {};
    var ajaxRequest = function (item, event) {

        /*var responsePromise = $http.get("test-data.json");

         responsePromise.success(function (data, status, headers, config) {
         $scope.myData.fromServer = data.title;
         });
         responsePromise.error(function (data, status, headers, config) {
         alert("AJAX failed!");
         });*/
        $scope.myData.fromServer = {"test": "ok"}.test
    }

    $scope.myData.doClick = ajaxRequest;
});