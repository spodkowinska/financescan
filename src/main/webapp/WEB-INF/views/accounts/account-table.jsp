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

    <title>Accounts</title>

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

                <!-- DataTales Example -->
                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <h6 class="m-0 font-weight-bold text-gray-800" style="float: left">Accounts</h6>
                        <button class="btn btn-secondary btn-sm" style="float: right; margin-bottom: -6px; margin-top: -6px;"
                                data-toggle="modal" data-target="#accountModal">
                            <span class="fa fa-plus"></span> Add new account
                        </button>
                    </div>
                    <div class="card-body">

                        <table id="account_table" class="finance_table">
                            <!-- TABLE HEADER -->
                            <thead>
                            <tr>
                                <th style="width: 60px">Actions</th>
                                <th style="width: 200px">Name</th>
                                <th style="width: 150px">Created</th>
                                <th>Description</th>
                            </tr>
                            </thead>
                            <!-- TABLE DATA -->
                            <tbody id="list">
                            <c:forEach items="${accountsList}" var="account">
                                <tr>
                                        <%-- COLUMN: ACTIONS --%>

                                    <td class="actions">
                                        <a data-toggle="modal" data-target="#accountModal" data-account-id="${account.id}"
                                           data-toggle="tooltip" title="Edit account" tabindex="0">
                                            <span class="fa fa-edit"></span>
                                        </a>

                                        <a data-toggle="modal" data-target="#accountDeleteModal" data-account-id="${account.id}"
                                           data-toggle="tooltip" title="Delete account" tabindex="0">
                                            <span class="fa fa-trash-alt"></span>
                                        </a>
                                    </td>

                                        <%-- COLUMN: NAME --%>

                                    <td style="text-align: center">
                                        ${account.name}
                                    </td>

                                        <%-- COLUMN: CREATED --%>

                                    <td style="text-align: center">
                                        <javatime:format value="${account.created}" style="MS" />
                                    </td>

                                        <%-- COLUMN: DESCRIPTION --%>

                                    <td>
                                        ${account.description}
                                    </td>

                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>

                    </div>

                </div>
                <!-- /.container-fluid -->

            </div>
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
    $('#accountModal').on('show.bs.modal', function(event) {
        // Unbind handlers to avoid situations in which this button has more than one onclick handler
        $('#accountModalSubmit').unbind();

        let accountId = $(event.relatedTarget).data('account-id');
        let link = accountId
            // If accountId is valid, then we are in edit mode
            ? '${pageContext.request.contextPath}/account/edit/' + accountId
            // Otherwise we are in creation mode
            : '${pageContext.request.contextPath}/account/add';

        // Update modal's title depending on mode
        $('#accountModalLabel').html(accountId ? 'Edit Account <span class="small text-gray-500">#' + accountId + '</span>' : 'Add New Account');

        $.get(link, function(data) {
            $('#accountModalBody').html(data);
            $('#accountModalSubmit').click(function(event) {
                // Submit
                $.post(link, $('#accountModalForm').serialize(), function () {
                    window.location.reload();
                });
            });
        });
    });

    $('#accountDeleteModal').on('show.bs.modal', function(event) {
        // Unbind handlers to avoid situations in which this button has more than one onclick handler
        const submitButton = $('#accountDeleteModalSubmit');
        submitButton.unbind();

        let accountId = $(event.relatedTarget).data('account-id');

        $('#accountDeleteModal-id').text(accountId);

        $.get('${pageContext.request.contextPath}/account/numberoftransactions/' + accountId, function(data) {
            let deletePossible = true;

            const transCnt = Number(data);
            if (transCnt > 0) {
                $('#accountDeleteModal-assignedTransactions').show();
                $('#accountDeleteModal-assignedCount').text(transCnt);
                deletePossible = false;
            }

            if ($('#list').children('tr').length === 1) {
                $('#accountDeleteModal-lastAccount').show();
                deletePossible = false;
            }

            $('#accountDeleteModal-possible').toggle(deletePossible);

            if (deletePossible) {
                submitButton.show();
                $('#accountDeleteModalSubmit').click(function(event) {
                    $.get('${pageContext.request.contextPath}/account/delete/' + accountId, function () {
                        window.location.reload();
                    });
                });
            }
            else
                submitButton.hide();
        });
    });
</script>

</body>

</html>
