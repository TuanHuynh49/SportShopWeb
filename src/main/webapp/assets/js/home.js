/**
 * home.js - Điều khiển trình chiếu Banner chính (Hero Slider) trên trang chủ
 * (Phần Shop by Category đã được chuyển sang 100% Java Servlet & JSP thuần qua tham số ?catPage=...)
 */
let currentHeroIndex = 0;

function showHeroSlide(index) {
    const slides = document.querySelectorAll('.album-hero-slide');
    if (!slides || slides.length <= 1) return;

    slides.forEach(slide => slide.classList.remove('active'));
    currentHeroIndex = (index + slides.length) % slides.length;
    slides[currentHeroIndex].classList.add('active');
}

function prevHeroSlide() {
    showHeroSlide(currentHeroIndex - 1);
}

function nextHeroSlide() {
    showHeroSlide(currentHeroIndex + 1);
}

// Tự động chuyển banner sau mỗi 6 giây
setInterval(function () {
    const slides = document.querySelectorAll('.album-hero-slide');
    if (slides && slides.length > 1) {
        nextHeroSlide();
    }
}, 6000);
