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
        function fillaform() {
            var selected = document.getElementById('selectSettings').value();
            var date = document.querySelector('#datePosition');
            date.value = selected.type;
            
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

                    <form role="form">

                        <div class="form-group">
                            <label>File input</label>
                            <input type="file">
                        </div>

                        <div class="form-group">
                            <label>Select your custom csv settings</label>
                            <select class="form-control" onchange="fillaform()" id="selectSettings">
                                <c:forEach items="${csvSettings}" var="setting">
                                    <option value="${setting}">${setting.name}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-group">
                            <label>Set date position</label>
                            <input class="form-control" id="datePosition">
                            <p class="help-block">This block can be imported from your custom CSV settings</p>
                        </div>

                        <div class="form-group">
                            <label>Set description position</label>
                            <input class="form-control" id="descriptionPosition">
                            <p class="help-block">This block can be imported from your custom CSV settings</p>
                        </div>

                        <div class="form-group">
                            <label>Set transaction partner position</label>
                            <input class="form-control" id="partyPosition">
                            <p class="help-block">This block can be imported from your custom CSV settings</p>
                        </div>

                        <div class="form-group">
                            <label>Set amount position</label>
                            <input class="form-control" id="amountPosition">
                            <p class="help-block">This block can be imported from your custom CSV settings</p>
                        </div>

                        <div class="form-group">
                            <label>Set separator</label>
                            <input class="form-control" id="separator">
                            <p class="help-block">This block can be imported from your custom CSV settings</p>
                        </div>

                        <div class="form-group">
                            <label>How many lines at the beginning should be skipped?</label>
                            <input class="form-control" id="skipLines">
                            <p class="help-block">This block can be imported from your custom CSV settings</p>
                        </div>

                        <button type="submit" class="btn btn-default">Submit Button</button>
                        <button type="reset" class="btn btn-default">Reset Button</button>

                    </form>

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