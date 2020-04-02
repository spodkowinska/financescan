<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<form:form modelAttribute="category" id="categoryModalForm">

    <form:input path="id" type="hidden" id="id"></form:input>

    <div class="form-group">
        <label>Name</label>
        <form:input path="name" class="form-control" id="categoryName"></form:input>
    </div>

    <div class="form-group">
        <label>Description</label>
        <form:input path="description" class="form-control" id="description" name="description"></form:input>
        <p class="help-block"></p>
    </div>

    <div class="form-group">
        <label>Keywords</label>
        <form:input path="keywords" class="form-control" id="keywords" name="keywords"></form:input>
        <p class="help-block">Words that will be used to assign categories to your transactions</p>
    </div>

    <div class="form-group">
        <label>Select color for category label</label>
        <form:input path="color" type="color" id="color" name="color"></form:input>
    </div>

</form:form>
