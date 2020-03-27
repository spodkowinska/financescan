<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<tbody>
<c:forEach items="${tl}" var="trans">

    <tr>
        <td class="actions">
            <a data-toggle="tooltip" title="Edit transaction"
               href="${pageContext.request.contextPath}/transaction/edit/${trans.id}"><span
                    class="fa fa-edit"></span></a>
            <a data-toggle="tooltip" title="Create keyword from this transaction"
               href="${pageContext.request.contextPath}/keyword/add/${trans.id}"><span class="fa fa-key"></span></a>
            <a data-toggle="tooltip" title="Delete transaction"
               href="${pageContext.request.contextPath}/transaction/delete/${trans.id}"><span
                    class="fa fa-trash-alt"></span></a>
        </td>
        <td class="center">${trans.transactionDate}</td>
        <td class="
                                            <c:choose>
                                                <c:when test="${trans.amount lt 0}">negative</c:when>
                                                <c:otherwise>positive</c:otherwise>
                                            </c:choose> right">
                ${trans.amount} zł
        </td>
        <td>
            <c:forEach items="${categoriesList}" var="category">
                <c:choose>
                    <c:when test="${fn:contains(trans.categories, category)}">
                        <a href="${pageContext.request.contextPath}/transaction/removecategory/${trans.id}/${category.id}"
                           class="tag tag${category.id}">${category.name}</a>
                    </c:when>
                </c:choose>
            </c:forEach>

            <a tabindex="0" class="tag tag-add" data-html="true" data-toggle="popover" data-trigger="focus"
               data-content="
                                                
                                                <c:forEach items="${categoriesList}" var="category">
                                                    <c:choose>
                                                        <c:when test="${fn:contains(trans.categories, category)}">
                                                        </c:when>
                                                        <c:otherwise>
                                                            <a href='${pageContext.request.contextPath}/transaction/addcategory/${trans.id}/${category.id}' class='tag tag${category.id}'>${category.name}</a>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:forEach>

                                                ">+</a>
        </td>
        <td>${trans.description}</td>
    </tr>

</c:forEach>

<!-- todo: handle changing categories -->
<!-- <select class="form-control" id="changeCategory${trans.id}" name="changeCategory"
                                        multiple size="4" onchange="sendData(${trans.id})" >
                                    <c:forEach items="${categoriesList}" var="category">
                                        <c:choose>
                                            <c:when test="${fn:contains(trans.categories, category)}">
                                                <option selected="selected"
                                            </c:when>
                                            <c:otherwise>
                                                <option
                                            </c:otherwise>
                                        </c:choose>
                                        value="${category.id}">${category.name}</option>
                                    </c:forEach>
                                </select> -->

</tbody>
