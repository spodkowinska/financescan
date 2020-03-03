<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Zaimportuj plik CSV</title>

    <!-- Custom fonts for this template -->
    <link href="${pageContext.request.contextPath}/vendor/fontawesome-free/css/all.min.css" rel="stylesheet"
          type="text/css">

    <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
          rel="stylesheet">

    <!-- Custom styles for this template-->
    <link href="${pageContext.request.contextPath}/css/sb-admin-2.css" rel="stylesheet">

    <!-- Custom styles for this page -->
    <link href="${pageContext.request.contextPath}/vendor/datatables/dataTables.bootstrap4.min.css"/>"
          rel="stylesheet">

    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>

    <script>
        function fillAForm() {
            var selectedValue = document.getElementById('selectSettings').value;

            switch (selectedValue) {
                <c:forEach items="${csvSettingsList}" var="setting">
                case "${setting.id}":
                    document.querySelector('#datePosition').value = "${setting.datePosition+1}";
                    document.querySelector('#descriptionPosition').value = "${setting.descriptionPosition+1}";
                    document.querySelector('#partyPosition').value = "${setting.partyPosition+1}";
                    document.querySelector('#amountPosition').value = "${setting.amountPosition+1}";
                    document.querySelector('#separator').value = "${setting.csvSeparator}";
                    document.querySelector('#skipLines').value = "${setting.skipLines}";
                    break;
                </c:forEach>
            }
        }
    </script>
</head>

<body>

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

                <%--todo frontend validation--%>

                <!-- Page Heading -->
                <h1 class="h3 mb-2 text-gray-800">Zaimportuj plik CSV</h1>

                <!-- Page Heading -->

                <div class="row">
                    <div class="col-lg-24">
                        <h1 class="page-header">
                            <div class="text-center">

                                <h10 class="h6 text-gray-900 mb-4">Każdy plik CSV może być inny, zawierać informacje dodatkowe, czy dane,
                                    których nie ma potrzeby przechowywać. W tym miejscu możesz zainportować dane według
                                    wprowadzonego szablonu lub zaposać nowy.
                                </h10>
                            </div>
                        </h1>
                    </div>
                </div>
                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <div class="row">
                            <div class="col-lg-24">

                                <form action="/preschoolImport" method="post" enctype="multipart/form-data">

                                    <div class="form-group">
                                        <label>Pobierz plik CSV</label>
                                        <input type="file" name="fileToUpload">
                                    </div>

                                    <div class="form-group">
                                        <label>Wybierz swoje ustawienia</label>
                                        <select class="form-control" onchange="fillAForm()" id="selectSettings">
                                            <c:forEach items="${csvSettingsList}" var="setting">
                                                <option value="${setting.id}">${setting.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="form-group">
                                        <label>Nazwa importu</label>
                                        <input class="form-control" id="importName" name="importName">
                                        <p class="help-block">Nazwa pozwala w prostszy sposób odnajdywać transakcje</p>
                                    </div>

                                    <div class="form-group">
                                        <label>Pozycja daty w pliku CSV</label>
                                        <input class="form-control" id="datePosition" name="datePosition">
                                    </div>

                                    <div class="form-group">
                                        <label>Pozycja opisu w pliku CSV</label>
                                        <input class="form-control" id="descriptionPosition" name="descriptionPosition">
                                    </div>

                                    <div class="form-group">
                                        <label>Pozycja strony transakcji</label>
                                        <input class="form-control" id="partyPosition" name="partyPosition">
                                    </div>

                                    <div class="form-group">
                                        <label>Pozycja kwoty transakcji</label>
                                        <input class="form-control" id="amountPosition" name="amountPosition">
                                    </div>

                                    <div class="form-group">
                                        <label>Separator użyty w pliku CSV</label>
                                        <input class="form-control" id="separator" name="separator">
                                    </div>

                                    <div class="form-group">
                                        <label>Ile linijek na górze pliku nie zawiera istotnych informacji?</label>
                                        <input class="form-control" id="skipLines" name="skipLines">
                                    </div>

                                    <button type="submit" class="btn btn-default">Submit CSV</button>
                                    <button type="reset" class="btn btn-default">Reset</button>
                                    <button type="submit" class="btn btn-default">Save settings</button>

                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- Bootstrap Core JavaScript -->
<script src="${pageContext.request.contextPath}/vendor/jquery/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

<!-- Core plugin JavaScript-->
<script src="${pageContext.request.contextPath}/vendor/jquery-easing/jquery.easing.min.js"></script>

<!-- Custom scripts for all pages-->
<script src="${pageContext.request.contextPath}/js/sb-admin-2.min.js"></script>
</body>

</html>
