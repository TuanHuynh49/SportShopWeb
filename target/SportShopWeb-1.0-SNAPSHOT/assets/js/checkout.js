/**
 * checkout.js — Validate form checkout + tính lại totalAmount realtime
 * Website Bán Đồ Thể Thao | Sprint 0 scaffold (đầy đủ ở Sprint 4)
 */
(function () {
    'use strict';
    document.addEventListener('DOMContentLoaded', () => {
        // Address selection
        document.querySelectorAll('.address-option').forEach((option) => {
            option.addEventListener('click', () => {
                document.querySelectorAll('.address-option').forEach((o) => o.classList.remove('selected'));
                option.classList.add('selected');
                const radio = option.querySelector('input[type="radio"]');
                if (radio) radio.checked = true;
            });
        });

        // Payment method tabs
        document.querySelectorAll('[data-payment]').forEach((tab) => {
            tab.addEventListener('click', () => {
                document.querySelectorAll('[data-payment]').forEach((t) => t.classList.remove('active'));
                tab.classList.add('active');
                const payInput = document.getElementById('payment-method');
                if (payInput) payInput.value = tab.dataset.payment;
            });
        });
    });
})();
