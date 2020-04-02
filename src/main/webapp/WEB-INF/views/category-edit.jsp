<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<script>
    function changeColorPreview() {
        $('#colorPreview').css('background', $('#color').val());
    }
</script>

<form:form modelAttribute="category" id="categoryModalForm">

    <div style="text-align: center; vertical-align: middle; height: 30px">
        <form:input path="color" type="color" id="color" name="color" style="display: none" onchange="changeColorPreview()" />
        <label for="color">
            <div class="tag" style="background: ${category.color}; font-size: 16px;" id="colorPreview">
                ${category.name}
            </div>
        </label>
        <label for="color" class="fa fa-paint-roller" style="margin: 0 0 0 3px; cursor: pointer; color: rgb(207, 207, 207);"></label>
    </div>

    <form:input path="id" type="hidden" id="id" />

    <div class="form-group">
        <label>Name</label>
        <form:input path="name" class="form-control" id="categoryName" />
    </div>

    <div class="form-group">
        <label>Description</label>
        <form:input path="description" class="form-control" id="description" name="description" />
        <p class="help-block"></p>
    </div>

    <div class="form-group">
        <label>Keywords</label>
        <form:input path="keywords" class="form-control" id="keywords" name="keywords" />
        <p class="help-block">Words that will be used to assign categories to your transactions</p>
    </div>

</form:form>
