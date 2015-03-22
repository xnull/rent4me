<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="page-header">
    <h1>Cities</h1>
</div>

<p>
    <a class="btn btn-primary" href="<c:url value="/secure/cities/new"/>">Add city</a>
</p>

<table class="table table-striped">
    <thead>
    <tr>
        <th>#</th>
        <th>Name</th>
        <th>Area</th>
        <th>Country</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="city" items="${cities}">
        <tr>
            <td>
                    ${city.id}
            </td>
            <td>
                    ${city.name}
            </td>
            <td>
                    ${city.area}
            </td>
            <td>
                    ${city.country.name}
            </td>
            <td>
                <a href="<c:url value="/secure/cities/edit/${city.id}"/>">Edit</a> |
                <a href="<c:url value="/secure/cities/delete/${city.id}"/>">Delete</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<%--<p class="lead">Pin a fixed-height footer to the bottom of the viewport in desktop browsers with this custom HTML and CSS. A fixed navbar has been added within <code>#wrap</code> with <code>padding-top: 60px;</code> on the <code>.container</code>.</p>--%>
<%--<p>Back to <a href="../sticky-footer">the default sticky footer</a> minus the navbar.</p>--%>