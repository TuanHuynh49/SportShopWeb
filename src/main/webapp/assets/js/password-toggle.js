/**
 * password-toggle.js - Bật/tắt ẩn hiện mật khẩu cho form Đăng nhập & Đăng ký
 */

//chạy sau HTML
document.addEventListener("DOMContentLoaded", function () {
    // Tìm tất cả các nút có class =
    const toggleButtons = document.querySelectorAll(".btn-toggle-password"); 

    // Duyệt qua từng nút
    toggleButtons.forEach(function (button) {
        // lắng nghe sự kiện click
        button.addEventListener("click", function (e) {
            e.preventDefault(); //Tránh trường hợp nút bấm bị hiểu nhầm là nút gửi form

            // Tìm ô input mật khẩu nằm chung khối với nút
            const container = button.closest(".input-with-icon");
            const input = container ? container.querySelector("input") : null;
            const icon = button.querySelector("i");

            if (!input || !icon) return; //tránh lỗi văn js

            // Đổi loại input giữa 'password' (ẩn) và 'text' (hiện)
            const isPassword = input.type === "password"; //che
            input.type = isPassword ? "text" : "password"; //đổi sang type -> ko che

            // Đổi icon con mắt: fa-eye (mắt mở) <-> fa-eye-slash (mắt gạch chéo)
            icon.classList.toggle("fa-eye-slash", isPassword);
            icon.classList.toggle("fa-eye", !isPassword);
            button.setAttribute("title", isPassword ? "Ẩn mật khẩu" : "Hiện mật khẩu");

            input.focus(); // điều trỏ chuột
        });
    });
});
