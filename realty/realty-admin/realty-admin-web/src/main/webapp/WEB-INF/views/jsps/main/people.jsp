<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%--@elvariable id="people" type="java.util.List<name.dargiri.data.model.Person>"--%>
<%--@elvariable id="person" type="name.dargiri.data.model.Person"--%>
<div class="page-header">
  <h1>People</h1>
</div>
<table class="table table-striped">
  <thead>
  <tr>
    <th>#</th>
    <th>Username</th>
    <th>Action</th>
  </tr>
  </thead>
  <tbody>
  <c:forEach var="person" items="${people}">
    <tr>
      <td>
          ${person.id}
      </td>
      <td>
          ${person.username}
      </td>
      <td>
        <a href="<c:url value="/secure/people/edit/${person.id}"/>">Edit</a> |
        <a href="<c:url value="/secure/people/delete/${person.id}"/>">Delete</a>
      </td>
    </tr>
  </c:forEach>
  </tbody>
</table>
<%--<p class="lead">Pin a fixed-height footer to the bottom of the viewport in desktop browsers with this custom HTML and CSS. A fixed navbar has been added within <code>#wrap</code> with <code>padding-top: 60px;</code> on the <code>.container</code>.</p>--%>
<%--<p>Back to <a href="../sticky-footer">the default sticky footer</a> minus the navbar.</p>--%>