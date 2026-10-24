
package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.CartDAO;
import com.mycompany.sportshopweb.dao.CartItemDAO;
import com.mycompany.sportshopweb.model.Cart;
import com.mycompany.sportshopweb.model.CartItem;
import com.mycompany.sportshopweb.model.Customer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private static final Logger LOGGER =
            Logger.getLogger(CartServlet.class.getName());

    private static final String SELECTED_IDS =
            "selectedVariantIds";

    private static final String CUSTOMER = "customer";

    private static final String CART_COUNT = "cartCount";

    private final CartDAO cartDAO = new CartDAO();
    private final CartItemDAO cartItemDAO = new CartItemDAO();

    // ==================== DO GET ====================

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (!isLoggedIn(session)) {
            response.sendRedirect(
                    request.getContextPath() + "/LoginServlet"
            );
            return;
        }

        try {
            Customer customer =
                    (Customer) session.getAttribute(CUSTOMER);

            int customerId = customer.getCustomerId();

            // Lấy giỏ hàng và danh sách sản phẩm.
            Cart cart = findCart(customerId);
            List<CartItem> cartItems = getCartItems(cart);

            // Lấy các sản phẩm đã chọn và loại ID không còn tồn tại.
            Set<Integer> selectedIds =
                    getSessionSelectedIds(session);

            selectedIds = filterSelectedIds(cart, selectedIds);

            session.setAttribute(SELECTED_IDS, selectedIds);

            // Tính tổng tiền của các sản phẩm đã chọn.
            BigDecimal total =
                    calculateTotal(cartItems, selectedIds);

            session.setAttribute(
                    CART_COUNT, countItems(cart)
            );

            request.setAttribute("cart", cart);
            request.setAttribute("cartItems", cartItems);
            request.setAttribute("total", total);
            request.setAttribute(
                    "selectedVariantIds", selectedIds
            );

            // Hiển thị thông báo lỗi nếu có.
            Object error = session.getAttribute("cartError");

            if (error != null) {
                request.setAttribute("cartError", error);
                session.removeAttribute("cartError");
            }

            request.getRequestDispatcher("/views/cart.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            LOGGER.log(
                    Level.SEVERE,
                    "Không thể tải giỏ hàng.", e
            );

            throw new ServletException(
                    "Lỗi khi tải giỏ hàng.", e
            );
        }
    }

    // ==================== DO POST ====================

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (!isLoggedIn(session)) {
            response.sendRedirect(
                    request.getContextPath() + "/LoginServlet"
            );
            return;
        }

        try {
            Customer customer =
                    (Customer) session.getAttribute(CUSTOMER);

            int customerId = customer.getCustomerId();
            String action = request.getParameter("action");

            if (action == null || action.isBlank()) {
                throw new IllegalArgumentException(
                        "Action không hợp lệ."
                );
            }

            // Lưu checkbox trước khi xử lý action.
            // Điều này giúp giữ lựa chọn khi nhấn Update.
            Set<Integer> selectedIds = readSelectedIds(request);

            session.setAttribute(SELECTED_IDS, selectedIds);

            // Chỉ cập nhật lựa chọn, không thay đổi database.
            if ("updateTotal".equals(action)) {
                redirectToCart(request, response);
                return;
            }

            int variantId = readPositiveInt(
                    request, "variantId"
            );

            if ("add".equals(action)) {
                int quantity = readPositiveInt(
                        request, "quantity"
                );

                validateIds(customerId, variantId);
                validateQuantity(quantity);

                cartItemDAO.insertCartItem(
                        customerId, variantId, quantity
                );

            } else if ("update".equals(action)) {
                int quantity = readPositiveInt(
                        request, "quantity"
                );

                validateIds(customerId, variantId);
                validateQuantity(quantity);

                cartItemDAO.updateCartItem(
                        customerId, variantId, quantity
                );

            } else if ("delete".equals(action)) {
                validateIds(customerId, variantId);

                cartItemDAO.deleteCartItem(
                        customerId, variantId
                );

                // Xóa sản phẩm khỏi danh sách được chọn.
                selectedIds.remove(variantId);

                session.setAttribute(
                        SELECTED_IDS, selectedIds
                );

            } else {
                throw new IllegalArgumentException(
                        "Action không được hỗ trợ."
                );
            }

            // Cập nhật số lượng sản phẩm trong biểu tượng giỏ hàng.
            Cart cart = findCart(customerId);

            session.setAttribute(
                    CART_COUNT, countItems(cart)
            );

            redirectToCart(request, response);

        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "ID hoặc số lượng phải là số nguyên hợp lệ."
            );

        } catch (IllegalArgumentException e) {
            session.setAttribute("cartError", e.getMessage());
            redirectToCart(request, response);

        } catch (Exception e) {
            LOGGER.log(
                    Level.SEVERE,
                    "Không thể xử lý thao tác giỏ hàng.", e
            );

            throw new ServletException(
                    "Lỗi khi xử lý giỏ hàng.", e
            );
        }
    }

    // ==================== CART HELPERS ====================

    // Kiểm tra khách hàng đã đăng nhập.
    private boolean isLoggedIn(HttpSession session) {
        return session != null
                && session.getAttribute(CUSTOMER) instanceof Customer;
    }

    // Tìm giỏ hàng theo khách hàng.
    private Cart findCart(int customerId) {
        return cartDAO.findByCustomerId(customerId);
    }

    // Lấy danh sách sản phẩm trong giỏ hàng.
    private List<CartItem> getCartItems(Cart cart) {
        if (cart == null || cart.getCartItems() == null) {
            return new ArrayList<>();
        }

        return cart.getCartItems();
    }

    // Đếm số dòng sản phẩm trong giỏ hàng.
    private int countItems(Cart cart) {
        if (cart == null || cart.getCartItems() == null) {
            return 0;
        }

        return cart.getCartItems().size();
    }

    // Kiểm tra ID khách hàng và sản phẩm.
    private void validateIds(int customerId, int variantId) {
        if (customerId <= 0 || variantId <= 0) {
            throw new IllegalArgumentException(
                    "Mã khách hàng hoặc sản phẩm không hợp lệ."
            );
        }
    }

    // Kiểm tra số lượng sản phẩm.
    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Số lượng phải lớn hơn 0."
            );
        }
    }

    // ==================== SELECTED ITEMS HELPERS ====================

    // Lấy danh sách sản phẩm đã chọn từ session.
    private Set<Integer> getSessionSelectedIds(
            HttpSession session) {

        Object savedIds = session.getAttribute(SELECTED_IDS);
        Set<Integer> result = new HashSet<>();

        if (savedIds instanceof Set<?>) {
            for (Object id : (Set<?>) savedIds) {
                if (id instanceof Integer) {
                    result.add((Integer) id);
                }
            }
        }

        return result;
    }

    // Đọc tất cả checkbox được gửi lên từ form.
    private Set<Integer> readSelectedIds(
            HttpServletRequest request) {

        String[] ids = request.getParameterValues(SELECTED_IDS);
        Set<Integer> selectedIds = new HashSet<>();

        if (ids == null) {
            return selectedIds;
        }

        for (String id : ids) {
            int variantId = Integer.parseInt(id);

            if (variantId <= 0) {
                throw new IllegalArgumentException(
                        "ID sản phẩm không hợp lệ."
                );
            }

            selectedIds.add(variantId);
        }

        return selectedIds;
    }

    // Chỉ giữ ID của những sản phẩm còn trong giỏ hàng.
    private Set<Integer> filterSelectedIds(
            Cart cart, Set<Integer> selectedIds) {

        Set<Integer> existingIds = new HashSet<>();

        if (cart == null || cart.getCartItems() == null) {
            return existingIds;
        }

        for (CartItem item : cart.getCartItems()) {
            if (item.getProductVariant() != null) {
                existingIds.add(
                        item.getProductVariant().getVariantId()
                );
            }
        }

        existingIds.retainAll(selectedIds);

        return existingIds;
    }

    // Tính tổng tiền của các sản phẩm đã chọn.
    private BigDecimal calculateTotal(
            List<CartItem> cartItems,
            Set<Integer> selectedIds) {

        BigDecimal total = BigDecimal.ZERO;

        if (cartItems == null || selectedIds == null) {
            return total;
        }

        for (CartItem item : cartItems) {
            if (item.getProductVariant() == null) {
                continue;
            }

            int variantId =
                    item.getProductVariant().getVariantId();

            if (selectedIds.contains(variantId)) {
                BigDecimal price = BigDecimal.valueOf(
                        item.getProductVariant().getPrice()
                );

                BigDecimal subtotal = price.multiply(
                        BigDecimal.valueOf(item.getQuantity())
                );

                total = total.add(subtotal);
            }
        }

        return total;
    }

    // Đọc một tham số số nguyên dương.
    private int readPositiveInt(
            HttpServletRequest request,
            String parameterName) {

        String value = request.getParameter(parameterName);

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Thiếu tham số: " + parameterName
            );
        }

        int number = Integer.parseInt(value);

        if (number <= 0) {
            throw new IllegalArgumentException(
                    parameterName + " phải lớn hơn 0."
            );
        }

        return number;
    }

    // Chuyển về trang giỏ hàng sau khi xử lý.
    private void redirectToCart(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        response.sendRedirect(
                request.getContextPath() + "/cart"
        );
    }
}
