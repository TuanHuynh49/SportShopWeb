/**
 * product-filter.js — Filter/sort danh sách sản phẩm (client-side support)
 * Website Bán Đồ Thể Thao | Sprint 0 scaffold (đầy đủ ở Sprint 3)
 */
(function () {
    'use strict';
    // TODO Sprint 3: filter tag click, price range, sort change → submit form hoặc AJAX
    document.addEventListener('DOMContentLoaded', () => {
        // Sort select → auto-submit form
        const sortSelect = document.getElementById('sort-select');
        if (sortSelect) {
            sortSelect.addEventListener('change', () => {
                const form = sortSelect.closest('form') || document.getElementById('filter-form');
                if (form) form.submit();
            });
        }

        // Filter tags
        document.querySelectorAll('.filter-tag[data-filter]').forEach((tag) => {
            tag.addEventListener('click', () => {
                tag.classList.toggle('active');
                const hiddenInput = document.getElementById(tag.dataset.filter + '-input');
                if (hiddenInput) hiddenInput.value = tag.classList.contains('active') ? tag.dataset.value : '';
            });
        });
    });
})();
