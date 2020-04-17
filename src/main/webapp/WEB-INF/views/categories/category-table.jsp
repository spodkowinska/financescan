<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/utils/header.jsp">
    <jsp:param name="pageTitle" value="Categories"/>
</jsp:include>

<style>
    .tag-col {
        padding-top: 0; padding-bottom: 0; vertical-align: middle;
    }
    .tag {
        margin: 0;
        padding: 4px 10px;
        border-radius: 3px;
        color: white;
        font-size: 12px;
    }
    .keyword {
        font-size: 12px;
        background: #EEE;
        font-weight: normal;
        margin-top: 2px;
        margin-bottom: 2px;
        border: 1px solid lightgray;
    }
</style>

<%-- CATEGORY MODAL --%>
<div class="modal fade" id="categoryModal" tabindex="-1" role="dialog" aria-labelledby="categoryModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header alert-secondary">
                <h5 class="modal-title" id="categoryModalLabel">Edit Category</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" id="categoryModalBody">
                <%-- Filled by AJAX from category-edit.jsp --%>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-primary" data-dismiss="modal" id="categoryModalSubmit">Save</button>
            </div>
        </div>
    </div>
</div>

<%-- CATEGORY DELETE MODAL --%>
<div class="modal fade" id="categoryDeleteModal" tabindex="-1" role="dialog" aria-labelledby="categoryDeleteModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header alert-secondary">
                <h5 class="modal-title" id="categoryDeleteModalLabel">Delete Category
                    <span class="small text-gray-500">#</span><span id="categoryDeleteModal-id" class="small text-gray-500"></span></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" id="categoryDeleteModalBody">
                <span id="categoryDeleteModal-assigned">This category is <b>assigned to <b id="categoryDeleteModal-assignedCount">0</b> transactions</b>.<br><br></span>
                <span id="categoryDeleteModal-noAssigned">This category is not assigned to any transaction.<br><br></span>
                <span id="categoryDeleteModal-unreviewed">This category is <b>suggested for <b id="categoryDeleteModal-unreviewedCount">0</b> transactions</b>.<br><br></span>
                <span id="categoryDeleteModal-noUnreviewed">This category is not suggested for any transaction.<br><br></span>
                <span id="categoryDeleteModal-conclusion-safe">It's safe to remove the category without modifying any transaction.</span>
                <span id="categoryDeleteModal-conclusion-decide">Deleting this category will remove it from all transactions. Are you sure you want to delete it?</span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-danger" data-dismiss="modal" id="categoryDeleteModalSubmit">Delete</button>
            </div>
        </div>
    </div>
</div>

<%-- CATEGORY TABLE --%>
<div class="card shadow mb-4">
    <div class="card-header py-3">
        <h6 class="m-0 font-weight-bold text-gray-800" style="float: left">Categories</h6>
        <button class="btn btn-secondary btn-sm" style="float: right; margin-bottom: -6px; margin-top: -6px;"
                data-toggle="modal" data-target="#categoryModal">
            <span class="fa fa-plus"></span> Add new category
        </button>
    </div>
    <div class="card-body">

        <table id="category_table" class="finance_table">
            <!-- TABLE HEADER -->
            <thead>
            <tr>
                <th style="width: 85px">Actions</th>
                <th style="width: 200px">Name</th>
                <th style="width: 400px">Description</th>
                <th>Keywords</th>
            </tr>
            </thead>
            <!-- TABLE DATA -->
            <tbody id="list">
                <c:forEach items="${cl}" var="category">
                    <tr>
                            <%-- COLUMN: ACTIONS --%>

                        <td class="actions">
                            <a data-toggle="modal" data-target="#categoryModal" data-category-id="${category.id}"
                               data-toggle="tooltip" title="Edit category" tabindex="0">
                                <span class="fa fa-edit"></span>
                            </a>
                            <a data-toggle="tooltip" title="Change category color" tabindex="0" style="cursor: default">
                                <input type="color" value="${category.color}" style="display: none"
                                       id="category_color_${category.id}" onchange="updateColor(${category.id})" />
                                <label for="category_color_${category.id}" class="fa fa-paint-roller" style="margin: 0; cursor: pointer;"></label>
                            </a>
                            <a data-toggle="modal" data-target="#categoryDeleteModal" data-category-id="${category.id}"
                               data-toggle="tooltip" title="Delete category" tabindex="0">
                                <span class="fa fa-trash-alt"></span>
                            </a>
                        </td>

                            <%-- COLUMN: NAME --%>

                        <td>

                            <a data-toggle="modal" data-target="#categoryModal" data-category-id="${category.id}"
                               data-toggle="tooltip" title="Edit category" tabindex="0"
                               class="tag" style="background: ${category.color}; color: ${category.fontColor} !important;">
                                    ${category.name}
                            </a>
                        </td>

                            <%-- COLUMN: DESCRIPTION --%>

                        <td>
                            ${category.description}
                        </td>

                            <%-- COLUMN: KEYWORDS --%>

                        <td>
                            <c:forEach items="${category.safeKeywords}" var="keyword">
                                <span class="badge badge-light keyword" data-toggle="tooltip" title="Safe keyword">
                                    <span class="fa fa-sm fa-star text-gray-600"></span> ${keyword}
                                </span>
                            </c:forEach>
                            <c:forEach items="${category.keywords}" var="keyword">
                                <span class="badge badge-light keyword">${keyword}</span>
                            </c:forEach>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="/WEB-INF/views/utils/footer.jsp">
    <jsp:param name="additionalScriptFile" value="categories.js"/>
</jsp:include>
