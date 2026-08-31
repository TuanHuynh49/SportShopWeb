<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.sportshop.dao.CategoryDAO, com.sportshop.dao.ProductDAO, com.sportshop.util.AttributeNames" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    // Khởi tạo dữ liệu trang chủ từ DAO nếu truy cập trực tiếp qua / hoặc index.jsp
    if (request.getAttribute(AttributeNames.CATEGORIES) == null) {
        request.setAttribute(AttributeNames.CATEGORIES, CategoryDAO.getInstance().findAllTree());
        request.setAttribute(AttributeNames.FEATURED_PRODUCTS, ProductDAO.getInstance().findFeatured(8));
        request.setAttribute(AttributeNames.NEW_ARRIVALS, ProductDAO.getInstance().findNewArrivals(8));
        request.setAttribute(AttributeNames.PAGE_TITLE, "Trang Chủ — SportShop");
        request.setAttribute(AttributeNames.VIEW_PATH, "/WEB-INF/views/customer/home.jsp");
    }
%>
<jsp:include page="/WEB-INF/views/common/layout-customer.jsp" />
