<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Projects</title>

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

    <link href="${pageContext.request.contextPath}/css/main.css" rel="stylesheet" />

</head>

<body id="page-top">

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

<%-- PROJECT MODAL --%>
<div class="modal fade" id="projectModal" tabindex="-1" role="dialog" aria-labelledby="projectModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header alert-secondary">
                <h5 class="modal-title" id="projectModalLabel">Edit Project</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" id="projectModalBody">
                <%-- Filled by AJAX from project-edit.jsp --%>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-primary" data-dismiss="modal" id="projectModalSubmit">Save</button>
            </div>
        </div>
    </div>
</div>

<%-- PROJECT DELETE MODAL --%>
<div class="modal fade" id="projectDeleteModal" tabindex="-1" role="dialog" aria-labelledby="projectDeleteModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header alert-secondary">
                <h5 class="modal-title" id="projectDeleteModalLabel">Delete project
                    <span class="small text-gray-500">#</span><span id="projectDeleteModal-id" class="small text-gray-500"></span></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" id="projectDeleteModalBody">
                This operation will <b style="color: red">delete all transactions, categories and accounts</b> related to this project.<br><br>
                ARE YOU SURE YOU WANT TO DELETE ALL THIS DATA?
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-danger" data-dismiss="modal" id="projectDeleteModalSubmit">Delete project and all its contents</button>
            </div>
        </div>
    </div>
</div>

<!-- Page Wrapper -->
<div id="wrapper">

    <!-- Sidebar -->
    <jsp:include page="/WEB-INF/views/utils/sidebar.jsp" />
    <!-- End of Sidebar -->

    <!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column">

        <!-- Main Content -->
        <div id="content">

            <!-- Topbar -->
            <jsp:include page="/WEB-INF/views/utils/topbar.jsp" />
            <!-- End of Topbar -->

            <!-- Begin Page Content -->
            <div class="container-fluid">

                <!-- DataTales Example -->
                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <h6 class="m-0 font-weight-bold text-gray-800" style="float: left">Projects</h6>
                        <button class="btn btn-secondary btn-sm" style="float: right; margin-bottom: -6px; margin-top: -6px;"
                                data-toggle="modal" data-target="#projectModal">
                            <span class="fa fa-plus"></span> Add new project
                        </button>
                    </div>
                    <div class="card-body">
                        <c:forEach items="${projectsList}" var="project" step="1" varStatus="counter" begin="0">

                            <c:if test="${counter.index % 2 == 0}">
                                <div class="card-deck">
                            </c:if>

                                <div class="card" style="margin-bottom: 10px">
                                    <div class="row no-gutters">
                                        <div class="col">
                                            <div class="card-block px-2" style="padding: 5px">
                                                <h4 class="card-title">${project.name} <span class="small text-gray-300">#${project.id}</span>
                                                    <a data-toggle="modal" data-target="#projectDeleteModal" data-project-id="${project.id}"
                                                       data-toggle="tooltip" title="Delete Project" tabindex="0" class="btn btn-outline-danger btn-sm"
                                                       style="float: right; margin-left: 5px; margin-right: -2px; margin-top: 2px; border: 0">
                                                        <span class="fa fa-trash-alt"></span> Delete
                                                    </a>
                                                    <a data-toggle="modal" data-target="#projectModal" data-project-id="${project.id}"
                                                       data-toggle="tooltip" title="Edit Project" tabindex="0" class="btn btn-outline-secondary btn-sm"
                                                       style="float: right; margin-left: 5px; margin-right: -2px; margin-top: 2px;  border: 0">
                                                        <span class="fa fa-edit"></span> Edit
                                                    </a>
                                                </h4>
                                                ${project.description}
                                            </div>
                                        </div>
                                    </div>
<%--                                    <div class="card-footer w-100 h-50 text-muted">--%>
<%--                                        <span class="footer-entry">Transactions: <span class="transaction-count" data-project-id="${project.id}">...</span></span>--%>
<%--                                        <span class="footer-entry">Imports: <span class="import-count" data-project-id="${project.id}">...</span></span>--%>
<%--                                        <span class="footer-entry">Created: <javatime:format value="${project.created}" style="S-" /></span>--%>
<%--                                    </div>--%>
                                </div>

                            <c:if test="${counter.index % 2 != 0}">
                                </div>
                            </c:if>

                            <c:if test="${counter.index % 2 == 0 && counter.index == projectsList.size() - 1}">
                                <div class="card" style="margin-bottom: 10px; visibility: hidden"></div></div>
                            </c:if>

                        </c:forEach>
                    </div>

                </div>
                <!-- /.container-fluid -->

            </div>
        </div>

        <!-- End of Main Content -->

        <!-- Footer -->
        <jsp:include page="/WEB-INF/views/utils/footer.jsp" />
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
    $('#projectModal').on('show.bs.modal', function(event) {
        // Unbind handlers to avoid situations in which this button has more than one onclick handler
        $('#projectModalSubmit').unbind();

        let projectId = $(event.relatedTarget).data('project-id');
        let link = projectId
            // If projectId is valid, then we are in edit mode
            ? '${pageContext.request.contextPath}/project/edit/' + projectId
            // Otherwise we are in creation mode
            : '${pageContext.request.contextPath}/project/add';

        // Update modal's title depending on mode
        $('#projectModalLabel').html(projectId ? 'Edit Project <span class="small text-gray-500">#' + projectId + '</span>' : 'Add New Project');

        $.get(link, function(data) {
            $('#projectModalBody').html(data);
            $('#projectModalSubmit').click(function(event) {
                // Submit
                $.post(link, $('#projectModalForm').serialize(), function () {
                    window.location.reload();
                });
            });
        });
    });

    $('#projectDeleteModal').on('show.bs.modal', function(event) {
        // Unbind handlers to avoid situations in which this button has more than one onclick handler
        const submitButton = $('#projectDeleteModalSubmit');
        submitButton.unbind();

        let projectId = $(event.relatedTarget).data('project-id');

        $('#projectDeleteModal-id').text(projectId);

        $.get('${pageContext.request.contextPath}/project/numberoftransactions/' + projectId, function(data) {
            $('#projectDeleteModalBody').children().hide();

            let deletePossible = true;

            const transCnt = parseInt(data);
            if (data && transCnt === 1) {
                $('#projectDeleteModal-assignedTransactions').show();
                $('#projectDeleteModal-assignedCount').text(transCnt);
                deletePossible = false;
            }

            if ($('#list').children('tr').length === 1) {
                $('#projectDeleteModal-lastProject').show();
                deletePossible = false;
            }

            if (deletePossible) {
                $('#projectDeleteModal-possible').show();
                submitButton.show();
                submitButton.click(function(event) {
                    $.get('${pageContext.request.contextPath}/project/delete/' + projectId, function () {
                        window.location.reload();
                    });
                });
            }
            else
                submitButton.hide();
        });
    });

    $('.transaction-count').each(function () {
        const target = $(this);
        const projectId = $(this).data('project-id');
        $.get('${pageContext.request.contextPath}/project/numberoftransactions/' + projectId, function (data) {
            target.text(data);
        });
    });
    $('.import-count').each(function () {
        const target = $(this);
        const projectId = $(this).data('project-id');
        $.get('${pageContext.request.contextPath}/project/numberofimports/' + projectId, function (data) {
            target.text(data);
        });
    });
</script>

</body>

</html>
