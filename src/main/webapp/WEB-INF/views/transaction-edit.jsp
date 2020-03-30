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
        <div style="text-align: center">
            <c:forEach items="${categories}" var="category">
                <label class="tag-check-container">
                    <c:choose>
                        <c:when test="${fn:contains(transaction.categories, category)}">
                            <input type="checkbox" id="category_${category.id}" name="category_${category.id}"
                                   style="display: none" value="${category.id}" checked>
                        </c:when>
                        <c:otherwise>
                            <input type="checkbox" id="category_${category.id}" name="category_${category.id}"
                                   style="display: none" value="${category.id}">
                        </c:otherwise>
                    </c:choose>
                    <label class="tag tag${category.id} tag-check" for="category_${category.id}">${category.name}</label>
                </label>
            </c:forEach>
        </div>
    </div>

</form:form>
