/**
 * verify-otp.js - Đếm ngược thời gian OTP và chỉ cho phép nhập số
 */
document.addEventListener("DOMContentLoaded", function () {
    const otpInput = document.getElementById("otp");
    const timerElem = document.getElementById("countdownTimer");

    // Chỉ cho phép gõ số vào ô OTP (tự động xóa nếu gõ chữ)
    if (otpInput) {
        otpInput.addEventListener("input", function () {
            this.value = this.value.replace(/[^0-9]/g, "");
        });
    }

    // Đồng hồ đếm ngược thời gian hết hạn mã OTP
    if (timerElem) {
        let secondsLeft = parseInt(timerElem.getAttribute("data-remaining") || "300", 10);

        function renderTimer() {
            if (secondsLeft <= 0) {
                timerElem.textContent = "Đã hết hạn";
                timerElem.style.color = "#dc2626";
                clearInterval(countdownInterval);
                return;
            }

            const minutes = String(Math.floor(secondsLeft / 60)).padStart(2, "0");
            const seconds = String(secondsLeft % 60).padStart(2, "0");
            timerElem.textContent = `${minutes}:${seconds}`;
            secondsLeft--;
        }

        renderTimer();
        const countdownInterval = setInterval(renderTimer, 1000);
    }
});
