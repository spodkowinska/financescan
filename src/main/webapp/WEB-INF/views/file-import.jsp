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

    <title>Import CSV</title>
    <!-- Bootstrap Core CSS -->
    <link href="<c:url value="css/bootstrap.min.css"/>" rel="stylesheet">

    <!-- Custom fonts for this template-->
    <link href="<c:url value="font-awesome-4.1.0/css/font-awesome.min.css"/>" rel="stylesheet" type="text/css">

    <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
          rel="stylesheet">

    <!-- Custom styles for this template-->
    <link href="<c:url value="css/sb-admin-2.min.css"/>" rel="stylesheet">

    <!-- Custom styles for this page -->
    <link href="<c:url value="vendor/datatables/dataTables.bootstrap4.min.css"/>" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="css/sb-admin.css" rel="stylesheet">


    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
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


    <div id="page-wrapper">

        <div class="container-fluid">

            <!-- Page Heading -->
            <div class="row">
                <div class="col-lg-24">
                    <h1 class="page-header">
                        <div class="text-center">
                            <h1 class="h4 text-gray-900 mb-4">Here you can prepare your new CSV reader</h1>
                        </div>
                    </h1>
                </div>
            </div>
            <!-- /.row -->

            <div class="row">
                <div class="col-lg-24">

                    <form action="/fileimport" method="post" enctype="multipart/form-data">

                        <div class="form-group">
                            <label>File input</label>
                            <input type="file" name="fileToUpload">
                        </div>

                        <div class="form-group">
                            <label>Select your custom csv settings</label>
                            <select class="form-control" onchange="fillAForm()" id="selectSettings">
                                <c:forEach items="${csvSettingsList}" var="setting">
                                    <option value="${setting.id}">${setting.name}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-group">
                            <label>Name of this import</label>
                            <input class="form-control" id="importName" name="importName">
                            <p class="help-block">Name helps you sort your transactions</p>
                        </div>


                        <div class="form-group">
                            <label>Select bank</label>
                            <select class="form-control" id="selectBank">
                                <c:forEach items="${banksList}" var="bank">
                                    <option value="${bank.id}">${bank.name}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-group">
                            <label>Set date position</label>
                            <input class="form-control" id="datePosition" name="datePosition">
                            <p class="help-block">This block can be imported from your custom CSV settings</p>
                        </div>

                        <div class="form-group">
                            <label>Set description position</label>
                            <input class="form-control" id="descriptionPosition" name="descriptionPosition">
                            <p class="help-block">This block can be imported from your custom CSV settings</p>
                        </div>

                        <div class="form-group">
                            <label>Set transaction partner position</label>
                            <input class="form-control" id="partyPosition" name="partyPosition">
                            <p class="help-block">This block can be imported from your custom CSV settings</p>
                        </div>

                        <div class="form-group">
                            <label>Set amount position</label>
                            <input class="form-control" id="amountPosition" name="amountPosition">
                            <p class="help-block">This block can be imported from your custom CSV settings</p>
                        </div>

                        <div class="form-group">
                            <label>Set separator</label>
                            <input class="form-control" id="separator" name="separator">
                            <p class="help-block">This block can be imported from your custom CSV settings</p>
                        </div>

                        <div class="form-group">
                            <label>How many lines at the beginning should be skipped?</label>
                            <input class="form-control" id="skipLines" name="skipLines">
                            <p class="help-block">This block can be imported from your custom CSV settings</p>
                        </div>

                        <button type="submit" class="btn btn-default">Submit CSV</button>
                        <button type="reset" class="btn btn-default">Reset</button>
                        <button type="submit" class="btn btn-default">Save settings</button>

                    </form>
                </div>
            </div>
            <!-- /.row -->

        </div>
        <!-- /.container-fluid -->

    </div>
    <!-- /#page-wrapper -->

</div>
<!-- /#wrapper -->

<!-- jQuery Version 1.11.0 -->
<script src="js/jquery-1.11.0.js"></script>

<!-- Bootstrap Core JavaScript -->
<script src="js/bootstrap.min.js"></script>

</body>

</html>
