/**
 * admin-chart.js — Vẽ biểu đồ thống kê (Chart.js) cho report.jsp
 * Website Bán Đồ Thể Thao | Sprint 0 scaffold (đầy đủ ở Sprint 5)
 */
(function () {
    'use strict';

    function initRevenueChart() {
        const canvas = document.getElementById('revenue-chart');
        if (!canvas || typeof Chart === 'undefined') return;

        // Dữ liệu được nhúng từ JSP qua hidden input / script tag
        const labels  = window.chartData?.revenueLabels  || [];
        const revenue = window.chartData?.revenueValues  || [];

        new Chart(canvas, {
            type: 'line',
            data: {
                labels,
                datasets: [{
                    label: 'Doanh thu (₫)',
                    data: revenue,
                    borderColor: '#1B5E3A',
                    backgroundColor: 'rgba(27,94,58,0.08)',
                    borderWidth: 2.5,
                    pointBackgroundColor: '#1B5E3A',
                    pointRadius: 4,
                    tension: 0.4,
                    fill: true
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false },
                    tooltip: {
                        callbacks: {
                            label: (ctx) => ' ' + new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(ctx.parsed.y)
                        }
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: (val) => new Intl.NumberFormat('vi-VN', { notation: 'compact', compactDisplay: 'short' }).format(val) + ' ₫'
                        },
                        grid: { color: 'rgba(0,0,0,0.05)' }
                    },
                    x: { grid: { display: false } }
                }
            }
        });
    }

    function initOrderStatusChart() {
        const canvas = document.getElementById('order-status-chart');
        if (!canvas || typeof Chart === 'undefined') return;

        const statusData = window.chartData?.orderStatus || {};
        new Chart(canvas, {
            type: 'doughnut',
            data: {
                labels: Object.keys(statusData),
                datasets: [{
                    data: Object.values(statusData),
                    backgroundColor: ['#F9A825', '#0277BD', '#3949AB', '#2E7D32', '#D32F2F', '#BF360C'],
                    borderWidth: 2,
                    borderColor: '#fff'
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { position: 'right', labels: { font: { family: 'Inter' }, padding: 12 } }
                },
                cutout: '65%'
            }
        });
    }

    document.addEventListener('DOMContentLoaded', () => {
        initRevenueChart();
        initOrderStatusChart();
    });
})();
