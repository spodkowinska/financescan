<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<form:form modelAttribute="transaction" id="editModalForm">

    <form:input path="id" type="hidden" id="id"></form:input>

    <div class="form-group">
        <label>Date</label>
        <form:input path="transactionDate" type="date"  class="form-control" name="transactionDate" id="transactionDate"></form:input>
    </div>

    <div class="form-group">
        <label>Description</label>
        <form:input path="description" class="form-control" id="description"></form:input>
        <p class="help-block"></p>
    </div>

    <div class="form-group">
        <label>Amount</label>
        <form:input path="amount" class="form-control" id="amount"></form:input>
    </div>

    <div class="form-group">
        <label>Transaction Partner</label>
        <form:input path="party" class="form-control" id="party"></form:input>
    </div>

    <div class="form-group">
        <label>Bank</label>
        <form:select path="account" class="form-control" id="accountId">
            <c:forEach items="${accounts}" var="account">
                <option value="${account.id}">${account.name}</option>
            </c:forEach>
        </form:select>
    </div>

    <style>
        .tag-check {
            opacity: 0.3;
            font-size: 14px;
        }
        .tag-check-container input:checked ~ .tag-check {
            opacity: 1;
        }
    </style>

    <div class="form-group">
        <label>Categories</label>
        <form:input path="rejectedCategories" name="rejectedCategories" id="rejectedCategories" type="hidden"></form:input>
        <div style="text-align: center">
            <c:forEach items="${categories}" var="category">
                <label class="tag-check-container">
                    <c:choose>
                        <c:when test="${fn:contains(transaction.categories, category)}">
                            <input type="checkbox" id="category_${category.id}" name="category_${category.id}"
                                   style="display: none" value="${category.id}" checked>
                            <label class="tag tag${category.id} tag-check" for="category_${category.id}">${category.name}</label>
                        </c:when>
                        <c:when test="${fn:contains(transaction.pendingCategories, category)}">
                            <input type="checkbox" id="category_${category.id}" name="category_pending_${category.id}"
                                   style="display: none" value="${category.id}" checked>
                            <label class="tag tag${category.id} tag-check pending-cat" tabindex="0" id="cat_label_pending_${category.id}"
                                   data-toggle="popover" data-trigger="focus" data-html="true" data-category-id="${category.id}"
                                   data-content="<a class='category-confirm btn btn-sm btn-success' id=category-confirm-${category.id}>Confirm</a>
                                    <a class='category-reject btn btn-sm btn-danger' id=category-reject-${category.id}>Reject</a>">
                                ${category.name} <span class="fa fa-question"></span>
                            </label>
                            <label class="tag tag${category.id} tag-check" for="category_${category.id}" style="display: none" id="cat_label_${category.id}">${category.name}</label>
                        </c:when>
                        <c:when test="${!fn:contains(trans.categories, category) && !fn:contains(trans.pendingCategories, category)}">
                            <input type="checkbox" id="category_${category.id}" name="category_${category.id}"
                                   style="display: none" value="${category.id}">
                            <label class="tag tag${category.id} tag-check" for="category_${category.id}">${category.name}</label>
                        </c:when>
                    </c:choose>
                </label>
            </c:forEach>
        </div>
    </div>

</form:form>

<script>
    // Enable popovers
    {
        let pops = $('.pending-cat');
        pops.popover('disable');
        pops.popover('enable');
        pops.on('shown.bs.popover', function () {
            let catId = $(this).data('category-id');

            // Category confirmation button
            let catConfirmButton = $('#category-confirm-' + catId);
            if (catConfirmButton) {
                catConfirmButton.css('color', 'white');
                catConfirmButton.unbind();
                catConfirmButton.click(function () {
                    $('#cat_label_pending_' + catId).remove();
                    $('#cat_label_' + catId).css('display', 'inline-block');

                    let input = $('#category_' + catId);
                    input.prop('checked', true);
                    input.attr('name', 'category_' + catId);
                });
            }

            // Category rejection button
            let catRejectButton = $('#category-reject-' + catId);
            if (catRejectButton) {
                catRejectButton.css('color', 'white');
                catRejectButton.unbind();
                catRejectButton.click(function () {
                    $('#cat_label_pending_' + catId).remove();
                    $('#cat_label_' + catId).css('display', 'inline-block');

                    let input = $('#category_' + catId);
                    input.prop('checked', false);
                    input.attr('name', 'category_' + catId);
                });
            }
        });
    }
</script>