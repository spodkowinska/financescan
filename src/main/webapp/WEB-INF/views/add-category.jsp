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

    <title>Add category</title>
    <!-- Bootstrap Core CSS -->
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <!-- Custom fonts for this template -->

    <link href="${pageContext.request.contextPath}/vendor/fontawesome-free/css/all.min.css" rel="stylesheet"
          type="text/css">
    <!--datatables-->
    <link href="${pageContext.request.contextPath}/cdn.datatables.net/1.10.20/css/jquery.dataTables.min.css"
          rel="stylesheet" type="text/css">

<%--    <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"--%>
<%--          rel="stylesheet">--%>

<%--    <!-- Custom fonts for this template-->--%>
<%--    <link href="${pageContext.request.contextPath}/font-awesome-4.1.0/css/font-awesome.min.css" rel="stylesheet"--%>
<%--          type="text/css">--%>

    <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
          rel="stylesheet">

    <!-- Custom styles for this template-->
    <link href="${pageContext.request.contextPath}/css/sb-admin-2.css" rel="stylesheet">

    <!-- Custom styles for this page -->
    <link href="${pageContext.request.contextPath}/vendor/datatables/dataTables.bootstrap4.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/css/sb-admin.css" rel="stylesheet">


    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body id="page-top">

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

<%--todo frontend validation--%>

            <!-- Page Heading -->
                <h1 class="h3 mb-2 text-gray-800">Add category</h1>


            <!-- Page Heading -->

            <div class="row">
                <div class="col-lg-24">
                    <h1 class="page-header">
                        <div class="text-center">

                            <h10 class="h6 text-gray-900 mb-4">More categories will give you more insight in your
                                expenses
                            </h10>
                        </div>
                    </h1>
                </div>
            </div>
                <div class="card shadow mb-4">
                    <div class="card-header py-3">
            <div class="row">
                <div class="col-lg-24">

                    <form action="" method="post">


                        <div class="form-group">
                            <label>Name</label>
                            <input class="form-control" id="name" name="name">
                        </div>

                        <div class="form-group">
                            <label>Description</label>
                            <input class="form-control" id="description" name="description">
                            <p class="help-block"></p>
                        </div>

                        <div class="form-group">
                            <label>Keywords</label>
                            <input class="form-control" id="keywords" name="keywords">
                            <%--    <form:errors path="keywords" cssClass="error" element="div"></form:errors>--%>
                            <%--    <div id="username.errors" class="error">keyword already used</div>--%>
                            <p class="help-block">Words that will be used to assign categories to your transactions</p>

                        </div>

                        <div class="form-group">
                            <label>Select parent category</label>
                            <select class="form-control" id="parent" name="parent">
                                <option value="0"> --select category if it is not a parent category--</option>
                                <c:forEach items="${categories}" var="category">
                                    <option value="${category.id}">${category.name}</option>
                                </c:forEach>
                            </select>
                        </div>


                        <button type="submit" class="btn btn-default">Save</button>
                        <button type="reset" class="btn btn-default">Reset</button>

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
<script src="${pageContext.request.contextPath}/js/jquery-1.11.0.js"></script>

<!-- Bootstrap Core JavaScript -->
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
            <script src="${pageContext.request.contextPath}/vendor/jquery/jquery.min.js"></script>
            <script src="${pageContext.request.contextPath}/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

            <!-- Core plugin JavaScript-->
            <script src="${pageContext.request.contextPath}/vendor/jquery-easing/jquery.easing.min.js"></script>

            <!-- Custom scripts for all pages-->
            <script src="${pageContext.request.contextPath}/js/sb-admin-2.min.js"></script>
</body>

</html>
