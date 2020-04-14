<!DOCTYPE html>
<html lang="en">

<head>

  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="">
  <meta name="author" content="">

  <title>Login</title>

  <!-- Custom fonts for this template-->
  <link href="${pageContext.request.contextPath}/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
  <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" rel="stylesheet">

  <!-- Custom styles for this template-->
  <link href="${pageContext.request.contextPath}/css/sb-admin-2.min.css" rel="stylesheet">

</head>

<body class="bg-gradient-primary">

<div class="container">
  <div class="row justify-content-center">
      <div class="card o-hidden border-0 shadow-lg my-5">
        <div class="card-body p-0">
          <div class="row">
              <div class="p-5" style="width: 350px; padding-top: 30px; padding-bottom: 20px">
                <div class="text-center">
                  <h1 class="h4 text-gray-900 mb-4">FINANCE SCAN</h1>
                </div>
                <form class="user" method="post">
                  <div class="form-group">
                    <input type="text" name="username" class="form-control form-control-user"
                           aria-describedby="emailHelp" placeholder="Enter User Name..." autocomplete="username">
                  </div>
                  <div class="form-group">
                    <input type="password" name="password" class="form-control form-control-user"
                           placeholder="Password" autocomplete="current-password">
                  </div>
<%--                  <div class="form-group">--%>
<%--                    <div class="custom-control custom-checkbox small">--%>
<%--                      <input type="checkbox" class="custom-control-input" id="customCheck">--%>
<%--                      <label class="custom-control-label" for="customCheck">Remember Me</label>--%>
<%--                    </div>--%>
<%--                  </div>--%>
                  <input type="submit" class="btn btn-primary btn-user btn-block" value="Login" />
                  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
                <hr>
                <div class="text-center">
                  <a class="small" href="${pageContext.request.contextPath}/forgotpassword">Forgot Password?</a>
                </div>
                <div class="text-center">
                  <a class="small" href="${pageContext.request.contextPath}/register">Create an Account!</a>
                </div>
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

</body>

</html>
