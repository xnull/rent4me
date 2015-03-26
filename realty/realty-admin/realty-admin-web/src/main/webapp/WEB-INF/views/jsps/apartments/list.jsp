<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="page-header">
    <h1>Apartments</h1>
</div>

<p>
    <a class="btn btn-primary" href="<c:url value="/secure/apartments/list" />?page=${page - 1}">Предыдущая</a>
    <a class="btn btn-primary" href="javascript:void(0)">Текущая страница: ${page}</a>
    <a class="btn btn-primary" href="<c:url value="/secure/apartments/list" />?page=${page + 1}">Следующая</a>
</p>

<table class="table table-striped">
    <thead>
    <tr>
        <th>#</th>
        <th>Data Source</th>
        <th>Will be shown for</th>
        <th>Address</th>
        <th>Metros</th>
        <th>Rental fee</th>
        <th>Rooms</th>
        <th>Floor Number</th>
        <th>Floors Total</th>
        <th>Area</th>
        <th>Published</th>
        <th>Text</th>
        <th>Created</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="apartment" items="${apartments}">
        <tr>
            <td>
                    ${apartment.id}
            </td>
            <td>
                    ${apartment.dataSource}
            </td>
            <td>
                    ${apartment.target}
            </td>
            <td>
                    ${apartment.address.formattedAddress}
            </td>
            <td>
                    <ul>
                        <c:forEach items="${apartment.metros}" var="metro">
                            <li>
                                ${metro.stationName}
                            </li>
                        </c:forEach>
                    </ul>
            </td>
            <td>
                    ${apartment.rentalFee}/${apartment.feePeriod}
            </td>
            <td>
                    ${apartment.roomCount}
            </td>
            <td>
                    ${apartment.floorNumber}
            </td>
            <td>
                    ${apartment.floorsTotal}
            </td>
            <td>
                    ${apartment.area} м<sup>2</sup>
            </td>
            <td>
                    ${apartment.published}
            </td>
            <td>
                    ${apartment.description}
            </td>
            <td>
                    ${apartment.created}
            </td>
            <td>
                <a class="btn btn-danger" href="<c:url value="/secure/apartments/hide/${apartment.id}"/>" onclick="return confirm('Are you sure you want to hide it from search?');">Hide in search</a>
                <a class="btn btn-success" href="<c:url value="/secure/apartments/show/${apartment.id}"/>" onclick="return confirm('Are you sure you want to show it in search?');">Show in search</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<%--<p class="lead">Pin a fixed-height footer to the bottom of the viewport in desktop browsers with this custom HTML and CSS. A fixed navbar has been added within <code>#wrap</code> with <code>padding-top: 60px;</code> on the <code>.container</code>.</p>--%>
<%--<p>Back to <a href="../sticky-footer">the default sticky footer</a> minus the navbar.</p>--%>