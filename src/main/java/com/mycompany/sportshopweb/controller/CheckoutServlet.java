
package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.AdressDAO;
import com.mycompany.sportshopweb.dao.CartDAO;
import com.mycompany.sportshopweb.dao.OrderDAO;
import com.mycompany.sportshopweb.dao.VoucherDAO;

import com.mycompany.sportshopweb.model.Adress;
import com.mycompany.sportshopweb.model.Cart;
import com.mycompany.sportshopweb.model.CartItem;
import com.mycompany.sportshopweb.model.Customer;
import com.mycompany.sportshopweb.model.Order;
import com.mycompany.sportshopweb.model.OrderStatus;
import com.mycompany.sportshopweb.model.PaymentMethod;
import com.mycompany.sportshopweb.model.ProductVariant;
import com.mycompany.sportshopweb.model.Voucher;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private static final String LOGIN_URL = "/LoginServlet";
    private static final String CHECKOUT_VIEW = "/views/checkout.jsp";
    private static final String CART_VIEW = "/views/cart.jsp";
    private static final String COD_SUCCESS_VIEW = "/views/COD-success.jsp";
    private static final String BANK_SUCCESS_VIEW = "/views/order-success.jsp";

    private static final String ACTION_REVIEW = "review";
    private static final String ACTION_CONFIRM = "confirm";

    private static final String METHOD_COD = "COD";
    private static final String METHOD_BANK = "BANK";

    private VoucherDAO voucherDAO;
    private OrderDAO orderDAO;
    private CartDAO cartDAO;
    private AdressDAO adressDAO;

    // =========================================================
    // INIT: KHỞI TẠO DAO
    // =========================================================
    @Override
    public void init() throws ServletException {
        super.init();
        voucherDAO = new VoucherDAO();
        orderDAO = new OrderDAO();
        cartDAO = new CartDAO();
        adressDAO = new AdressDAO();
    }

    // =========================================================
    // GET: HIỂN THỊ CHECKOUT
    // =========================================================
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Customer customer = getLoggedInCustomer(session);

        if (customer == null) {
            response.sendRedirect(
                    request.getContextPath() + LOGIN_URL);
            return;
        }

        try {
            Set<Integer> selectedIds =
                    getSelectedIdsFromSession(session);

            if (selectedIds.isEmpty()) {
                selectedIds = parseSelectedIds(
                        request.getParameterValues("selectedVariantIds"));

                if (!selectedIds.isEmpty()) {
                    session.setAttribute(
                            "checkoutVariantIds", selectedIds);
                }
            }

            List<CartItem> selectedItems =
                    getSelectedCartItems(customer.getCustomerId(),
                            selectedIds);

            BigDecimal total = calculateTotal(selectedItems);

            Voucher voucher = getSessionVoucher(session);
            BigDecimal discount = calculateDiscount(total, voucher);

            setCheckoutAttributes(
                    request, session, customer.getCustomerId(),
                    selectedItems, total, discount, voucher);

            request.getRequestDispatcher(CHECKOUT_VIEW)
                    .forward(request, response);

        } catch (RuntimeException e) {
            log("Không thể tải trang checkout.", e);
            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Không thể tải trang thanh toán.");
        }
    }

    // =========================================================
    // POST: REVIEW HOẶC CONFIRM
    // =========================================================
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        Customer customer = getLoggedInCustomer(session);

        if (customer == null) {
            response.sendRedirect(
                    request.getContextPath() + LOGIN_URL);
            return;
        }

        String action = request.getParameter("action");

        if (!ACTION_REVIEW.equals(action)
                && !ACTION_CONFIRM.equals(action)) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Action không hợp lệ.");
            return;
        }

        try {
            // 1. Lấy danh sách sản phẩm được chọn.
            Set<Integer> selectedIds = parseSelectedIds(
                    request.getParameterValues("selectedVariantIds"));

            if (selectedIds.isEmpty()) {
                selectedIds = getSelectedIdsFromSession(session);
            }

            if (selectedIds.isEmpty()) {
                forwardWithError(
                        request, response, CART_VIEW,
                        "Vui lòng chọn ít nhất một sản phẩm.");
                return;
            }

            session.setAttribute("checkoutVariantIds", selectedIds);

            // 2. Lấy giỏ hàng và xác minh sản phẩm.
            List<CartItem> selectedItems = getSelectedCartItems(
                    customer.getCustomerId(), selectedIds);

            if (selectedItems.isEmpty()) {
                forwardWithError(
                        request, response, CART_VIEW,
                        "Không tìm thấy sản phẩm được chọn trong giỏ hàng.");
                return;
            }

            // 3. Cập nhật thông tin checkout trong session.
            saveCheckoutFormToSession(request, session,
                    customer.getCustomerId());

            // 4. Tính lại tổng tiền từ dữ liệu trong database.
            BigDecimal total = calculateTotal(selectedItems);

            Voucher voucher = getSessionVoucher(session);
            BigDecimal discount = calculateDiscount(total, voucher);
            BigDecimal finalTotal = total.subtract(discount);

            // 5. Chuẩn bị dữ liệu cho checkout.jsp.
            setCheckoutAttributes(
                    request, session, customer.getCustomerId(),
                    selectedItems, total, discount, voucher);

            // Review chỉ hiển thị lại thông tin, không tạo đơn hàng.
            if (ACTION_REVIEW.equals(action)) {
                request.getRequestDispatcher(CHECKOUT_VIEW)
                        .forward(request, response);
                return;
            }

            // 6. Kiểm tra thông tin trước khi xác nhận đơn hàng.
            String fullName = getSessionString(
                    session, "checkoutFullName");
            String phone = getSessionString(
                    session, "checkoutPhone");
            String paymentMethod = getSessionString(
                    session, "checkoutPaymentMethod");

            Integer addressId = getSessionInteger(
                    session, "selectedAddressId");

            if (fullName == null || fullName.trim().isEmpty()
                    || phone == null || phone.trim().isEmpty()) {
                forwardWithError(
                        request, response, CHECKOUT_VIEW,
                        "Vui lòng nhập họ tên và số điện thoại.");
                return;
            }

            if (!METHOD_COD.equals(paymentMethod)
                    && !METHOD_BANK.equals(paymentMethod)) {
                forwardWithError(
                        request, response, CHECKOUT_VIEW,
                        "Phương thức thanh toán không hợp lệ.");
                return;
            }

            if (addressId == null) {
                forwardWithError(
                        request, response, CHECKOUT_VIEW,
                        "Vui lòng chọn địa chỉ giao hàng.");
                return;
            }

            Adress address = adressDAO.findByIdAndCustomerId(
                    addressId, customer.getCustomerId());

            if (address == null) {
                session.removeAttribute("selectedAddressId");

                forwardWithError(
                        request, response, CHECKOUT_VIEW,
                        "Địa chỉ giao hàng không hợp lệ.");
                return;
            }

            if (finalTotal.compareTo(BigDecimal.ZERO) <= 0) {
                forwardWithError(
                        request, response, CHECKOUT_VIEW,
                        "Tổng tiền đơn hàng phải lớn hơn 0.");
                return;
            }

            // 7. Tạo đơn hàng và xử lý thanh toán.
            Order order = createOrder(
                    customer, address, selectedItems,
                    voucher, discount, finalTotal, paymentMethod);

            // 8. Chuẩn bị dữ liệu cho trang thành công.
            request.setAttribute("orderId", order.getOrderId());
            request.setAttribute("orderTotal", finalTotal);
            request.setAttribute("discountAmount", discount);
            request.setAttribute("paymentMethod", paymentMethod);

            // Lấy lại order để JSP có orderItems đã tải.
            Order savedOrder = findCustomerOrder(
                    customer.getCustomerId(), order.getOrderId());

            if (savedOrder != null) {
                request.setAttribute(
                        "orderItems", savedOrder.getOrderItems());
            }

            // Chỉ xóa session checkout sau khi transaction thành công.
            clearCheckoutSession(session);

            if (METHOD_COD.equals(paymentMethod)) {

                request.getRequestDispatcher(COD_SUCCESS_VIEW)
                        .forward(request, response);
                return;

            } else if (METHOD_BANK.equals(paymentMethod)) {

                request.getRequestDispatcher(BANK_SUCCESS_VIEW)
                        .forward(request, response);
                return;
            }
        } catch (NumberFormatException e) {
            forwardWithError(
                    request, response, CHECKOUT_VIEW,
                    "Mã sản phẩm hoặc địa chỉ không hợp lệ.");

        } catch (IllegalStateException e) {
            log("Không thể xác nhận đơn hàng.", e);
            forwardWithError(
                    request, response, CHECKOUT_VIEW,
                    e.getMessage() != null
                            ? e.getMessage()
                            : "Không thể xác nhận đơn hàng.");

        } catch (RuntimeException e) {
            log("Lỗi khi xử lý checkout.", e);
            response.sendRedirect(
                    request.getContextPath() + "/checkout");
        }
    }

    // =========================================================
    // TẠO ORDER, ORDER ITEM VÀ XỬ LÝ COD TRONG MỘT TRANSACTION
    // =========================================================
    private Order createOrder(
            Customer customer,
            Adress address,
            List<CartItem> selectedItems,
            Voucher voucher,
            BigDecimal discount,
            BigDecimal finalTotal,
            String paymentMethod) {

        return orderDAO.createOrderTransaction(
                customer,
                address,
                selectedItems,
                voucher,
                discount.longValue(),
                finalTotal.longValue(),
                paymentMethod);
    }

    // =========================================================
    // LẤY DỮ LIỆU CHECKOUT
    // =========================================================
    private List<CartItem> getSelectedCartItems(
            int customerId, Set<Integer> selectedIds) {

        List<CartItem> result = new ArrayList<>();

        if (selectedIds == null || selectedIds.isEmpty()) {
            return result;
        }

        Cart cart = cartDAO.findByCustomerId(customerId);

        if (cart == null || cart.getCartItems() == null) {
            return result;
        }

        for (CartItem item : cart.getCartItems()) {
            if (item.getProductVariant() == null) {
                continue;
            }

            int variantId =
                    item.getProductVariant().getVariantId();

            if (selectedIds.contains(variantId)) {
                result.add(item);
            }
        }

        return result;
    }

    private BigDecimal calculateTotal(List<CartItem> items) {
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : items) {
            if (item.getProductVariant() == null
                    || item.getQuantity() <= 0) {
                throw new IllegalStateException(
                        "Sản phẩm trong giỏ hàng không hợp lệ.");
            }

            BigDecimal price = BigDecimal.valueOf(
                    item.getProductVariant().getPrice());

            BigDecimal subtotal = price.multiply(
                    BigDecimal.valueOf(item.getQuantity()));

            total = total.add(subtotal);
        }

        return total;
    }

    private BigDecimal calculateDiscount(
            BigDecimal total, Voucher voucher) {

        if (voucher == null
                || !voucher.isActive()
                || voucher.getDiscount() <= 0
                || voucher.getDiscount() > 100) {
            return BigDecimal.ZERO;
        }

        BigDecimal discount = total
                .multiply(BigDecimal.valueOf(voucher.getDiscount()))
                .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP);

        if (discount.compareTo(total) > 0) {
            return total;
        }

        return discount;
    }

    private Voucher getSessionVoucher(HttpSession session) {
        String code = getSessionString(
                session, "checkoutVoucherCode");

        if (code == null || code.trim().isEmpty()) {
            return null;
        }

        Voucher voucher = voucherDAO.findByCode(code.trim());

        if (voucher == null
                || !voucher.isActive()
                || voucher.getDiscount() <= 0
                || voucher.getDiscount() > 100) {
            session.removeAttribute("checkoutVoucherCode");
            return null;
        }

        return voucher;
    }

    private void setCheckoutAttributes(
            HttpServletRequest request,
            HttpSession session,
            int customerId,
            List<CartItem> selectedItems,
            BigDecimal total,
            BigDecimal discount,
            Voucher voucher) {

        request.setAttribute("selectedItems", selectedItems);
        request.setAttribute("total", total);
        request.setAttribute("discountAmount", discount);
        request.setAttribute("finalTotal", total.subtract(discount));
        request.setAttribute("voucherCode",
                getSessionString(session, "checkoutVoucherCode"));
        request.setAttribute("selectedVoucher", voucher);
        request.setAttribute(
                "activeVouchers", voucherDAO.findActiveVouchers());

        request.setAttribute(
                "fullName", session.getAttribute("checkoutFullName"));
        request.setAttribute(
                "phone", session.getAttribute("checkoutPhone"));
        request.setAttribute(
                "note", session.getAttribute("checkoutNote"));
        request.setAttribute(
                "paymentMethod",
                session.getAttribute("checkoutPaymentMethod"));

        List<Adress> addresses =
                adressDAO.findByCustomerId(customerId);
        request.setAttribute("addresses", addresses);

        Adress defaultAddress =
                adressDAO.findDefaultByCustomerId(customerId);
        request.setAttribute("defaultAddress", defaultAddress);

        Integer addressId = getSessionInteger(
                session, "selectedAddressId");

        Adress selectedAddress = null;

        if (addressId != null) {
            selectedAddress = adressDAO.findByIdAndCustomerId(
                    addressId, customerId);
        }

        if (selectedAddress == null) {
            selectedAddress = defaultAddress;
        }

        if (selectedAddress != null) {
            session.setAttribute(
                    "selectedAddressId", selectedAddress.getAddressId());
        }

        request.setAttribute("selectedAddress", selectedAddress);
    }

    // =========================================================
    // FORM VÀ SESSION HELPERS
    // =========================================================
    private void saveCheckoutFormToSession(
            HttpServletRequest request,
            HttpSession session,
            int customerId) {

        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String note = request.getParameter("note");
        String paymentMethod = request.getParameter("paymentMethod");
        String addressIdValue = request.getParameter("addressId");

        if (fullName != null) {
            session.setAttribute("checkoutFullName", fullName.trim());
        }

        if (phone != null) {
            session.setAttribute("checkoutPhone", phone.trim());
        }

        if (note != null) {
            session.setAttribute("checkoutNote", note);
        }

        if (paymentMethod != null) {
            session.setAttribute(
                    "checkoutPaymentMethod", paymentMethod);
        }

        if (addressIdValue != null
                && !addressIdValue.trim().isEmpty()) {

            int addressId = Integer.parseInt(addressIdValue);

            Adress address = adressDAO.findByIdAndCustomerId(
                    addressId, customerId);

            if (address != null) {
                session.setAttribute(
                        "selectedAddressId", address.getAddressId());
            }
        }

        String voucherCode = request.getParameter("voucherCode");

        if (voucherCode != null) {
            if (voucherCode.trim().isEmpty()) {
                session.removeAttribute("checkoutVoucherCode");
            } else {
                session.setAttribute(
                        "checkoutVoucherCode", voucherCode.trim());
            }
        }
    }

    private Customer getLoggedInCustomer(HttpSession session) {
        if (session == null) {
            return null;
        }

        Object value = session.getAttribute("customer");

        if (!(value instanceof Customer)) {
            return null;
        }

        return (Customer) value;
    }

    private Set<Integer> getSelectedIdsFromSession(
            HttpSession session) {

        Object value = session.getAttribute("checkoutVariantIds");

        if (!(value instanceof Set<?>)) {
            return new HashSet<>();
        }

        Set<Integer> result = new HashSet<>();

        for (Object id : (Set<?>) value) {
            if (id instanceof Integer && (Integer) id > 0) {
                result.add((Integer) id);
            }
        }

        return result;
    }

    private Set<Integer> parseSelectedIds(String[] ids) {
        Set<Integer> result = new HashSet<>();

        if (ids == null) {
            return result;
        }

        for (String id : ids) {
            if (id == null || id.trim().isEmpty()) {
                continue;
            }

            int parsed = Integer.parseInt(id.trim());

            if (parsed > 0) {
                result.add(parsed);
            }
        }

        return result;
    }

    private String getSessionString(
            HttpSession session, String key) {

        Object value = session.getAttribute(key);

        return value instanceof String ? (String) value : null;
    }

    private Integer getSessionInteger(
            HttpSession session, String key) {

        Object value = session.getAttribute(key);

        return value instanceof Integer ? (Integer) value : null;
    }

    private Order findCustomerOrder(int customerId, int orderId) {
        return orderDAO.findByIdAndCustomerId(orderId, customerId);
    }

    private void clearCheckoutSession(HttpSession session) {
        session.removeAttribute("checkoutVariantIds");
        session.removeAttribute("checkoutVoucherCode");
        session.removeAttribute("checkoutFullName");
        session.removeAttribute("checkoutPhone");
        session.removeAttribute("checkoutNote");
        session.removeAttribute("checkoutPaymentMethod");
        session.removeAttribute("selectedAddressId");
    }

    private void forwardWithError(
            HttpServletRequest request,
            HttpServletResponse response,
            String view,
            String message)
            throws ServletException, IOException {

        request.setAttribute("error", message);
        request.getRequestDispatcher(view).forward(request, response);
    }
}
