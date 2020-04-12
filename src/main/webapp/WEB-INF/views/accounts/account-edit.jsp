<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

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

</form:form>
