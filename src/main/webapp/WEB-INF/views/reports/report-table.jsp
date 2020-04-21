<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/utils/header.jsp">
    <jsp:param name="pageTitle" value="Reports"/>
</jsp:include>

<%-- CATEGORY TABLE --%>
<div class="card shadow mb-4">
    <div class="card-header py-3">
        <h6 class="m-0 font-weight-bold text-gray-800">Reports</h6>
    </div>
    <div class="card-body">

        <table>
            <thead>
                <tr>
                    <th>Category</th>
                    <th>January</th><th>February</th><th>March</th>
                    <th>April</th><th>May</th><th>June</th>
                    <th>July</th><th>August</th><th>September</th>
                    <th>October</th><th>November</th><th>December</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${categories}" var="cat">
                    <tr data-cat-id="${cat.id}">
                        <td>${cat.name}</td>
                        <td></td><td></td><td></td><td></td><td></td><td></td>
                        <td></td><td></td><td></td><td></td><td></td><td></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

    </div>
</div>

<jsp:include page="/WEB-INF/views/utils/footer.jsp">
    <jsp:param name="additionalScriptFile" value="reports.js"/>
</jsp:include>
