<ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">

    <!-- Sidebar - Brand -->
    <a class="sidebar-brand d-flex align-items-center justify-content-center" href="index.html">
        <div class="sidebar-brand-icon rotate-n-15">
            <i class="fas fa-laugh-wink"></i>
        </div>
        <div class="sidebar-brand-text mx-3">FinanceScan</div>
    </a>

    <!-- Divider -->
    <hr class="sidebar-divider my-0">

    <!-- Nav Item - Dashboard -->
    <li class="nav-item">
        <a class="nav-link" href="${pageContext.request.contextPath}/home">
            <i class="fas fa-fw fa-tachometer-alt"></i>
            <span>Home</span></a>

    </li>

    <!-- Divider -->
    <hr class="sidebar-divider">

    <!-- Heading -->
    <div class="sidebar-heading">
        Settings
    </div>
    <!-- Nav Item - Pages Collapse Menu -->
    <li class="nav-item">
        <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseTwo"
           aria-expanded="true" aria-controls="collapseTwo">
            <i class="fas fa-fw fa-cog"></i>
            <span>Categories</span>
        </a>
        <div id="collapseTwo" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
            <div class="bg-white py-2 collapse-inner rounded">
                <a class="collapse-item" href="${pageContext.request.contextPath}/category/add">Add New Category</a>
                <a class="collapse-item" href="${pageContext.request.contextPath}/category/list">Category List</a>
                <a class="collapse-item" href="${pageContext.request.contextPath}/category/keyword/add">Add New Keywords</a>
                <a class="collapse-item" href="${pageContext.request.contextPath}/category/keyword/list">Keywords List</a>
            </div>
        </div>
    </li>


    <!-- Nav Item - Utilities Collapse Menu -->
    <li class="nav-item">
        <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseThree"
           aria-expanded="true" aria-controls="collapseThree">
            <i class="fas fa-fw fa-wrench"></i>
            <span>Transactions</span>
        </a>
        <div id="collapseThree" class="collapse" aria-labelledby="headingThree"
             data-parent="#accordionSidebar">
            <div class="bg-white py-2 collapse-inner rounded">
                <a class="collapse-item" href="${pageContext.request.contextPath}/transaction/fileimport">Import Transactions</a>
                <a class="collapse-item" href="${pageContext.request.contextPath}/transaction/add">Add New Transaction</a>
                <a class="collapse-item" href="${pageContext.request.contextPath}/transaction/list">Transaction List</a>
                <a class="collapse-item" href="${pageContext.request.contextPath}/csvsettings">CSV Import Settings</a>
            </div>
        </div>
    </li>

    <li class="nav-item">
        <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseFour"
           aria-expanded="true" aria-controls="collapseFour">
            <i class="fas fa-fw fa-wrench"></i>
            <span>Account</span>
        </a>
        <div id="collapseFour" class="collapse" aria-labelledby="headingFour"
             data-parent="#accordionSidebar">
            <div class="bg-white py-2 collapse-inner rounded">
                <a class="collapse-item" href="${pageContext.request.contextPath}/account/add">Add New Account</a>
                <a class="collapse-item" href="${pageContext.request.contextPath}/account/list">Account List</a>
                <a class="collapse-item" href="utilities-other.html">Other</a>
            </div>
        </div>
    </li>

    <!-- Divider -->
    <hr class="sidebar-divider">

    <!-- Heading -->
    <div class="sidebar-heading">
        Reports
    </div>
    <!-- Nav Item - Charts -->
    <li class="nav-item">
        <a class="nav-link" href="${pageContext.request.contextPath}/report">
            <i class="fas fa-fw fa-chart-area"></i>
            <span>Report</span></a>
    </li>


    <!-- Nav Item - Pages Collapse Menu -->
    <li class="nav-item">
        <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapsePages"
           aria-expanded="true" aria-controls="collapsePages">
            <i class="fas fa-fw fa-folder"></i>
            <span>Pages</span>
        </a>
        <div id="collapsePages" class="collapse" aria-labelledby="headingPages" data-parent="#accordionSidebar">
            <div class="bg-white py-2 collapse-inner rounded">
                <h6 class="collapse-header">Login Screens:</h6>
                <a class="collapse-item" href="login.html">Login</a>
                <a class="collapse-item" href="register.html">Register</a>
                <a class="collapse-item" href="forgot-password.html">Forgot Password</a>
                <div class="collapse-divider"></div>
                <h6 class="collapse-header">Other Pages:</h6>
                <a class="collapse-item" href="404.html">404 Page</a>
                <a class="collapse-item" href="blank.html">Blank Page</a>
            </div>
        </div>
    </li>


    <!-- Nav Item - Tables -->
    <li class="nav-item active">
        <a class="nav-link" href="tables.html">
            <i class="fas fa-fw fa-table"></i>
            <span>Tables</span></a>
    </li>

    <!-- Divider -->
    <hr class="sidebar-divider d-none d-md-block">

    <!-- Sidebar Toggler (Sidebar) -->
    <div class="text-center d-none d-md-inline">
        <button class="rounded-circle border-0" id="sidebarToggle"></button>
    </div>

</ul>