<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="person" type="name.dargiri.data.dto.PersonDTO"--%>
<div class="page-header">
    <h1>FB Page</h1>

    <jsp:include page="./_info.jsp"/>
</div>
<form role="form" method="post" id="userEditForm">
    <div class="form-group">
        <label for="id">#</label>
        <input type="hidden" class="form-control" name="id" placeholder="#" value="${page.id}" readonly>
        <input type="text" class="form-control" id="id" placeholder="#" value="${page.id}" readonly>
    </div>
    <div class="form-group">
        <label for="externalId">External id</label>
        <input type="text" class="form-control" name="externalId" id="externalId" placeholder="External Id"
               value="${page.externalId}">
    </div>
    <div class="form-group">
        <label for="externalId">Link to page</label>
        <input type="text" class="form-control" name="link" id="link" placeholder="Link to fb page"
               value="${page.link}">
    </div>
    <div class="form-group checkbox">
        <label for="enabled">Enabled</label>
        <input type="checkbox" class="form-control" name="enabled" id="enabled" placeholder="Link to fb page"
               <c:if test="${page.enabled}">checked</c:if>>
    </div>
    <button type="submit" class="btn btn-primary"><i class="glyphicon glyphicon-floppy-save"></i> Save</button>
    &nbsp;
    <c:if test="${page.id != null}">
        <a class="btn btn-danger" href="<c:url value="/secure/socialnet/vk/${page.id}/delete" />"
           onclick="return confirm('Are you sure?')"><i class="glyphicon glyphicon-trash"></i> Delete</a>&nbsp;
    </c:if>
    <a href="<c:url value="/secure/socialnet/vk"/>" class="btn btn-default">Cancel</a>
</form>
<%--<p class="lead">Pin a fixed-height footer to the bottom of the viewport in desktop browsers with this custom HTML and CSS. A fixed navbar has been added within <code>#wrap</code> with <code>padding-top: 60px;</code> on the <code>.container</code>.</p>--%>
<%--<p>Back to <a href="../sticky-footer">the default sticky footer</a> minus the navbar.</p>--%>