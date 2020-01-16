<%@ page contentType="text/html;charset=UTF-8" %>
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

    <title>Edit category</title>
    <!-- Bootstrap Core CSS -->
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom fonts for this template-->
    <link href="${pageContext.request.contextPath}/font-awesome-4.1.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">

    <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
          rel="stylesheet">

    <!-- Custom styles for this template-->
    <link href="${pageContext.request.contextPath}/css/sb-admin-2.min.css" rel="stylesheet">

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

<body>

<div id="wrapper">


<div id="page-wrapper">

<div class="container-fluid">

<!-- Page Heading -->
<div class="row">
    <div class="col-lg-24">
        <h1 class="page-header">
            <div class="text-center">
                <h10 class="h4 text-gray-900 mb-4">Edit category</h10>
            </div>
        </h1>
    </div>
</div>
<!-- /.row -->

<div class="row">
<div class="col-lg-24">

<form:form action="../../category/edit/${category.id}" method="post" modelAttribute="category">


<div class="form-group">
    <label>Name</label>
    <form:input path="name" class="form-control" id="categoryName" ></form:input>
</div>

<div class="form-group">
    <label>Description</label>
    <form:input path="description" class="form-control" id="description" name="description"></form:input>
    <p class="help-block"></p>
</div>

<div class="form-group">
<label>Keywords</label>
    <form:input path="keywords" class="form-control" id="keywords" name="keywords"></form:input>
<%--    <form:errors path="keywords" cssclass="error" element="div"></form:errors>--%>
<%--    <div id="username.errors" class="error">keyword already used</div>--%>
    <p class="help-block">Words that will be used to assign categories to your transactions</p>

    </div>

    <div class="form-group">
    <label>Select parent category</label>
    <form:select path="parentCategoryId" class="form-control" id="parent" name="parent">
    <option value="0"> --select category if it is not a parent category-- </option>
    <c:forEach items="${categories}" var="category">
        <option value="${category.id}">${category.name}</option>
    </c:forEach>
    </form:select>
    </div>


    <button type="submit" class="btn btn-default">Save</button>
    <button type="reset" class="btn btn-default">Reset</button>

    </form:form>
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

    </body>

    </html>
