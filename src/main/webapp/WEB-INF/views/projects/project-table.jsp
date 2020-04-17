<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="/WEB-INF/views/utils/header.jsp">
    <jsp:param name="pageTitle" value="Projects"/>
</jsp:include>

<style>
    .footer-entry {
        margin-right: 20px;
    }
    .footer-entry:after {
        color: #dbdded;
        margin-left: 20px;
        content: '|';
    }
    .footer-entry:last-of-type:after {
        content: '';
    }
    .card-footer {
        padding: 5px 10px;
    }
</style>

<%-- PROJECT MODAL --%>
<div class="modal fade" id="projectModal" tabindex="-1" role="dialog" aria-labelledby="projectModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header alert-secondary">
                <h5 class="modal-title" id="projectModalLabel">Edit Project</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" id="projectModalBody">
                <%-- Filled by AJAX from project-edit.jsp --%>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-primary" data-dismiss="modal" id="projectModalSubmit">Save</button>
            </div>
        </div>
    </div>
</div>

<%-- PROJECT DELETE MODAL --%>
<div class="modal fade" id="projectDeleteModal" tabindex="-1" role="dialog" aria-labelledby="projectDeleteModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header alert-secondary">
                <h5 class="modal-title" id="projectDeleteModalLabel">Delete project
                    <span class="small text-gray-500">#</span><span id="projectDeleteModal-id" class="small text-gray-500"></span></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" id="projectDeleteModalBody">
                This operation will <b style="color: red">delete all transactions, categories and accounts</b> related to this project.<br><br>
                ARE YOU SURE YOU WANT TO DELETE ALL THIS DATA?
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-danger" data-dismiss="modal" id="projectDeleteModalSubmit">Delete project and all its contents</button>
            </div>
        </div>
    </div>
</div>

<%-- PROJECT TABLE --%>
<div class="card shadow mb-4">
    <div class="card-header py-3">
        <h6 class="m-0 font-weight-bold text-gray-800" style="float: left">Projects</h6>
        <button class="btn btn-secondary btn-sm" style="float: right; margin-bottom: -6px; margin-top: -6px;"
                data-toggle="modal" data-target="#projectModal">
            <span class="fa fa-plus"></span> Add new project
        </button>
    </div>
    <div class="card-body">
        <c:forEach items="${projectsList}" var="project" step="1" varStatus="counter" begin="0">

            <c:if test="${counter.index % 2 == 0}">
                <div class="card-deck">
            </c:if>

                <div class="card" style="margin-bottom: 10px">
                    <div class="row no-gutters">
                        <div class="col">
                            <div class="card-block px-2" style="padding: 5px">
                                <h4 class="card-title">${project.name} <span class="small text-gray-300">#${project.id}</span>
                                    <a data-toggle="modal" data-target="#projectDeleteModal" data-project-id="${project.id}"
                                       data-toggle="tooltip" title="Delete Project" tabindex="0" class="btn btn-outline-danger btn-sm"
                                       style="float: right; margin-left: 5px; margin-right: -2px; margin-top: 2px; border: 0">
                                        <span class="fa fa-trash-alt"></span> Delete
                                    </a>
                                    <a data-toggle="modal" data-target="#projectModal" data-project-id="${project.id}"
                                       data-toggle="tooltip" title="Edit Project" tabindex="0" class="btn btn-outline-secondary btn-sm"
                                       style="float: right; margin-left: 5px; margin-right: -2px; margin-top: 2px;  border: 0">
                                        <span class="fa fa-edit"></span> Edit
                                    </a>
                                </h4>
                                ${project.description}
                            </div>
                        </div>
                    </div>
<%--                                    <div class="card-footer w-100 h-50 text-muted">--%>
<%--                                        <span class="footer-entry">Transactions: <span class="transaction-count" data-project-id="${project.id}">...</span></span>--%>
<%--                                        <span class="footer-entry">Imports: <span class="import-count" data-project-id="${project.id}">...</span></span>--%>
<%--                                        <span class="footer-entry">Created: <javatime:format value="${project.created}" style="S-" /></span>--%>
<%--                                    </div>--%>
                </div>

            <c:if test="${counter.index % 2 != 0}">
                </div>
            </c:if>

            <c:if test="${counter.index % 2 == 0 && counter.index == projectsList.size() - 1}">
                <div class="card" style="margin-bottom: 10px; visibility: hidden"></div></div>
            </c:if>

        </c:forEach>
    </div>
</div>

<jsp:include page="/WEB-INF/views/utils/footer.jsp">
    <jsp:param name="additionalScriptFile" value="projects.js"/>
</jsp:include>
