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
        <label>Keywords</label>
        <input class="form-control" id="keywords" name="keywords"
               <c:if test="${keywords!=null}">
               value = "${keywords}"
               </c:if>
        >
        <p class="help-block">Words that will be used to assign categories to your
            transactions. They should be separated with coma.</p>

    </div>
</form>
