<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        <th>Address</th>
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
                    ${delta.apartment.address.formattedAddress}
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