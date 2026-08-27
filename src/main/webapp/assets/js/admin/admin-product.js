/**
 * admin-product.js — Form thêm/sửa Product + ProductVariant động
 * Website Bán Đồ Thể Thao | Sprint 0 scaffold (đầy đủ ở Sprint 5)
 */
(function () {
    'use strict';
    let variantCount = 0;

    function addVariantRow() {
        const container = document.getElementById('variant-rows');
        if (!container) return;
        variantCount++;
        const row = document.createElement('div');
        row.className = 'variant-row animate-fadeIn';
        row.dataset.index = variantCount;
        row.innerHTML = `
            <input type="text"   name="variants[${variantCount}].size"     class="form-control" placeholder="Size (VD: 40)" data-label="Size" required>
            <input type="text"   name="variants[${variantCount}].color"    class="form-control" placeholder="Màu sắc">
            <input type="number" name="variants[${variantCount}].price"    class="form-control" placeholder="Giá (₫)" min="0" step="1000" required>
            <input type="number" name="variants[${variantCount}].quantity" class="form-control" placeholder="Tồn kho" min="0" required>
            <button type="button" class="variant-row__remove" onclick="this.closest('.variant-row').remove()" title="Xoá dòng">✕</button>
        `;
        container.appendChild(row);
    }

    document.addEventListener('DOMContentLoaded', () => {
        const addBtn = document.getElementById('add-variant-btn');
        if (addBtn) addBtn.addEventListener('click', addVariantRow);

        // Count existing rows
        variantCount = document.querySelectorAll('.variant-row').length;

        // Image upload zone drag & drop
        const zone = document.getElementById('image-upload-zone');
        const fileInput = document.getElementById('product-images');
        if (zone && fileInput) {
            zone.addEventListener('click', () => fileInput.click());
            zone.addEventListener('dragover', (e) => { e.preventDefault(); zone.classList.add('drag-over'); });
            zone.addEventListener('dragleave', () => zone.classList.remove('drag-over'));
            zone.addEventListener('drop', (e) => {
                e.preventDefault();
                zone.classList.remove('drag-over');
                // TODO Sprint 5: preview files
            });
            fileInput.addEventListener('change', () => {
                // TODO Sprint 5: preview selected images
            });
        }
    });
})();
