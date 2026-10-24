/**
 * chatbot.js - Điều khiển giao diện AI Chatbot & Lưu lịch sử bằng sessionStorage
 */
document.addEventListener('DOMContentLoaded', function () {
    const toggler = document.getElementById('chatToggler');
    const box = document.getElementById('chatBox');
    const closeBtn = document.getElementById('chatCloseBtn');
    const clearBtn = document.getElementById('chatClearBtn');
    const form = document.getElementById('chatForm');
    const input = document.getElementById('chatInput');
    const messages = document.getElementById('chatMessages');

    if (!toggler || !box || !form) return;

    const DEFAULT_MSG = 'Xin chào! Tôi là trợ lý ảo SportShop. Bạn cần hỗ trợ gì về sản phẩm hoặc đơn hàng ạ?';
    const endpoint = form.getAttribute('data-action') || '/rag-chat';

    // Hàm tạo và hiển thị tin nhắn lên khung chat
    function addMessage(sender, text) {
        const div = document.createElement('div');
        div.className = 'chat-msg ' + sender;
        div.innerText = text;
        messages.appendChild(div);
        messages.scrollTop = messages.scrollHeight;
    }

    // Tải lại lịch sử trò chuyện từ sessionStorage khi vào trang
    function loadHistory() {
        messages.innerHTML = '';
        const history = JSON.parse(sessionStorage.getItem('chat_history') || '[]');
        if (history.length === 0) {
            addMessage('bot', DEFAULT_MSG);
        } else {
            history.forEach(m => addMessage(m.sender, m.text));
        }
    }

    // Lưu một tin nhắn mới vào sessionStorage
    function saveMessage(sender, text) {
        const history = JSON.parse(sessionStorage.getItem('chat_history') || '[]');
        history.push({ sender: sender, text: text });
        sessionStorage.setItem('chat_history', JSON.stringify(history));
    }

    // Khởi tạo: khôi phục tin nhắn & trạng thái Mở/Đóng từ trước
    loadHistory();
    if (sessionStorage.getItem('chat_open') === 'true') {
        box.classList.add('show');
    }

    // Bật / Tắt khung chat
    toggler.addEventListener('click', function () {
        const isOpen = box.classList.toggle('show');
        sessionStorage.setItem('chat_open', isOpen);
        if (isOpen) {
            setTimeout(() => input.focus(), 100);
        }
    });

    if (closeBtn) {
        closeBtn.addEventListener('click', function () {
            box.classList.remove('show');
            sessionStorage.setItem('chat_open', false);
        });
    }

    // Nút làm mới (xóa lịch sử chat để bắt đầu lại)
    if (clearBtn) {
        clearBtn.addEventListener('click', function () {
            if (confirm('Làm mới cuộc trò chuyện với trợ lý AI?')) {
                sessionStorage.removeItem('chat_history');
                loadHistory();
            }
        });
    }

    // Xử lý gửi câu hỏi tới AI Servlet
    form.addEventListener('submit', function (e) {
        e.preventDefault();
        const text = input.value.trim();
        if (!text) return;

        // Hiện câu hỏi của người dùng và lưu vào storage
        addMessage('user', text);
        saveMessage('user', text);
        input.value = '';

        // Hiệu ứng đang tìm kiếm
        const loading = document.createElement('div');
        loading.className = 'chat-msg bot loading';
        loading.innerText = 'Đang tìm kiếm thông tin... ⏳';
        messages.appendChild(loading);
        messages.scrollTop = messages.scrollHeight;

        // Gửi yêu cầu AJAX ngầm tới Servlet (/rag-chat)
        fetch(endpoint, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
            body: 'query=' + encodeURIComponent(text)
        })
            .then(res => res.text())
            .then(reply => {
                loading.remove();
                const botText = reply.trim() || 'Xin lỗi, tôi chưa có thông tin về vấn đề này.';
                addMessage('bot', botText);
                saveMessage('bot', botText);
            })
            .catch(() => {
                loading.remove();
                addMessage('bot', 'Lỗi kết nối tới máy chủ AI.');
            });
    });
});
