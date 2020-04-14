<%--
  Created by IntelliJ IDEA.
  User: sandracoderslab
  Date: 14/04/2020
  Time: 19:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="/file/csvsettings" method="post">
    <div class="form-group">
        <label>Select your custom csv settings</label>
        <select class="form-control" onchange="fillAForm()" id="selectSettings">
            <c:forEach items="${csvSettingsList}" var="setting">
                <option value="${setting.id}">${setting.name}</option>
            </c:forEach>
        </select>
    </div>


    <div class="form-group">
        <label>Select account</label>
        <select class="form-control" id="selectAccount" name="selectAccount">
            <c:forEach items="${accountsList}" var="account">
                <option value="${account.id}">${account.name}</option>
            </c:forEach>
        </select>
    </div>

    <div class="form-group">
        <label>Set date position</label>
        <input class="form-control" id="datePosition" name="datePosition">
        <p class="help-block">This block can be imported from your custom CSV
            settings</p>
    </div>

    <div class="form-group">
        <label>Set description position</label>
        <input class="form-control" id="descriptionPosition" name="descriptionPosition">
        <p class="help-block">This block can be imported from your custom CSV
            settings</p>
    </div>

    <div class="form-group">
        <label>Set transaction partner position</label>
        <input class="form-control" id="partyPosition" name="partyPosition">
        <p class="help-block">This block can be imported from your custom CSV
            settings</p>
    </div>

    <div class="form-group">
        <label>Set amount position</label>
        <input class="form-control" id="amountPosition" name="amountPosition">
        <p class="help-block">This block can be imported from your custom CSV
            settings</p>
    </div>

    <div class="form-group">
        <label>Set separator</label>
        <input class="form-control" id="separator" name="separator">
        <p class="help-block">This block can be imported from your custom CSV
            settings</p>
    </div>

    <div class="form-group">
        <label>How many lines at the beginning should be skipped?</label>
        <input class="form-control" id="skipLines" name="skipLines">
        <p class="help-block">This block can be imported from your custom CSV
            settings</p>
    </div>
    <button type="submit" class="btn btn-default"
            formaction="${pageContext.request.contextPath}/file/csvsettings">Save settings
    </button>

    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>
</body>
</html>
