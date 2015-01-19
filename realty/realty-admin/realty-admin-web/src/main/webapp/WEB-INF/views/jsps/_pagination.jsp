<%--
  Created by IntelliJ IDEA.
  User: dionis
  Date: 8/11/13
  Time: 4:21 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="paginationHelper" type="name.dargiri.web.utils.PaginationHelper"--%>

<div class="row">
    <div class="span4">
        <c:if test="${paginationHelper.listable}">
            <nav>
                <ul class="pagination">
                    <c:choose>
                        <c:when test="${paginationHelper.hasPrev}">
                            <li><a aria-label="Previous"
                                   href="<c:url value="${paginationHelper.location}?page=${paginationHelper.prevPage}&limit=${paginationHelper.resultsOnPage}"/>">&laquo;</a>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="disabled"><a href="#">&laquo;</a></li>
                        </c:otherwise>
                    </c:choose>
                    <li>
                        <select class="btn btn-default dropdown-toggle"
                                style="float: left;width: 150px; margin-bottom: 0px; border-left: 0px; border-radius: 0px;"
                                onkeyup="this.blur();this.focus();" onchange="window.location.href='<c:url
                                value="${paginationHelper.location}?page="/>' + this.options[this.selectedIndex].value+'&limit=${paginationHelper.resultsOnPage}';">
                            <c:forEach items="${paginationHelper.pages}" var="pageNumber">
                                <c:choose>
                                    <c:when test="${paginationHelper.currentPage == pageNumber}">
                                        <option disabled="disabled" value="${pageNumber}" selected="selected">
                                            Page ${pageNumber} of ${paginationHelper.pageTotalCount}</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${pageNumber}">Page ${pageNumber}
                                            of ${paginationHelper.pageTotalCount}</option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </select>
                    </li>
                    <c:choose>
                        <c:when test="${paginationHelper.hasNext}">
                            <li><a aria-label="Next"
                                   href="<c:url value="${paginationHelper.location}?page=${paginationHelper.nextPage}&limit=${paginationHelper.resultsOnPage}"/>">&raquo;</a>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="disabled"><a href="#">&raquo;</a></li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </nav>
        </c:if>
    </div>
    <div class="span4" style="text-align: center; padding-top: 25px;">Record(s) <b><c:out
            value="${(paginationHelper.size > 0) ? paginationHelper.currentPage * paginationHelper.resultsOnPage - paginationHelper.resultsOnPage + 1 : 0}"/>&dash;<c:out
            value="${((paginationHelper.currentPage * paginationHelper.resultsOnPage) > paginationHelper.size) ? paginationHelper.size : paginationHelper.currentPage * paginationHelper.resultsOnPage}"/></b>
        of <b><c:out value="${paginationHelper.size}"/></b></div>
    <div class="span4" style="text-align: right; padding-top: 20px;">Show
        <select style="width: 70px; vertical-align: baseline;" onkeyup="this.blur();this.focus();"
                onchange="window.location.href='<c:url
                        value="${paginationHelper.location}?limit="/>' + this.options[this.selectedIndex].value;">
            <option
                    <c:if test="${paginationHelper.resultsOnPage == 10}">selected="selected"</c:if> value="10">10
            </option>
            <option
                    <c:if test="${paginationHelper.resultsOnPage == 25}">selected="selected"</c:if> value="25">25
            </option>
            <option
                    <c:if test="${paginationHelper.resultsOnPage == 50}">selected="selected"</c:if> value="50">50
            </option>
            <option
                    <c:if test="${paginationHelper.resultsOnPage == 100}">selected="selected"</c:if> value="100">100
            </option>
            <option
                    <c:if test="${paginationHelper.resultsOnPage == 250}">selected="selected"</c:if> value="250">250
            </option>
        </select>
        on page
    </div>
</div>
