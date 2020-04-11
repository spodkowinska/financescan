<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Transactions</title>

    <!-- Custom fonts for this template -->
    <link href="${pageContext.request.contextPath}/vendor/fontawesome-free/css/all.min.css" rel="stylesheet"
          type="text/css">
    <!--datatables-->
    <link href="http://cdn.datatables.net/1.10.20/css/jquery.dataTables.min.css"
          rel="stylesheet" type="text/css">

    <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
          rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="${pageContext.request.contextPath}/css/sb-admin-2.css" rel="stylesheet">

    <!-- Custom styles for this page -->
    <link href="${pageContext.request.contextPath}/vendor/datatables/dataTables.bootstrap4.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/vanillaSelectBox.css">

    <script src="${pageContext.request.contextPath}/js/sorttable.js"></script>

    <link href="${pageContext.request.contextPath}/css/main.css" rel="stylesheet" />

    <script>
        var gSearchableColumnsIds = [];

        var gMonth = "all";
        var gYear = "all";

        var gUncategorizedCount = 0;
        var gUnreviewedCount = 0;
        var gSelectedCount = 0;
        var gBulkEditEnabled = false;
        
        function init() {
            reloadTransactionTable('all','all');
            gatherSearchableColumnsIds();

            $(document).on('click', '.stop-propagation', function (e) {
                e.stopPropagation();
            })

            $('#bulkEditSwitch').change(function () {
                gBulkEditEnabled = $(this).is(':checked');
                if (gBulkEditEnabled){
                    $('.bulk-controls').show();
                }
                else {
                    $('.bulk-controls').hide();
                    clearSelection();
                    $('#onlySelectedCheck').prop('checked', false);
                    applyFilters();
                }
            });
        }
        
        function gatherSearchableColumnsIds() {
            gSearchableColumnsIds = [];

            let table = document.getElementById("transaction_table");
            let firstRow = table.getElementsByTagName("tr")[0];
            let ths = firstRow.getElementsByTagName("th");
            var offset = 0;

            for (var i = 0; i < ths.length; i++) {
                let th = ths[i];
                if (th) {
                    if (th.getAttribute('data-searchable') === 'true')
                        gSearchableColumnsIds.push(i + offset);
                    let colspan = th.getAttribute('colspan');
                    if (colspan) {
                        offset += parseInt(colspan) - 1;
                    }
                }
            }
        }

        function enablePopovers(pops) {
            pops.popover();
            pops.on('shown.bs.popover', function () {
                let transId = $(this).data('transaction-id');
                let catId = $(this).data('category-id');

                if (!catId) {
                    // Deletion confirmation popover
                    if (transId) {
                        // Single transaction deletion
                        let deleteButton = $('#delete-confirm-' + transId);
                        if (deleteButton) {
                            deleteButton.unbind();
                            deleteButton.click(function() {
                                deleteTransaction(transId);
                            });
                        }
                    }
                    else {
                        // Selected transactions deletion
                        const deleteButton = $('#delete-confirm-selected');
                        if (deleteButton) {
                            deleteButton.unbind();
                            deleteButton.click(function() {
                                deleteSelectedTransactions();
                                $('#bulkMenu').hide();
                            });
                        }
                    }
                }
                else {
                    // Category confirmation button
                    let catConfirmButton = $('#category-confirm-' + transId + '-' + catId);
                    if (catConfirmButton) {
                        catConfirmButton.css('color', 'white');
                        catConfirmButton.unbind();
                        catConfirmButton.click(function () {
                            addCategory(transId, catId);
                        });
                    }
                    // Category rejection button
                    let catRejectButton = $('#category-reject-' + transId + '-' + catId);
                    if (catRejectButton) {
                        catRejectButton.css('color', 'white');
                        catRejectButton.unbind();
                        catRejectButton.click(function () {
                            removeCategory(transId, catId);
                        });
                    }
                }
            });

            // This line is needed to prevent category drop-right from disappearing
            $('.tag-add-popover').on("click.bs.dropdown", function (e) { e.stopPropagation(); e.preventDefault(); });
        }

        function enableLoadingEffect(enable) {
            if (enable) {
                $('#loading-indicator').show();
                $('#list').css('filter', 'grayscale(1) opacity(0.5)');
            }
            else {
                $('#loading-indicator').hide();
                $('#list').css('filter', '');
            }
        }

        function prepareRows() {
            $('.finance_table tbody tr').contextmenu(function(e) {
                if (!$(this).hasClass('selected'))
                    return false;
                const top = e.pageY;
                const left = e.pageX;
                changeBulkMenuPage('#bulkMenu-mainPage')
                $('#bulkMenu').css({
                    display: 'block',
                    top: top,
                    left: left
                });
                $('#bulkMenuCount').text(gSelectedCount);
                $('#bulkMenuCount2').text(gSelectedCount);
                return false;
            }).click(function () {
                $('#bulkMenu').hide();
            });
            $('.transaction-row-checkbox').change(function () {
                const row = $(this).parent().parent();
                const checked = $(this).is(':checked');
                row.toggleClass('selected', checked);
                gSelectedCount += checked ? 1 : -1;
                refreshFilterBadges();
            });
            if (gBulkEditEnabled)
                $('.bulk-controls').show();
        }

        function toggleSelectionForAllVisibleRows() {
            const checked = $('#master-check').is(':checked');
            const selectedMod = checked ? 1 : -1;
            $('.transaction-row-checkbox').each(function () {
                if (!$(this).is(':hidden') && $(this).is(':checked') !== checked) {
                    $(this).prop('checked', checked);
                    $(this).parent().parent().toggleClass('selected', checked);
                    gSelectedCount += selectedMod;
                }
            });
            refreshFilterBadges();
        }

        function clearSelection() {
            $('#master-check').prop('checked', false);
            $('.transaction-row-checkbox').each(function () {
                $(this).prop('checked', false);
                $(this).parent().parent().removeClass('selected');
            });
            gSelectedCount = 0;
            refreshFilterBadges();
        }

        function gatherSelectedRows() {
            const selectedRows = [];
            $('.transaction-row-checkbox').each(function () {
                if ($(this).is(':checked'))
                    selectedRows.push($(this).parent().parent());
            });
            return selectedRows;
        }

        function reloadTransactionTable(year, month) {
            console.log('reloading transaction table');

            enableLoadingEffect(true);

            if (year !== null) {
                gYear = year;
            }
            if (month !== null) {
                gMonth = month;
            }

            $.get("${pageContext.request.contextPath}/transaction/table/" + gYear + "/" + gMonth, function (data) {
                $('#list').html(data);

                // Enable popovers
                enablePopovers($('[data-toggle="popover"]'));

                // Some rows maintenance
                prepareRows();

                // Update sorting
                applySorting();

                // Update text and category filtering
                applyFilters();

                enableLoadingEffect(false);
            });

            // Update year & month buttons state
            $('#btn_year_' + gYear).addClass('active').siblings().removeClass('active');

            if (gYear === 'all') {
                // Hide months if all years are displayed
                $('#months').hide('fast');
            }
            else {
                // Show months if only a specific year is displayed
                $('#months').show('fast');

                // Activate only current month button if a specific year is displayed
                $('#btn_month_' + gMonth).addClass('active').siblings().removeClass('active');
            }
        }

        function deleteTransaction(transId) {
            $.get('${pageContext.request.contextPath}/transaction/delete/' + transId);
            let row = $('#cat_row_' + transId);
            if (row.length) {
                if (row.hasClass('selected'))
                    gSelectedCount--;
                if (row.data('uncategorized'))
                    gUncategorizedCount--;
                if (row.data('unreviewed'))
                    gUnreviewedCount--;
                row.remove();
                refreshFilterBadges();
            }
        }

        function deleteSelectedTransactions() {
            gatherSelectedRows().forEach(function(row) {
                deleteTransaction(row.data('transaction-id'));
            });
        }

        function refreshRowForTransaction(transactionId, preloadedData){
            if (preloadedData) {
                $('#cat_row_' + transactionId).replaceWith(preloadedData);
                afterRowRefreshed(transactionId);
            }
            else {
                $.get("${pageContext.request.contextPath}/transaction/gettransaction/" + transactionId, function (data) {
                    $('#cat_row_' + transactionId).replaceWith(data);
                    afterRowRefreshed(transactionId);
                })
            }
        }

        function afterRowRefreshed(transactionId) {
            enablePopovers($('.popover-button-' + transactionId));
            prepareRows();
            applyFilters();
        }

        function selectTransaction(transRowId, event) {
            if (!gBulkEditEnabled)
                return;

            // Ignoring all clicks in delete, edit, categories, etc. Only <td> clicks should pass here.
            if (event.target.tagName.toUpperCase() === 'TD') {
                const row = $('#' + transRowId);

                // Toggle the checkbox
                const checkbox = row.find('input');
                checkbox.trigger('click');
            }
        }

        function changeCategory(transactionId, categoryId) {
            let parentId = $('#cat_tag_' + transactionId + '_' + categoryId).parent().attr('id');

            if (parentId === ('cat_current_' + transactionId))
                removeCategory(transactionId, categoryId);
            else
                addCategory(transactionId, categoryId);
        }

        function addCategory(transactionId, categoryId) {
            $.get("${pageContext.request.contextPath}/transaction/addcategory/" + transactionId + "/" + categoryId, function (data) {
                afterCategoriesChangedForRow(transactionId, data);
            });

            const catElem = $('#cat_tag_' + transactionId + '_' + categoryId);
            const pendingElem = $('#cat_tag_pending_' + transactionId + '_' + categoryId);
            if (pendingElem.length) {
                pendingElem.remove();
                catElem.css('display', 'inline-block');
            }
            else
                catElem.appendTo('#cat_current_' + transactionId);
        }

        function removeAllCategories(transactionId) {
            $.get("${pageContext.request.contextPath}/transaction/removeallcategories/" + transactionId, function (data) {
                afterCategoriesChangedForRow(transactionId, '0,0');
            });

            const children = $('#cat_current_' + transactionId).children();
            children.filter(':visible').show().appendTo('#cat_others_' + transactionId);
            children.remove();
        }

        function removeCategory(transactionId, categoryId) {
            $.get("${pageContext.request.contextPath}/transaction/removecategory/" + transactionId + "/" + categoryId, function (data) {
                afterCategoriesChangedForRow(transactionId, data);
            });

            let catElem = $('#cat_tag_' + transactionId + '_' + categoryId);
            const pendingElem = $('#cat_tag_pending_' + transactionId + '_' + categoryId);
            if (pendingElem.length) {
                pendingElem.remove();
                catElem.css('display', 'inline-block');
            }
            catElem.appendTo('#cat_others_' + transactionId);
        }

        function afterCategoriesChangedForRow(transactionId, responseData) {
            const catCnt = responseData.split(",").map(Number);
            const catRowUncategorized = catCnt[0] === 0 && catCnt[1] === 0;
            const catRowUnreviewed = catCnt[1] !== 0;

            const catRow = $('#cat_row_' + transactionId);

            // Uncategorized
            {
                const catRowWasUncategorized = catRow.data('uncategorized');
                if (catRowUncategorized) {
                    if (!catRowWasUncategorized) {
                        catRow.data('uncategorized', true);
                        gUncategorizedCount++;
                    }
                }
                else if (catRowWasUncategorized) {
                    catRow.data('uncategorized', false);
                    gUncategorizedCount--;
                }
            }

            // Unreviewed
            {
                const catRowWasUnreviewed = catRow.data('unreviewed');
                if (catRowUnreviewed) {
                    if (!catRowWasUnreviewed) {
                        catRow.data('unreviewed', true);
                        gUnreviewedCount++;
                    }
                }
                else if (catRowWasUnreviewed) {
                    catRow.data('unreviewed', false);
                    gUnreviewedCount--;
                }
            }

            refreshFilterBadges();
        }

        function applyFilters() {
            const showOnlyUnreviewed = $('#unreviewedCheck').is(':checked');
            const showOnlyUncategorized = $('#uncategorizedCheck').is(':checked');
            const showOnlySelected = $('#onlySelectedCheck').is(':checked');

            gUnreviewedCount = 0;
            gUncategorizedCount = 0;
            gSelectedCount = 0;

            const filter = $('#text_filter').val().toUpperCase();
            const rows = $('#list tr');

            // Loop through all table rows, and hide those which don't match the search query
            rows.each(function() {
                const row = $(this);
                const cells = row.children('td');

                if (cells.length === 0)
                    return;

                let show = true;

                const rowUnreviewed = row.data('unreviewed');
                if (rowUnreviewed) gUnreviewedCount++;

                const rowUncategorized = row.data('uncategorized');
                if (rowUncategorized) gUncategorizedCount++;

                const rowSelected = row.hasClass('selected');
                if (rowSelected) gSelectedCount++;

                if (showOnlySelected && !rowSelected) {
                    show = false;
                }
                else if (showOnlyUnreviewed || showOnlyUncategorized) {
                    show = showOnlyUnreviewed && rowUnreviewed || showOnlyUncategorized && rowUncategorized;
                }

                if (show) {
                    show = false;

                    for (const j of gSearchableColumnsIds) {
                        const cell = cells.eq(j);
                        if (cell) {
                            const txtValue = cell.attr('sorttable_customkey') || cell.val() || cell.text();
                            if (txtValue.toUpperCase().indexOf(filter) > -1) {
                                show = true;
                                break;
                            }
                        }
                    }
                }

                row.toggle(show);
            });

            refreshFilterBadges();
        }

        function refreshSelectionBadge() {
            let selectedCount = $('#onlySelectedCount');
            selectedCount.text(gSelectedCount);
        }

        function refreshFilterBadges() {
            let unreviewedCount = $('#unreviewedCount');
            unreviewedCount.text(gUnreviewedCount);
            if (gUnreviewedCount === 0) {
                unreviewedCount.removeClass('badge-danger');
                unreviewedCount.addClass('badge-secondary');
            }
            else {
                unreviewedCount.removeClass('badge-secondary');
                unreviewedCount.addClass('badge-danger');
            }

            let uncategorizedCount = $('#uncategorizedCount');
            uncategorizedCount.text(gUncategorizedCount);
            if (gUncategorizedCount === 0) {
                uncategorizedCount.removeClass('badge-danger');
                uncategorizedCount.addClass('badge-secondary');
            }
            else {
                uncategorizedCount.removeClass('badge-secondary');
                uncategorizedCount.addClass('badge-danger');
            }

            refreshSelectionBadge();
        }

        function applySorting() {
            // Get all <th>s from the transaction table
            $('table#transaction_table thead tr th').each(function() {
                // If the <th> has 'sorttable_sorted' then it was sorted.
                // We have to remove the class first to allow sorting on new data.
                if ($(this).hasClass('sorttable_sorted')) {
                    $(this).removeClass('sorttable_sorted');
                    sorttable.innerSortFunction.apply($(this)[0], []);
                }
                // If the <th> has 'sorttable_sorted_reverse' then it was sorted in reversed order.
                // We have to remove the class first to allow sorting on new data.
                // We have to sort TWICE to achieve reversed order (first is standard).
                // Fortunately the second sort is only a reverse - no actual sorting is performed.
                else if ($(this).hasClass('sorttable_sorted_reverse')) {
                    $(this).removeClass('sorttable_sorted_reverse');
                    sorttable.innerSortFunction.apply($(this)[0], []);
                    sorttable.innerSortFunction.apply($(this)[0], []);
                }
            });
        }

        function changeBulkMenuPage(pageId) {
            const pageDiv = $(pageId);
            pageDiv.siblings().hide();
            pageDiv.show();
        }
    </script>

    <style>
        <c:forEach items="${categoriesList}" var="category">
            .tag${category.id} {
                background: ${category.color};
                color: ${category.fontColor} !important;
            }
        </c:forEach>
    </style>
</head>

<body id="page-top" onload="init()">

<!-- Page Wrapper -->
<div id="wrapper">

    <!-- Sidebar -->
    <jsp:include page="/WEB-INF/views/sidebar.jsp"></jsp:include>
    <!-- End of Sidebar -->

    <!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column">

        <!-- Main Content -->
        <div id="content">

            <!-- Topbar -->
            <jsp:include page="/WEB-INF/views/topbar.jsp"></jsp:include>
            <!-- End of Topbar -->

            <!-- Begin Page Content -->
            <div class="container-fluid">

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
                        <div id="months" class="btn-group">
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
                                    <div class="input-group-append">
                                        <button class="btn btn-outline-secondary" type="button">Filter</button>
                                    </div>
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
                    <h6 class="dropdown-header" style="padding: 0; margin-bottom: 5px;">Bulk changing <span id="bulkMenuCount"></span> transaction(s)</h6>
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
                            <a class="dropdown-item" href="#" style="padding: 0">
                                <i class="fas fa-plus-circle mr-2 text-gray-600"></i> Accept all suggested categories
                            </a>
                            <%-- Reject All Category Suggestions --%>
                            <a class="dropdown-item" href="#" style="padding: 0">
                                <i class="fas fa-minus-circle mr-2 text-gray-600"></i> Reject all suggested categories
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
                    </div>
                </div>

            </div>
            <!-- /.container-fluid -->

        </div>
        <!-- End of Main Content -->

        <!-- Footer -->
        <jsp:include page="/WEB-INF/views/footer.jsp"></jsp:include>
        <!-- End of Footer -->

    </div>
    <!-- End of Content Wrapper -->

</div>
<!-- End of Page Wrapper -->

<!-- Scroll to Top Button-->
<a class="scroll-to-top rounded" href="#page-top">
    <i class="fas fa-angle-up"></i>
</a>

<!-- Bootstrap core JavaScript-->
<script src="${pageContext.request.contextPath}/vendor/jquery/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

<!-- Core plugin JavaScript-->
<script src="${pageContext.request.contextPath}/vendor/jquery-easing/jquery.easing.min.js"></script>

<!-- Custom scripts for all pages-->
<script src="${pageContext.request.contextPath}/js/sb-admin-2.min.js"></script>

<!-- Page level plugins -->
<script src="${pageContext.request.contextPath}/vendor/datatables/jquery.dataTables.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/datatables/dataTables.bootstrap4.min.js"></script>

<!-- Page level custom scripts -->
<script src="${pageContext.request.contextPath}/js/demo/datatables-demo.js"></script>
<!--datatables-->
<script src="http://cdn.datatables.net/1.10.20/js/jquery.dataTables.min.js"></script>

<script>
    $('#editModal').on('show.bs.modal', function(event) {
        // Unbind handlers to avoid situations in which this button has more than one onclick handler
        $('#editModalSubmit').unbind();

        let transId = $(event.relatedTarget).data('transaction-id');

        let link = transId
            // If transId is valid, then we are in edit mode
            ? '${pageContext.request.contextPath}/transaction/edit/' + transId
            // Otherwise we are in creation mode
            : '${pageContext.request.contextPath}/transaction/add';

        // Update modal's title depending on mode
        $('#editModalLabel').html(transId ? 'Edit Transaction <span class="small text-gray-500">#' + transId + '</span>' : 'Add New Transaction');

        $.get(link, function(data) {
            $('#editModalBody').html(data);
            $('#editModalSubmit').click(function(event) {
                $('#categories').val(gModalCategories.join(','));
                $('#pendingCategories').val(gModalPendingCategories.join(','));
                $('#rejectedCategories').val(gModalRejectedCategories.join(','));

                $.post(link, $('#editModalForm').serialize(), function(newRowData) {
                    if (transId) {
                        // Edit mode: update the edited row
                        $('#cat_row_' + transId).replaceWith(newRowData);
                        afterRowRefreshed(transId);
                    }
                    else {
                        // Add mode: update whole table
                        reloadTransactionTable(null, null);
                    }
                });
            });
        });
    });

    $('#keywordModal').on('show.bs.modal', function(event) {
        // Unbind handlers to avoid situations in which this button has more than one onclick handler
        $('#keywordModalSubmit').unbind();

        let transId = $(event.relatedTarget).data('transaction-id');
        let getLink = '${pageContext.request.contextPath}/category/keyword/add/' + transId;
        let postLink = '${pageContext.request.contextPath}/category/keyword/add';

        $.get(getLink, function(data) {
            $('#keywordModalBody').html(data);
            $('#keywordModalSubmit').click(function(event) {
                $.post(postLink, $('#keywordModalForm').serialize());
            });
        });
    });

    $('.tag-add-to-selected').click(function () {
        const categoryId = $(this).data('category-id');
        gatherSelectedRows().forEach(function(row) {
            addCategory(row.data('transaction-id'), categoryId);
        })
    });

    $('.tag-remove-from-selected').click(function () {
        const categoryId = $(this).data('category-id');
        if (categoryId) {
            gatherSelectedRows().forEach(function(row) {
                removeCategory(row.data('transaction-id'), categoryId);
            });
        }
        else {
            gatherSelectedRows().forEach(function(row) {
                removeAllCategories(row.data('transaction-id'));
            });
        }
    });

</script>

</body>

</html>