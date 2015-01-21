<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="people" type="java.util.List<name.dargiri.data.model.Person>"--%>
<%--@elvariable id="person" type="name.dargiri.data.model.Person"--%>
<div class="page-header">
    <h1>FB Pages</h1>
</div>
<div>
    <p>
        <a href="http://lookup-id.com/" target="_blank">http://lookup-id.com/</a> - Find out FB
        id (Best choice).
        <a href="http://wallflux.com/facebook_id/" target="_blank">http://wallflux.com/facebook_id/</a> - Find out FB
        id(with captcha).
    </p>

    <p>
        <a href="<c:url value="/secure/socialnet/fb/new"/>" class="btn btn-primary"><i
                class="glyphicon glyphicon-plus"></i> Add Page</a><br/>
    </p>

    <p>
        <a href="<c:url value="/secure/socialnet/fb/posts"/>" class="btn btn-default"><i
                class="glyphicon glyphicon-th-list"></i> FB Posts</a><br/>
    </p>
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
                <a class="btn btn-default" href="<c:url value="/secure/socialnet/fb/${page.id}/edit"/>"><i
                        class="glyphicon glyphicon-edit"></i> Edit</a>&nbsp;
                <a class="btn btn-danger" href="<c:url value="/secure/socialnet/fb/${page.id}/delete" />"
                   onclick="return confirm('Are you sure?')"><i class="glyphicon glyphicon-trash"></i> Delete</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<%--<p class="lead">Pin a fixed-height footer to the bottom of the viewport in desktop browsers with this custom HTML and CSS. A fixed navbar has been added within <code>#wrap</code> with <code>padding-top: 60px;</code> on the <code>.container</code>.</p>--%>
<%--<p>Back to <a href="../sticky-footer">the default sticky footer</a> minus the navbar.</p>--%>