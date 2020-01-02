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

    <title>Custom CSV</title>

    <!-- Custom fonts for this template-->
    <link href="<c:url value="vendor/fontawesome-free/css/all.min.css"/>" rel="stylesheet" type="text/css">
    <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
          rel="stylesheet">

    <!-- Custom styles for this template-->
    <link href="<c:url value="css/sb-admin-2.min.css"/>" rel="stylesheet">


</head>

<body class="bg-gradient-primary">
<div class="p-5">
    <div class="text-center">
        <h1 class="h4 text-gray-900 mb-4">Here you can prepare your new CSV reader</h1>
    </div>
    <form:form modelAttribute="csvSettings" method="post">
        <div class="form-group shadow-textarea">

            <div class="form-group">
                <form:input path="name" class="form-control form-control-user" id="csvName"
                            placeholder="Name for your CSV Reader Settings"></form:input>
            </div>
            <div class="form-group">
                <form:input path="datePosition" class="form-control form-control-user" id="datePosition"
                            placeholder="In which column is date?"></form:input>
            </div>
            <div class="form-group">
                <form:input path="descriptionPosition" class="form-control form-control-user" id="descriptionPosition"
                            placeholder="In which column is transaction description?"></form:input>
            </div>
            <div class="form-group">
                <form:input path="partyPosition" class="form-control form-control-user" id="partyPosition"
                            placeholder="In which column is partner of the transaction?"></form:input>
            </div>
            <div class="form-group">
                <form:input path="amountPosition" class="form-control form-control-user" id="amountPosition"
                            placeholder="In which column is amount?"></form:input>
            </div>
            <div class="form-group">
                <form:input path="csvSeparator" class="form-control form-control-user" id="separator"
                            placeholder="What is separator in your CSV?"></form:input>
            </div>
            <div class="form-group">
                <form:input path="skipLines" class="form-control form-control-user" id="skipLines"
                            placeholder="How many lines should be skipped?"></form:input>
            </div>
        </div>

        <input type="submit" placeholder="Set settings">
    </form:form>
</div>
</div>
</div>

<!-- Bootstrap core JavaScript-->
<script src="vendor/jquery/jquery.min.js"></script>
<script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

<!-- Core plugin JavaScript-->
<script src="vendor/jquery-easing/jquery.easing.min.js"></script>

<!-- Custom scripts for all pages-->
<script src="js/sb-admin-2.min.js"></script>

<!-- Page level plugins -->
<script src="vendor/chart.js/Chart.min.js"></script>

<!-- Page level custom scripts -->
<script src="js/demo/chart-area-demo.js"></script>
<script src="js/demo/chart-pie-demo.js"></script>

</body>