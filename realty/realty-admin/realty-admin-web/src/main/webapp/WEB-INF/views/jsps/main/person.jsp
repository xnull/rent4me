<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%--@elvariable id="person" type="name.dargiri.data.dto.PersonDTO"--%>
<div class="page-header">
  <h1>Person</h1>
</div>
<form role="form" method="post" id="userEditForm">
  <div class="form-group">
    <label for="id">#</label>
    <input type="hidden" class="form-control" name="id" placeholder="#" value="${person.id}" readonly>
    <input type="text" class="form-control" id="id" placeholder="#" value="${person.id}" readonly>
  </div>
  <div class="form-group">
    <label for="username">Username</label>
    <input type="text" class="form-control" name="username" id="username" placeholder="Username" value="${person.username}">
  </div>
  <button type="submit" class="btn btn-default">Save</button>
</form>
<%--<p class="lead">Pin a fixed-height footer to the bottom of the viewport in desktop browsers with this custom HTML and CSS. A fixed navbar has been added within <code>#wrap</code> with <code>padding-top: 60px;</code> on the <code>.container</code>.</p>--%>
<%--<p>Back to <a href="../sticky-footer">the default sticky footer</a> minus the navbar.</p>--%>