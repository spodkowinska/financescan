<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

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
    <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" rel="stylesheet">

    <!-- Custom styles for this template-->
    <link href="<c:url value="css/sb-admin-2.min.css"/>" rel="stylesheet">


</head>

<body class="bg-gradient-primary">
<div class="p-5">
    <div class="text-center">
        <h1 class="h4 text-gray-900 mb-4">Here you can prepare your new CSV reader</h1>
    </div>

        <form:form modelAttribute="present1" method="post">
        <div class="form-group shadow-textarea">
            <form:textarea path="description3" class="form-control z-depth-1"
                           id="exampleFormControlTextarea6" rows="3"
                           placeholder="${placeholder}"></form:textarea>
        <div class="form-group">
            <form:textarea path="" type="text" class="form-control form-control-user" id="csvName" placeholder="Name for your CSV Reader Settings">
        </div>
        <div class="form-group">
            <input type="text" class="form-control form-control-user" id="datePosition" placeholder="In which column is date?">
        </div>
        <div class="form-group">
            <input type="text" class="form-control form-control-user" id="descriptionPosition" placeholder="In which column is transaction description?">
        </div>
        <div class="form-group">
            <input type="text" class="form-control form-control-user" id="partyPosition" placeholder="In which column is partner of the transaction?">
        </div>
        <div class="form-group">
            <input type="text" class="form-control form-control-user" id="amountPosition" placeholder="In which column is amount?">
        </div>
        <div class="form-group">
            <input type="text" class="form-control form-control-user" id="separator" placeholder="What is separator in your CSV?">
        </div>
        <div class="form-group">
            <input type="text" class="form-control form-control-user" id="skipLines" placeholder="How many lines should be skipped?">
        </div>

        <div class="form-group">
            <div class="custom-control custom-checkbox small">
                <input type="checkbox" class="custom-control-input" id="customCheck">
                <label class="custom-control-label" for="customCheck">Remember Me</label>
            </div>
        </div>
        <a href="index.html" class="btn btn-primary btn-user btn-block">
            Login
        </a>
        <hr>
        <a href="index.html" class="btn btn-google btn-user btn-block">
            <i class="fab fa-google fa-fw"></i> Login with Google
        </a>
        <a href="index.html" class="btn btn-facebook btn-user btn-block">
            <i class="fab fa-facebook-f fa-fw"></i> Login with Facebook
        </a>
    </form>
    <hr>
    <div class="text-center">
        <a class="small" href="forgot-password.html">Forgot Password?</a>
    </div>
    <div class="text-center">
        <a class="small" href="register.html">Create an Account!</a>
    </div>
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