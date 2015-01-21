<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="people" type="java.util.List<name.dargiri.data.model.Person>"--%>
<%--@elvariable id="person" type="name.dargiri.data.model.Person"--%>
<div class="page-header">
    <h1>Maintenance</h1>
</div>
<p>
    <a class="btn btn-primary" href="<c:url value="/secure/maintenance/reparse_existing_fb_posts"/>"
       onclick="return confirm('Re-parse ALL FaceBook post contents & reapply parsing algorithms?');">Re-parse FB
        Posts</a>
</p>

<p>
    <a class="btn btn-primary" href="<c:url value="/secure/maintenance/manual_sync"/>"
       onclick="return confirm('Are you sure you want to sync manually with FB?');">FB Manual sync</a>
</p>
<%--<p class="lead">Pin a fixed-height footer to the bottom of the viewport in desktop browsers with this custom HTML and CSS. A fixed navbar has been added within <code>#wrap</code> with <code>padding-top: 60px;</code> on the <code>.container</code>.</p>--%>
<%--<p>Back to <a href="../sticky-footer">the default sticky footer</a> minus the navbar.</p>--%>