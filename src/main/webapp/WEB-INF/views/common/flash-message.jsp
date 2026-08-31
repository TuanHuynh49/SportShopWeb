<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:if test="${not empty sessionScope.flashSuccess}">
    <div class="alert alert-success animate-slideDown" data-auto-close="5000" role="alert" id="flash-success">
        <span class="alert-icon">✓</span>
        <span class="alert-content">${sessionScope.flashSuccess}</span>
        <button type="button" class="alert-close" aria-label="Đóng">✕</button>
    </div>
    <c:remove var="flashSuccess" scope="session"/>
</c:if>

<c:if test="${not empty sessionScope.flashError}">
    <div class="alert alert-danger animate-slideDown" data-auto-close="6000" role="alert" id="flash-error">
        <span class="alert-icon">✗</span>
        <span class="alert-content">${sessionScope.flashError}</span>
        <button type="button" class="alert-close" aria-label="Đóng">✕</button>
    </div>
    <c:remove var="flashError" scope="session"/>
</c:if>

<c:if test="${not empty sessionScope.flashWarning}">
    <div class="alert alert-warning animate-slideDown" data-auto-close="5000" role="alert" id="flash-warning">
        <span class="alert-icon">⚠</span>
        <span class="alert-content">${sessionScope.flashWarning}</span>
        <button type="button" class="alert-close" aria-label="Đóng">✕</button>
    </div>
    <c:remove var="flashWarning" scope="session"/>
</c:if>

<%-- Request-scope errors (không xoá, dùng khi forward sau lỗi server-side validate) --%>
<c:if test="${not empty requestScope.errorMsg}">
    <div class="alert alert-danger" role="alert">
        <span class="alert-icon">✗</span>
        <span class="alert-content">${requestScope.errorMsg}</span>
    </div>
</c:if>
