<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="people" type="java.util.List<name.dargiri.data.model.Person>"--%>
<%--@elvariable id="person" type="name.dargiri.data.model.Person"--%>
<div class="page-header">
    <h1>FB Pages</h1>
</div>
<div>
    <a href="http://wallflux.com/facebook_id/" target="_blank">http://wallflux.com/facebook_id/</a> - Find out FB id.
    <br/>
    <a href="<c:url value="/secure/socialnet/fb/new"/>" class="btn btn-primary">Add Page</a>
</div>
<table class="table table-striped">
    <thead>
    <tr>
        <th>#</th>
        <th>Ext id</th>
        <th>link</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="page" items="${pages}">
        <tr>
            <td>
                    ${page.id}
            </td>
            <td>
                    ${page.externalId}
            </td>
            <td>
                <a href="${page.link}" target="_blank">${page.link}</a>
            </td>
            <td>
                <a href="<c:url value="/secure/socialnet/fb/${page.id}/edit"/>">Edit</a> |
                <a href="<c:url value="/secure/socialnet/fb/${page.id}/delete" />"
                   onclick="return confirm('Are you sure?')">Delete</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<%--<p class="lead">Pin a fixed-height footer to the bottom of the viewport in desktop browsers with this custom HTML and CSS. A fixed navbar has been added within <code>#wrap</code> with <code>padding-top: 60px;</code> on the <code>.container</code>.</p>--%>
<%--<p>Back to <a href="../sticky-footer">the default sticky footer</a> minus the navbar.</p>--%>