<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="page-header">
    <h1>FB Pages</h1>
</div>

<jsp:include page="../../_pagination.jsp"/>

<table class="table table-striped">
    <thead>
    <tr>
        <th>Message</th>
        <th>Link</th>
        <th>Created</th>
        <th>Updated</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="post" items="${posts}">
        <tr>
            <td>
                <c:forEach items="${post.imageUrls}" var="img">
                    <img src="${img}">
                </c:forEach><br/>
                    ${post.message}
            </td>
            <td>
                <a href="${post.link}" target="_blank">${post.link}</a>
            </td>
            <td>
                    ${post.created}
            </td>
            <td>
                    ${post.updated}
            </td>
            <td>
                    <%--<a href="<c:url value="/secure/socialnet/fb/${page.id}/edit"/>">Edit</a> |--%>
                    <%--<a href="<c:url value="/secure/socialnet/fb/${page.id}/delete" />"--%>
                    <%--onclick="return confirm('Are you sure?')">Delete</a>--%>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<jsp:include page="../../_pagination.jsp"/>

<%--<p class="lead">Pin a fixed-height footer to the bottom of the viewport in desktop browsers with this custom HTML and CSS. A fixed navbar has been added within <code>#wrap</code> with <code>padding-top: 60px;</code> on the <code>.container</code>.</p>--%>
<%--<p>Back to <a href="../sticky-footer">the default sticky footer</a> minus the navbar.</p>--%>