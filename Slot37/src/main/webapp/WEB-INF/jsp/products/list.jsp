<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/jsp/fragments/header.jsp" />

<div class="d-flex justify-content-between align-items-center mb-3">
    <h3 class="fw-bold text-dark mb-0">Products Catalog</h3>
    <a href="${pageContext.request.contextPath}/products/create" class="btn btn-success">
        <i class="bi bi-plus-circle me-1"></i>New Product
    </a>
</div>

<!-- Flash Message -->
<c:if test="${not empty successMessage}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        <i class="bi bi-check-circle-fill me-2"></i>${successMessage}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>

<!-- Search Form (GET) -->
<div class="card shadow-sm mb-4">
    <div class="card-body">
        <form action="${pageContext.request.contextPath}/products" method="get" class="row g-2">
            <div class="col-md-9">
                <input type="text" name="keyword" value="${keyword}" class="form-control" placeholder="Search by name...">
            </div>
            <div class="col-md-3 d-flex gap-2">
                <button type="submit" class="btn btn-primary w-100"><i class="bi bi-search me-1"></i>Search</button>
                <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-secondary">Reset</a>
            </div>
        </form>
    </div>
</div>

<!-- Products Table -->
<div class="card shadow-sm">
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-hover table-striped align-middle mb-0">
                <thead class="table-dark">
                <tr>
                    <th style="width: 15%;">ID</th>
                    <th style="width: 35%;">Name</th>
                    <th style="width: 20%;">Category</th>
                    <th style="width: 15%;">Price</th>
                    <th style="width: 15%;" class="text-center">Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${empty products}">
                        <tr>
                            <td colspan="5" class="text-center py-4 text-muted">
                                <i class="bi bi-inbox fs-2 d-block mb-1"></i>No products found.
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${products}" var="p">
                            <tr>
                                <td class="fw-semibold text-secondary">${p.id}</td>
                                <td class="fw-bold">${p.name}</td>
                                <td><span class="badge bg-secondary">${p.category}</span></td>
                                <td class="fw-bold text-success">$${p.price}</td>
                                <td class="text-center">
                                    <div class="d-inline-flex gap-1">
                                        <a href="${pageContext.request.contextPath}/products/edit/${p.id}" class="btn btn-sm btn-outline-warning" title="Edit">
                                            <i class="bi bi-pencil-square"></i>
                                        </a>
                                        <form action="${pageContext.request.contextPath}/products/delete/${p.id}" method="post" onsubmit="return confirm('Delete this product?');">
                                            <button type="submit" class="btn btn-sm btn-outline-danger" title="Delete">
                                                <i class="bi bi-trash"></i>
                                            </button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/fragments/footer.jsp" />
