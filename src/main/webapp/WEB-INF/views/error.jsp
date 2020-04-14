<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Error ${code}</title>

    <link href="${pageContext.request.contextPath}/css/sb-admin-2.min.css" rel="stylesheet">

    <style>
        #more {
            display: none;
            background: #EEE;
            border: 1px solid #DDD;
            border-radius: 5px;
            font-size: 12px;
            padding: 10px;
        }
        #more-link {
            margin-top: 5px;
            margin-bottom: 2px;
            cursor: pointer;
        }
    </style>

</head>

<body class="bg-gradient-primary">

<div class="container" style="max-width: 520px">
    <div class="card o-hidden border-0 shadow-lg my-5">
        <div class="card-body p-0">
            <div class="row">
                <div class="p-5" style="width: 500px; padding-top: 20px !important; padding-bottom: 20px !important;">
                    <div class="text-center">
                        <h1 class="h1 text-gray-900 mb-4">Error ${code}</h1>
                    </div>
                    <c:if test="${code == ''}">
                        <b>Unknown Error</b><br>
                        Something really strange happened... Better tell your administrator what you did!
                    </c:if>
                    <c:if test="${code == '403'}">
                        <b>Forbidden</b><br>
                        Halt! You don't have access to the requested resource.
                    </c:if>
                    <c:if test="${code == '404'}">
                        <b>Page Not Found</b><br>
                        Looks like the page your are looking for does not exist!
                    </c:if>
                    <c:if test="${code == '500'}">
                        <b>Internal Server Error</b><br>
                        Dang, something went wrong on the server side. Please contact the administration if you think this should work.
                    </c:if>
                    <br>
                    <a class="small" tabindex="0" id="more-link">More info...</a>
                    <div id="more">
                        <li><b>Code:</b> ${code != '' ? code : 'None'}</li>
                        <li><b>Exception:</b> ${exception != '' ? exception : 'None'}</li>
                        <li><b>Exception type:</b> ${exceptionType != '' ? exceptionType : 'None'}</li>
                        <li><b>Message:</b> ${message != '' ? message : 'None'}</li>
                        <li><b>Request URI:</b> ${requestUri != '' ? requestUri : 'None'}</li>
                        <li><b>Servlet name:</b> ${servletName != '' ? servletName : 'None'}</li>
                    </div>
                    <hr>
                    <div class="text-center">
                        <a class="small" href="javascript:history.back()">Go back</a><br>
                        <a class="small" href="${pageContext.request.contextPath}/">Go to home screen</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap core JavaScript-->
<script src="${pageContext.request.contextPath}/vendor/jquery/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

<!-- Core plugin JavaScript-->
<script src="${pageContext.request.contextPath}/vendor/jquery-easing/jquery.easing.min.js"></script>

<!-- Custom scripts for all pages-->
<script src="${pageContext.request.contextPath}/js/sb-admin-2.min.js"></script>

<script>
    $('#more-link').click(function () {
        $('#more').slideToggle();
    })
</script>

</body>

</html>
