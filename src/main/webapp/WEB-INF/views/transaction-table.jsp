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

        var gCategoryFilteringEnabled = false;
        var gUncategorizedCount = 0;
        var gUnreviewedCount = 0;
        
        function init() {
            reloadTransactionTable('all','all');
            gatherSearchableColumnsIds();
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
                    let deleteButton = $('#delete-confirm-' + transId);
                    if (deleteButton) {
                        deleteButton.css('color', 'white');
                        deleteButton.unbind();
                        deleteButton.click(function() {
                            deleteTransaction(transId);
                        });
                    }
                }
                else {
                    // Category confirmation button
                    let catConfirmButton = $('#category-confirm-' + transId + '-' + catId);
                    if (catConfirmButton) {
                        catConfirmButton.css('color', 'white');
                        catConfirmButton.unbind();
                        catConfirmButton.click(function () {
                            addCategory(transId, catId, true);
                        });
                    }
                    // Category rejection button
                    let catRejectButton = $('#category-reject-' + transId + '-' + catId);
                    if (catRejectButton) {
                        catRejectButton.css('color', 'white');
                        catRejectButton.unbind();
                        catRejectButton.click(function () {
                            removeCategory(transId, catId, true);
                        });
                    }
                }
            });

            // This line is needed to prevent category drop-right from disappearing
            $('.tag-add-popover').on("click.bs.dropdown", function (e) { e.stopPropagation(); e.preventDefault(); });
        }

        function reloadTransactionTable(year, month) {
            console.log('reloading transaction table');

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

                // Update sorting
                applySorting();

                // Update text and category filtering
                applyFilters();
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
            if (row)
                row.remove();
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
            applyFilters();
        }

        function changeCategory(transactionId, categoryId) {
            let parentId = $('#cat_tag_' + transactionId + '_' + categoryId).parent().attr('id');

            if (parentId === ('cat_current_' + transactionId))
                removeCategory(transactionId, categoryId);
            else
                addCategory(transactionId, categoryId);
        }

        function addCategory(transactionId, categoryId, pending) {
            $.get("${pageContext.request.contextPath}/transaction/addcategory/" + transactionId + "/" + categoryId, function (data) {
                if (gCategoryFilteringEnabled) {
                    refreshRowForTransaction(transactionId, data);
                }
            });

            let catElem = $('#cat_tag_' + transactionId + '_' + categoryId);
            if (pending) {
                $('#cat_tag_pending_' + transactionId + '_' + categoryId).remove();
                catElem.css('display', 'inline-block');
            }
            else
                catElem.appendTo('#cat_current_' + transactionId);
        }

        function removeCategory(transactionId, categoryId, pending) {
            $.get("${pageContext.request.contextPath}/transaction/removecategory/" + transactionId + "/" + categoryId, function (data) {
                if (gCategoryFilteringEnabled) {
                    refreshRowForTransaction(transactionId, data);
                }
            });

            let catElem = $('#cat_tag_' + transactionId + '_' + categoryId);
            if (pending) {
                $('#cat_tag_pending_' + transactionId + '_' + categoryId).remove();
                catElem.css('display', 'inline-block');
            }
            catElem.appendTo('#cat_others_' + transactionId);
        }

        function applyFilters() {
            const showOnlyUnreviewed = $('#unreviewedCheck').is(':checked');
            const showOnlyUncategorized = $('#uncategorizedCheck').is(':checked');

            gCategoryFilteringEnabled = showOnlyUnreviewed || showOnlyUncategorized;
            gUnreviewedCount = 0;
            gUncategorizedCount = 0;

            const input = document.getElementById("text_filter");
            const filter = input.value.toUpperCase();
            const table = document.getElementById("transaction_table");
            const trs = table.getElementsByTagName("tr");

            // Loop through all table rows, and hide those which don't match the search query
            for (let i = 0; i < trs.length; i++) {
                const tds = trs[i].getElementsByTagName("td");

                if (tds.length === 0)
                    continue;

                let show = true;

                const rowUnreviewed = trs[i].getAttribute('data-unreviewed') === 'true';
                if (rowUnreviewed)
                    gUnreviewedCount++;

                const rowUncategorized = trs[i].getAttribute('data-uncategorized') === 'true';
                if (rowUncategorized)
                    gUncategorizedCount++;

                if (showOnlyUnreviewed || showOnlyUncategorized) {
                    show = showOnlyUnreviewed && rowUnreviewed || showOnlyUncategorized && rowUncategorized;
                }

                if (show) {
                    show = false;

                    for (let j of gSearchableColumnsIds) {
                        const td = tds[j];
                        if (td) {
                            const txtValue = td.getAttribute('sorttable_customkey') || td.textContent || td.innerText;
                            if (txtValue.toUpperCase().indexOf(filter) > -1) {
                                show = true;
                                break;
                            }
                        }
                    }
                }

                trs[i].style.display = show ? "" : "none";
            }

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
    <jsp:include page="sidebar.jsp"></jsp:include>
    <!-- End of Sidebar -->

    <!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column">

        <!-- Main Content -->
        <div id="content">

            <!-- Topbar -->
            <jsp:include page="topbar.jsp"></jsp:include>
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
                                <%-- Filled by AJAX from edit-keyword.jsp --%>
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
                        <h6 class="m-0 font-weight-bold text-gray-800">Transactions</h6>
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
                            <div class="dropdown-menu dropdown-menu-right shadow"
                                 aria-labelledby="transOperationsDropdown">
                                <!-- Dropdown Item: Add new transaction -->
                                <a class="dropdown-item" data-toggle="modal" data-target="#editModal" tabindex="0">
                                    <i class="fas fa-plus mr-2 text-gray-600"></i>
                                    Add new transaction
                                </a>
                                <div class="dropdown-divider"></div>
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
                        </div>

                        <div style="height: 15px"></div>

                        <table id="transaction_table" class="finance_table sortable">
                            <!-- TABLE HEADER -->
                            <thead>
                            <tr>
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

            </div>
            <!-- /.container-fluid -->

        </div>
        <!-- End of Main Content -->

        <!-- Footer -->
        <jsp:include page="footer.jsp"></jsp:include>
        <!-- End of Footer -->

    </div>
    <!-- End of Content Wrapper -->

</div>
<!-- End of Page Wrapper -->

<!-- Scroll to Top Button-->
<a class="scroll-to-top rounded" href="#page-top">
    <i class="fas fa-angle-up"></i>
</a>

<%--<!-- Logout Modal-->--%>
<%--<div class="modal fade" id="logoutModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"--%>
<%--     aria-hidden="true">--%>
<%--    <div class="modal-dialog" role="document">--%>
<%--        <div class="modal-content">--%>
<%--            <div class="modal-header">--%>
<%--                <h5 class="modal-title" id="exampleModalLabel">Ready to Leave?</h5>--%>
<%--                <button class="close" type="button" data-dismiss="modal" aria-label="Close">--%>
<%--                    <span aria-hidden="true">×</span>--%>
<%--                </button>--%>
<%--            </div>--%>
<%--            <div class="modal-body">Select "Logout" below if you are ready to end your current session.</div>--%>
<%--            <div class="modal-footer">--%>
<%--                <button class="btn btn-secondary" type="button" data-dismiss="modal">Cancel</button>--%>
<%--                <a class="btn btn-primary" href="login.html">Logout</a>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</div>--%>



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
        $('#editModalLabel').text(transId ? 'Edit Transaction' : 'Add New Transaction');

        $.get(link, function(data) {
            $('#editModalBody').html(data);
            $('#editModalSubmit').click(function(event) {
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
</script>

</body>

</html>
