<%-- 
    Document   : profile
    Created on : 10 Oct 2026, 3:37:04 PM
    Author     : Anh Tuan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>My Profile - SPORTSHOP</title>
        <!-- Nhúng CSS dùng chung và CSS riêng cho Account -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/common.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/account.css">
    </head>
    <body>

        <!-- Nhúng Header dùng chung -->
        <jsp:include page="/views/common/header.jsp" />

        <!-- Phần nội dung chính của Trang Hồ sơ cá nhân -->
        <main class="profile-wrapper">
            
            <!-- Kiểm tra: Nếu chưa đăng nhập thì hiện thông báo yêu cầu đăng nhập -->
            <c:if test="${empty sessionScope.customer && empty sessionScope.user}">
                <div class="auth-card auth-card-center">
                    <div class="auth-header">
                        <h2>Sign In Required</h2>
                        <p>Please sign in to access and manage your profile details.</p>
                    </div>
                    <a href="${pageContext.request.contextPath}/LoginServlet" class="auth-btn auth-btn-block">
                        Go to Sign In
                    </a>
                </div>
            </c:if>

            <!-- Nếu đã đăng nhập: Hiển thị giao diện hồ sơ đầy đủ -->
            <c:if test="${not empty sessionScope.customer || not empty sessionScope.user}">
                
                <!-- Bố cục 2 cột: Sidebar trái & Nội dung chính phải -->
                <div class="profile-layout">
                    
                    <!-- Cột 1: Sidebar điều hướng tài khoản -->
                    <aside class="profile-sidebar">
                        <div class="profile-avatar-box">
                            <div class="avatar-circle">
                                <i class="fa-solid fa-user"></i>
                            </div>
                            <h3 class="profile-name">
                                <c:choose>
                                    <c:when test="${not empty sessionScope.customer}">
                                        ${sessionScope.customer.firstName} ${sessionScope.customer.lastName}
                                    </c:when>
                                    <c:otherwise>
                                        Member
                                    </c:otherwise>
                                </c:choose>
                            </h3>
                            <p class="profile-email">
                                <c:choose>
                                    <c:when test="${not empty sessionScope.user}">
                                        ${sessionScope.user.email}
                                    </c:when>
                                    <c:otherwise>
                                        ${sessionScope.customer.user.email}
                                    </c:otherwise>
                                </c:choose>
                            </p>
                            <!-- Huy hiệu hạng thành viên -->
                            <span class="tier-badge">
                                <i class="fa-solid fa-medal"></i> 
                                <c:choose>
                                    <c:when test="${not empty sessionScope.customer.membershipTier}">
                                        ${sessionScope.customer.membershipTier.tierName}
                                    </c:when>
                                    <c:otherwise>
                                        Bronze Member
                                    </c:otherwise>
                                </c:choose>
                            </span>
                        </div>

                        <!-- Danh sách menu con trong tài khoản -->
                        <ul class="profile-nav-list">
                            <li class="profile-nav-item ${empty activeTab || activeTab == 'info' ? 'active' : ''}">
                                <a href="${pageContext.request.contextPath}/ProfileServlet?tab=info">
                                    <i class="fa-solid fa-id-card"></i> Personal Info
                                </a>
                            </li>
                            <li class="profile-nav-item ${activeTab == 'password' ? 'active' : ''}">
                                <a href="${pageContext.request.contextPath}/ProfileServlet?tab=password">
                                    <i class="fa-solid fa-key"></i> Change Password
                                </a>
                            </li>
                            <li class="profile-nav-item">
                                <a href="${pageContext.request.contextPath}/addresses">
                                    <i class="fa-solid fa-location-dot"></i> Address Book
                                </a>
                            </li>
                            <li class="profile-nav-item">
                                <a href="${pageContext.request.contextPath}/orders">
                                    <i class="fa-solid fa-box-archive"></i> Order History
                                </a>
                            </li>
                            <li class="profile-nav-item logout">
                                <a href="${pageContext.request.contextPath}/LogoutServlet">
                                    <i class="fa-solid fa-right-from-bracket"></i> Sign Out
                                </a>
                            </li>
                        </ul>
                    </aside>

                    <!-- Cột 2: Nội dung chính -->
                    <section class="profile-main-content">
                        
                        <!-- Thông báo cập nhật thành công hoặc lỗi (nếu có) -->
                        <c:if test="${not empty message}">
                            <div class="auth-alert-success">
                                <i class="fa-solid fa-circle-check"></i> ${message}
                            </div>
                        </c:if>
                        <c:if test="${not empty error}">
                            <div class="auth-alert-error">
                                <i class="fa-solid fa-circle-exclamation"></i> ${error}
                            </div>
                        </c:if>

                        <!-- TAB 1: THÔNG TIN CÁ NHÂN -->
                        <div id="personalInfoSection" style="${activeTab == 'password' ? 'display: none;' : 'display: block;'}">
                            <!-- Khối Form cập nhật thông tin -->
                            <div class="profile-card">
                                <h3 class="profile-card-title">Personal Information</h3>
                                
                                <form action="${pageContext.request.contextPath}/ProfileServlet" method="post">
                                    <input type="hidden" name="action" value="updateProfile">
                                    
                                    <div class="form-row">
                                        <div class="form-group">
                                            <label class="form-label" for="firstName">First Name</label>
                                            <input type="text" 
                                                   id="firstName" 
                                                   name="firstName" 
                                                   class="form-input" 
                                                   value="${sessionScope.customer.firstName}" 
                                                   required>
                                        </div>
                                        <div class="form-group">
                                            <label class="form-label" for="lastName">Last Name</label>
                                            <input type="text" 
                                                   id="lastName" 
                                                   name="lastName" 
                                                   class="form-input" 
                                                   value="${sessionScope.customer.lastName}" 
                                                   required>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="form-label" for="email">Email Address (Read-only)</label>
                                        <input type="email" 
                                               id="email" 
                                               class="form-input form-input-readonly" 
                                               value="${not empty sessionScope.user ? sessionScope.user.email : sessionScope.customer.user.email}" 
                                               readonly>
                                    </div>

                                    <div class="form-row">
                                        <div class="form-group">
                                            <label class="form-label" for="phone">Phone Number</label>
                                            <input type="tel" 
                                                   id="phone" 
                                                   name="phone" 
                                                   class="form-input" 
                                                   value="${not empty sessionScope.user ? sessionScope.user.phone : sessionScope.customer.user.phone}">
                                        </div>
                                        <div class="form-group">
                                            <label class="form-label" for="gender">Gender</label>
                                            <select id="gender" name="gender" class="form-select">
                                                <option value="Male" ${sessionScope.customer.gender == 'Male' ? 'selected' : ''}>Male</option>
                                                <option value="Female" ${sessionScope.customer.gender == 'Female' ? 'selected' : ''}>Female</option>
                                                <option value="Other" ${sessionScope.customer.gender == 'Other' ? 'selected' : ''}>Other</option>
                                            </select>
                                        </div>
                                    </div>

                                    <button type="submit" class="profile-save-btn">Save Changes</button>

                                </form>
                            </div>

                            <!-- Khối thông tin hạng thành viên & ưu đãi -->
                            <div class="profile-card" style="margin-top: 28px;">
                                <h3 class="profile-card-title">Membership Tier & Benefits</h3>
                                <p class="tier-status-text">
                                    Your current status: <strong class="tier-status-highlight"><c:choose><c:when test="${not empty sessionScope.customer.membershipTier}">${sessionScope.customer.membershipTier.tierName}</c:when><c:otherwise>Bronze Member</c:otherwise></c:choose></strong>
                                </p>
                                <p class="tier-discount-text">
                                    Discount rate: <span class="tier-discount-rate">${not empty sessionScope.customer.membershipTier ? sessionScope.customer.membershipTier.discountRate : 0}% OFF</span> on all orders.
                                </p>
                            </div>
                        </div>

                        <!-- TAB 2: ĐỔI MẬT KHẨU -->
                        <div id="changePasswordSection" style="${activeTab == 'password' ? 'display: block;' : 'display: none;'}">
                            <div class="profile-card">
                                <h3 class="profile-card-title">Change Password</h3>
                                <p style="color: #6b7280; font-size: 0.9rem; margin-top: -12px; margin-bottom: 24px;">
                                    For your security, please choose a strong password that is at least 6 characters long.
                                </p>

                                <form action="${pageContext.request.contextPath}/ProfileServlet" method="post">
                                    <input type="hidden" name="action" value="changePassword">

                                    <div class="form-group">
                                        <label class="form-label" for="currentPassword">Current Password</label>
                                        <input type="password" 
                                               id="currentPassword" 
                                               name="currentPassword" 
                                               class="form-input" 
                                               placeholder="Enter current password" 
                                               required>
                                    </div>

                                    <div class="form-group">
                                        <label class="form-label" for="newPassword">New Password</label>
                                        <input type="password" 
                                               id="newPassword" 
                                               name="newPassword" 
                                               class="form-input" 
                                               placeholder="At least 6 characters" 
                                               required>
                                    </div>

                                    <div class="form-group">
                                        <label class="form-label" for="confirmPassword">Confirm New Password</label>
                                        <input type="password" 
                                               id="confirmPassword" 
                                               name="confirmPassword" 
                                               class="form-input" 
                                               placeholder="Re-enter new password" 
                                               required>
                                    </div>

                                    <button type="submit" class="profile-save-btn">
                                        <i class="fa-solid fa-key" style="margin-right: 6px;"></i> Update Password
                                    </button>
                                </form>
                            </div>
                        </div>

                    </section>
                </div>

            </c:if>

        </main>

        <!-- Nhúng Footer dùng chung -->
        <jsp:include page="/views/common/footer.jsp" />

    </body>
</html>
