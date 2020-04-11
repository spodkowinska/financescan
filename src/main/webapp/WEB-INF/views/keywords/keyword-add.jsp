<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form id="keywordModalForm">

    <div class="form-group">
        <label>Select category</label>
        <select class="form-control" id="category" name="category">
            </option>
            <c:forEach items="${categories}" var="category">
                <option value="${category.id}">${category.name}</option>
            </c:forEach>
        </select>
    </div>

    <div class="form-group">
        <label>Keyword
            <a tabindex="0" data-toggle="popover" data-trigger="focus" data-html="true"
               data-content="Words that will be used to automatically suggest this category for your transactions.
            Use the checkbox below to mark the keyword as <i>safe</i> - it will be used to <b>assign</b> categories instead of just <b>suggesting</b> them.">
                <span class="fa fa-question-circle"></span>
            </a>
        </label>

        <input name="safeKeywords" id="notSentKeywords" type="hidden" />
        <input name="keywords" id="sentKeywords" class="form-control" />

        <div class="form-check" style="margin-top: 5px">
            <input class="form-check-input" type="checkbox" id="safeCheck" style="margin-top: 6px">
            <label class="form-check-label small" for="safeCheck">Safe keyword</label>
        </div>
    </div>
</form>

<script>
    $('#sentKeywords').val('${keywords != null ? keywords : ''}');

    $('#safeCheck').change(function () {
        const checked = $(this).is(':checked');
        $('#sentKeywords').attr('name', checked ? 'safeKeywords' : 'keywords');
        $('#notSentKeywords').attr('name', checked ? 'keywords' : 'safeKeywords');
    });

    $('[data-toggle="popover"]').popover();
</script>
