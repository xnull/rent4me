<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <%--<link rel="shortcut icon" href="../../assets/ico/favicon.ico">--%>
    <title>Rent4.me Admin</title>
    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.0/css/bootstrap.min.css">
    <!-- Custom styles for this template -->
    <style type="text/css">
        /* Sticky footer styles
        -------------------------------------------------- */
        html,
        body {
            height: 100%;
            /* The html and body elements cannot have any padding or margin. */
        }

        /* Wrapper for page content to push down footer */
        #wrap {
            min-height: 100%;
            height: auto;
            /* Negative indent footer by its height */
            margin: 0 auto -60px;
            /* Pad bottom by footer height */
            padding: 0 0 60px;
        }

        /* Set the fixed height of the footer here */
        #footer {
            height: 60px;
            background-color: #f5f5f5;
        }

        /* Custom page CSS
        -------------------------------------------------- */
        /* Not required for template or sticky footer method. */
        #wrap > .container {
            padding: 60px 15px 0;
        }

        .container .text-muted {
            margin: 20px 0;
        }

        #footer > .container {
            padding-left: 15px;
            padding-right: 15px;
        }

        code {
            font-size: 80%;
        }
    </style>
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<!-- Wrap all page content here -->
<div id="wrap">
    <!-- Fixed navbar -->
    <div class="navbar navbar-default navbar-fixed-top" role="navigation">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#">Rent4.Me admin</a>
            </div>
            <div class="collapse navbar-collapse">
                <sec:authorize ifAnyGranted="ROLE_ADMIN">
                    <ul class="nav navbar-nav">
                            <%--<li class="active"><a href="<c:url value="/"/>">All</a></li>--%>
                        <li><a href="<c:url value="/secure/people/new"/>">Users</a></li>

                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">Social <b class="caret"></b></a>
                            <ul class="dropdown-menu">
                                <li><strong>Facebook</strong></li>
                                <li><a href="<c:url value="/secure/socialnet/fb"/>">FB pages</a></li>
                                <li><a href="<c:url value="/secure/socialnet/fb/posts"/>">FB posts</a></li>
                                <li class="divider"></li>
                                <li><strong>ВКонтакте</strong></li>
                                <li><a href="<c:url value="/secure/socialnet/vk"/>">ВК страницы</a></li>
                                    <%--<li><a href="<c:url value="/secure/socialnet/vk/posts"/>">ВК посты</a></li>--%>
                                    <%--<li><a href="#">Another action</a></li>--%>
                                    <%--<li><a href="#">Something else here</a></li>--%>
                                    <%--<li class="divider"></li>--%>
                                    <%--<li class="dropdown-header">Nav header</li>--%>
                                    <%--<li><a href="#">Separated link</a></li>--%>
                                    <%--<li><a href="#">One more separated link</a></li>--%>
                            </ul>
                        </li>

                        <li><a href="<c:url value="/secure/maintenance/"/>">Maintenance</a></li>
                    </ul>

                    <ul class="nav navbar-nav navbar-right">
                            <%--<li class="active"><a href="<c:url value="/"/>">All</a></li>--%>
                            <%--<li><a href="<c:url value="/secure/people/new"/>">New</a></li>--%>

                        <li class="dropdown-menu-right"><a href="<c:url value="/j_spring_security_logout"/>">Logout</a>
                        </li>

                    </ul>
                </sec:authorize>
            </div>
            <!--/.nav-collapse -->
        </div>
    </div>
    <!-- Begin page content -->
    <div class="container">

        <c:if test="${not empty error}">
            <div class="error">
                Your login attempt was not successful, try again.<br/> Caused :
                    ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
            </div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">
                    ${errorMessage}
            </div>
        </c:if>
        <c:if test="${not empty infoMessage}">
            <div class="alert alert-info">
                    ${infoMessage}
            </div>
        </c:if>

        <sitemesh:write property='body'/>
    </div>
</div>
<div id="footer">
    <div class="container">
        <p class="text-muted">&copy; Company <%=java.util.Calendar.getInstance().get(java.util.Calendar.YEAR) + ""%>
        </p>
    </div>
</div>
<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>
<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.0/js/bootstrap.min.js"></script>
</body>
</html>