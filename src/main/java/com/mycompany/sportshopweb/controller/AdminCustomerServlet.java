package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.AdminCustomerDAO;
import com.mycompany.sportshopweb.model.Customer;
import com.mycompany.sportshopweb.model.Order;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminCustomerServlet", urlPatterns = { "/admin/customers" })
public class AdminCustomerServlet extends HttpServlet {

    private final AdminCustomerDAO adminCustomerDAO = new AdminCustomerDAO();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");

        if (keyword != null) {
            keyword = keyword.trim();
        }

        // Lấy danh sách khách hàng cùng User và MembershipTier từ DAO
        List<Customer> customers = adminCustomerDAO.getAllCustomersWithDetails();

        // Lọc theo tên, email hoặc số điện thoại.
        if (keyword != null && !keyword.isEmpty()) {
            final String search = keyword.toLowerCase();

            customers = customers.stream()
                    .filter(customer -> {
                        String firstName = customer.getFirstName();
                        String lastName = customer.getLastName();

                        String fullName = ((firstName == null ? "" : firstName)
                                + " "
                                + (lastName == null ? "" : lastName))
                                .toLowerCase();

                        String email = customer.getUser().getEmail();
                        String phone = customer.getUser().getPhone();

                        email = email == null
                                ? ""
                                : email.toLowerCase();

                        phone = phone == null
                                ? ""
                                : phone.toLowerCase();

                        return fullName.contains(search)
                                || email.contains(search)
                                || phone.contains(search);
                    })
                    .toList();
        }

        // Thống kê khách hàng.
        long totalCustomers = customers.size();
        long newCustomers = customers.stream()
                .filter(customer -> customer.getUser().getCreatedAt() != null)
                .count();

        // Lấy ID các khách hàng có đơn hàng từ DAO.
        List<Integer> customerIdsWithOrders = adminCustomerDAO.getCustomerIdsWithOrders();

        long customersWithOrders = customers.stream()
                .filter(customer -> customerIdsWithOrders.contains(
                        customer.getCustomerId()))
                .count();

        long customersWithoutOrders = totalCustomers - customersWithOrders;

        request.setAttribute("customers", customers);
        request.setAttribute("totalCustomers", totalCustomers);
        request.setAttribute("newCustomers", newCustomers);
        request.setAttribute("customersWithOrders", customersWithOrders);
        request.setAttribute("customersWithoutOrders", customersWithoutOrders);
        request.setAttribute("keyword", keyword);

        // Nếu URL có ?id=..., lấy khách hàng được chọn.
        String idParam = request.getParameter("id");

        if (idParam != null && !idParam.isBlank()) {
            try {
                int customerId = Integer.parseInt(idParam);

                Customer selectedCustomer = adminCustomerDAO.findCustomerById(customerId);

                request.setAttribute("selectedCustomer", selectedCustomer);

                if (selectedCustomer != null) {
                    List<Order> customerOrders = adminCustomerDAO.getOrdersByCustomerId(customerId);
                    request.setAttribute("customerOrders", customerOrders);
                } else {
                    request.setAttribute("customerOrders", List.of());
                }

            } catch (NumberFormatException e) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid customer ID.");
                return;
            }
        }

        request.getRequestDispatcher("/views/admin/customers.jsp").forward(request, response);
    }
}
