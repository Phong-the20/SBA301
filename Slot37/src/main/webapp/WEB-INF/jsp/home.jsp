<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/jsp/fragments/header.jsp" />

<div class="p-5 mb-4 bg-white rounded-3 shadow-sm border">
    <div class="container-fluid py-2">
        <h1 class="display-6 fw-bold text-dark">${pageTitle}</h1>
        <p class="col-md-8 fs-5 text-secondary">
            Spring MVC server-side rendering application utilizing JSP, JSTL declarative presentation, and MongoDB document persistence with Layered Architecture.
        </p>
        <a class="btn btn-primary btn-lg" href="${pageContext.request.contextPath}/products">
            <i class="bi bi-grid-3x3-gap-fill me-2"></i>Manage Products
        </a>
    </div>
</div>

<div class="row g-4 mb-4">
    <div class="col-md-6">
        <div class="card shadow-sm border-start border-primary border-4">
            <div class="card-body">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <h6 class="text-muted text-uppercase mb-1 fw-bold">Total Products</h6>
                        <h2 class="fw-bold mb-0">${productCount}</h2>
                    </div>
                    <i class="bi bi-boxes fs-1 text-primary"></i>
                </div>
            </div>
        </div>
    </div>
    <div class="col-md-6">
        <div class="card shadow-sm border-start border-success border-4">
            <div class="card-body">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <h6 class="text-muted text-uppercase mb-1 fw-bold">Active Products</h6>
                        <h2 class="fw-bold mb-0">${activeCount}</h2>
                    </div>
                    <i class="bi bi-check-circle-fill fs-1 text-success"></i>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/fragments/footer.jsp" />
