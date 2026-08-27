/**
 * main.js — Init chung: navbar toggle, tooltip, scroll effects
 * Website Bán Đồ Thể Thao | Sprint 0 scaffold
 */
(function () {
    'use strict';

    // ── Navbar mobile toggle ──────────────────────────────────────────────────
    function initNavbarToggle() {
        const toggler = document.getElementById('navbar-toggler');
        const nav = document.getElementById('navbar-nav');
        if (!toggler || !nav) return;

        toggler.addEventListener('click', () => {
            const isOpen = nav.classList.toggle('open');
            toggler.setAttribute('aria-expanded', isOpen);
        });

        // Đóng menu khi click ngoài
        document.addEventListener('click', (e) => {
            if (!toggler.contains(e.target) && !nav.contains(e.target)) {
                nav.classList.remove('open');
                toggler.setAttribute('aria-expanded', 'false');
            }
        });
    }

    // ── Admin sidebar toggle (mobile) ─────────────────────────────────────────
    function initAdminSidebar() {
        const toggle = document.getElementById('admin-sidebar-toggle');
        const sidebar = document.getElementById('admin-sidebar');
        if (!toggle || !sidebar) return;

        toggle.addEventListener('click', () => {
            sidebar.classList.toggle('open');
        });
    }

    // ── Auto-close flash messages ─────────────────────────────────────────────
    function initFlashMessages() {
        document.querySelectorAll('.alert[data-auto-close]').forEach((el) => {
            const delay = parseInt(el.dataset.autoClose, 10) || 5000;
            setTimeout(() => {
                el.style.transition = 'opacity 0.3s ease';
                el.style.opacity = '0';
                setTimeout(() => el.remove(), 300);
            }, delay);
        });

        document.querySelectorAll('.alert-close').forEach((btn) => {
            btn.addEventListener('click', () => {
                const alert = btn.closest('.alert');
                if (alert) {
                    alert.style.opacity = '0';
                    setTimeout(() => alert.remove(), 300);
                }
            });
        });
    }

    // ── Sticky header shrink effect ───────────────────────────────────────────
    function initStickyHeader() {
        const header = document.querySelector('.site-header');
        if (!header) return;
        // Chỉ dành cho Sprint 1+; placeholder hiện tại không làm gì
    }

    // ── Active nav link (theo current URL) ────────────────────────────────────
    function initActiveNav() {
        const currentPath = window.location.pathname;
        document.querySelectorAll('.nav-link, .category-nav__link').forEach((link) => {
            if (link.getAttribute('href') && currentPath.includes(link.getAttribute('href'))) {
                link.classList.add('active');
            }
        });
        document.querySelectorAll('.admin-nav-item').forEach((item) => {
            if (item.getAttribute('href') && currentPath.includes(item.getAttribute('href'))) {
                item.classList.add('active');
            }
        });
    }

    // ── Back-to-top button ────────────────────────────────────────────────────
    function initBackToTop() {
        const btn = document.getElementById('back-to-top');
        if (!btn) return;

        window.addEventListener('scroll', () => {
            btn.style.display = window.scrollY > 400 ? 'flex' : 'none';
        }, { passive: true });

        btn.addEventListener('click', () => {
            window.scrollTo({ top: 0, behavior: 'smooth' });
        });
    }

    // ── Quantity stepper (static fallback, cart.js xử lý AJAX) ──────────────
    function initQtyStepper() {
        document.querySelectorAll('.qty-stepper').forEach((stepper) => {
            const input = stepper.querySelector('.qty-stepper__input');
            const minus = stepper.querySelector('[data-action="minus"]');
            const plus  = stepper.querySelector('[data-action="plus"]');
            if (!input) return;

            const min = parseInt(input.min, 10) || 1;
            const max = parseInt(input.max, 10) || 999;

            function update() {
                let val = parseInt(input.value, 10) || min;
                val = Math.max(min, Math.min(max, val));
                input.value = val;
                if (minus) minus.disabled = val <= min;
                if (plus)  plus.disabled  = val >= max;
            }

            if (minus) minus.addEventListener('click', () => { input.value = parseInt(input.value) - 1; update(); input.dispatchEvent(new Event('change')); });
            if (plus)  plus.addEventListener('click',  () => { input.value = parseInt(input.value) + 1; update(); input.dispatchEvent(new Event('change')); });
            input.addEventListener('change', update);
            update();
        });
    }

    // ── Init ──────────────────────────────────────────────────────────────────
    document.addEventListener('DOMContentLoaded', () => {
        initNavbarToggle();
        initAdminSidebar();
        initFlashMessages();
        initActiveNav();
        initBackToTop();
        initQtyStepper();
    });

})();
