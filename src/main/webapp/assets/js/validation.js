/**
 * validation.js — Hàm validate dùng chung (email, phone, required, min/max)
 * Website Bán Đồ Thể Thao | Sprint 0 scaffold (áp dụng đầy đủ ở Sprint 6)
 */
(function () {
    'use strict';

    // ── Regex patterns ────────────────────────────────────────────────────────
    const PATTERNS = {
        email:    /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
        phone:    /^(0|\+84)[3-9]\d{8}$/,
        username: /^[a-zA-Z0-9_]{3,30}$/,
        password: /^.{8,}$/,
        positiveInt: /^[1-9]\d*$/,
        numeric:  /^\d+(\.\d{1,2})?$/
    };

    // ── Helper: hiển thị / ẩn lỗi ────────────────────────────────────────────
    function showError(input, message) {
        input.classList.add('is-invalid');
        input.classList.remove('is-valid');
        let errEl = input.parentElement.querySelector('.error-text');
        if (!errEl) {
            errEl = document.createElement('span');
            errEl.className = 'error-text';
            input.parentElement.appendChild(errEl);
        }
        errEl.textContent = message;
        errEl.classList.add('show');
    }

    function clearError(input) {
        input.classList.remove('is-invalid');
        input.classList.add('is-valid');
        const errEl = input.parentElement.querySelector('.error-text');
        if (errEl) errEl.classList.remove('show');
    }

    // ── Validate 1 field ──────────────────────────────────────────────────────
    function validateField(input) {
        const value   = input.value.trim();
        const type    = input.dataset.validate;
        const required = input.hasAttribute('required') || input.dataset.required === 'true';
        const label   = input.dataset.label || input.placeholder || 'Trường này';

        // Required check
        if (required && value === '') {
            showError(input, `${label} không được để trống.`);
            return false;
        }
        if (value === '') { clearError(input); return true; }

        // Pattern check
        switch (type) {
            case 'email':
                if (!PATTERNS.email.test(value)) { showError(input, 'Email không đúng định dạng.'); return false; }
                break;
            case 'phone':
                if (!PATTERNS.phone.test(value)) { showError(input, 'Số điện thoại không hợp lệ (VD: 0901234567).'); return false; }
                break;
            case 'password':
                if (!PATTERNS.password.test(value)) { showError(input, 'Mật khẩu phải có ít nhất 8 ký tự.'); return false; }
                break;
            case 'confirm-password': {
                const target = document.getElementById(input.dataset.match);
                if (target && value !== target.value) { showError(input, 'Mật khẩu xác nhận không khớp.'); return false; }
                break;
            }
            case 'min': {
                const min = parseFloat(input.min);
                if (parseFloat(value) < min) { showError(input, `Giá trị phải ≥ ${min}.`); return false; }
                break;
            }
            case 'positive-int':
                if (!PATTERNS.positiveInt.test(value)) { showError(input, 'Vui lòng nhập số nguyên dương.'); return false; }
                break;
            case 'numeric':
                if (!PATTERNS.numeric.test(value)) { showError(input, 'Vui lòng nhập số hợp lệ.'); return false; }
                break;
        }

        // Length check
        if (input.maxLength > 0 && value.length > input.maxLength) {
            showError(input, `Tối đa ${input.maxLength} ký tự.`);
            return false;
        }
        if (input.minLength > 0 && value.length < input.minLength) {
            showError(input, `Tối thiểu ${input.minLength} ký tự.`);
            return false;
        }

        clearError(input);
        return true;
    }

    // ── Validate cả form ──────────────────────────────────────────────────────
    function validateForm(form) {
        let valid = true;
        form.querySelectorAll('input[required], select[required], textarea[required], [data-validate]').forEach((input) => {
            if (!validateField(input)) valid = false;
        });
        return valid;
    }

    // ── Auto-attach to forms with data-validate-form ──────────────────────────
    document.addEventListener('DOMContentLoaded', () => {
        document.querySelectorAll('form[data-validate-form]').forEach((form) => {
            // Real-time validation on blur
            form.querySelectorAll('input, select, textarea').forEach((input) => {
                input.addEventListener('blur', () => validateField(input));
                input.addEventListener('input', () => {
                    if (input.classList.contains('is-invalid')) validateField(input);
                });
            });

            // Block submit if invalid
            form.addEventListener('submit', (e) => {
                if (!validateForm(form)) {
                    e.preventDefault();
                    e.stopPropagation();
                    // Scroll to first error
                    const firstErr = form.querySelector('.is-invalid');
                    if (firstErr) firstErr.scrollIntoView({ behavior: 'smooth', block: 'center' });
                }
            });
        });
    });

    // ── Export cho dùng thủ công ──────────────────────────────────────────────
    window.Validation = { validateField, validateForm, showError, clearError };

})();
