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
        height: 250px;
        width: 250px;
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
                        <div id="map-canvas1" class="map"></div>
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
                        <div id="map-canvas2" class="map"></div>
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
    <tr>
        <td>
            Distance between addresses
        </td>
        <td>
            ${at:distanceHumanReadable(form.apartment.location, form.location)}
        </td>
    </tr>
</table>


<script type="text/javascript">



    function initialize() {

        var mapOptions1 = {
            zoom: 12,
            center: new google.maps.LatLng(${form.apartment.location.latitude}, ${form.apartment.location.longitude})
        };
        var map1 = new google.maps.Map(document.getElementById('map-canvas1'),
                mapOptions1);


        var marker1 = new google.maps.Marker({
            position: new google.maps.LatLng(${form.apartment.location.latitude}, ${form.apartment.location.longitude}),
            map: map1
        });

        var mapOptions2 = {
            zoom: 12,
            center: new google.maps.LatLng(${form.location.latitude}, ${form.location.longitude})
        };
        var map2 = new google.maps.Map(document.getElementById('map-canvas2'),
                mapOptions2);


        var marker2 = new google.maps.Marker({
            position: new google.maps.LatLng(${form.location.latitude}, ${form.location.longitude}),
            map: map2
        });

    }

    google.maps.event.addDomListener(window, 'load', initialize)

</script>
<%--<p class="lead">Pin a fixed-height footer to the bottom of the viewport in desktop browsers with this custom HTML and CSS. A fixed navbar has been added within <code>#wrap</code> with <code>padding-top: 60px;</code> on the <code>.container</code>.</p>--%>
<%--<p>Back to <a href="../sticky-footer">the default sticky footer</a> minus the navbar.</p>--%>