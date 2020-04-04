<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<form:form modelAttribute="category" id="categoryModalForm">

    <div style="text-align: center; vertical-align: middle; height: 30px">
        <form:input path="color" type="color" id="color" name="color" style="display: none"
                    onchange="refreshColorPreview()" onkeypress="refreshColorPreview()" />
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
        <form:input path="name" class="form-control" id="categoryName" onkeyup="refreshColorPreview()" />
    </div>

    <div class="form-group">
        <label>Description</label>
        <form:input path="description" class="form-control" id="description" name="description" />
    </div>

    <div class="form-group">
        <label>Keywords
            <a tabindex="0" data-toggle="popover" data-trigger="focus" title="Keywords" data-html="true"
               data-content="Words that will be used to automatically suggest this category for your transactions.
            Use the checkbox to mark the keyword as <i>safe</i> - it will be used to <b>assign</b> categories instead of just <b>suggesting</b> them.">
                <span class="fa fa-lg fa-question-circle"></span>
            </a>
        </label>
        <form:input path="keywords" class="form-control" id="keywords" name="keywords" style="display: none" />
        <form:input path="safeKeywords" class="form-control" id="safeKeywords" name="safeKeywords" style="display: none" />
        <div id="keywordList">
            <%-- Filled by JS below --%>
        </div>
        <label class="btn btn-sm" onclick="addKeyword()" style="cursor: pointer"><span class="fa fa-plus"></span> Add new keyword</label>
    </div>

</form:form>

<script>
    function refreshColorPreview() {
        let colorPreview = $('#colorPreview');

        let input = document.getElementById("categoryName");
        let name = input.value;
        let displayName = name && name !== "" ? name : 'Placeholder name';

        colorPreview.css('background', $('#color').val());
        colorPreview.text(displayName);
    }

    var keywordCounter = 0;

    function refreshKeywordList() {
        let keywordList = $('#keywordList');

        let safeKeywordsStr = $('#safeKeywords').val();
        if (safeKeywordsStr !== "") {
            let safeKeywords = safeKeywordsStr.split(',');
            for (let keyword of safeKeywords) {
                addKeyword(keyword, true);
            }
        }

        let keywordsStr = $('#keywords').val();
        if (keywordsStr !== "") {
            let keywords = keywordsStr.split(',');
            for (let keyword of keywords) {
                addKeyword(keyword);
            }
        }
    }

    function deleteKeyword(keywordNumber) {
        $('#keyword' + keywordNumber).remove();
    }

    function addKeyword(keyword, safe) {
        keyword = keyword ? keyword : "";
        $('#keywordList').append('<div class="input-group" id="keyword' + keywordCounter + '">' +
            '  <div class="input-group-prepend">' +
            '    <div class="input-group-text keyword"><input type="checkbox" aria-label="Safe keyword" id="safe-keyword-check-' + keywordCounter + '"' + (safe ? ' checked' : '') + '></div>' +
            '  </div>' +
            '  <input type="text" class="form-control keyword keyword-text" data-keyword-number="' + keywordCounter + '" placeholder="Put a keyword here..." aria-label="Keyword" aria-describedby="button-addon-' + keywordCounter + '" value="' + keyword +'">' +
            '  <div class="input-group-append">' +
            '    <button onclick="deleteKeyword(' + keywordCounter + ')" class="btn btm-sm keyword" type="button" id="button-addon-' + keywordCounter + '" style="cursor: pointer;"><span class="fa fa-trash"></span></button>' +
            '  </div>' +
            '</div>');
        keywordCounter++;
    }

    refreshColorPreview();
    refreshKeywordList();

    $('[data-toggle="popover"]').popover()
</script>