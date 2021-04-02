<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form:form modelAttribute="keywordDTO" id="keywordModalForm">

    <div class="form-group">
        <label>Select category</label>
        <form:select path="category" class="form-control" id="category" name="category">
<%--            </option>--%>
<%--            <c:forEach items="${categories}" var="categoryToChoose">--%>
                <form:options items="${categories}" itemValue="id" itemLabel="name"></form:options>
<%--            </c:forEach>--%>
        </form:select>
    </div>

    <div class="form-group">
        <label>Keyword
            <a tabindex="0" data-toggle="popover" data-trigger="focus" data-html="true"
               data-content="Words that will be used to automatically suggest this category for your transactions.
            Use the checkbox below to mark the keyword as <i>safe</i> - it will be used to <b>assign</b> categories instead of just <b>suggesting</b> them.">
                <span class="fa fa-question-circle"></span>
            </a>
        </label>

        <input name="hiddenSafeKeywords" id="notSentKeywords" type="hidden"/>
        <form:input path="keyword" name="keyword" id="keywords" class="form-control"/>

        <div class="form-check" style="margin-top: 5px">
            <form:checkbox path="safeKeyword" name="safeKeyword" class="form-check-input" id="safeKeyword" style="margin-top: 6px"></form:checkbox>
            <label class="form-check-label small" for="safeKeyword">Safe keyword</label>
        </div>
    </div>
</form:form>

<%--<script>--%>
<%--    $('#keywords').val('${keyword != null ? keyword : ''}');--%>

<%--    $('#safeKeyword').change(function () {--%>
<%--        const checked = $(this).is(':checked');--%>
<%--        $('#sentKeywords').attr('name', checked ? 'safeKeyword' : 'keyword');--%>
<%--        $('#notSentKeywords').attr('name', checked ? 'keyword' : 'safeKeyword');--%>
<%--    });--%>

<%--    $('[data-toggle="popover"]').popover();--%>
<%--</script>--%>
