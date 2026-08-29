package com.sportshop.util;

/**
 * AttributeNames — Hằng số tên attribute dùng chung giữa Servlet và JSP.
 *
 * Nguyên tắc (§ Ghi chú kỹ thuật #3):
 * - Servlet set: request.setAttribute(AttributeNames.PRODUCT_LIST, list)
 * - JSP sử dụng: ${productList} (tên = giá trị hằng số)
 *
 * Tuyệt đối KHÔNG dùng chuỗi literal khi setAttribute/getAttribute.
 */
public final class AttributeNames {

    private AttributeNames() {
        /* utility class — không khởi tạo */ }

    // ── Layout ──────────────────────────────────────────────────────────────
    /** Path tới JSP con, truyền vào layout-customer/admin để c:import */
    public static final String VIEW_PATH = "viewPath";
    public static final String PAGE_TITLE = "pageTitle";
    public static final String PAGE_DESCRIPTION = "pageDescription";
    public static final String BREADCRUMB = "breadcrumb";
    public static final String LOAD_CHART_JS = "loadChartJs";
    public static final String EXTRA_HEAD = "extraHead";
    public static final String EXTRA_SCRIPTS = "extraScripts";

    // ── Flash Messages (session scope) ───────────────────────────────────────
    public static final String FLASH_SUCCESS = "flashSuccess";
    public static final String FLASH_ERROR = "flashError";
    public static final String FLASH_WARNING = "flashWarning";

    // ── Error / Validation ───────────────────────────────────────────────────
    public static final String ERROR_MSG = "errorMsg";

    // ── Session: User & Auth ────────────────────────────────────────────────
    /** Người dùng đăng nhập trong Session (User object theo chuẩn RBAC) */
    public static final String AUTH_USER = "authUser";
    /** Customer đã đăng nhập (Customer / User object) */
    public static final String SESSION_USER = "authUser";
    /** Admin đã đăng nhập (User object với role ADMIN) */
    public static final String SESSION_ADMIN_USER = "adminUser";
    /** URL đích cần quay lại sau khi đăng nhập thành công */
    public static final String REDIRECT_URL = "redirectUrl";
    /** Tên Cookie ghi nhớ đăng nhập Remember Me */
    public static final String REMEMBER_ME_COOKIE = "sportshop_remember";
    /** Giỏ hàng đang hoạt động (Cart object) */
    public static final String SESSION_CART = "cart";
    /** Voucher đã áp dụng (Voucher object) */
    public static final String SESSION_APPLIED_VOUCHER = "appliedVoucher";

    // ── Navbar ───────────────────────────────────────────────────────────────
    /** List<Category> dùng cho thanh category nav */
    public static final String NAV_CATEGORIES = "navCategories";

    // ── Home Page ────────────────────────────────────────────────────────────
    /** List<Product> sản phẩm nổi bật */
    public static final String FEATURED_PRODUCTS = "featuredProducts";
    /** List<Product> sản phẩm mới nhất */
    public static final String NEW_ARRIVALS = "newArrivals";
    /** List<Category> tất cả danh mục */
    public static final String CATEGORIES = "categories";

    // ── Product List ─────────────────────────────────────────────────────────
    /** List<Product> kết quả tìm kiếm/filter */
    public static final String PRODUCT_LIST = "productList";
    /** List<Brand> tất cả thương hiệu */
    public static final String BRANDS = "brands";
    /** Trang hiện tại (int) */
    public static final String CURRENT_PAGE = "currentPage";
    /** Tổng số trang (int) */
    public static final String TOTAL_PAGE = "totalPage";
    /** Tổng số kết quả (long) */
    public static final String TOTAL_COUNT = "totalCount";

    // ── Product Detail ───────────────────────────────────────────────────────
    /** Product đơn lẻ */
    public static final String PRODUCT = "product";
    /** List<ProductVariant> */
    public static final String VARIANT_LIST = "variantList";
    /** List<ProductImage> */
    public static final String IMAGE_LIST = "imageList";
    /** List<Review> */
    public static final String REVIEW_LIST = "reviewList";
    /** Điểm đánh giá trung bình (Double) */
    public static final String AVG_RATING = "avgRating";

    // ── Cart ─────────────────────────────────────────────────────────────────
    /** List<CartItem> JOIN với ProductVariant+Product */
    public static final String CART_ITEMS = "cartItems";

    // ── Checkout ─────────────────────────────────────────────────────────────
    /** List<Address> của customer đang đăng nhập */
    public static final String ADDRESS_LIST = "addressList";
    /** Tổng tiền sau giảm giá (BigDecimal) */
    public static final String TOTAL_AMOUNT = "totalAmount";

    // ── Order ────────────────────────────────────────────────────────────────
    /** List<Order> */
    public static final String ORDER_LIST = "orderList";
    /** Order đơn lẻ */
    public static final String ORDER = "order";
    /** List<OrderItem> */
    public static final String ORDER_ITEMS = "orderItems";
    /** List<OrderStatusHistory> */
    public static final String STATUS_HISTORY = "statusHistory";
    /** Payment */
    public static final String PAYMENT = "payment";

    // ── Account / Address ────────────────────────────────────────────────────
    public static final String ADDRESS = "address";

    // ── Admin — Dashboard ────────────────────────────────────────────────────
    public static final String REVENUE_TODAY = "revenueToday";
    public static final String ORDER_COUNT_BY_STATUS = "orderCountByStatus";
    public static final String TOP_PRODUCTS = "topProducts";
    public static final String PENDING_ORDER_COUNT = "pendingOrderCount";

    // ── Admin — Category / Brand ─────────────────────────────────────────────
    public static final String CATEGORY_LIST = "categoryList";
    public static final String CATEGORY = "category";
    public static final String BRAND_LIST = "brandList";
    public static final String BRAND = "brand";

    // ── Admin — Product / Variant / Inventory ───────────────────────────────
    public static final String PRODUCT_FORM = "productForm"; /* form thêm/sửa */
    // PRODUCT, VARIANT_LIST dùng lại từ trên

    // ── Admin — Order ────────────────────────────────────────────────────────
    // ORDER, ORDER_ITEMS, STATUS_HISTORY, PAYMENT dùng lại từ trên

    // ── Admin — Customer ─────────────────────────────────────────────────────
    public static final String CUSTOMER_LIST = "customerList";

    // ── Admin — Voucher ───────────────────────────────────────────────────────
    public static final String VOUCHER_LIST = "voucherList";
    public static final String VOUCHER = "voucher";

    // ── Admin — Review ────────────────────────────────────────────────────────
    // REVIEW_LIST dùng lại từ trên

    // ── Admin — Report ────────────────────────────────────────────────────────
    public static final String REVENUE_BY_MONTH = "revenueByMonth";
    public static final String BEST_SELLERS = "bestSellers";
}
