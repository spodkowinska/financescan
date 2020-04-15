<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Transactions representation</title>

    <!-- Custom fonts for this template-->
    <link href="${pageContext.request.contextPath}/vendor/fontawesome-free/css/all.min.css" rel="stylesheet"
          type="text/css">
    <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
          rel="stylesheet">

    <!-- Custom styles for this template-->
    <link href="${pageContext.request.contextPath}/css/sb-admin-2.min.css" rel="stylesheet">

</head>

<body id="page-top">

<!-- Page Wrapper -->
<div id="wrapper">

    <!-- Sidebar -->
    <jsp:include page="/WEB-INF/views/utils/sidebar.jsp" />
    <!-- End of Sidebar -->

    <!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column">

        <!-- Main Content -->
        <div id="content">

            <!-- Topbar -->
            <jsp:include page="/WEB-INF/views/utils/topbar.jsp" />
            <!-- End of Topbar -->

            <!-- Begin Page Content -->
            <div class="container-fluid">

                <!-- Page Heading -->
                <h1 class="h3 mb-2 text-gray-800">Visualisation of your expenses</h1>
                <p class="mb-4">Select options to customise your graphs. You can also just analyse by default all
                    transactions. </p>
                <!-- Content Row -->
                <div class="row">

                    <div class="col-xl-8 col-lg-7">

                        <!-- Area Chart -->
                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">Last year of transactions</h6>
                            </div>
                            <div class="card-body">
                                <div class="chart-area">
                                    <canvas id="AreaChartLastYearAllTransactions"></canvas>
                                </div>
                                <hr>
                               Month by month all transactions show global trend of your expenses.
                                Visualisations of trends can prepare you for the reocurring bigger expenses like birthdays, car insurence or holidays.
                            </div>
                        </div>

                        <!-- Bar Chart -->
                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">Bar Chart</h6>
                            </div>
                            <div class="card-body">
                                <div class="chart-bar">
                                    <canvas id="myBarChart"></canvas>
                                </div>
                                <hr>
                                Styling for the bar chart can be found in the <code>/js/demo/chart-bar-demo.js</code>
                                file.
                            </div>
                        </div>

                    </div>

                    <!-- Donut Chart -->
                    <div class="col-xl-4 col-lg-5">
                        <div class="card shadow mb-4">
                            <!-- Card Header - Dropdown -->
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">All expenses by categories</h6>
                            </div>
                            <!-- Card Body -->
                            <div class="card-body">
                                <div class="chart-pie pt-4">
                                    <canvas id="PieChartAllTransactions"></canvas>
                                </div>
                                <hr>
                                Here you can see which categories take the biggest part in your expenses in the long
                                run.
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- /.container-fluid -->

        </div>
        <!-- End of Main Content -->

        <!-- Footer -->
        <jsp:include page="/WEB-INF/views/utils/footer.jsp" />
        <!-- End of Footer -->

    </div>
    <!-- End of Content Wrapper -->

</div>
<!-- End of Page Wrapper -->

<!-- Scroll to Top Button-->
<a class="scroll-to-top rounded" href="#page-top">
    <i class="fas fa-angle-up"></i>
</a>

<!-- Logout Modal-->
<div class="modal fade" id="logoutModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">Ready to Leave?</h5>
                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
            </div>
            <div class="modal-body">Select "Logout" below if you are ready to end your current session.</div>
            <div class="modal-footer">
                <button class="btn btn-secondary" type="button" data-dismiss="modal">Cancel</button>
                <a class="btn btn-primary" href="login.jsp">Logout</a>
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
<!-- Page level plugins -->
<script src="${pageContext.request.contextPath}/vendor/chart.js/Chart.min.js"></script>
<%--<script type="text/javascript">--%>

<%--    // Set new default font family and font color to mimic Bootstrap's default styling--%>
<%--    Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';--%>
<%--    Chart.defaults.global.defaultFontColor = '#858796';--%>

<%--    // Pie Chart All Transactions by Categories--%>
<%--    var ctx = document.getElementById("PieChartAllTransactions");--%>
<%--    var myPieChart = new Chart(ctx, {--%>
<%--        type: 'doughnut',--%>
<%--        data: {--%>
<%--            labels: [--%>
<%--                <c:forEach items="${categoriesWithAmounts}" var="category">--%>
<%--                "${category.key}", </c:forEach>--%>
<%--            ],--%>
<%--            datasets: [{--%>
<%--                data: [--%>
<%--                    <c:forEach items="${categoriesWithAmounts.values()}" var="amount">--%>
<%--                    ${amount}.toFixed(2), </c:forEach>--%>
<%--                ],--%>
<%--                backgroundColor: ['#4e73df', '#1cc88a', '#36b9cc'],--%>
<%--                hoverBackgroundColor: ['#2e59d9', '#17a673', '#2c9faf'],--%>
<%--                hoverBorderColor: "rgba(234, 236, 244, 1)",--%>
<%--        }]--%>
<%--            },--%>
<%--        options: {--%>
<%--            maintainAspectRatio: false,--%>
<%--            tooltips: {--%>
<%--                backgroundColor: "rgb(255,255,255)",--%>
<%--                bodyFontColor: "#858796",--%>
<%--                borderColor: '#dddfeb',--%>
<%--                borderWidth: 1,--%>
<%--                xPadding: 15,--%>
<%--                yPadding: 15,--%>
<%--                displayColors: false,--%>
<%--                caretPadding: 10,--%>
<%--            },--%>
<%--            legend: {--%>
<%--                display: false--%>
<%--            },--%>
<%--            cutoutPercentage: 80,--%>
<%--        },--%>
<%--    });--%>

<%--</script>--%>
<script>
    // Set new default font family and font color to mimic Bootstrap's default styling
    Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
    Chart.defaults.global.defaultFontColor = '#858796';

    function number_format(number, decimals, dec_point, thousands_sep) {
        // *     example: number_format(1234.56, 2, ',', ' ');
        // *     return: '1 234,56'
        number = (number + '').replace(',', '').replace(' ', '');
        var n = !isFinite(+number) ? 0 : +number,
            prec = !isFinite(+decimals) ? 0 : Math.abs(decimals),
            sep = (typeof thousands_sep === 'undefined') ? ',' : thousands_sep,
            dec = (typeof dec_point === 'undefined') ? '.' : dec_point,
            s = '',
            toFixedFix = function(n, prec) {
                var k = Math.pow(10, prec);
                return '' + Math.round(n * k) / k;
            };
        // Fix for IE parseFloat(0.55).toFixed(0) = 0;
        s = (prec ? toFixedFix(n, prec) : '' + Math.round(n)).split('.');
        if (s[0].length > 3) {
            s[0] = s[0].replace(/\B(?=(?:\d{3})+(?!\d))/g, sep);
        }
        if ((s[1] || '').length < prec) {
            s[1] = s[1] || '';
            s[1] += new Array(prec - s[1].length + 1).join('0');
        }
        return s.join(dec);
    }

    // Area Chart Example
    var ctx = document.getElementById("AreaChartLastYearAllTransactions");
    var myLineChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: [<c:forEach items="${lastYearBalances}" var="month">
                "${month.key}",
                </c:forEach>
    ],
            datasets: [{
                label: "",
                lineTension: 0.3,
                backgroundColor: "rgba(78, 115, 223, 0.05)",
                borderColor: "rgba(78, 115, 223, 1)",
                pointRadius: 3,
                pointBackgroundColor: "rgba(78, 115, 223, 1)",
                pointBorderColor: "rgba(78, 115, 223, 1)",
                pointHoverRadius: 3,
                pointHoverBackgroundColor: "rgba(78, 115, 223, 1)",
                pointHoverBorderColor: "rgba(78, 115, 223, 1)",
                pointHitRadius: 10,
                pointBorderWidth: 2,
                data: [<c:forEach items="${lastYearBalances}" var="month">
                    ${month.value},
                    </c:forEach>],
            }],
        },
        options: {
            maintainAspectRatio: false,
            layout: {
                padding: {
                    left: 10,
                    right: 25,
                    top: 25,
                    bottom: 0
                }
            },
            scales: {
                xAxes: [{
                    time: {
                        unit: 'date'
                    },
                    gridLines: {
                        display: false,
                        drawBorder: false
                    },
                    ticks: {
                        maxTicksLimit: 7
                    }
                }],
                yAxes: [{
                    ticks: {
                        maxTicksLimit: 5,
                        padding: 10,
                        callback: function(value, index, values) {
                            return number_format(value) + ' zł';
                        }
                    },
                    gridLines: {
                        color: "rgb(234, 236, 244)",
                        zeroLineColor: "rgb(234, 236, 244)",
                        drawBorder: false,
                        borderDash: [2],
                        zeroLineBorderDash: [2]
                    }
                }],
            },
            legend: {
                display: false
            },
            tooltips: {
                backgroundColor: "rgb(255,255,255)",
                bodyFontColor: "#858796",
                titleMarginBottom: 10,
                titleFontColor: '#6e707e',
                titleFontSize: 14,
                borderColor: '#dddfeb',
                borderWidth: 1,
                xPadding: 15,
                yPadding: 15,
                displayColors: false,
                intersect: false,
                mode: 'index',
                caretPadding: 10,
                callbacks: {
                    label: function(tooltipItem, chart) {
                        var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
                        return datasetLabel  + number_format(tooltipItem.yLabel) + ' zł';
                    }
                }
            }
        }
    });
</script>
<script type="text/javascript">

    // Set new default font family and font color to mimic Bootstrap's default styling
    Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
    Chart.defaults.global.defaultFontColor = '#858796';

// Pie Chart All Spendings by Categories
var ctx = document.getElementById("PieChartAllTransactions");
var myPieChart = new Chart(ctx, {
type: 'doughnut',
data: {
labels: [
<c:forEach items="${categoriesWithSpendings}" var="category">
    "${category.key}", </c:forEach>
],
datasets: [{
data: [
<c:forEach items="${categoriesWithSpendings.values()}" var="amount">
    ${amount}.toFixed(2), </c:forEach>
],
backgroundColor: ['#4e73df', '#1cc88a', '#36b9cc'],
hoverBackgroundColor: ['#2e59d9', '#17a673', '#2c9faf'],
hoverBorderColor: "rgba(234, 236, 244, 1)",
}]
},
options: {
maintainAspectRatio: false,
tooltips: {
backgroundColor: "rgb(255,255,255)",
bodyFontColor: "#858796",
borderColor: '#dddfeb',
borderWidth: 1,
xPadding: 15,
yPadding: 15,
displayColors: false,
caretPadding: 10,
},
legend: {
display: false
},
cutoutPercentage: 80,
},
});

</script>


<!-- Page level custom scripts -->
<%--<script src="${pageContext.request.contextPath}/js/demo/chart-area-demo.js"></script>--%>
<script src="${pageContext.request.contextPath}/js/demo/chart-bar-demo.js"></script>

</body>

</html>
