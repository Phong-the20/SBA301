<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${pageTitle != null ? pageTitle : 'Product Management - Slot 37'}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <style>
        .error { color: #dc3545; font-size: 0.875em; margin-top: 0.25rem; }
    </style>
</head>
<body class="bg-light d-flex flex-column min-vh-100">
<nav class="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm mb-4">
    <div class="container">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/">
            <i class="bi bi-diagram-3-fill me-2 text-warning"></i>SBA301 - Slot 37 JSP
        </a>
        <div class="navbar-nav ms-auto">
            <a class="nav-link" href="${pageContext.request.contextPath}/">Dashboard</a>
            <a class="nav-link" href="${pageContext.request.contextPath}/products">Products</a>
            <a class="nav-link" href="${pageContext.request.contextPath}/products/create">+ Add Product</a>
        </div>
    </div>
</nav>
<div class="container flex-grow-1">
