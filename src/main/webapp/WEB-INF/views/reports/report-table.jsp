<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/utils/header.jsp">
    <jsp:param name="pageTitle" value="Reports"/>
</jsp:include>

<div class="card shadow mb-4">
    <div class="card-header py-3">
        <h6 class="m-0 font-weight-bold text-gray-800" style="float: left">Reports</h6>
    </div>
    <div class="card-body">

        <c:set var="months" value="${['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec']}" />

        <table id="one_year_report_table" class="finance_table">
            <!-- TABLE HEADER -->
            <thead>
            <tr>
                <th rowspan="2" style="width: 150px">Category</th>
                <th colspan="12">Months</th>
                <th rowspan="2" style="width: 85px">AVG</th>
                <th rowspan="2" style="width: 85px">SUM</th>
            </tr>
            <tr>
                <c:forEach items="${months}" var="month">
                    <th style="width: 85px">${month}</th>
                </c:forEach>
            </tr>
            </thead>

            <!-- TABLE DATA -->
            <tbody id="list">

            <c:forEach items="${categories}" var="category">
                <tr data-category-id="${category.id}">
                    <td><a class="tag" style="background-color: ${category.color}; color: ${category.fontColor} !important; width: 100%; text-align: center; font-size: 12px;">${category.name}</a></td>

                    <c:forEach items="${months}" var="month" begin="0" step="1" varStatus="i">
                        <td class="month_${i.index} center" id="month_${i.index}_cat_${category.id}"></td>
                    </c:forEach>

                    <td class="center avg"></td>
                    <td class="center sum"></td>
                </tr>
            </c:forEach>

                <tr data-category-id="0">
                    <td><a class="tag" style="width: 100%; text-align: center; font-size: 12px; font-style: italic;">Uncategorized</a></td>

                    <c:forEach items="${months}" var="month" begin="0" step="1" varStatus="i">
                        <td class="month_${i.index} center" id="month_${i.index}_cat_0"></td>
                    </c:forEach>

                    <td class="center avg"></td>
                    <td class="center sum"></td>
                </tr>

            </tbody>

            <!-- TABLE FOOTER -->
            <tfoot>
                <tr>
                    <td>SUM</td>

                    <c:forEach items="${months}" var="month" begin="0" step="1" varStatus="i">
                        <td class="month_${i.index} center" id="month_${i.index}_sum"></td>
                    </c:forEach>

                    <td class="center" id="avg_sum"></td>
                    <td class="center" id="sum_sum"></td>
                </tr>
            </tfoot>
        </table>
    </div>
</div>

<jsp:include page="/WEB-INF/views/utils/footer.jsp">
    <jsp:param name="additionalScriptFile" value="reports.js"/>
</jsp:include>
