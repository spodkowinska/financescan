<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time" %>

<jsp:include page="/WEB-INF/views/utils/header.jsp">
    <jsp:param name="pageTitle" value="Accounts"/>
</jsp:include>

<style>
    .footer-entry {
        margin-right: 20px;
    }
    .footer-entry:after {
        color: #dbdded;
        margin-left: 20px;
        content: '|';
    }
    .footer-entry:last-of-type:after {
        content: '';
    }
    .card-footer {
        padding: 5px 10px;
    }
</style>

<%-- ACCOUNT MODAL --%>
<div class="modal fade" id="accountModal" tabindex="-1" role="dialog" aria-labelledby="accountModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header alert-secondary">
                <h5 class="modal-title" id="accountModalLabel">Edit Account</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" id="accountModalBody">
                <%-- Filled by AJAX from account-edit.jsp --%>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-primary" data-dismiss="modal" id="accountModalSubmit">Save</button>
            </div>
        </div>
    </div>
</div>

<%-- ACCOUNT DELETE MODAL --%>
<div class="modal fade" id="accountDeleteModal" tabindex="-1" role="dialog" aria-labelledby="accountDeleteModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header alert-secondary">
                <h5 class="modal-title" id="accountDeleteModalLabel">Delete Account
                    <span class="small text-gray-500">#</span><span id="accountDeleteModal-id" class="small text-gray-500"></span></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" id="accountDeleteModalBody">
<span id="accountDeleteModal-assignedTransactions"><b id="accountDeleteModal-assignedCount">0</b> transaction(s)
    are assigned to this account. Delete or move the transactions first in order to delete this account.<br><br></span>
                <span id="accountDeleteModal-lastAccount">This is the last account in this project. It can't be deleted.<br><br></span>
                <span id="accountDeleteModal-possible">No transactions are assigned to this account. You can safely delete it.<br><br></span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-danger" data-dismiss="modal" id="accountDeleteModalSubmit">Delete</button>
            </div>
        </div>
    </div>
</div>

<!-- ACCOUNT TABLE -->
<div class="card shadow mb-4">
    <div class="card-header py-3">
        <h6 class="m-0 font-weight-bold text-gray-800" style="float: left">Accounts</h6>
        <button class="btn btn-secondary btn-sm" style="float: right; margin-bottom: -6px; margin-top: -6px;"
                data-toggle="modal" data-target="#accountModal">
            <span class="fa fa-plus"></span> Add new account
        </button>
    </div>
    <div class="card-body">
        <c:forEach items="${accountsList}" var="account" step="1" varStatus="counter" begin="0">

            <c:if test="${counter.index % 2 == 0}">
                <div class="card-deck">
            </c:if>

                <div class="card" style="margin-bottom: 10px">
                    <div class="row no-gutters">
                        <div class="col-auto">
                            <img src="${pageContext.request.contextPath}/img/banks/${account.logoImage}" class="img-fluid"
                                 style="filter: ${account.logoFilter}; border-top-left-radius: 0.35rem;" alt="">
                        </div>
                        <div class="col">
                            <div class="card-block px-2" style="padding: 5px">
                                <h4 class="card-title">${account.name} <span class="small text-gray-300">#${account.id}</span>
                                    <a data-toggle="modal" data-target="#accountDeleteModal" data-account-id="${account.id}"
                                       data-toggle="tooltip" title="Delete account" tabindex="0" class="btn btn-outline-danger btn-sm"
                                       style="float: right; margin-left: 5px; margin-right: -2px; margin-top: 2px; border: 0">
                                        <span class="fa fa-trash-alt"></span> Delete
                                    </a>
                                    <a data-toggle="modal" data-target="#accountModal" data-account-id="${account.id}"
                                       data-toggle="tooltip" title="Edit account" tabindex="0" class="btn btn-outline-secondary btn-sm"
                                       style="float: right; margin-left: 5px; margin-right: -2px; margin-top: 2px;  border: 0">
                                        <span class="fa fa-edit"></span> Edit
                                    </a>
                                </h4>
                            </div>
                        </div>
                    </div>
                    <div class="card-footer w-100 h-50 text-muted">
                        <span class="footer-entry">Transactions: <span class="transaction-count" data-account-id="${account.id}">...</span></span>
                        <span class="footer-entry">Imports: <span class="import-count" data-account-id="${account.id}">...</span></span>
                        <span class="footer-entry">Created: <javatime:format value="${account.created}" style="S-" /></span>
                    </div>
                </div>

            <c:if test="${counter.index % 2 != 0}">
                </div>
            </c:if>

            <c:if test="${counter.index % 2 == 0 && counter.index == accountsList.size() - 1}">
                <div class="card" style="margin-bottom: 10px; visibility: hidden"></div></div>
            </c:if>

        </c:forEach>
    </div>

<jsp:include page="/WEB-INF/views/utils/footer.jsp">
    <jsp:param name="additionalScriptFile" value="accounts.js"/>
</jsp:include>
