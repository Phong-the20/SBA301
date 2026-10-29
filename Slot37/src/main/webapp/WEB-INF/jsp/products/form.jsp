<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="/WEB-INF/jsp/fragments/header.jsp" />

<div class="row justify-content-center">
    <div class="col-md-7">
        <div class="card shadow-sm">
            <div class="card-header bg-dark text-white py-3">
                <h4 class="mb-0 fw-bold">
                    <c:choose>
                        <c:when test="${empty product.id}">Create Product</c:when>
                        <c:otherwise>Edit Product</c:otherwise>
                    </c:choose>
                </h4>
            </div>
            <div class="card-body p-4">
                <form:form method="post" action="${pageContext.request.contextPath}/products/save" modelAttribute="product">
                    <form:hidden path="id"/>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Product Name <span class="text-danger">*</span></label>
                        <form:input path="name" cssClass="form-control" placeholder="Enter product name"/>
                        <form:errors path="name" cssClass="error d-block"/>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Category <span class="text-danger">*</span></label>
                        <form:select path="category" cssClass="form-select">
                            <form:option value="" label="-- Select Category --"/>
                            <form:options items="${categories}"/>
                        </form:select>
                        <form:errors path="category" cssClass="error d-block"/>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Price ($) <span class="text-danger">*</span></label>
                        <form:input path="price" type="number" step="0.01" cssClass="form-control" placeholder="0.00"/>
                        <form:errors path="price" cssClass="error d-block"/>
                    </div>

                    <div class="mb-4 form-check form-switch">
                        <form:checkbox path="active" cssClass="form-check-input" id="activeCheck"/>
                        <label class="form-check-label fw-semibold" for="activeCheck">Active in Catalog</label>
                    </div>

                    <div class="d-flex justify-content-end gap-2">
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-secondary">Cancel</a>
                        <button type="submit" class="btn btn-primary px-4 fw-bold">
                            <i class="bi bi-save me-1"></i>Save Product
                        </button>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/fragments/footer.jsp" />
