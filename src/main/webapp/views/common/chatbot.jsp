<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>

<!-- Link CSS của Chatbot -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/chatbot.css">

<!-- Icon nổi CỐ ĐỊNH ở góc dưới phải màn hình -->
<button type="button" class="chatbot-toggler" id="chatToggler" title="Trợ lý tư vấn SportShop">
    <i class="fa-solid fa-comments"></i>
</button>

<!-- Hộp thoại Chatbox CỐ ĐỊNH ngay phía trên icon -->
<div class="chatbot-box" id="chatBox">
    
    <!-- Tiêu đề Chat -->
    <div class="chatbot-header">
        <div class="chatbot-header-title">
            <i class="fa-solid fa-robot"></i>
            <span>Trợ lý SportShop (RAG AI)</span>
        </div>
        <div style="display: flex; align-items: center; gap: 8px;">
            <!-- Nút làm mới/xóa lịch sử chat -->
            <button type="button" class="chatbot-close-btn" id="chatClearBtn" title="Làm mới cuộc trò chuyện" style="font-size: 14px;">
                <i class="fa-solid fa-rotate-right"></i>
            </button>
            <!-- Nút đóng khung chat -->
            <button type="button" class="chatbot-close-btn" id="chatCloseBtn" title="Đóng">&times;</button>
        </div>
    </div>

    <!-- Vùng hiển thị tin nhắn (Được render động qua chatbot.js từ sessionStorage) -->
    <div class="chatbot-messages" id="chatMessages"></div>

    <!-- Ô nhập tin nhắn bọc trong thẻ Form chuẩn HTML -->
    <form class="chatbot-input-area" id="chatForm" data-action="${pageContext.request.contextPath}/rag-chat">
        <input type="text" id="chatInput" placeholder="Nhập câu hỏi..." autocomplete="off" required>
        <button type="submit" id="chatSendBtn">
            <i class="fa-solid fa-paper-plane"></i>
        </button>
    </form>

</div>

<!-- Link file JavaScript điều khiển riêng biệt -->
<script src="${pageContext.request.contextPath}/assets/js/chatbot.js"></script>
