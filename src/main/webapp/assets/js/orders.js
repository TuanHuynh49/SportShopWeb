/**
 * orders.js - Lọc danh sách đơn hàng theo trạng thái (Tất cả, Chờ xử lý, Đã giao...)
 */
document.addEventListener("DOMContentLoaded", function () {
    const tabs = document.querySelectorAll(".order-tab");
    const cards = document.querySelectorAll(".order-card");
    const emptyMessage = document.getElementById("filteredEmpty");

    // Hàm lọc đơn hàng theo trạng thái đã chọn
    function filterOrders(selectedStatus) {
        let visibleCount = 0;

        // Duyệt qua từng đơn hàng để ẩn hoặc hiện
        cards.forEach(function (card) {
            const orderStatus = card.dataset.status;
            const isVisible = (selectedStatus === "ALL" || orderStatus === selectedStatus);

            card.style.display = isVisible ? "" : "none";
            if (isVisible) visibleCount++;
        });

        // Cập nhật giao diện tab nào đang được kích hoạt (active)
        tabs.forEach(function (tab) {
            tab.classList.toggle("active", tab.dataset.filter === selectedStatus);
        });

        // Nếu không có đơn hàng nào khớp bộ lọc thì hiện thông báo rỗng
        if (emptyMessage) {
            emptyMessage.style.display = visibleCount === 0 ? "block" : "none";
        }
    }

    // Gắn sự kiện click cho từng tab lọc
    tabs.forEach(function (tab) {
        tab.addEventListener("click", function () {
            filterOrders(tab.dataset.filter);
        });
    });

    // Mặc định ban đầu: hiển thị tất cả đơn hàng ("ALL")
    if (tabs.length > 0) {
        filterOrders("ALL");
    }
});
