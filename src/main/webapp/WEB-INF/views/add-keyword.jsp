<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Add keywords</title>

    <!-- Custom fonts for this template -->
    <link href="${pageContext.request.contextPath}/vendor/fontawesome-free/css/all.min.css" rel="stylesheet"
          type="text/css">

    <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
          rel="stylesheet">

    <!-- Custom styles for this template-->
    <link href="${pageContext.request.contextPath}/css/sb-admin-2.css" rel="stylesheet">

    <!-- Custom styles for this page -->
    <link href="${pageContext.request.contextPath}/vendor/datatables/dataTables.bootstrap4.min.css" rel="stylesheet">

    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>


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
                <h1 class="h3 mb-2 text-gray-800">Add keywords</h1>

                    <form action="" method="post">

                        <div class="form-group">
                            <label>Select category</label>
                            <select class="form-control" id="category" name="category">
<%--                                <option value="0"> --select category if it is not a parent category----%>
                                </option>
                                <c:forEach items="${categories}" var="category">
                                    <option value="${category.id}">${category.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Keywords</label>
                            <input class="form-control" id="keyword1" name="keyword1">
                            <p class="help-block">Words that will be used to assign categories to your
                                transactions</p>
                        </div>

                        <div class="form-group">
                            <label>Keywords</label>
                            <input class="form-control" id="keyword2" name="keyword2">
                            <p class="help-block">Words that will be used to assign categories to your
                                transactions</p>
                        </div>

                        <div class="form-group">
                            <label>Keywords</label>
                            <input class="form-control" id="keyword3" name="keyword3">
                            <p class="help-block">Words that will be used to assign categories to your
                                transactions</p>
                        </div>

<%--                        todo create working button--%>
                        <button>Add more keywords</button>








                        <button type="submit" class="btn btn-default">Save</button>
                        <button type="reset" class="btn btn-default">Reset</button>

                    </form>
</body>
</html>