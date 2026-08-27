/**
 * voucher.js — AJAX áp mã giảm giá tại trang checkout
 * Website Bán Đồ Thể Thao | Sprint 0 scaffold (đầy đủ ở Sprint 4)
 */
(function () {
    'use strict';

    const VOUCHER_URL = (window.contextPath || '') + '/voucher';

    function formatCurrency(amount) {
        return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
    }

    function setVoucherState(state, data) {
        const msgEl   = document.getElementById('voucher-message');
        const discEl  = document.getElementById('discount-amount');
        const totalEl = document.getElementById('total-amount');
        const discRow = document.getElementById('discount-row');

        if (state === 'valid') {
            if (msgEl)   { msgEl.textContent = '✓ ' + (data.message || 'Áp mã thành công'); msgEl.className = 'form-text text-success'; }
            if (discRow) discRow.style.display = 'flex';
            if (discEl)  discEl.textContent = '- ' + formatCurrency(data.discountAmount);
            if (totalEl) totalEl.textContent = formatCurrency(data.newTotal);
        } else if (state === 'invalid') {
            if (msgEl)   { msgEl.textContent = '✗ ' + (data.message || 'Mã không hợp lệ'); msgEl.className = 'form-text text-danger'; }
            if (discRow) discRow.style.display = 'none';
            if (discEl)  discEl.textContent = '- 0 ₫';
        } else {
            if (msgEl)   msgEl.textContent = '';
        }
    }

    async function applyVoucher() {
        const input = document.getElementById('voucher-code');
        const btn   = document.getElementById('voucher-apply-btn');
        if (!input || !btn) return;

        const code = input.value.trim().toUpperCase();
        if (!code) { setVoucherState('invalid', { message: 'Vui lòng nhập mã voucher.' }); return; }

        btn.disabled = true;
        btn.classList.add('btn-loading');

        try {
            const body = new URLSearchParams({ action: 'apply', code });
            const res = await fetch(VOUCHER_URL, {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: body.toString()
            });
            const data = await res.json();
            setVoucherState(data.valid ? 'valid' : 'invalid', data);
        } catch (e) {
            setVoucherState('invalid', { message: 'Lỗi kết nối. Vui lòng thử lại.' });
        } finally {
            btn.disabled = false;
            btn.classList.remove('btn-loading');
        }
    }

    async function removeVoucher() {
        try {
            const body = new URLSearchParams({ action: 'remove' });
            await fetch(VOUCHER_URL, {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: body.toString()
            });
        } catch (e) { /* silent */ }

        const input = document.getElementById('voucher-code');
        if (input) input.value = '';
        setVoucherState('none', {});
        // Restore original total từ data-original attribute
        const totalEl = document.getElementById('total-amount');
        if (totalEl && totalEl.dataset.original) {
            totalEl.textContent = formatCurrency(parseFloat(totalEl.dataset.original));
        }
    }

    document.addEventListener('DOMContentLoaded', () => {
        const applyBtn  = document.getElementById('voucher-apply-btn');
        const removeBtn = document.getElementById('voucher-remove-btn');
        const input     = document.getElementById('voucher-code');

        if (applyBtn) applyBtn.addEventListener('click', applyVoucher);
        if (removeBtn) removeBtn.addEventListener('click', removeVoucher);
        if (input) {
            input.addEventListener('keydown', (e) => { if (e.key === 'Enter') { e.preventDefault(); applyVoucher(); } });
        }
    });

})();
