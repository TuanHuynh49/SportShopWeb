<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>
        <c:choose>
            <c:when test="${editMode}">Edit Album</c:when>
            <c:otherwise>Add Album</c:otherwise>
        </c:choose>
        | SportShop Admin
    </title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <style>
        .variant-picker-box {
            border: 1px solid #e2e8f0;
            border-radius: 8px;
            background: #f8fafc;
            padding: 16px;
            max-height: 420px;
            overflow-y: auto;
        }
        .variant-item-row {
            display: flex;
            align-items: center;
            padding: 10px 12px;
            background: #fff;
            border: 1px solid #edf2f7;
            border-radius: 6px;
            margin-bottom: 8px;
            transition: all 0.2s;
            cursor: pointer;
        }
        .variant-item-row:hover {
            border-color: #cbd5e1;
            background: #f1f5f9;
        }
        .variant-item-row.selected {
            border-color: #3b82f6;
            background: #eff6ff;
        }
        .variant-checkbox {
            margin-right: 12px;
            transform: scale(1.2);
            cursor: pointer;
        }
        .variant-thumb {
            width: 44px;
            height: 44px;
            object-fit: cover;
            border-radius: 4px;
            border: 1px solid #e2e8f0;
            margin-right: 12px;
            background: #fff;
        }
        .variant-meta {
            flex: 1;
        }
        .variant-name {
            font-weight: 600;
            font-size: 14px;
            color: #0f172a;
        }
        .variant-tags {
            font-size: 12px;
            color: #64748b;
            margin-top: 2px;
        }
        .variant-price {
            font-size: 13px;
            font-weight: 600;
            color: #2563eb;
        }
        .form-section-card {
            background: #fff;
            border: 1px solid #e2e8f0;
            border-radius: 8px;
            padding: 24px;
            margin-bottom: 24px;
        }
        .form-section-title {
            font-size: 16px;
            font-weight: 600;
            color: #0f172a;
            margin: 0 0 16px;
            padding-bottom: 10px;
            border-bottom: 1px solid #f1f5f9;
            display: flex;
            align-items: center;
            gap: 8px;
        }
        .preview-container {
            margin-top: 10px;
            display: inline-block;
            position: relative;
        }
        .preview-img {
            max-width: 280px;
            max-height: 160px;
            border-radius: 6px;
            border: 1px solid #cbd5e1;
            object-fit: cover;
            display: block;
        }
    </style>
</head>
<body class="admin-body">
    <div class="admin-layout">

        <!-- Sidebar -->
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
                <a href="${pageContext.request.contextPath}/admin/products">
                    <span class="admin-nav-icon">▣</span>Products
                </a>
                <a class="active" href="${pageContext.request.contextPath}/admin/albums">
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

        <!-- Main content -->
        <main class="admin-main">
            <header class="admin-topbar">
                <div>
                    <h1 class="admin-topbar-title">
                        <c:choose>
                            <c:when test="${editMode}">Edit Album #${album.albumId}</c:when>
                            <c:otherwise>Add New Album</c:otherwise>
                        </c:choose>
                    </h1>
                    <p class="admin-topbar-subtitle">Workspace / Albums / 
                        <c:choose>
                            <c:when test="${editMode}">Edit</c:when>
                            <c:otherwise>Create</c:otherwise>
                        </c:choose>
                    </p>
                </div>
                <div class="admin-topbar-actions">
                    <a href="${pageContext.request.contextPath}/admin/albums" class="admin-btn admin-btn-secondary">
                        <i class="fa-solid fa-arrow-left"></i> Back to Albums
                    </a>
                </div>
            </header>

            <section class="admin-content">

                <c:if test="${not empty error}">
                    <div class="admin-alert error" style="margin-bottom: 20px; padding: 12px 16px; background: #fef2f2; color: #991b1b; border: 1px solid #fecaca; border-radius: 6px;">
                        <i class="fa-solid fa-triangle-exclamation"></i> ${error}
                    </div>
                </c:if>

                <form method="post" action="${pageContext.request.contextPath}/admin/albums" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="${editMode ? 'update' : 'create'}">
                    <c:if test="${editMode}">
                        <input type="hidden" name="albumId" value="${album.albumId}">
                        <input type="hidden" name="existingImageUrl" value="${album.imageUrl}">
                    </c:if>

                    <!-- Section 1: Basic Album Info -->
                    <div class="form-section-card">
                        <h3 class="form-section-title">
                            <i class="fa-solid fa-circle-info" style="color: #3b82f6;"></i> Thông tin Album / Lookbook
                        </h3>

                        <div class="form-group" style="margin-bottom: 18px;">
                            <label style="display: block; font-weight: 600; margin-bottom: 6px; font-size: 14px;">
                                Tên Album Lookbook <span style="color: #ef4444;">*</span>
                            </label>
                            <input type="text" name="name" value="${album.name}" required
                                   placeholder="Ví dụ: FC BARCELONA X SPOTIFY, SUMMER RUNNING ESSENTIALS..."
                                   class="admin-input" 
                                   style="width: 100%; max-width: 600px; padding: 10px 12px; border: 1px solid #cbd5e1; border-radius: 6px; font-size: 14px;">
                        </div>

                        <div class="form-group" style="margin-bottom: 18px;">
                            <label style="display: block; font-weight: 600; margin-bottom: 6px; font-size: 14px;">
                                Mô tả phối đồ (Lookbook Description)
                            </label>
                            <textarea name="description" rows="3"
                                      placeholder="Mô tả phong cách, sự phối hợp giữa áo, quần, phụ kiện trong set đồ thể thao này..."
                                      style="width: 100%; max-width: 600px; padding: 10px 12px; border: 1px solid #cbd5e1; border-radius: 6px; font-size: 14px;">${album.description}</textarea>
                        </div>

                        <div class="form-group" style="margin-bottom: 8px;">
                            <label style="display: block; font-weight: 600; margin-bottom: 6px; font-size: 14px;">
                                Ảnh bìa Album (Lookbook Cover Photo)
                            </label>
                            <p style="font-size: 12px; color: #64748b; margin: 0 0 8px;">
                                Ảnh này sẽ hiển thị làm banner trượt trên Trang chủ (Hero slider) và trang Lookbook. Ảnh sẽ được tự động lưu lên thư mục <code>Album</code> trên Cloudinary.
                            </p>
                            <input type="file" name="imageFile" id="imageFileInput" accept="image/*"
                                   style="display: block; margin-bottom: 10px;" onchange="previewCoverImage(this)">

                            <!-- Khối hiển thị ảnh bìa hiện tại / xem trước ảnh mới -->
                            <div id="imagePreviewBox" class="preview-container" style="${empty album.imageUrl ? 'display: none;' : ''}">
                                <img id="previewImgTag" 
                                     src="${not empty album.imageUrl ? album.imageUrl : ''}" 
                                     alt="Preview" class="preview-img">
                                <span style="display: block; font-size: 12px; color: #64748b; margin-top: 4px;" id="previewCaption">
                                    ${not empty album.imageUrl ? 'Ảnh bìa hiện tại' : 'Ảnh mới chọn'}
                                </span>
                            </div>
                        </div>
                    </div>

                    <!-- Section 2: Items in Set (Lookbook Variant Picker) -->
                    <div class="form-section-card">
                        <h3 class="form-section-title">
                            <i class="fa-solid fa-shirt" style="color: #3b82f6;"></i> Các món đồ trong Set (Product Variants)
                        </h3>
                        <p style="font-size: 13px; color: #64748b; margin: 0 0 14px;">
                            Tích chọn các sản phẩm & biến thể thuộc bộ phối đồ này. Người mua có thể xem trọn bộ set và đặt mua từng món.
                        </p>

                        <!-- Search Filter -->
                        <div style="margin-bottom: 12px; display: flex; align-items: center; gap: 8px; max-width: 400px;">
                            <div style="position: relative; width: 100%;">
                                <input type="text" id="variantSearchInput" placeholder="Tìm kiếm sản phẩm, size, màu..."
                                       onkeyup="filterVariants()"
                                       style="width: 100%; padding: 8px 12px 8px 32px; border: 1px solid #cbd5e1; border-radius: 6px; font-size: 13px;">
                                <i class="fa-solid fa-magnifying-glass" style="position: absolute; left: 10px; top: 11px; color: #94a3b8; font-size: 13px;"></i>
                            </div>
                            <span id="selectedCountBadge" style="font-size: 12px; font-weight: 600; color: #2563eb; white-space: nowrap; background: #eff6ff; padding: 6px 10px; border-radius: 9999px;">
                                0 selected
                            </span>
                        </div>

                        <!-- Variant List -->
                        <div class="variant-picker-box" id="variantListContainer">
                            <c:choose>
                                <c:when test="${not empty variantList}">
                                    <c:forEach var="v" items="${variantList}">
                                        <!-- Kiểm tra biến thể có đang nằm trong album không -->
                                        <c:set var="isInAlbum" value="false" />
                                        <c:if test="${editMode && not empty album.items}">
                                            <c:forEach var="item" items="${album.items}">
                                                <c:if test="${item.variantId == v.variantId}">
                                                    <c:set var="isInAlbum" value="true" />
                                                </c:if>
                                            </c:forEach>
                                        </c:if>

                                        <label class="variant-item-row ${isInAlbum ? 'selected' : ''}" 
                                               data-search="${v.product.productName.toLowerCase()} ${v.variantName.toLowerCase()} ${v.size.toLowerCase()} ${v.color.toLowerCase()}">
                                            <input type="checkbox" name="variantIds" value="${v.variantId}" 
                                                   class="variant-checkbox" ${isInAlbum ? 'checked' : ''}
                                                   onchange="toggleRowSelection(this)">

                                            <!-- Thumbnail -->
                                            <c:choose>
                                                <c:when test="${not empty v.product.images}">
                                                    <img src="${v.product.images[0].imageUrl}" alt="${v.product.productName}" class="variant-thumb">
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="variant-thumb" style="display: flex; align-items: center; justify-content: center; color: #cbd5e1;">
                                                        <i class="fa-solid fa-image"></i>
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>

                                            <!-- Meta -->
                                            <div class="variant-meta">
                                                <div class="variant-name">${v.product.productName}</div>
                                                <div class="variant-tags">
                                                    Biến thể: <strong>${v.variantName}</strong> &bull; Size: <strong>${v.size}</strong> &bull; Màu: <strong>${v.color}</strong>
                                                </div>
                                            </div>

                                            <!-- Price -->
                                            <div class="variant-price">
                                                <fmt:formatNumber value="${v.price}" pattern="#,###" /> đ
                                            </div>
                                        </label>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <div style="padding: 24px; text-align: center; color: #94a3b8;">
                                        Không tìm thấy sản phẩm nào trong hệ thống.
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <!-- Submit Actions -->
                    <div style="display: flex; align-items: center; gap: 12px; margin-top: 10px;">
                        <button type="submit" class="admin-btn admin-btn-primary" style="padding: 10px 24px; font-size: 14px; font-weight: 600;">
                            <i class="fa-solid fa-floppy-disk"></i>
                            <c:choose>
                                <c:when test="${editMode}">Update Album</c:when>
                                <c:otherwise>Create Album</c:otherwise>
                            </c:choose>
                        </button>
                        <a href="${pageContext.request.contextPath}/admin/albums" class="admin-btn admin-btn-secondary" style="padding: 10px 20px; font-size: 14px;">
                            Cancel
                        </a>
                    </div>
                </form>

            </section>
        </main>
    </div>

    <script>
        function previewCoverImage(input) {
            const previewBox = document.getElementById('imagePreviewBox');
            const previewImg = document.getElementById('previewImgTag');
            const caption = document.getElementById('previewCaption');

            if (input.files && input.files[0]) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    previewImg.src = e.target.result;
                    previewBox.style.display = 'inline-block';
                    if (caption) caption.textContent = 'Ảnh mới chọn';
                };
                reader.readAsDataURL(input.files[0]);
            }
        }

        function toggleRowSelection(checkbox) {
            const row = checkbox.closest('.variant-item-row');
            if (checkbox.checked) {
                row.classList.add('selected');
            } else {
                row.classList.remove('selected');
            }
            updateSelectedCount();
        }

        function filterVariants() {
            const keyword = document.getElementById('variantSearchInput').value.toLowerCase().trim();
            const rows = document.querySelectorAll('.variant-item-row');

            rows.forEach(row => {
                const searchData = row.getAttribute('data-search') || '';
                if (!keyword || searchData.includes(keyword)) {
                    row.style.display = 'flex';
                } else {
                    row.style.display = 'none';
                }
            });
        }

        function updateSelectedCount() {
            const checkedBoxes = document.querySelectorAll('.variant-checkbox:checked');
            const countBadge = document.getElementById('selectedCountBadge');
            if (countBadge) {
                countBadge.textContent = checkedBoxes.length + ' selected';
            }
        }

        // Khởi tạo đếm số món đã chọn khi load trang
        document.addEventListener('DOMContentLoaded', updateSelectedCount);
    </script>
</body>
</html>
