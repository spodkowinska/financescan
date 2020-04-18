<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<form:form modelAttribute="project" id="projectModalForm">

    <form:input path="id" type="hidden" id="id" />
    <form:input path="createdDate" type="hidden" id="createdDate" />
    <form:input path="archivedDate" type="hidden" id="archivedDate" />
    <form:input path="archived" type="hidden" id="archived" />

    <div class="form-group">
        <label>Name</label>
        <form:input path="name" class="form-control" id="name" />
    </div>

    <div class="form-group">
        <label>Description</label>
        <form:textarea path="description" class="form-control" id="description" name="description" rows="5" />
    </div>

</form:form>
