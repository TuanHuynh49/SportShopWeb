/**
 * checkout.js - Xử lý chọn voucher và giữ lại thông tin người nhận khi áp dụng voucher
 */

// Khi người dùng click nút "Áp dụng" một voucher trong danh sách
function selectVoucher(button) {
    const voucherInput = document.getElementById("voucherCode");
    const voucherForm = document.getElementById("voucherForm");

    if (voucherInput && button && button.dataset) {
        voucherInput.value = button.dataset.code;
    }
    if (voucherForm) {
        voucherForm.requestSubmit();
    }
}

document.addEventListener("DOMContentLoaded", function () {
    const voucherForm = document.getElementById("voucherForm");
    if (!voucherForm) return;

    // Trước khi gửi mã voucher lên server, copy thông tin giao hàng hiện tại
    // để sau khi trang reload, người dùng không phải nhập lại họ tên, số điện thoại
    voucherForm.addEventListener("submit", function () {
        const getVal = (id) => (document.getElementById(id) || {}).value || "";
        const getCheckedVal = (name) => (document.querySelector(`input[name="${name}"]:checked`) || {}).value || "";

        const copyMap = {
            "voucherFullName": getVal("fullName"),
            "voucherPhone": getVal("phone"),
            "voucherNote": getVal("note"),
            "voucherAddressId": getCheckedVal("addressId"),
            "voucherPaymentMethod": getCheckedVal("paymentMethod")
        };

        for (const [targetId, val] of Object.entries(copyMap)) {
            const targetInput = document.getElementById(targetId);
            if (targetInput) targetInput.value = val;
        }
    });
});
