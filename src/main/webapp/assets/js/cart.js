/**
 * cart.js - Xử lý chọn sản phẩm và gửi dữ liệu giỏ hàng
 */
document.addEventListener("DOMContentLoaded", function () {
    const checkboxes = document.querySelectorAll(".product-checkbox");
    const selectAllBtn = document.getElementById("selectAllBtn");
    const updateTotalForm = document.getElementById("updateTotalForm");
    const checkoutForm = document.getElementById("proceedCheckoutForm");
    const quantityForms = document.querySelectorAll(".quantity-form");

    // Cập nhật nút "Chọn tất cả" / "Bỏ chọn tất cả"
    function updateSelectAllButton() {
        if (!selectAllBtn || checkboxes.length === 0) return;

        // Đếm số checkbox đang được tick
        const allChecked = Array.from(checkboxes).every(cb => cb.checked);

        selectAllBtn.textContent = allChecked ? "Bỏ chọn tất cả" : "Chọn tất cả";
        selectAllBtn.classList.toggle("selected", allChecked);
        selectAllBtn.setAttribute("aria-pressed", String(allChecked));
    }

    // Sự kiện khi click nút "Chọn tất cả"
    if (selectAllBtn) {
        selectAllBtn.addEventListener("click", function () {
            const allChecked = Array.from(checkboxes).every(cb => cb.checked);
            // Nếu đã chọn hết thì bỏ chọn tất cả, ngược lại thì chọn tất cả
            checkboxes.forEach(cb => cb.checked = !allChecked);
            updateSelectAllButton();
        });
    }

    // Khi người dùng click từng checkbox riêng lẻ
    checkboxes.forEach(cb => cb.addEventListener("change", updateSelectAllButton));
    updateSelectAllButton();

    // Hàm đính kèm các mã sản phẩm đã chọn (hidden input) vào form trước khi gửi lên Server
    function appendSelectedIds(form) {
        if (!form) return 0;

        // Xóa các input ẩn cũ (nếu có)
        form.querySelectorAll("[data-selected-id]").forEach(el => el.remove());

        let count = 0;
        checkboxes.forEach(function (cb) {
            if (cb.checked) {
                const hiddenInput = document.createElement("input");
                hiddenInput.type = "hidden";
                hiddenInput.name = "selectedVariantIds";
                hiddenInput.value = cb.value;
                hiddenInput.setAttribute("data-selected-id", "true");
                form.appendChild(hiddenInput);
                count++;
            }
        });
        return count;
    }

    // Bắt sự kiện submit form "Cập nhật tổng tiền" và "Đặt hàng"
    [updateTotalForm, checkoutForm].forEach(function (form) {
        if (form) {
            form.addEventListener("submit", function (e) {
                const count = appendSelectedIds(form);
                if (count === 0) {
                    e.preventDefault();
                    alert("Vui lòng chọn ít nhất một sản phẩm!");
                }
            });
        }
    });

    // Khi tăng/giảm số lượng sản phẩm, giữ nguyên danh sách sản phẩm đang được chọn
    quantityForms.forEach(function (form) {
        form.addEventListener("submit", function () {
            appendSelectedIds(form);
        });
    });
});
