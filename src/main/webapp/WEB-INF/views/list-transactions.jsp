<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Transactions</title>

    <!-- Custom fonts for this template -->
    <link href="${pageContext.request.contextPath}/vendor/fontawesome-free/css/all.min.css" rel="stylesheet"
          type="text/css">
    <!--datatables-->
    <link href="http://cdn.datatables.net/1.10.20/css/jquery.dataTables.min.css"
          rel="stylesheet" type="text/css">

    <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
          rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="${pageContext.request.contextPath}/css/sb-admin-2.css" rel="stylesheet">

    <!-- Custom styles for this page -->
    <link href="${pageContext.request.contextPath}/vendor/datatables/dataTables.bootstrap4.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/vanillaSelectBox.css">

    <script src="${pageContext.request.contextPath}/js//vanillaSelectBox.js"></script>
    <script>
        function sendData(selectId) {
            var category = $('#changeCategory' + selectId);
            var categoryIds = category.val();
            if (categoryIds == "") {
                $.get("${pageContext.request.contextPath}/transaction/setcategories/" + selectId + "/0");
            } else {
                $.get("${pageContext.request.contextPath}/transaction/setcategories/" + selectId + "/" + categoryIds.join(","));
            }
        }

        var gMonth = "all";
        var gYear = "all";

        function getData(year, month) {
            if (year !== null) {
                gYear = year;
            }
            if (month !== null) {
                gMonth = month;
            }
            $.get("${pageContext.request.contextPath}/transaction/table/" + gYear + "/" + gMonth, function (data) {
                $('#list').html(data);

                // This line is needed to prevent category drop-right from disappearing
                $('.tag-add-popover').on("click.bs.dropdown", function (e) { e.stopPropagation(); e.preventDefault(); });
            });

            // Update year & month buttons state
            $('#btn_year_' + gYear).addClass('active').siblings().removeClass('active');

            if (gYear === 'all') {
                // Hide months if all years are displayed
                $('#months').hide('fast');
            }
            else {
                // Show months if only a specific year is displayed
                $('#months').show('fast');

                // Activate only current month button if a specific year is displayed
                $('#btn_month_' + gMonth).addClass('active').siblings().removeClass('active');
            }
        }

        function changeCategory(transactionId, categoryId) {
            let parentId = $('#cat_tag_' + transactionId + '_' + categoryId).parent().attr('id');

            if (parentId === ('cat_current_' + transactionId))
                removeCategory(transactionId, categoryId);
            else
                addCategory(transactionId, categoryId);
        }

        function addCategory(transactionId, categoryId) {
            $.get("${pageContext.request.contextPath}/transaction/addcategory/" + transactionId + "/" + categoryId);
            $('#cat_tag_' + transactionId + '_' + categoryId).appendTo('#cat_current_' + transactionId);
        }

        function removeCategory(transactionId, categoryId) {
            $.get("${pageContext.request.contextPath}/transaction/removecategory/" + transactionId + "/" + categoryId);
            $('#cat_tag_' + transactionId + '_' + categoryId).appendTo('#cat_others_' + transactionId);
        }
    </script>


</head>

<body id="page-top" onload="getData('all','all')">

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


                <!-- NEW TABLE - WORK IN PROGRESS -->
                <style>
                    .raf {
                        width: 100%;
                        font-size: 12px;
                        font-family: Arial;
                        color: black;
                    }

                    .raf table {
                        border-spacing: 0;
                        border: 1px solid black;
                    }

                    .raf tr:hover {
                        background: #F7F7F7;
                    }

                    .raf th, td {
                        border: 1px solid lightgray;
                        padding: 2px;
                        padding-left: 5px;
                        padding-right: 5px;
                    }

                    .raf th {
                        border-color: #858796;
                        text-align: center;
                        color: #858796;
                        background: white;
                        font-family: Arial, Helvetica, sans-serif;
                        font-size: 14px;
                        font-weight: normal;
                    }

                    .raf td.actions a {
                        display: inline-block;
                        margin-left: 5px;
                        margin-right: 5px;
                        color: rgb(207, 207, 207);
                    }

                    .raf td.categories-list {
                        width: 230px;
                        padding-left: 2px;
                        border-right: 0;
                    }

                    .raf td.categories-add {
                        width: 20px;
                        border-left: 0;
                        padding-right: 2px;
                        vertical-align: top;
                    }

                    .raf td.actions a:hover {
                        color: gray;
                    }

                    .raf .center {
                        text-align: center;
                    }

                    .raf .right {
                        text-align: right;
                    }

                    .raf td.negative {
                        color: rgb(226, 0, 0);
                    }

                    .raf td.positive {
                        color: rgb(0, 150, 0);
                    }

                    div.tag-add-popover {
                        text-align: center;
                        padding: 5px;
                        min-width: 300px;
                    }

                    a.tag {
                        display: inline-block;
                        margin: 1px;
                        font-size: 10px;
                        padding: 2px;
                        padding-left: 10px;
                        padding-right: 10px;
                        border-radius: 3px;
                        color: white;
                        text-decoration: none;
                    }

                    a.tag:hover {
                        cursor: pointer;
                        filter: brightness(120%);
                        color: white;
                    }

                    a.tag-add {
                        background: rgb(207, 207, 207);
                    }

                    a.tag1 {
                        background: green;
                    }

                    a.tag2 {
                        background: red;
                    }

                    a.tag3 {
                        background: rgb(0, 171, 214);
                    }

                    a.tag4 {
                        background: darkorchid;
                    }

                    a.tag5 {
                        background: darkgoldenrod;
                    }

                    a.tag6 {
                        background: gray;
                    }

                    a.tag7 {
                        background: blue;
                    }

                    a.tag8 {
                        background: navy;
                    }

                    a.tag9 {
                        background: darkslateblue;
                    }

                    a.tag10 {
                        background: darkslategrey;
                    }

                    a.tag11 {
                        background: black;
                    }

                    a.tag12 {
                        background: darkred;
                    }

                    a.tag-add:hover {
                        background: gray;
                    }
                </style>

                <h1 class="h3 mb-2 text-gray-800">Transactions</h1>

                <div class="card shadow mb-4">
                    <div class="card-body">

                        <%-- YEARS --%>
                        <div class="btn-group">
                            <button id="btn_year_all" class="btn btn-outline-secondary btn-sm active" onclick="getData('all', null)">
                                ALL
                            </button>
                            <c:forEach items="${years}" var="year">
                                <button id="btn_year_${year}" class="btn btn-outline-secondary btn-sm" onclick="getData(${year}, null)">
                                    ${year}
                                </button>
                            </c:forEach>
                        </div>

                        <%-- MONTHS --%>
                        <div id="months" class="btn-group">
                            <button id="btn_month_all" class="btn btn-outline-secondary btn-sm active" onclick="getData(null, 'all')">
                                ALL
                            </button>

                            <script>
                                let months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];

                                for (var i = 0; i < 12; i++) {
                                    let monthId = i + 1;
                                    document.write("<button id=\"btn_month_" + monthId + "\" class=\"btn btn-outline-secondary btn-sm\" onclick='getData(null," + monthId + ")'>");
                                    document.write(months[i]);
                                    document.write("</button>");
                                }
                            </script>
                        </div>

                        <!-- Three Dots Dropdown -->
                        <div class="btn-group" style="float: right">
                            <!-- Three Dots -->
                            <a href="#" id="transOperationsDropdown" role="button" data-toggle="dropdown"
                               aria-haspopup="true" aria-expanded="true">
                                <i class="fas fa-ellipsis-v mr-2 text-gray-600"></i>
                            </a>
                            <!-- Actual Dropdown -->
                            <div class="dropdown-menu dropdown-menu-right shadow"
                                 aria-labelledby="transOperationsDropdown">
                                <a class="dropdown-item" href="${pageContext.request.contextPath}/transaction/assign">
                                    <i class="fas fa-magic mr-2 text-gray-600"></i>
                                    Assign default keywords
                                </a>
                            </div>
                        </div>

                        <div class="input-group input-group-sm mb-3" style="width: 300px; margin-top: 10px;">
                            <input type="text" class="form-control" placeholder="Filter by description..."
                                   aria-label="Filter by description..." aria-describedby="basic-addon2">
                            <div class="input-group-append">
                                <button class="btn btn-outline-secondary" type="button">Filter</button>
                            </div>
                        </div>

                        <div style="height: 15px"></div>

                        <table class="raf">
                            <!-- TABLE HEADER -->
                            <thead>
                            <tr>
                                <th style="width: 85px">Actions</th>
                                <th style="width: 100px">Date</th>
                                <th style="width: 100px">Amount</th>
                                <th colspan="2" style="width: 250px">Categories</th>
                                <th>Description</th>
                            </tr>
                            </thead>
                            <!-- TABLE DATA -->
                            <tbody id="list">
                            <%--                                <c:forEach items="${tl}" var="trans">--%>

                            <%--                                    <tr>--%>
                            <%--                                        <td class="actions">--%>
                            <%--                                            <a data-toggle="tooltip" title="Edit transaction" href="${pageContext.request.contextPath}/transaction/edit/${trans.id}"><span class="fa fa-edit"></span></a>--%>
                            <%--                                            <a data-toggle="tooltip" title="Create keyword from this transaction" href="${pageContext.request.contextPath}/keyword/add/${trans.id}"><span class="fa fa-key"></span></a>--%>
                            <%--                                            <a data-toggle="tooltip" title="Delete transaction" href="${pageContext.request.contextPath}/transaction/delete/${trans.id}"><span class="fa fa-trash-alt"></span></a>--%>
                            <%--                                        </td>--%>
                            <%--                                        <td class="center">${trans.transactionDate}</td>--%>
                            <%--                                        <td class="--%>
                            <%--                                            <c:choose>--%>
                            <%--                                                <c:when test="${trans.amount lt 0}">negative</c:when>--%>
                            <%--                                                <c:otherwise>positive</c:otherwise>--%>
                            <%--                                            </c:choose> right">--%>
                            <%--                                            ${trans.amount} zł--%>
                            <%--                                        </td>--%>
                            <%--                                        <td>--%>
                            <%--                                            <c:forEach items="${categoriesList}" var="category">--%>
                            <%--                                                <c:choose>--%>
                            <%--                                                    <c:when test="${fn:contains(trans.categories, category)}">--%>
                            <%--                                                        <a href="${pageContext.request.contextPath}/transaction/removecategory/${trans.id}/${category.id}" class="tag tag${category.id}">${category.name}</a>--%>
                            <%--                                                    </c:when>--%>
                            <%--                                                </c:choose>--%>
                            <%--                                            </c:forEach>--%>

                            <%--                                            <a tabindex="0" class="tag tag-add" data-html="true" data-toggle="popover" data-trigger="focus"--%>
                            <%--                                                data-content="--%>
                            <%--                                                --%>
                            <%--                                                <c:forEach items="${categoriesList}" var="category">--%>
                            <%--                                                    <c:choose>--%>
                            <%--                                                        <c:when test="${fn:contains(trans.categories, category)}">--%>
                            <%--                                                        </c:when>--%>
                            <%--                                                        <c:otherwise>--%>
                            <%--                                                            <a href='${pageContext.request.contextPath}/transaction/addcategory/${trans.id}/${category.id}' class='tag tag${category.id}'>${category.name}</a>--%>
                            <%--                                                        </c:otherwise>--%>
                            <%--                                                    </c:choose>--%>
                            <%--                                                </c:forEach>--%>

                            <%--                                                " >+</a>--%>
                            <%--                                        </td>--%>
                            <%--                                        <td>${trans.description}</td>--%>
                            <%--                                    </tr>--%>

                            <%--                                </c:forEach>--%>

                            <%--                                <!-- todo: handle changing categories -->--%>
                            <%--                                <!-- <select class="form-control" id="changeCategory${trans.id}" name="changeCategory"--%>
                            <%--                                        multiple size="4" onchange="sendData(${trans.id})" >--%>
                            <%--                                    <c:forEach items="${categoriesList}" var="category">--%>
                            <%--                                        <c:choose>--%>
                            <%--                                            <c:when test="${fn:contains(trans.categories, category)}">--%>
                            <%--                                                <option selected="selected"--%>
                            <%--                                            </c:when>--%>
                            <%--                                            <c:otherwise>--%>
                            <%--                                                <option--%>
                            <%--                                            </c:otherwise>--%>
                            <%--                                        </c:choose>--%>
                            <%--                                        value="${category.id}">${category.name}</option>--%>
                            <%--                                    </c:forEach>--%>
                            <%--                                </select> -->--%>

                            </tbody>
                            <%--                            <jsp:include page="table-transactions.jsp"></jsp:include>--%>
                        </table>
                    </div>
                </div>

            </div>
            <!-- /.container-fluid -->

        </div>
        <!-- End of Main Content -->

        <!-- Footer -->
        <jsp:include page="footer.jsp"></jsp:include>
        <!-- End of Footer -->

    </div>
    <!-- End of Content Wrapper -->

</div>
<!-- End of Page Wrapper -->

<!-- Scroll to Top Button-->
<a class="scroll-to-top rounded" href="#page-top">
    <i class="fas fa-angle-up"></i>
</a>

<%--<!-- Logout Modal-->--%>
<%--<div class="modal fade" id="logoutModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"--%>
<%--     aria-hidden="true">--%>
<%--    <div class="modal-dialog" role="document">--%>
<%--        <div class="modal-content">--%>
<%--            <div class="modal-header">--%>
<%--                <h5 class="modal-title" id="exampleModalLabel">Ready to Leave?</h5>--%>
<%--                <button class="close" type="button" data-dismiss="modal" aria-label="Close">--%>
<%--                    <span aria-hidden="true">×</span>--%>
<%--                </button>--%>
<%--            </div>--%>
<%--            <div class="modal-body">Select "Logout" below if you are ready to end your current session.</div>--%>
<%--            <div class="modal-footer">--%>
<%--                <button class="btn btn-secondary" type="button" data-dismiss="modal">Cancel</button>--%>
<%--                <a class="btn btn-primary" href="login.html">Logout</a>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</div>--%>



<!-- Bootstrap core JavaScript-->
<script src="${pageContext.request.contextPath}/vendor/jquery/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

<!-- Core plugin JavaScript-->
<script src="${pageContext.request.contextPath}/vendor/jquery-easing/jquery.easing.min.js"></script>

<!-- Custom scripts for all pages-->
<script src="${pageContext.request.contextPath}/js/sb-admin-2.min.js"></script>

<!-- Page level plugins -->
<script src="${pageContext.request.contextPath}/vendor/datatables/jquery.dataTables.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/datatables/dataTables.bootstrap4.min.js"></script>

<!-- Page level custom scripts -->
<script src="${pageContext.request.contextPath}/js/demo/datatables-demo.js"></script>
<!--datatables-->
<script src="http://cdn.datatables.net/1.10.20/js/jquery.dataTables.min.js"></script>

</body>

</html>
