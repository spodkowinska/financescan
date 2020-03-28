<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<c:forEach items="${tl}" var="trans">
    <tr>
            <%-- COLUMN: ACTIONS --%>

        <td class="actions">
            <a data-toggle="tooltip" title="Edit transaction"
               href="${pageContext.request.contextPath}/transaction/edit/${trans.id}">
                <span class="fa fa-edit"></span>
            </a>
            <a data-toggle="tooltip" title="Create keyword from this transaction"
               href="${pageContext.request.contextPath}/keyword/add/${trans.id}">
                <span class="fa fa-key"></span>
            </a>
            <a data-toggle="tooltip" title="Delete transaction"
               href="${pageContext.request.contextPath}/transaction/delete/${trans.id}">
                <span class="fa fa-trash-alt"></span>
            </a>
        </td>

            <%-- COLUMN: ACTIONS --%>

        <td class="center">${trans.transactionDate}</td>

            <%-- COLUMN: AMOUNT --%>

        <c:choose>
            <c:when test="${trans.amount lt 0}">
                <td class="negative right">${trans.amount} zł</td>
            </c:when>
            <c:otherwise>
                <td class="positive right">${trans.amount} zł</td>
            </c:otherwise>
        </c:choose>

            <%-- COLUMN: CATEGORIES --%>

        <td class="categories">

            <c:forEach items="${categoriesList}" var="category"><c:choose><c:when
                    test="${fn:contains(trans.categories, category)}"><a
                    tabindex="0" onclick="removeCategory(${trans.id},${category.id})"
                    class="tag tag${category.id}">${category.name}</a></c:when></c:choose></c:forEach>

            <div class="btn-group dropright" style="float: right">
                <a tabindex="0" class="tag tag-add" id="cat_drop${trans.id}" data-toggle="dropdown" aria-haspopup="true"
                   aria-expanded="true">
                    +
                </a>

                <div class="dropdown-menu shadow tag-add-popover" aria-labelledby="cat_drop${trans.id}">

                    <c:forEach items="${categoriesList}" var="category"><c:choose><c:when
                            test="${!fn:contains(trans.categories, category)}"><a
                            tabindex="0" onclick="addCategory(${trans.id},${category.id})"
                            class="tag tag${category.id}">${category.name}</a></c:when></c:choose></c:forEach>

                </div>
            </div>
        </td>

            <%-- COLUMN: DESCRIPTION --%>

        <td>${trans.description}</td>
    </tr>

</c:forEach>