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
    <link href="${pageContext.request.contextPath}vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
          rel="stylesheet">

    <!-- Custom styles for this template-->
    <link href="${pageContext.request.contextPath}css/sb-admin-2.min.css" rel="stylesheet">


</head>

<body class="bg-gradient-grey">
<div class="p-5">
    <div class="text-center">
        <h1 class="h4 text-gray-900 mb-4">Here you can prepare your new CSV reader</h1>
    </div>
    <form:form modelAttribute="csvSettings" method="post">
        <div class="form-group shadow-textarea">
            <div class="form-group">
                <label>Name of CSV Reader Settings</label>
                <form:input path="name" class="form-control" id="csvName" ></form:input>
            </div>
            <div class="form-group">
                <label>In which column is date?</label>
                <form:input path="datePosition" class="form-control form-control-user" id="datePosition"></form:input>
            </div>
            <div class="form-group">
                <label>In which column is transaction description?</label>
                <form:input path="descriptionPosition" class="form-control form-control-user" id="descriptionPosition"></form:input>
            </div>
            <div class="form-group">
                <label>In which column is partner of the transaction?</label>
                <form:input path="partyPosition" class="form-control form-control-user" id="partyPosition"></form:input>
            </div>
            <div class="form-group">
                <label>In which column is amount?</label>
                <form:input path="amountPosition" class="form-control form-control-user" id="amountPosition"></form:input>
            </div>
            <div class="form-group">
                <label>What is separator in your CSV?</label>
                <form:input path="csvSeparator" class="form-control form-control-user" id="separator"
                            ></form:input>
            </div>
            <div class="form-group">
                <label>How many lines should be skipped?</label>
                <form:input path="skipLines" class="form-control form-control-user" id="skipLines"></form:input>
            </div>
        </div>

        <input type="submit" placeholder="Set settings">
    </form:form>
</div>
</div>
</div>

<!-- Bootstrap core JavaScript-->
<script src="${pageContext.request.contextPath}vendor/jquery/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

<!-- Core plugin JavaScript-->
<script src="${pageContext.request.contextPath}vendor/jquery-easing/jquery.easing.min.js"></script>

<!-- Custom scripts for all pages-->
<script src="${pageContext.request.contextPath}js/sb-admin-2.min.js"></script>

<!-- Page level plugins -->
<script src="${pageContext.request.contextPath}vendor/chart.js/Chart.min.js"></script>

<!-- Page level custom scripts -->
<script src="${pageContext.request.contextPath}js/demo/chart-area-demo.js"></script>
<script src="${pageContext.request.contextPath}js/demo/chart-pie-demo.js"></script>

</body>