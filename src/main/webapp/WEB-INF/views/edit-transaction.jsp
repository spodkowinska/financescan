<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<form:form action="/transaction/edit/${transaction.id}" method="post" modelAttribute="transaction">

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

    <div class="form-group">
        <label>Category</label>
        <form:select path="categories" class="form-control" id="categoryId">
            <c:forEach items="${categories}" var="category">
                <option value="${category.id}">${category.name}</option>
            </c:forEach>
        </form:select>
    </div>

</form:form>
