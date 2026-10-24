/**
 * password-toggle.js - Bật/tắt ẩn hiện mật khẩu cho form Đăng nhập & Đăng ký
 */
document.addEventListener("DOMContentLoaded", function () {
    const toggleButtons = document.querySelectorAll(".btn-toggle-password");

    toggleButtons.forEach(function (button) {
        button.addEventListener("click", function (e) {
            e.preventDefault();

            // Tìm ô input mật khẩu nằm chung khối với nút
            const container = button.closest(".input-with-icon");
            const input = container ? container.querySelector("input") : null;
            const icon = button.querySelector("i");

            if (!input || !icon) return;

            // Đổi loại input giữa 'password' (ẩn) và 'text' (hiện)
            const isPassword = input.type === "password";
            input.type = isPassword ? "text" : "password";

            // Đổi icon con mắt: fa-eye (mắt mở) <-> fa-eye-slash (mắt gạch chéo)
            icon.classList.toggle("fa-eye-slash", isPassword);
            icon.classList.toggle("fa-eye", !isPassword);
            button.setAttribute("title", isPassword ? "Ẩn mật khẩu" : "Hiện mật khẩu");

            input.focus();
        });
    });
});
