<%--
  Created by IntelliJ IDEA.
  User: dionis
  Date: 10/4/13
  Time: 11:17 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div align="center">
<form name='f' action="<c:url value='/j_spring_security_check' />" method='POST'>

    <fieldset>
        <legend>Sign in</legend>

        <div class="control-group ">
        <label>Username
            <input type="text" name='j_username' value='' />
        </label>


        <label>Password
            <input type="password" name='j_password' />
        </label>


        <button type="submit" class="btn btn-primary">Sign in</button>
    </fieldset>

</form>
</div>