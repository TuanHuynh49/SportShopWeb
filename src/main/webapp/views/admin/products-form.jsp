<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>

        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1">

            <title>
                <c:choose>
                    <c:when test="${editMode}">Edit Product</c:when>
                    <c:otherwise>Add Product</c:otherwise>
                </c:choose>
                | SportShop Admin
            </title>

            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">
        </head>

        <body class="admin-body">
            <div class="admin-layout">

                <aside class="admin-sidebar">
                    <div class="admin-brand">
                        <span class="admin-brand-mark">S</span>
                        <div>
                            <div class="admin-brand-name">SPORTSHOP</div>
                            <span class="admin-brand-caption">Administration</span>
                        </div>
                    </div>

                    <div class="admin-nav-label">Workspace</div>

                    <nav class="admin-nav">
                        <a href="${pageContext.request.contextPath}/admin/dashboard">
                            <span class="admin-nav-icon">▦</span>Dashboard
                        </a>

                        <a class="active" href="${pageContext.request.contextPath}/admin/products">
                            <span class="admin-nav-icon">▣</span>Products
                        </a>

                        <a href="${pageContext.request.contextPath}/admin/albums">
                            <span class="admin-nav-icon">☷</span>Albums
                        </a>

                        <a href="${pageContext.request.contextPath}/admin/orders">
                            <span class="admin-nav-icon">⇄</span>Orders
                        </a>

                        <a href="${pageContext.request.contextPath}/admin/customers">
                            <span class="admin-nav-icon">♙</span>Customers
                        </a>
                    </nav>

                    <div class="admin-sidebar-bottom">
                        <nav class="admin-nav">
                            <a href="${pageContext.request.contextPath}/HomeServlet">
                                <span class="admin-nav-icon">↗</span>Back to Store
                            </a>
                        </nav>
                    </div>
                </aside>

                <main class="admin-main">

                    <header class="admin-topbar">
                        <div>
                            <h1 class="admin-topbar-title">
                                <c:choose>
                                    <c:when test="${editMode}">Edit Product</c:when>
                                    <c:otherwise>Add Product</c:otherwise>
                                </c:choose>
                            </h1>

                            <p class="admin-topbar-subtitle">
                                Workspace / Products /
                                <c:choose>
                                    <c:when test="${editMode}">Edit</c:when>
                                    <c:otherwise>Add</c:otherwise>
                                </c:choose>
                            </p>
                        </div>

                        <div class="admin-topbar-actions">
                            <span class="admin-date-label">ADMIN</span>
                        </div>
                    </header>

                    <section class="admin-content">

                        <div class="page-heading-row">
                            <div>
                                <h2 class="admin-page-title">
                                    <c:choose>
                                        <c:when test="${editMode}">
                                            Update Product Information
                                        </c:when>
                                        <c:otherwise>
                                            Product Information
                                        </c:otherwise>
                                    </c:choose>
                                </h2>

                                <p class="admin-page-description">
                                    <c:choose>
                                        <c:when test="${editMode}">
                                            Update the selected product's information.
                                        </c:when>
                                        <c:otherwise>
                                            Enter product information and its initial variant.
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                            </div>

                            <a class="admin-button" href="${pageContext.request.contextPath}/admin/products">
                                Back to Products
                            </a>
                        </div>

                        <section class="admin-panel">

                            <c:if test="${not empty error}">
                                <div class="form-error" role="alert">
                                    <c:out value="${error}" />
                                </div>
                            </c:if>

                            <form class="admin-form" action="${pageContext.request.contextPath}/ProductActionServlet"
                                method="post" enctype="multipart/form-data">

                                <input type="hidden" name="action" value="${editMode ? 'update' : 'create'}">

                                <c:if test="${editMode}">
                                    <input type="hidden" name="productId" value="${product.productId}">
                                </c:if>

                                <div class="admin-form-grid">

                                    <!-- Product information -->

                                    <div class="admin-form-group full">
                                        <label for="productName">Product name *</label>

                                        <input id="productName" name="productName" type="text" maxlength="150" required
                                            value="${param.productName != null ? param.productName : product.productName}"
                                            placeholder="Enter product name">
                                    </div>

                                    <div class="admin-form-group">
                                        <label for="brandId">Brand *</label>

                                        <select id="brandId" name="brandId" required>
                                            <option value="">Select a brand</option>

                                            <c:forEach items="${brands}" var="brand">
                                                <option value="${brand.brandId}" <c:if test="${param.brandId != null
                                            ? param.brandId == brand.brandId
                                            : product.brand.brandId == brand.brandId}">
                                                    selected
                                                    </c:if>>
                                                    <c:out value="${brand.name}" />
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="admin-form-group">
                                        <label for="categoryId">Category *</label>

                                        <select id="categoryId" name="categoryId" required>
                                            <option value="">Select a category</option>

                                            <c:forEach items="${categories}" var="category">
                                                <option value="${category.categoryId}" <c:if test="${param.categoryId != null
                                            ? param.categoryId == category.categoryId
                                            : product.category.categoryId == category.categoryId}">
                                                    selected
                                                    </c:if>>
                                                    <c:out value="${category.name}" />
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="admin-form-group full">
                                        <label for="description">Description</label>

                                        <textarea id="description" name="description"
                                            placeholder="Describe the product"><c:out value="${param.description != null ? param.description : product.description}"/></textarea>
                                    </div>

                                    <!-- Initial variant: only required when creating -->

                                    <c:if test="${not editMode}">
                                        <div class="admin-form-group full">
                                            <h3>Initial Product Variant</h3>
                                            <p class="help-text">
                                                Every new product needs an initial variant.
                                                Stock quantity belongs to the variant, not Product.
                                            </p>
                                        </div>

                                        <div class="admin-form-group">
                                            <label for="variantName">Variant name *</label>

                                            <input id="variantName" name="variantName" type="text" maxlength="100"
                                                required value="${param.variantName}" placeholder="Example: Basic">
                                        </div>

                                        <div class="admin-form-group">
                                            <label for="size">Size *</label>

                                            <input id="size" name="size" type="text" maxlength="50" required
                                                value="${param.size}" placeholder="Example: M, L, XL">
                                        </div>

                                        <div class="admin-form-group">
                                            <label for="color">Color *</label>

                                            <input id="color" name="color" type="text" maxlength="50" required
                                                value="${param.color}" placeholder="Example: Black">
                                        </div>

                                        <div class="admin-form-group">
                                            <label for="price">Price (VND) *</label>

                                            <input id="price" name="price" type="number" min="0" step="1" required
                                                value="${param.price}" placeholder="Example: 250000">
                                        </div>

                                        <div class="admin-form-group">
                                            <label for="quantity">Initial stock *</label>

                                            <input id="quantity" name="quantity" type="number" min="0" step="1" required
                                                value="${param.quantity}" placeholder="Enter stock quantity">
                                        </div>
                                    </c:if>

                                    <!-- Product images -->

                                    <div class="admin-form-group full">
                                        <label for="images">Product images</label>

                                        <c:if test="${editMode and not empty product.images}">
                                            <div style="margin-bottom: 15px;">
                                                <span style="font-size: 13px; font-weight: 600; color: #475569; display: block; margin-bottom: 8px;">
                                                    Ảnh hiện tại (tích chọn "Xóa ảnh" và bấm "Save Changes" để gỡ bỏ):
                                                </span>
                                                <div style="display: flex; flex-wrap: wrap; gap: 14px;">
                                                    <c:forEach var="img" items="${product.images}">
                                                        <div class="img-preview-card" style="border: 1px solid #e2e8f0; border-radius: 8px; padding: 8px; text-align: center; width: 120px; background: #f8fafc; box-shadow: 0 1px 3px rgba(0,0,0,0.05); transition: all 0.2s;">
                                                            <img src="${img.imageUrl}" alt="Product Image" style="width: 104px; height: 104px; object-fit: cover; border-radius: 6px; display: block; margin: 0 auto 6px; transition: all 0.2s;">
                                                            <c:if test="${img.primary}">
                                                                <span style="display: inline-block; font-size: 11px; background: #22c55e; color: #fff; padding: 2px 6px; border-radius: 4px; margin-bottom: 4px;">Ảnh chính</span>
                                                            </c:if>
                                                            <div>
                                                                <label style="font-size: 12px; color: #ef4444; font-weight: 500; cursor: pointer; display: flex; align-items: center; justify-content: center; gap: 4px; margin-top: 4px;">
                                                                    <input type="checkbox" name="deleteImageIds" value="${img.productImageId}" onchange="toggleDeleteImage(this)">
                                                                    Xóa ảnh
                                                                </label>
                                                                <span class="delete-status" style="display: none; font-size: 10px; color: #dc2626; margin-top: 2px;">(Sẽ xóa khi lưu)</span>
                                                            </div>
                                                        </div>
                                                    </c:forEach>
                                                </div>
                                            </div>
                                            <script>
                                                function toggleDeleteImage(cb) {
                                                    var card = cb.closest('.img-preview-card');
                                                    var img = card.querySelector('img');
                                                    var statusText = card.querySelector('.delete-status');
                                                    if (cb.checked) {
                                                        card.style.borderColor = '#ef4444';
                                                        card.style.backgroundColor = '#fef2f2';
                                                        img.style.opacity = '0.35';
                                                        img.style.filter = 'grayscale(100%)';
                                                        if (statusText) statusText.style.display = 'block';
                                                    } else {
                                                        card.style.borderColor = '#e2e8f0';
                                                        card.style.backgroundColor = '#f8fafc';
                                                        img.style.opacity = '1';
                                                        img.style.filter = 'none';
                                                        if (statusText) statusText.style.display = 'none';
                                                    }
                                                }
                                            </script>
                                        </c:if>

                                        <input id="images" name="images" type="file" accept="image/*" multiple>

                                        <span class="help-text">
                                            <c:choose>
                                                <c:when test="${editMode}">
                                                    Select one or more new image files to add to this product. Check "Delete" above if you wish to remove existing images.
                                                </c:when>
                                                <c:otherwise>
                                                    Select one or more image files.
                                                    The first uploaded image becomes the primary image.
                                                </c:otherwise>
                                            </c:choose>
                                        </span>
                                    </div>

                                </div>

                                <div class="form-actions">
                                    <a class="admin-button" href="${pageContext.request.contextPath}/admin/products">
                                        Cancel
                                    </a>

                                    <button class="admin-button primary" type="submit">
                                        <c:choose>
                                            <c:when test="${editMode}">Save Changes</c:when>
                                            <c:otherwise>Create Product</c:otherwise>
                                        </c:choose>
                                    </button>
                                </div>

                            </form>
                        </section>
                    </section>
                </main>
            </div>
        </body>

        </html>