<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="at" uri="/WEB-INF/admintaglib.tld" %>
<div class="page-header">
    <h1>City</h1>
</div>

<!-- google section -->
<script src="//maps.googleapis.com/maps/api/js?language=ru&v=3.exp&libraries=places"></script>

<style type="text/css">
    .map {
        height: 150px;
        width: 150px;
    }
</style>

<p>
    <a href="<c:url value="/secure/apartment_deltas/approve_delta/${form.id}"/>" class="btn btn-primary">Approve</a>
    <a href="<c:url value="/secure/apartment_deltas/reject_delta/${form.id}"/>" class="btn btn-danger">Reject</a>
</p>


<table class="table">
    <tr>
        <th>Currently</th>
        <th>After</th>
    </tr>
    <tr>
        <td>
            <table class="table">
                <tr>
                    <th>Key</th>
                    <th>Value</th>
                </tr>
                <tr>
                    <td>Address</td>
                    <td>
                        Formatted address: ${form.apartment.address.formattedAddress}<br/>
                        Street: ${form.apartment.address.streetAddress}<br/>
                        Zip: ${form.apartment.address.zipCode}<br/>
                        District: ${form.apartment.address.district}<br/>
                        City: ${form.apartment.address.city}<br/>
                        County: ${form.apartment.address.county}<br/>
                        Country: ${form.apartment.address.country}<br/>
                        Country code: ${form.apartment.address.countryCode}<br/>
                        <div id="map-canvas1"></div>
                    </td>
                </tr>
                <tr>
                    <td>
                        Distance between addresses
                    </td>
                    <td>
                        ${at:distanceHumanReadable(form.apartment.location, form.location)}
                    </td>
                </tr>
                <tr>
                    <td>Room count</td>
                    <td>
                        ${form.apartment.roomCount}
                    </td>
                </tr>
                <tr>
                    <td>Floor number</td>
                    <td>
                        ${form.apartment.floorNumber}
                    </td>
                </tr>
                <tr>
                    <td>Floors total</td>
                    <td>
                        ${form.apartment.floorsTotal}
                    </td>
                </tr>
            </table>
        </td>
        <td>
            <table class="table">
                <tr>
                    <th>Key</th>
                    <th>Value</th>
                </tr>
                <tr>
                    <td>Address</td>
                    <td>
                        Formatted address: ${form.addressComponents.formattedAddress}<br/>
                        Street: ${form.addressComponents.streetAddress}<br/>
                        Zip: ${form.addressComponents.zipCode}<br/>
                        District: ${form.addressComponents.district}<br/>
                        City: ${form.addressComponents.city}<br/>
                        County: ${form.addressComponents.county}<br/>
                        Country: ${form.addressComponents.country}<br/>
                        Country code: ${form.addressComponents.countryCode}<br/>
                        <div id="map-canvas1"></div>
                    </td>
                </tr>
                <tr>
                    <td>
                        Distance between addresses
                    </td>
                    <td>
                        ${at:distanceHumanReadable(form.apartment.location, form.location)}
                    </td>
                </tr>
                <tr>
                    <td>Room count</td>
                    <td>
                        ${form.roomCount}
                    </td>
                </tr>
                <tr>
                    <td>Floor number</td>
                    <td>
                        ${form.floorNumber}
                    </td>
                </tr>
                <tr>
                    <td>Floors total</td>
                    <td>
                        ${form.floorsTotal}
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>


<script type="text/javascript">
    var domElement = document.getElementById('name');
    var autocomplete = new google.maps.places.Autocomplete(domElement);
    var map;
    var area = null;
    var marker = null;
/*
    google.maps.event.addListener(autocomplete, 'place_changed', function () {
        var place = autocomplete.getPlace();
        if (!place) return;

//        console.log('dump:');
//        console.log(dump);
        var addressComponents = place['address_components'];

        var viewPort = place.geometry.viewport;

        var bounds = viewPort ? {
            northEast: {
                lat: viewPort.getNorthEast().lat(),
                lng: viewPort.getNorthEast().lng()
            },

            southWest: {
                lat: viewPort.getSouthWest().lat(),
                lng: viewPort.getSouthWest().lng()
            }
        } : null;

        var location = {
            latitude: place['geometry']['location'].lat(),
            longitude: place['geometry']['location'].lng()
        };

        var formatted_address = place['formatted_address'];
        var name = place['name'];

//        console.log('new location:');
//        console.log(location);

        document.getElementById('area.high.latitude').value = bounds ? bounds.northEast.lat : location.latitude;
        document.getElementById('area.high.longitude').value = bounds ? bounds.northEast.lng : location.longitude;

        document.getElementById('area.low.latitude').value = bounds ? bounds.southWest.lat : location.latitude;
        document.getElementById('area.low.longitude').value = bounds ? bounds.southWest.lng : location.longitude;
//        document.getElementById('name').value = name;

        map.setCenter(new google.maps.LatLng(location.latitude, location.longitude));

        if(area) {
            area.setMap(null);
        }

        if(marker) {
            marker.setMap(null);
            marker = null;
        }

        if(!bounds) {
            marker = new google.maps.Marker({
                position: new google.maps.LatLng(location.latitude, location.longitude),
                map: map
            });
        }

        var coords = bounds ? [
            new google.maps.LatLng(bounds.northEast.lat, bounds.northEast.lng),
            new google.maps.LatLng(bounds.southWest.lat, bounds.northEast.lng),
            new google.maps.LatLng(bounds.southWest.lat, bounds.southWest.lng),
            new google.maps.LatLng(bounds.northEast.lat, bounds.southWest.lng),
            new google.maps.LatLng(bounds.northEast.lat, bounds.northEast.lng)
        ] : [
        ];
        console.log(coords);

        area = new google.maps.Polygon({
            paths: coords,
            strokeColor: '#FF0000',
            strokeOpacity: 0.8,
            strokeWeight: 2,
            fillColor: '#FF0000',
            fillOpacity: 0.35
        });
        area.setMap(map);

    });


    function initialize() {
        var centerLat = (${city.area.high.latitude} + ${city.area.low.latitude})/2.0;
        var centerLng = (${city.area.high.longitude} + ${city.area.low.longitude})/2.0;

        var mapOptions = {
            zoom: 8,
            center: new google.maps.LatLng(centerLat, centerLng)
        };
        map = new google.maps.Map(document.getElementById('map-canvas'),
                mapOptions);


        var coords = [
            new google.maps.LatLng(${city.area.high.latitude}, ${city.area.high.longitude}),
            new google.maps.LatLng(${city.area.low.latitude}, ${city.area.high.longitude}),
            new google.maps.LatLng(${city.area.low.latitude}, ${city.area.low.longitude}),
            new google.maps.LatLng(${city.area.high.latitude}, ${city.area.low.longitude}),
            new google.maps.LatLng(${city.area.high.latitude}, ${city.area.high.longitude})
        ];
        console.log(coords);

        area = new google.maps.Polygon({
            paths: coords,
            strokeColor: '#FF0000',
            strokeOpacity: 0.8,
            strokeWeight: 2,
            fillColor: '#FF0000',
            fillOpacity: 0.35
        });
        area.setMap(map);
    }

    google.maps.event.addDomListener(window, 'load', initialize);
*/


</script>
<%--<p class="lead">Pin a fixed-height footer to the bottom of the viewport in desktop browsers with this custom HTML and CSS. A fixed navbar has been added within <code>#wrap</code> with <code>padding-top: 60px;</code> on the <code>.container</code>.</p>--%>
<%--<p>Back to <a href="../sticky-footer">the default sticky footer</a> minus the navbar.</p>--%>