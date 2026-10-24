
package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.OrderDAO;
import com.mycompany.sportshopweb.model.Customer;
import com.mycompany.sportshopweb.model.Order;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/orders")
public class OrderServlet extends HttpServlet {

    private OrderDAO orderDAO;
    
    @Override
    public void init() throws ServletException{
        orderDAO = new OrderDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // 1. Kiểm tra đăng nhập
        if (!isLoggedIn(session)) {
            response.sendRedirect(
                    request.getContextPath() + "/LoginServlet");
            return;
        }

        // 2. Lấy thông tin khách hàng
        Customer customer = (Customer) session.getAttribute("customer");
        int customerId = customer.getCustomerId();

        try {
            // 3. Lấy lịch sử đơn hàng
            List<Order> orders = orderDAO.findByCustomerId(customerId);
            request.setAttribute("orders", orders);

            // 4. Lấy thông báo thành công hoặc lỗi
            moveMessage(session, request, "orderSuccess", "success");
            moveMessage(session, request, "orderError", "error");

            // 5. Hiển thị lịch sử đơn hàng
            request.getRequestDispatcher("/views/orders.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            throw new ServletException(
                    "Lỗi khi tải lịch sử đơn hàng.", e);
        }
    }

    private boolean isLoggedIn(HttpSession session) {
        return session != null
                && session.getAttribute("customer") != null;
    }

    private void moveMessage(HttpSession session,
            HttpServletRequest request,
            String sessionKey,
            String requestKey) {

        Object message = session.getAttribute(sessionKey);

        if (message != null) {
            request.setAttribute(requestKey, message);
            session.removeAttribute(sessionKey);
        }
    }
}
