<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form:form modelAttribute="newProject"  id="projectModalForm">


    <div class="form-group">
        <label>Project Name
            <a tabindex="0" data-toggle="popover" data-trigger="focus" data-html="true"
               data-content="">
                <span class="fa fa-question-circle"></span>
            </a>
        </label>

        <form:input path="name" id="projectName" class="form-control"/>

    </div>

    <div class="form-group">
        <label>Description
            <a tabindex="0" data-toggle="popover" data-trigger="focus" data-html="true"
               data-content="">
                <span class="fa fa-question-circle"></span>
            </a>
        </label>

        <form:input path="description" id="projectDescription" class="form-control"/>

    </div>

</form:form>

<script>
    $('#sentKeywords').val('${keywords != null ? keywords : ''}');

    $('#safeCheck').change(function () {
        const checked = $(this).is(':checked');
        $('#sentKeywords').attr('name', checked ? 'safeKeywords' : 'keywords');
        $('#notSentKeywords').attr('name', checked ? 'keywords' : 'safeKeywords');
    });

    $('[data-toggle="popover"]').popover();
</script>
