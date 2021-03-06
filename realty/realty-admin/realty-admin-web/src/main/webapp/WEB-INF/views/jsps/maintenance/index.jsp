<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="people" type="java.util.List<name.dargiri.data.model.Person>"--%>
<%--@elvariable id="person" type="name.dargiri.data.model.Person"--%>
<div class="page-header">
    <h1>Maintenance</h1>
</div>

<div class="alert alert-info">
    <c:choose >
        <c:when test="${not empty activeJobName}">
            Jobs active: ${activeJobName}
        </c:when>
        <c:otherwise>
            No active jobs
        </c:otherwise>
    </c:choose>
</div>

<p>
    <a class="btn btn-primary" href="<c:url value="/secure/maintenance/reparse_existing_fb_posts"/>"
       onclick="return confirm('Re-parse ALL FaceBook post contents & reapply parsing algorithms?');">Re-parse FB
        Posts</a>
</p>

<p>
    <a class="btn btn-primary" href="<c:url value="/secure/maintenance/reparse_existing_vk_posts"/>"
       onclick="return confirm('Re-parse ALL Vkontakte post contents & reapply parsing algorithms?');">Re-parse VK
        Posts</a>
</p>

<p>
    <a class="btn btn-primary" href="<c:url value="/secure/maintenance/manual_sync_fb"/>"
       onclick="return confirm('Are you sure you want to sync manually with FB?');">FB Manual sync</a>
</p>

<p>
    <a class="btn btn-primary" href="<c:url value="/secure/maintenance/manual_sync_vk"/>"
       onclick="return confirm('Are you sure you want to sync manually with VK?');">VK Manual sync</a>
</p>

<p>
    <a class="btn btn-primary" href="<c:url value="/secure/maintenance/manual_sync_moscow_metros"/>"
       onclick="return confirm('Are you sure you want to sync manually metros?');">Moscow Metros Manual sync</a>
</p>

<p>
    <a class="btn btn-primary" href="<c:url value="/secure/maintenance/manual_sync_st_petersburg_metros"/>"
       onclick="return confirm('Are you sure you want to sync manually metros?');">St Petersburg Metros Manual sync</a>
</p>

<p>
    <a class="btn btn-primary" href="<c:url value="/secure/maintenance/trigger_clean_up"/>"
       onclick="return confirm('Are you sure you want to close old apartments');">Close old stuff</a>
</p>
<p>
    <a class="btn btn-primary" href="<c:url value="/secure/maintenance/reparse_apartment_idents"/>"
       onclick="return confirm('Are you sure you want to reparse all apartments ident');">Reparse apartments</a>
</p>
<%--<p class="lead">Pin a fixed-height footer to the bottom of the viewport in desktop browsers with this custom HTML and CSS. A fixed navbar has been added within <code>#wrap</code> with <code>padding-top: 60px;</code> on the <code>.container</code>.</p>--%>
<%--<p>Back to <a href="../sticky-footer">the default sticky footer</a> minus the navbar.</p>--%>