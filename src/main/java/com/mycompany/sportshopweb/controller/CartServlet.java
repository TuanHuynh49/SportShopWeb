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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private final CartDAO cartDAO = new CartDAO();
    private final CartItemDAO cartItemDAO = new CartItemDAO();

    private static final String SELECTED_IDS = "selectedVariantIds";

    // ========================================
    // GET: HIỂN THỊ GIỎ HÀNG
    // ========================================
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("customer") == null) {
            response.sendRedirect(
                    request.getContextPath() + "/LoginServlet"
            );
            return;
        }

        try {
            Customer customer =
                    (Customer) session.getAttribute("customer");

            int customerId = customer.getCustomerId();
            updateCartCount(session, customerId);

            Cart cart = cartDAO.findByCustomerId(customerId);

            List<CartItem> cartItems = new ArrayList<>();

            if (cart != null && cart.getCartItems() != null) {
                cartItems = cart.getCartItems();
            }

            // Lấy danh sách ID đã chọn được lưu trong session.
            Object savedIds = session.getAttribute(SELECTED_IDS);

            Set<Integer> selectedIds = new HashSet<>();

            if (savedIds instanceof Set<?>) {
                for (Object id : (Set<?>) savedIds) {
                    if (id instanceof Integer) {
                        selectedIds.add((Integer) id);
                    }
                }
            }

            // Loại bỏ ID không còn tồn tại trong giỏ hàng.
            Set<Integer> existingIds = new HashSet<>();

            for (CartItem item : cartItems) {
                existingIds.add(
                        item.getProductVariant().getVariantId()
                );
            }

            selectedIds.retainAll(existingIds);

            // Lưu lại danh sách sau khi đồng bộ với giỏ hàng.
            session.setAttribute(SELECTED_IDS, selectedIds);

            // Chỉ tính tổng tiền các sản phẩm đã chọn.
            BigDecimal total = BigDecimal.ZERO;

            for (CartItem item : cartItems) {
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

            request.setAttribute("cart", cart);
            request.setAttribute("cartItems", cartItems);
            request.setAttribute("total", total);
            request.setAttribute("selectedVariantIds", selectedIds);

            System.out.println("===== CART DO GET =====");
            System.out.println("Customer ID = " + customerId);
            System.out.println("Selected IDs = " + selectedIds);
            System.out.println("Total selected = " + total);
            
            
            request.getRequestDispatcher("/views/cart.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            throw new ServletException(
                    "Lỗi khi tải giỏ hàng.", e
            );
        }
    }

    // ========================================
    // POST: XỬ LÝ CÁC THAO TÁC GIỎ HÀNG
    // ========================================
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("customer") == null) {
            response.sendRedirect(
                    request.getContextPath() + "/LoginServlet"
            );
            return;
        }

        try {
            Customer customer =
                    (Customer) session.getAttribute("customer");

            int customerId = customer.getCustomerId();

            String action = request.getParameter("action");
            // ------------------------------------
            // 1. CẬP NHẬT TỔNG TIỀN THEO CHECKBOX
            // ------------------------------------
            if ("updateTotal".equals(action)) {

                Set<Integer> selectedIds =
                        readSelectedIds(request, response);

                if (selectedIds == null) {
                    return;
                }

                // Lưu lựa chọn để doGet giữ checkbox sau khi tải lại.
                session.setAttribute(SELECTED_IDS, selectedIds);

                response.sendRedirect(
                        request.getContextPath() + "/cart"
                );
                return;
            }

            // ------------------------------------
            // 2. CÁC ACTION CÒN LẠI
            // ------------------------------------
            if (action == null || action.isBlank()) {
                throw new IllegalArgumentException(
                        "Action không hợp lệ."
                );
            }

            String variantParam = request.getParameter("variantId");

            if (variantParam == null || variantParam.isBlank()) {
                throw new IllegalArgumentException(
                        "Thiếu mã phiên bản sản phẩm."
                );
            }

            int variantId = Integer.parseInt(variantParam);

            if (variantId <= 0) {
                throw new IllegalArgumentException(
                        "Mã phiên bản sản phẩm không hợp lệ."
                );
            }

            // ------------------------------------
            // 3. THÊM SẢN PHẨM
            // ------------------------------------
            if ("add".equals(action)) {

                int quantity = Integer.parseInt(
                        request.getParameter("quantity")
                );

                if (quantity <= 0) {
                    throw new IllegalArgumentException(
                            "Số lượng phải lớn hơn 0."
                    );
                }

                cartItemDAO.insertCartItem(
                        customerId, variantId, quantity
                );

            // ------------------------------------
            // 4. CẬP NHẬT SỐ LƯỢNG
            // ------------------------------------
            } else if ("update".equals(action)) {

                int quantity = Integer.parseInt(
                        request.getParameter("quantity")
                );

                if (quantity <= 0) {
                    throw new IllegalArgumentException(
                            "Số lượng phải lớn hơn 0."
                    );
                }

                cartItemDAO.updateCartItem(
                        customerId, variantId, quantity
                );

            // ------------------------------------
            // 5. XÓA SẢN PHẨM
            // ------------------------------------
            } else if ("delete".equals(action)) {

                cartItemDAO.deleteCartItem(
                        customerId, variantId
                );

                // Nếu sản phẩm bị xóa, bỏ ID đó khỏi lựa chọn.
                Object savedIds = session.getAttribute(SELECTED_IDS);

                if (savedIds instanceof Set<?>) {
                    Set<Integer> selectedIds = new HashSet<>();

                    for (Object id : (Set<?>) savedIds) {
                        if (id instanceof Integer) {
                            selectedIds.add((Integer) id);
                        }
                    }

                    selectedIds.remove(variantId);

                    session.setAttribute(SELECTED_IDS, selectedIds);
                }

            } else {
                throw new IllegalArgumentException(
                        "Action không hợp lệ."
                );
            }
            updateCartCount(session, customerId);
            // Quay lại giỏ hàng; doGet sẽ giữ checkbox đã lưu.
            response.sendRedirect(
                    request.getContextPath() + "/cart"
            );

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Mã sản phẩm hoặc số lượng phải là số nguyên."
            );

        } catch (IllegalArgumentException | IllegalStateException e) {

            request.getSession().setAttribute(
                    "cartError", e.getMessage()
            );

            response.sendRedirect(
                    request.getContextPath() + "/cart"
            );

        } catch (Exception e) {

            throw new ServletException(
                    "Lỗi khi xử lý giỏ hàng.", e
            );
        }
    }

    // ========================================
    // ĐỌC DANH SÁCH CHECKBOX ĐƯỢC GỬI LÊN
    // ========================================
    private Set<Integer> readSelectedIds(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        String[] ids =
                request.getParameterValues(SELECTED_IDS);

        Set<Integer> selectedIds = new HashSet<>();

        if (ids == null) {
            return selectedIds;
        }

        try {
            for (String id : ids) {
                int variantId = Integer.parseInt(id);

                if (variantId <= 0) {
                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "ID sản phẩm không hợp lệ."
                    );
                    return null;
                }

                selectedIds.add(variantId);
            }

        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "ID sản phẩm không hợp lệ."
            );
            return null;
        }

        return selectedIds;
    }
    private void updateCartCount(HttpSession session, int customerId) {
    Cart cart = cartDAO.findByCustomerId(customerId);

    int cartCount = 0;

    if (cart != null && cart.getCartItems() != null) {
        cartCount = cart.getCartItems().size();
    }

    session.setAttribute("cartCount", cartCount);
}
}
