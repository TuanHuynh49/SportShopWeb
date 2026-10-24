
package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.OrderDAO;
import com.mycompany.sportshopweb.model.Customer;
import com.mycompany.sportshopweb.model.Order;
import com.mycompany.sportshopweb.model.OrderStatus;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/cancel-order")
public class CancelOrderServlet extends HttpServlet {

    private OrderDAO orderDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        orderDAO = new OrderDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Kiểm tra khách hàng đã đăng nhập
        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("customer") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/LoginServlet");
            return;
        }

        Customer customer =
                (Customer) session.getAttribute("customer");

        // 2. Lấy mã đơn hàng từ request
        String orderIdParam = request.getParameter("orderId");

        try {
            if (orderIdParam == null || orderIdParam.trim().isEmpty()) {
                response.sendRedirect(
                        request.getContextPath() + "/orders");
                return;
            }

            int orderId = Integer.parseInt(orderIdParam);

            // Tìm đơn hàng thuộc khách hàng đang đăng nhập trực tiếp qua DAO
            Order selectedOrder = orderDAO.findByIdAndCustomerId(
                    orderId, customer.getCustomerId());

            // 4. Không tìm thấy đơn hàng của khách
            if (selectedOrder == null) {
                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Không tìm thấy đơn hàng.");
                return;
            }

            // 5. Chỉ cho phép hủy đơn đã xác nhận
            if (selectedOrder.getOrderStatus()
                    != OrderStatus.CONFIRMED) {

                session.setAttribute(
                        "orderError",
                        "Không thể hủy đơn hàng ở trạng thái hiện tại.");

                response.sendRedirect(
                        request.getContextPath() + "/orders");
                return;
            }

            // 6. Cập nhật trạng thái đơn hàng
            selectedOrder.setOrderStatus(OrderStatus.CANCELLED);

            orderDAO.updateOrder(selectedOrder);

            // 7. Thông báo kết quả
            session.setAttribute(
                    "orderSuccess",
                    "Hủy đơn hàng thành công.");

            response.sendRedirect(
                    request.getContextPath() + "/orders");

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    request.getContextPath() + "/orders");

        } catch (Exception e) {

            e.printStackTrace();

            session.setAttribute(
                    "orderError",
                    "Hủy đơn hàng thất bại. Vui lòng thử lại.");

            response.sendRedirect(
                    request.getContextPath() + "/orders");
        }
    }
}
