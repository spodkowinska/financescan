<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

{
    "year" : ${year},
<c:if test="${month != null}">
    "month" : ${month},
</c:if>
    "numberOfTransactions" : ${numberOfTransactions},
    "sumOfIncomes" : ${sumOfIncomes},
    "sumOfExpenses" : ${sumOfExpenses},
    "balance" : ${balance},
    "cats" : {
        <c:forEach items="${categoriesWithStatistics}" var="cs" varStatus="loop">
            "${cs.key == null ? 0 : cs.key}" : {
                "id" : ${cs.value.categoryId == null ? 0 : cs.value.categoryId},
                "income" : ${cs.value.income},
                "outcome" : ${cs.value.outcome},
                "balance" : ${cs.value.balance}
            }${loop.last ? '' : ','}
        </c:forEach>
    }
}
