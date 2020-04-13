<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    #bank-image-container {
        width: 320px;
        float: left;
    }
    #bank-image-container label {
        background-size: 32px;
        width: 32px;
        height: 32px;
        margin: 1px;
        float: left;
        opacity: 0.6;
        filter: grayscale(1);
    }
    #bank-image-container input:checked + label {
        opacity: 1.0;
        filter: none;
    }
    #bank-image-container input {
        display: none;
    }
    #bank-image-preview {
        width: 128px;
        height: 128px;
        float: right;
        border-radius: 6px;
    }
</style>

<form:form modelAttribute="account" id="accountModalForm">

    <form:input path="id" type="hidden" id="id" />
    <form:input path="created" type="hidden" id="created" />
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
        <div id="bank-image-container">
            <c:forEach items="${images}" var="image">
                <c:choose>
                    <c:when test="${image == account.logoImage}">
                        <input name="logoImage" type="radio" id="image-${image}" value="${image}" checked="checked" />
                    </c:when>
                    <c:otherwise>
                        <input name="logoImage" type="radio" id="image-${image}" value="${image}" />
                    </c:otherwise>
                </c:choose>
                <label for="image-${image}"
                       style="background-image: url('${pageContext.request.contextPath}/img/banks/${image}')">
                </label>
            </c:forEach>
        </div>
        <div id="bank-image-preview">
        </div>
        </div>
    </div>

</form:form>

<script>
    {
        const bgImg = $('#bank-image-container input:checked + label').css('background-image');
        $('#bank-image-preview').css('background-image', bgImg);

        $('#bank-image-container input').change(function() {
            if ($(this).is(':checked'))
                $('#bank-image-preview').css('background-image', $('#bank-image-container input:checked + label').css('background-image'));
        });
    }
</script>

