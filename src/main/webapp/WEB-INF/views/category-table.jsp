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

        function updateColor(categoryId) {
            let color = $('#category_color_' + categoryId).val();
            $.post('${pageContext.request.contextPath}/category/setcolor/' + categoryId, { 'color' : color }, function () {
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
                                            <a tabindex="0" data-toggle="popover" data-trigger="focus" data-html="true" data-category-id="${category.id}"
                                               data-content="<a class='delete-confirm btn btn-sm btn-danger' id=delete-confirm-${category.id}>Delete</a>">
                                                <span class="fa fa-trash-alt"></span>
                                            </a>
                                        </td>

                                            <%-- COLUMN: NAME --%>

                                        <td>

                                            <a data-toggle="modal" data-target="#categoryModal" data-category-id="${category.id}"
                                               data-toggle="tooltip" title="Edit category" tabindex="0"
                                               class="tag" style="background: ${category.color}">
                                                    ${category.name}
                                            </a>
                                        </td>

                                            <%-- COLUMN: DESCRIPTION --%>

                                        <td>
                                            ${category.description}
                                        </td>

                                            <%-- COLUMN: KEYWORDS --%>

                                        <td>
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

<!-- Logout Modal-->
<div class="modal fade" id="logoutModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">Ready to Leave?</h5>
                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
            </div>
            <div class="modal-body">Select "Logout" below if you are ready to end your current session.</div>
            <div class="modal-footer">
                <button class="btn btn-secondary" type="button" data-dismiss="modal">Cancel</button>
                <a class="btn btn-primary" href="login.html">Logout</a>
            </div>
        </div>
    </div>
</div>

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
        $('#categoryModalLabel').text(categoryId ? 'Edit Category' : 'Add New Category');

        $.get(link, function(data) {
            $('#categoryModalBody').html(data);
            $('#categoryModalSubmit').click(function(event) {
                // Find all keyword inputs and merge them before submitting
                let keywords = [];
                $('#keywordList .keyword-text').each(function() {
                    let val = $(this).val();
                    if (val && val !== "")
                        keywords.push(val);
                });
                $('#keywords').val(keywords.join(','));

                // Submit
                $.post(link, $('#categoryModalForm').serialize(), function () {
                    reloadCategoryTable();
                });
            });
        });
    });
</script>

</body>

</html>
