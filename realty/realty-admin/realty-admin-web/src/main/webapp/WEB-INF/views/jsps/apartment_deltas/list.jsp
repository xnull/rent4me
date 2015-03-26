<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="at" uri="/WEB-INF/admintaglib.tld" %>

<div class="page-header">
    <h1>Deltas</h1>
</div>

<p>
    <a class="btn btn-primary" href="<c:url value="/secure/cities/new"/>">Add city</a>
</p>

<table class="table table-striped">
    <thead>
    <tr>
        <th>#</th>
        <th>Delta</th>
        <th>Distance between addresses</th>
        <th>Applied?</th>
        <th>Rejected?</th>
        <th>Action</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="delta" items="${deltas}">
        <tr>
            <td>
                    ${delta.id}
            </td>
            <td>
                    Address: ${delta.apartment.address.formattedAddress} -> ${delta.addressComponents.formattedAddress}<br/>
                    Area: ${delta.apartment.area} -> ${delta.area} м<sup>2</sup><br/>
                    Rooms: ${delta.apartment.roomCount} -> ${delta.roomCount} м<sup>2</sup><br/>
                    Floors: ${delta.apartment.floorNumber}/${delta.apartment.floorsTotal} -> ${delta.floorNumber}/${delta.floorsTotal}<br/>
                    <br/>
            </td>
            <td>
                    ${at:distanceHumanReadable(delta.apartment.location, delta.location)}
            </td>
            <td>
                    ${delta.applied}
            </td>
            <td>
                    ${delta.rejected}
            </td>
            <td>
                <a href="<c:url value="/secure/apartment_deltas/review_delta/${delta.id}"/>">Review</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<%--<p class="lead">Pin a fixed-height footer to the bottom of the viewport in desktop browsers with this custom HTML and CSS. A fixed navbar has been added within <code>#wrap</code> with <code>padding-top: 60px;</code> on the <code>.container</code>.</p>--%>
<%--<p>Back to <a href="../sticky-footer">the default sticky footer</a> minus the navbar.</p>--%>