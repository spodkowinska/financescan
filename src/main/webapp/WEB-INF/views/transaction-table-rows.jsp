<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<c:forEach items="${tl}" var="trans">
    <tr id="cat_row_${trans.id}">
            <%-- COLUMN: ACTIONS --%>

        <td class="actions">
            <a data-toggle="modal" data-target="#editModal" data-transaction-id="${trans.id}"
               data-toggle="tooltip" title="Edit transaction" tabindex="0">
                <span class="fa fa-edit"></span>
            </a>
            <a data-toggle="modal" data-target="#keywordModal" data-transaction-id="${trans.id}"
               data-toggle="tooltip" title="Create keyword from this transaction"
                <span class="fa fa-key"></span>
            </a>
            <a tabindex="0" data-toggle="popover" data-trigger="focus" data-html="true" data-transaction-id="${trans.id}"
               data-content="<a class='delete-confirm btn btn-sm btn-danger' id=delete-confirm-${trans.id}>Delete</a>">
                <span class="fa fa-trash-alt"></span>
            </a>
        </td>


        <td class="center">${trans.transactionDate}</td>

            <%-- COLUMN: AMOUNT --%>

        <c:choose>
            <c:when test="${trans.amount lt 0}">
                <td class="negative right" sorttable_customkey="${trans.amount}">${trans.amount} zł</td>
            </c:when>
            <c:otherwise>
                <td class="positive right" sorttable_customkey="${trans.amount}">${trans.amount} zł</td>
            </c:otherwise>
        </c:choose>

            <%-- COLUMN: CATEGORIES --%>

        <td class="categories-list" id="cat_current_${trans.id}">

            <c:forEach items="${categoriesList}" var="category"><c:choose><c:when
                    test="${fn:contains(trans.categories, category)}"><a
                    id="cat_tag_${trans.id}_${category.id}"
                    tabindex="0"
                    onclick="changeCategory(${trans.id},${category.id})"
                    class="tag tag${category.id}">${category.name}</a></c:when><c:when
                    test="${fn:contains(trans.pendingCategories, category)}"><a
                    id="cat_tag_${trans.id}_${category.id}"
                    tabindex="0"
                    style="display: none"
                    onclick="changeCategory(${trans.id},${category.id})"
                    class="tag tag${category.id}">${category.name}</a><a
                    id="cat_tag_pending_${trans.id}_${category.id}"
                    tabindex="0" data-toggle="popover" data-trigger="focus" data-html="true"
                    data-transaction-id="${trans.id}" data-category-id="${category.id}"
                    data-content="<a class='category-confirm btn btn-sm btn-success' id=category-confirm-${trans.id}-${category.id}>Confirm</a>
                    <a class='category-reject btn btn-sm btn-danger' id=category-reject-${trans.id}-${category.id}>Reject</a>"
                    class="tag tag${category.id}">${category.name} <span class="fa fa-question"></span></a></c:when></c:choose></c:forEach>

        </td>
        <td class="categories-add">
            <div class="btn-group dropright" style="float: right">
                <a tabindex="0" class="tag tag-add" id="cat_drop_${trans.id}" data-toggle="dropdown"
                   aria-haspopup="true"
                   aria-expanded="true">+</a>

                <div id="cat_others_${trans.id}" class="dropdown-menu shadow animated--fade-in tag-add-popover"
                     aria-labelledby="cat_drop_${trans.id}">

                    <c:forEach items="${categoriesList}" var="category"><c:choose><c:when
                            test="${!fn:contains(trans.categories, category) && !fn:contains(trans.pendingCategories, category)}"><a
                            id="cat_tag_${trans.id}_${category.id}"
                            tabindex="0"
                            onclick="changeCategory(${trans.id},${category.id})"
                            class="tag tag${category.id}">${category.name}</a></c:when></c:choose></c:forEach>

                </div>
            </div>
        </td>

            <%-- COLUMN: DESCRIPTION --%>

        <td>${trans.description}</td>
    </tr>

</c:forEach>