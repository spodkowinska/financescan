<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Categories</title>

    <!-- Custom fonts for this template -->
    <link href="${pageContext.request.contextPath}/vendor/fontawesome-free/css/all.min.css" rel="stylesheet"
          type="text/css">

    <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
          rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="${pageContext.request.contextPath}/css/sb-admin-2.css" rel="stylesheet">

    <!-- Custom styles for this page -->
    <link href="${pageContext.request.contextPath}/vendor/datatables/dataTables.bootstrap4.min.css" rel="stylesheet">

    <link href="${pageContext.request.contextPath}/css/main.css" rel="stylesheet" />

    <style>
        .tag-col {
            padding-top: 0; padding-bottom: 0; vertical-align: middle;
        }
        .tag {
            margin: 0;
            padding: 4px 10px;
            border-radius: 3px;
            color: white;
            font-size: 12px;
        }
        .keyword {
            font-size: 12px;
            background: #EEE;
            font-weight: normal;
            margin-top: 2px;
            margin-bottom: 2px;
            border: 1px solid lightgray;
        }
    </style>

    <script>
        function init() {
            // Enable popovers
            let pops = $('[data-toggle="popover"]')
            pops.popover();
            pops.on('shown.bs.popover', function () {
                let categoryId = $(this).data('category-id');
                // Deletion confirmation popover
                let deleteButton = $('#delete-confirm-' + categoryId);
                deleteButton.css('color', 'white');
                deleteButton.unbind();
                deleteButton.click(function() {
                    deleteCategory(categoryId);
                });
            });
        }

        function deleteCategory(categoryId) {
            console.log(categoryId);
            window.location.href = '${pageContext.request.contextPath}/category/delete/' + categoryId;
        }

        function reloadCategoryTable() {
            console.log('reload');
            window.location.reload();
        }

        // https://stackoverflow.com/a/5624139/1550934
        function hexToRgb(hex) {
            let result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
            return result ? {
                r: parseInt(result[1], 16),
                g: parseInt(result[2], 16),
                b: parseInt(result[3], 16)
            } : null;
        }

        // https://stackoverflow.com/a/1855903/1550934
        function calculateContrastingColor(hex) {
            let rgb = hexToRgb(hex);
            // Counting the perceptive luminance - human eye favors green color...
            let luminance = ( 0.299 * rgb.r + 0.587 * rgb.g + 0.114 * rgb.b)/255;
            return luminance > 0.5 ? '#000000' : '#FFFFFF';
        }

        function updateColor(categoryId) {
            let color = $('#category_color_' + categoryId).val();
            let fontColor = calculateContrastingColor(color);
            $.post('${pageContext.request.contextPath}/category/setcolor/' + categoryId, { 'color' : color, 'fontColor' : fontColor }, function () {
                reloadCategoryTable();
            });
        }
    </script>
</head>

<body id="page-top" onload="init()">

<%-- CATEGORY MODAL --%>
<div class="modal fade" id="categoryModal" tabindex="-1" role="dialog" aria-labelledby="categoryModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header alert-secondary">
                <h5 class="modal-title" id="categoryModalLabel">Edit Category</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" id="categoryModalBody">
                <%-- Filled by AJAX from category-edit.jsp --%>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-primary" data-dismiss="modal" id="categoryModalSubmit">Save</button>
            </div>
        </div>
    </div>
</div>

<%-- CATEGORY DELETE MODAL --%>
<div class="modal fade" id="categoryDeleteModal" tabindex="-1" role="dialog" aria-labelledby="categoryDeleteModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header alert-secondary">
                <h5 class="modal-title" id="categoryDeleteModalLabel">Delete Category
                    <span class="small text-gray-500">#</span><span id="categoryDeleteModal-id" class="small text-gray-500"></span></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" id="categoryDeleteModalBody">
                <span id="categoryDeleteModal-assigned">This category is <b>assigned to <b id="categoryDeleteModal-assignedCount">0</b> transactions</b>.<br><br></span>
                <span id="categoryDeleteModal-noAssigned">This category is not assigned to any transaction.<br><br></span>
                <span id="categoryDeleteModal-unreviewed">This category is <b>suggested for <b id="categoryDeleteModal-unreviewedCount">0</b> transactions</b>.<br><br></span>
                <span id="categoryDeleteModal-noUnreviewed">This category is not suggested for any transaction.<br><br></span>
                <span id="categoryDeleteModal-conclusion-safe">It's safe to remove the category without modifying any transaction.</span>
                <span id="categoryDeleteModal-conclusion-decide">Deleting this category will remove it from all transactions. Are you sure you want to delete it?</span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-danger" data-dismiss="modal" id="categoryDeleteModalSubmit">Delete</button>
            </div>
        </div>
    </div>
</div>

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

                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <h6 class="m-0 font-weight-bold text-gray-800" style="float: left">Categories</h6>
                        <button class="btn btn-secondary btn-sm" style="float: right; margin-bottom: -6px; margin-top: -6px;"
                                data-toggle="modal" data-target="#categoryModal">
                            <span class="fa fa-plus"></span> Add new category
                        </button>
                    </div>
                    <div class="card-body">

                        <table id="category_table" class="finance_table">
                            <!-- TABLE HEADER -->
                            <thead>
                            <tr>
                                <th style="width: 85px">Actions</th>
                                <th style="width: 200px">Name</th>
                                <th style="width: 400px">Description</th>
                                <th>Keywords</th>
                            </tr>
                            </thead>
                            <!-- TABLE DATA -->
                            <tbody id="list">
                                <c:forEach items="${cl}" var="category">
                                    <tr>
                                            <%-- COLUMN: ACTIONS --%>

                                        <td class="actions">
                                            <a data-toggle="modal" data-target="#categoryModal" data-category-id="${category.id}"
                                               data-toggle="tooltip" title="Edit category" tabindex="0">
                                                <span class="fa fa-edit"></span>
                                            </a>
                                            <a data-toggle="tooltip" title="Change category color" tabindex="0" style="cursor: default">
                                                <input type="color" value="${category.color}" style="display: none"
                                                       id="category_color_${category.id}" onchange="updateColor(${category.id})" />
                                                <label for="category_color_${category.id}" class="fa fa-paint-roller" style="margin: 0; cursor: pointer;"></label>
                                            </a>
                                            <a data-toggle="modal" data-target="#categoryDeleteModal" data-category-id="${category.id}"
                                               data-toggle="tooltip" title="Delete category" tabindex="0">
                                                <span class="fa fa-trash-alt"></span>
                                            </a>
                                        </td>

                                            <%-- COLUMN: NAME --%>

                                        <td>

                                            <a data-toggle="modal" data-target="#categoryModal" data-category-id="${category.id}"
                                               data-toggle="tooltip" title="Edit category" tabindex="0"
                                               class="tag" style="background: ${category.color}; color: ${category.fontColor} !important;">
                                                    ${category.name}
                                            </a>
                                        </td>

                                            <%-- COLUMN: DESCRIPTION --%>

                                        <td>
                                            ${category.description}
                                        </td>

                                            <%-- COLUMN: KEYWORDS --%>

                                        <td>
                                            <c:forEach items="${category.safeKeywords}" var="keyword">
                                                <span class="badge badge-light keyword" data-toggle="tooltip" title="Safe keyword">
                                                    <span class="fa fa-sm fa-star text-gray-600"></span> ${keyword}
                                                </span>
                                            </c:forEach>
                                            <c:forEach items="${category.keywords}" var="keyword">
                                                <span class="badge badge-light keyword">${keyword}</span>
                                            </c:forEach>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
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

<script>
    $('#categoryModal').on('show.bs.modal', function(event) {
        // Unbind handlers to avoid situations in which this button has more than one onclick handler
        $('#categoryModalSubmit').unbind();

        let categoryId = $(event.relatedTarget).data('category-id');
        let link = categoryId
            // If categoryId is valid, then we are in edit mode
            ? '${pageContext.request.contextPath}/category/edit/' + categoryId
            // Otherwise we are in creation mode
            : '${pageContext.request.contextPath}/category/add';

        // Update modal's title depending on mode
        $('#categoryModalLabel').html(categoryId ? 'Edit Category <span class="small text-gray-500">#' + categoryId + '</span>' : 'Add New Category');

        $.get(link, function(data) {
            $('#categoryModalBody').html(data);
            $('#categoryModalSubmit').click(function(event) {
                // Find all keyword inputs and merge them before submitting
                let keywords = [], safeKeywords = [];
                $('#keywordList .keyword-text').each(function() {
                    let val = $(this).val();
                    if (val && val !== "") {
                        let keywordNumber = $(this).data('keyword-number');
                        if ($('#safe-keyword-check-' + keywordNumber).is(':checked'))
                            safeKeywords.push(val);
                        else
                            keywords.push(val);
                    }
                });
                $('#keywords').val(keywords.join(','));
                $('#safeKeywords').val(safeKeywords.join(','));

                // Submit
                $.post(link, $('#categoryModalForm').serialize(), function () {
                    reloadCategoryTable();
                });
            });
        });
    });

    $('#categoryDeleteModal').on('show.bs.modal', function(event) {
        // Unbind handlers to avoid situations in which this button has more than one onclick handler
        $('#categoryDeleteModalSubmit').unbind();

        let categoryId = $(event.relatedTarget).data('category-id');

        $('#categoryDeleteModal-id').text(categoryId);

        $.get('${pageContext.request.contextPath}/category/numberoftransactions/' + categoryId, function(data) {
            const transCnt = data.split(',').map(Number);
            const transAssignedCnt = transCnt[0];
            const transPendingCnt = transCnt[1];

            if (transAssignedCnt > 0) {
                $('#categoryDeleteModal-assigned').show();
                $('#categoryDeleteModal-noAssigned').hide();
                $('#categoryDeleteModal-assignedCount').text(transAssignedCnt);
            }
            else {
                $('#categoryDeleteModal-assigned').hide();
                $('#categoryDeleteModal-noAssigned').show();
            }

            if (transPendingCnt > 0) {
                $('#categoryDeleteModal-unreviewed').show();
                $('#categoryDeleteModal-noUnreviewed').hide();
                $('#categoryDeleteModal-unreviewedCount').text(transPendingCnt);
            }
            else {
                $('#categoryDeleteModal-unreviewed').hide();
                $('#categoryDeleteModal-noUnreviewed').show();
            }

            if (transAssignedCnt === 0 && transPendingCnt === 0) {
                $('#categoryDeleteModal-conclusion-safe').show();
                $('#categoryDeleteModal-conclusion-decide').hide();
            }
            else {
                $('#categoryDeleteModal-conclusion-safe').hide();
                $('#categoryDeleteModal-conclusion-decide').show();
            }

            // Submit
            $('#categoryDeleteModalSubmit').click(function(event) {
                $.get('${pageContext.request.contextPath}/category/delete/' + categoryId, function () {
                    reloadCategoryTable();
                });
            });
        });
    });
</script>

</body>

</html>
