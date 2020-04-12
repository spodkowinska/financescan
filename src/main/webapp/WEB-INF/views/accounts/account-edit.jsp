<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form:form modelAttribute="account" id="accountModalForm">

    <form:input path="id" type="hidden" id="id" />
    <form:input path="created" type="hidden" id="created" />
    <form:input path="logoImage" type="hidden" id="logoImage" />
    <form:input path="logoFilter" type="hidden" id="logoFilter" />

    <div class="form-group">
        <label>Name</label>
        <form:input path="name" class="form-control" id="name" />
    </div>

    <div class="form-group">
        <label>Description</label>
        <form:textarea path="description" class="form-control" id="description" name="description" rows="5" />
    </div>

    <div class="form-group">
        <label>Image</label>
        <div>
            <c:forEach items="${images}" var="image">
                <img src="${pageContext.request.contextPath}/img/banks/${image}" style="width: 32px; height: 32px; margin: 1px; float: left">
            </c:forEach>
        </div>
    </div>

</form:form>
