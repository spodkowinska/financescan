<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="/WEB-INF/views/utils/header.jsp">
    <jsp:param name="pageTitle" value="Transactions"/>
</jsp:include>

<style>
    <c:forEach items="${categoriesList}" var="category">
    .tag${category.id} {
        background: ${category.color};
        color: ${category.fontColor} !important;
    }
    </c:forEach>
</style>

<%-- TRANSACTION ADD/EDIT MODAL --%>
<div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="editModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header alert-secondary">
                <h5 class="modal-title" id="editModalLabel">Edit Transaction</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" id="editModalBody">
                <%-- Filled by AJAX from transaction-edit.jsp --%>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-primary" data-dismiss="modal" id="editModalSubmit">Save</button>
            </div>
        </div>
    </div>
</div>

<%-- KEYWORD MODAL --%>
<div class="modal fade" id="keywordModal" tabindex="-1" role="dialog" aria-labelledby="keywordModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header alert-secondary">
                <h5 class="modal-title" id="keywordModalLabel">Add Keyword</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" id="keywordModalBody">
                <%-- Filled by AJAX from keyword-add.jsp --%>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-primary" data-dismiss="modal" id="keywordModalSubmit">Save</button>
            </div>
        </div>
    </div>
</div>

<%-- TRANSACTION TABLE --%>
<div class="card shadow mb-4">
    <div class="card-header py-3">
        <h6 class="m-0 font-weight-bold text-gray-800" style="float: left">Transactions</h6>
        <div id="loading-indicator" style="position: absolute; margin-top: -5px; margin-left: 105px;">
            <div class="spinner-border spinner-border-sm text-gray-500" role="status" style="height: 20px; width: 20px">
                <span class="sr-only">Loading...</span>
            </div>
        </div>
        <button class="btn btn-secondary btn-sm" style="float: right; margin-bottom: -6px; margin-top: -6px;" data-toggle="modal" data-target="#editModal"
                data-toggle="modal" data-target="#categoryModal">
            <span class="fa fa-plus"></span> Add new transaction
        </button>
    </div>
    <div class="card-body">

        <%-- YEARS --%>
        <div class="btn-group">
            <button id="btn_year_all" class="btn btn-outline-secondary btn-sm active" onclick="reloadTransactionTable('all', null)">
                ALL
            </button>
            <c:forEach items="${years}" var="year">
                <button id="btn_year_${year}" class="btn btn-outline-secondary btn-sm" onclick="reloadTransactionTable(${year}, null)">
                    ${year}
                </button>
            </c:forEach>
        </div>

        <%-- MONTHS --%>
        <div id="months" class="btn-group" style="display: none">
            <button id="btn_month_all" class="btn btn-outline-secondary btn-sm active" onclick="reloadTransactionTable(null, 'all')">
                ALL
            </button>

            <script>
                let months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];

                for (var i = 0; i < 12; i++) {
                    let monthId = i + 1;
                    document.write("<button id=\"btn_month_" + monthId + "\" class=\"btn btn-outline-secondary btn-sm\" " +
                        "onclick='reloadTransactionTable(null," + monthId + ")'>");
                    document.write(months[i]);
                    document.write("</button>");
                }
            </script>
        </div>

        <!-- Three Dots Dropdown -->
        <div class="btn-group" style="float: right">
            <!-- Three Dots -->
            <a href="#" id="transOperationsDropdown" role="button" data-toggle="dropdown"
               aria-haspopup="true" aria-expanded="true">
                <i class="fas fa-ellipsis-v mr-2 text-gray-600"></i>
            </a>
            <!-- Actual Dropdown -->
            <div class="dropdown-menu dropdown-menu-right shadow stop-propagation" id="transOperationsDropdownMenu"
                 aria-labelledby="transOperationsDropdown">
                <!-- Dropdown Item: Import from CSV -->
                <a class="dropdown-item" href="${pageContext.request.contextPath}/transaction/fileimport">
                    <i class="fas fa-file-upload mr-2 text-gray-600"></i>
                    Import from CSV
                </a>
                <!-- Dropdown Item: CSV Import Settings -->
                <a class="dropdown-item" href="${pageContext.request.contextPath}/csvsettings">
                    <i class="fas fa-cog mr-2 text-gray-600"></i>
                    CSV Import Settings
                </a>
                <div class="dropdown-divider"></div>
                <!-- Dropdown Item: Assign default keywords -->
                <a class="dropdown-item" href="${pageContext.request.contextPath}/transaction/assign">
                    <i class="fas fa-magic mr-2 text-gray-600"></i>
                    Assign default keywords
                </a>
                <div class="dropdown-divider"></div>
                <label class="dropdown-item" style="padding-left: 14px">
                    <div class="custom-control custom-switch">
                        <input type="checkbox" class="custom-control-input" id="bulkEditSwitch">
                        <label class="custom-control-label" for="bulkEditSwitch">
                            <span style="display: inline-block; margin-top: 3px">Bulk edit</span>
                        </label>
                    </div>
                </label>
            </div>
        </div>

        <div class="form-row align-items-center" style="margin-top: 10px;">
            <div class="col-auto">

                <!-- Filter: text -->
                <div class="input-group input-group-sm" style="width: 300px">
                    <input type="text" class="form-control" placeholder="Filter by description..."
                           aria-label="Filter by description..." aria-describedby="basic-addon2"
                           id="text_filter" onkeyup="applyFilters()">
                </div>

            </div>
            <div class="col-auto">

                <!-- Filter: account -->
                <div class="input-group input-group-sm">
                    <select id="account_filter" class="custom-select" onchange="applyFilters()">
                        <option value="0">All accounts</option>
                        <option disabled>&#9472;</option>
                        <c:forEach items="${bl}" var="account">
                            <option value="${account.id}">${account.name}</option>
                        </c:forEach>
                    </select>
                </div>

            </div>
            <div class="col-auto">

                <!-- Filter: unreviewed category suggestions -->
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" id="unreviewedCheck"
                           style="margin-top: 6px" onclick="applyFilters()">
                    <label class="form-check-label small" for="unreviewedCheck">
                        Unreviewed categories
                        <span class="badge badge-pill badge-danger" id="unreviewedCount"></span>
                    </label>
                </div>

            </div>
            <div class="col-auto">

                <!-- Filter: uncategoried  -->
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" id="uncategorizedCheck"
                           style="margin-top: 6px" onclick="applyFilters()">
                    <label class="form-check-label small" for="uncategorizedCheck">
                        No categories
                        <span class="badge badge-pill badge-danger" id="uncategorizedCount"></span>
                    </label>
                </div>

            </div>
            <div class="col-auto bulk-controls">

                <!-- Filter: only selected  -->
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" id="onlySelectedCheck"
                           style="margin-top: 6px" onclick="applyFilters()">
                    <label class="form-check-label small" for="onlySelectedCheck">
                        Only selected
                        <span class="badge badge-pill badge-secondary" id="onlySelectedCount"></span>
                    </label>
                </div>

            </div>
            <div class="col-auto" id="filterRefresh" style="display: none">

                <!-- Button: refresh filters  -->
                <button class="btn btn-sm btn-outline-secondary py-0" style="font-size: 0.8em;" id="filterRefreshButton">
                    <span class="fa fa-sync-alt fa-sm"></span> Refresh filters
                </button>

            </div>
        </div>

        <div style="height: 15px"></div>

        <table id="transaction_table" class="finance_table sortable">
            <!-- TABLE HEADER -->
            <thead>
                <tr>
                    <th style="width: 20px" class="sorttable_nosort bulk-controls"><input class="form-check" type="checkbox" id="master-check" onchange="toggleSelectionForAllVisibleRows()"></th>
                    <th style="width: 85px" class="sorttable_nosort">Actions</th>
                    <th style="width: 100px" data-searchable="true">Date</th>
                    <th style="width: 100px" data-searchable="true" class="sorttable_numeric">Amount</th>
                    <th colspan="2" style="width: 250px" class="sorttable_nosort">Categories</th>
                    <th data-searchable="true" class="sorttable_nosort">Description</th>
                    <th style="width: 18px" class="sorttable_nosort" data-toggle="tooltip" title="Account">
                        <i class="fa fa-sm fa-piggy-bank "></i>
                    </th>
                </tr>
            </thead>
            <!-- TABLE DATA -->
            <tbody id="list">
                <%-- Filled by transaction-table-rows.jsp --%>
            </tbody>
        </table>
    </div>
</div>

<%-- BULK EDIT CONTEXT MENU --%>
<div id="bulkMenu" class="dropdown-menu shadow shadow-sm" style="display: none; position: absolute; padding: 10px; width: 250px">
    <h6 class="dropdown-header" style="padding: 0; margin-bottom: 5px;">
        Selected transactions: <span id="bulkMenuCount"></span>
        <button id="bulkMenu-deselectAll" style="font-size: 0.8em; margin-top: -2px; float: right;" class="btn btn-sm btn-secondary py-0">Unselect</button>
    </h6>
    <div class="dropdown-divider"></div>
    <div>
        <%-- Bulk Menu: Main Page --%>
        <div id="bulkMenu-mainPage">
            <%-- Add Category --%>
            <a class="dropdown-item" tabindex="0" onclick="changeBulkMenuPage('#bulkMenu-addCategoriesPage')" style="padding: 0">
                <i class="fas fa-plus-square mr-2 text-gray-600"></i> Add category...
            </a>
            <%-- Remove Category --%>
            <a class="dropdown-item" tabindex="0" onclick="changeBulkMenuPage('#bulkMenu-removeCategoriesPage')" style="padding: 0">
                <i class="fas fa-minus-square mr-2 text-gray-600"></i> Remove category...
            </a>
            <div class="dropdown-divider"></div>
            <%-- Accept All Category Suggestions --%>
            <a id="bulkMenu-acceptAllCats" class="dropdown-item" tabindex="0" style="padding: 0">
                <i class="fas fa-plus-circle mr-2 text-gray-600"></i> Accept all suggested categories
            </a>
            <%-- Reject All Category Suggestions --%>
            <a id="bulkMenu-rejectAllCats" class="dropdown-item" tabindex="0" style="padding: 0">
                <i class="fas fa-minus-circle mr-2 text-gray-600"></i> Reject all suggested categories
            </a>
            <div class="dropdown-divider"></div>
            <%-- Change Account --%>
            <a class="dropdown-item" tabindex="0" onclick="changeBulkMenuPage('#bulkMenu-changeAccountPage')" style="padding: 0">
                <i class="fas fa-piggy-bank mr-2 text-gray-600"></i> Change account...
            </a>
            <div class="dropdown-divider"></div>
            <%-- Delete Transactions --%>
            <a class="dropdown-item" tabindex="0" style="padding: 0" data-toggle="popover" data-trigger="focus" data-html="true"
               data-content="<a class='btn btn-sm btn-danger delete-confirm' id='delete-confirm-selected'>Delete</a>">
                <i class="fas fa-trash-alt mr-2 text-gray-600"></i> Delete <span id="bulkMenuCount2"></span> transaction(s)
            </a>
        </div>
        <%-- Bulk Menu: Add Categories Page --%>
        <div id="bulkMenu-addCategoriesPage" style="display: none">
            <a class="dropdown-item" onclick="changeBulkMenuPage('#bulkMenu-mainPage')" style="padding: 0">
                <i class="fas fa-chevron-left mr-2 text-gray-600"></i> Add category
            </a>
            <div class="dropdown-divider"></div>
            <div style="text-align: center">
                <c:forEach items="${categoriesList}" var="category">
                    <a tabindex="0" class="tag tag${category.id} tag-add-to-selected" data-category-id="${category.id}">
                        ${category.name}
                    </a>
                </c:forEach>
            </div>
        </div>
        <%-- Bulk Menu: Remove Categories Page --%>
        <div id="bulkMenu-removeCategoriesPage" style="display: none">
            <a class="dropdown-item" onclick="changeBulkMenuPage('#bulkMenu-mainPage')" style="padding: 0">
                <i class="fas fa-chevron-left mr-2 text-gray-600"></i> Remove category
            </a>
            <div class="dropdown-divider"></div>
            <div style="text-align: center">
                <c:forEach items="${categoriesList}" var="category">
                    <a tabindex="0" class="tag tag${category.id} tag-remove-from-selected" data-category-id="${category.id}">
                        ${category.name}
                    </a>
                </c:forEach>
            </div>
            <div class="dropdown-divider"></div>
            <a class="dropdown-item tag-remove-from-selected" tabindex="0" style="padding: 0">
                <i class="fas fa-minus-square mr-2 text-gray-600"></i> Remove all
            </a>
        </div>
        <%-- Bulk Menu: Change Account Page --%>
        <div id="bulkMenu-changeAccountPage" style="display: none">
            <a class="dropdown-item" onclick="changeBulkMenuPage('#bulkMenu-mainPage')" style="padding: 0">
                <i class="fas fa-chevron-left mr-2 text-gray-600"></i> Change account
            </a>
            <div class="dropdown-divider"></div>
            <div style="text-align: center">
                <select class="custom-select">
                    <c:forEach items="${bl}" var="account">
                        <option value="${account.id}">${account.name}</option>
                    </c:forEach>
                </select>
                <a tabindex="0" style="font-size: 0.8em; margin-top: 2px;" class="btn btn-sm btn-outline-secondary py-0">
                    Change
                </a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/utils/footer.jsp">
    <jsp:param name="additionalScriptFile" value="transactions.js"/>
</jsp:include>
