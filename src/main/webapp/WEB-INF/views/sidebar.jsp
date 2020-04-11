<ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">

    <!-- Sidebar - Brand -->
    <a class="sidebar-brand d-flex align-items-center justify-content-center" href="${pageContext.request.contextPath}/home">
        <div class="sidebar-brand-icon">
            <i class="fas fa-search-dollar"></i>
        </div>
        <div class="sidebar-brand-text mx-3">FinanceScan</div>
    </a>

    <hr class="sidebar-divider my-0">

    <li class="nav-item">
        <a class="nav-link" href="${pageContext.request.contextPath}/home">
            <i class="fas fa-fw fa-home"></i>
            <span>Home</span>
        </a>
    </li>

    <hr class="sidebar-divider">
    <div class="sidebar-heading">Manage</div>

    <li class="nav-item">
        <a class="nav-link" href="${pageContext.request.contextPath}/transaction/list">
            <i class="fas fa-fw fa-coins"></i>
            <span>Transactions</span>
        </a>
    </li>

    <li class="nav-item">
        <a class="nav-link" href="${pageContext.request.contextPath}/category/list">
            <i class="fas fa-fw fa-tag"></i>
            <span>Categories</span>
        </a>
    </li>

    <li class="nav-item">
        <a class="nav-link" href="${pageContext.request.contextPath}/account/list">
            <i class="fas fa-fw fa-piggy-bank"></i>
            <span>Accounts</span>
        </a>
    </li>

    <hr class="sidebar-divider">
    <div class="sidebar-heading">Analyze</div>

    <li class="nav-item">
        <a class="nav-link" href="${pageContext.request.contextPath}/report">
            <i class="fas fa-fw fa-chart-area"></i>
            <span>Reports</span>
        </a>
    </li>

    <hr class="sidebar-divider d-none d-md-block">

    <div class="text-center d-none d-md-inline">
        <button class="rounded-circle border-0" id="sidebarToggle"></button>
    </div>

</ul>