/**
 * cart.js — AJAX cập nhật số lượng / xoá item giỏ hàng
 * Website Bán Đồ Thể Thao | Sprint 0 scaffold (logic đầy đủ ở Sprint 3)
 */
(function () {
    'use strict';

    const CART_URL = (window.contextPath || '') + '/cart';

    /**
     * Gọi CartServlet qua fetch(), nhận JSON phản hồi.
     * @param {string} action  - 'add' | 'update' | 'remove'
     * @param {object} params  - { variantId, qty }
     * @returns {Promise<object>}
     */
    async function callCartApi(action, params) {
        const body = new URLSearchParams({ action, ...params });
        const res = await fetch(CART_URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: body.toString()
        });
        if (!res.ok) throw new Error('Lỗi kết nối server');
        return res.json();
    }

    /**
     * Cập nhật hiển thị subtotal của 1 dòng và tổng giỏ hàng.
     */
    function updateCartUI(data) {
        // Cập nhật subtotal từng dòng
        if (data.items) {
            data.items.forEach((item) => {
                const row = document.querySelector(`[data-variant-id="${item.variantId}"]`);
                if (row) {
                    const subtotalEl = row.querySelector('.cart-item__price');
                    if (subtotalEl) subtotalEl.textContent = formatCurrency(item.subtotal);
                }
            });
        }

        // Cập nhật tổng
        const totalEl = document.getElementById('cart-total-amount');
        if (totalEl && data.totalAmount !== undefined) {
            totalEl.textContent = formatCurrency(data.totalAmount);
        }

        // Cập nhật badge số lượng trên navbar
        const badgeEl = document.getElementById('cart-item-count');
        if (badgeEl && data.totalItems !== undefined) {
            badgeEl.textContent = data.totalItems;
            badgeEl.style.display = data.totalItems > 0 ? 'flex' : 'none';
        }

        // Nếu giỏ trống → hiển thị empty state
        if (data.totalItems === 0) {
            const cartItems = document.getElementById('cart-items-container');
            const cartEmpty = document.getElementById('cart-empty');
            if (cartItems) cartItems.style.display = 'none';
            if (cartEmpty) cartEmpty.style.display = 'flex';
        }
    }

    function formatCurrency(amount) {
        return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
    }

    function showError(message) {
        // Sprint 3+ sẽ dùng toast component
        alert(message);
    }

    // ── Xử lý thay đổi số lượng ──────────────────────────────────────────────
    async function handleQtyChange(input) {
        const row = input.closest('[data-variant-id]');
        if (!row) return;

        const variantId = row.dataset.variantId;
        const qty = parseInt(input.value, 10);
        if (isNaN(qty) || qty < 1) { input.value = 1; return; }

        const maxQty = parseInt(input.max, 10) || 999;
        if (qty > maxQty) {
            input.value = maxQty;
            showError(`Chỉ còn ${maxQty} sản phẩm trong kho.`);
            return;
        }

        try {
            input.disabled = true;
            const data = await callCartApi('update', { variantId, qty });
            if (data.success) {
                updateCartUI(data);
            } else {
                showError(data.message || 'Không thể cập nhật giỏ hàng.');
                input.value = data.currentQty || qty;
            }
        } catch (e) {
            showError('Lỗi kết nối. Vui lòng thử lại.');
        } finally {
            input.disabled = false;
        }
    }

    // ── Xử lý xoá item ───────────────────────────────────────────────────────
    async function handleRemove(btn) {
        const row = btn.closest('[data-variant-id]');
        if (!row) return;

        if (!confirm('Bạn có chắc muốn xoá sản phẩm này khỏi giỏ hàng?')) return;

        const variantId = row.dataset.variantId;
        try {
            btn.disabled = true;
            const data = await callCartApi('remove', { variantId });
            if (data.success) {
                row.style.transition = 'opacity 0.3s ease, max-height 0.3s ease';
                row.style.opacity = '0';
                setTimeout(() => { row.remove(); updateCartUI(data); }, 300);
            } else {
                showError(data.message || 'Không thể xoá sản phẩm.');
            }
        } catch (e) {
            showError('Lỗi kết nối. Vui lòng thử lại.');
        } finally {
            btn.disabled = false;
        }
    }

    // ── Init ──────────────────────────────────────────────────────────────────
    document.addEventListener('DOMContentLoaded', () => {
        // Debounce qty input
        let debounceTimer;
        document.addEventListener('change', (e) => {
            if (e.target.matches('.qty-stepper__input[data-cart-qty]')) {
                clearTimeout(debounceTimer);
                debounceTimer = setTimeout(() => handleQtyChange(e.target), 400);
            }
        });

        // Remove buttons
        document.addEventListener('click', (e) => {
            const btn = e.target.closest('.cart-item__remove[data-variant-id]');
            if (btn) handleRemove(btn);
        });
    });

})();
