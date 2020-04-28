<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

            </div>
            <!-- /.container-fluid -->

        </div>
        <!-- End of Main Content -->

    <!-- Footer -->
    <footer class="sticky-footer bg-white">
        <div class="container my-auto">
            <div class="copyright text-center my-auto">
                <span>FinanceScan by Sandra Podkowińska</span>
            </div>
        </div>
    </footer>
    <!-- End of Footer -->

    </div>
<!-- End of Content Wrapper -->

</div>
<!-- End of Page Wrapper -->

<!-- Scroll to Top Button-->
<a class="scroll-to-top rounded" href="#page-top">
    <i class="fas fa-angle-up"></i>
</a>

<!-- Bootstrap core JavaScript-->
<script src="${pageContext.request.contextPath}/vendor/jquery/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

<!-- Core plugin JavaScript-->
<script src="${pageContext.request.contextPath}/vendor/jquery-easing/jquery.easing.min.js"></script>

<!-- Custom scripts for all pages-->
<script src="${pageContext.request.contextPath}/js/sb-admin-2.min.js"></script>

<script src="${pageContext.request.contextPath}/js/main.js"></script>

<c:if test="${param.additionalScriptFile != null && param.additionalScriptFile != ''}">
    <c:forEach items="${fn:split(param.additionalScriptFile,';')}" var="scriptFile">
        <script src="${pageContext.request.contextPath}/js/${fn:trim(scriptFile)}"></script>
    </c:forEach>
</c:if>

</body>

</html>
