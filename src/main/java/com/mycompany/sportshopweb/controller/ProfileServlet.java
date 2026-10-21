/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.CustomerDAO;
import com.mycompany.sportshopweb.dao.UserDAO;
import com.mycompany.sportshopweb.model.Customer;
import com.mycompany.sportshopweb.model.User;
import com.mycompany.sportshopweb.util.PasswordUtil;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Anh Tuan
 */
@WebServlet(name = "ProfileServlet", urlPatterns = {"/ProfileServlet"})
public class ProfileServlet extends HttpServlet {

    // Khi người dùng vào trang xem hồ sơ cá nhân
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // Nếu chưa đăng nhập thì chuyển hướng sang trang đăng nhập
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }

        User user = (User) session.getAttribute("user");
        CustomerDAO customerDAO = new CustomerDAO();
        Customer customer = customerDAO.findByUserId(user.getUserId());
        if (customer != null) {
            session.setAttribute("customer", customer);
        }

        // Lấy tab yêu cầu: "info" (mặc định) hoặc "password"
        String tab = request.getParameter("tab");
        request.setAttribute("activeTab", "password".equalsIgnoreCase(tab) ? "password" : "info");

        // Đã đăng nhập -> hiển thị trang profile.jsp
        request.getRequestDispatcher("/views/profile.jsp").forward(request, response);
    }

    // Khi người dùng bấm nút cập nhật thông tin hoặc đổi mật khẩu
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        // Kiểm tra phiên đăng nhập
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }

        User user = (User) session.getAttribute("user");
        Customer customer = (Customer) session.getAttribute("customer");

        String action = request.getParameter("action");
        if (action == null) {
            action = "updateProfile";
        }

        if ("changePassword".equals(action)) {
            handleChangePassword(request, response, session, user);
        } else {
            handleUpdateProfile(request, response, session, user, customer);
        }
    }

    // Xử lý đổi mật khẩu
    private void handleChangePassword(HttpServletRequest request, HttpServletResponse response,
            HttpSession session, User sessionUser) throws ServletException, IOException {
        request.setAttribute("activeTab", "password");

        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // Kiểm tra không được để trống
        if (currentPassword == null || currentPassword.isEmpty()
                || newPassword == null || newPassword.isEmpty()
                || confirmPassword == null || confirmPassword.isEmpty()) {
            request.setAttribute("error", "Please fill in all password fields.");
            request.getRequestDispatcher("/views/profile.jsp").forward(request, response);
            return;
        }

        // Kiểm tra độ dài mật khẩu mới
        if (newPassword.length() < 6) {
            request.setAttribute("error", "New password must be at least 6 characters long.");
            request.getRequestDispatcher("/views/profile.jsp").forward(request, response);
            return;
        }

        // Kiểm tra xác nhận mật khẩu mới có trùng khớp không
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "New password and confirmation password do not match.");
            request.getRequestDispatcher("/views/profile.jsp").forward(request, response);
            return;
        }

        UserDAO userDAO = new UserDAO();
        User userInDb = userDAO.findById(sessionUser.getUserId());
        if (userInDb == null) {
            request.setAttribute("error", "User account not found. Please sign in again.");
            request.getRequestDispatcher("/views/profile.jsp").forward(request, response);
            return;
        }

        // Kiểm tra mật khẩu hiện tại có đúng không
        if (!PasswordUtil.verifyPassword(currentPassword, userInDb.getPasswordHash())) {
            request.setAttribute("error", "Current password is incorrect.");
            request.getRequestDispatcher("/views/profile.jsp").forward(request, response);
            return;
        }

        // Kiểm tra mật khẩu mới không được trùng mật khẩu cũ
        if (PasswordUtil.verifyPassword(newPassword, userInDb.getPasswordHash())) {
            request.setAttribute("error", "New password cannot be the same as your current password.");
            request.getRequestDispatcher("/views/profile.jsp").forward(request, response);
            return;
        }

        // Băm mật khẩu mới và lưu vào database
        String hashedNewPassword = PasswordUtil.hashPassword(newPassword);
        userInDb.setPasswordHash(hashedNewPassword);
        boolean updated = userDAO.update(userInDb);

        if (!updated) {
            request.setAttribute("error", "System error while updating password. Please try again.");
            request.getRequestDispatcher("/views/profile.jsp").forward(request, response);
            return;
        }

        // Cập nhật lại thông tin mới vào Session
        session.setAttribute("user", userInDb);
        request.setAttribute("message", "Password changed successfully!");
        request.getRequestDispatcher("/views/profile.jsp").forward(request, response);
    }

    // Xử lý cập nhật thông tin cá nhân (First name, Last name, Phone, Gender)
    private void handleUpdateProfile(HttpServletRequest request, HttpServletResponse response,
            HttpSession session, User user, Customer customer) throws ServletException, IOException {
        request.setAttribute("activeTab", "info");

        // Lấy dữ liệu người dùng vừa chỉnh sửa từ form
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String phone = request.getParameter("phone");
        String gender = request.getParameter("gender");

        if (firstName != null) firstName = firstName.trim();
        if (lastName != null) lastName = lastName.trim();
        if (phone != null) phone = phone.trim();

        // Kiểm tra không được để trống họ và tên
        if (firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty()) {
            request.setAttribute("error", "First name and last name cannot be empty.");
            request.getRequestDispatcher("/views/profile.jsp").forward(request, response);
            return;
        }

        UserDAO userDAO = new UserDAO();
        CustomerDAO customerDAO = new CustomerDAO();

        // Nếu người dùng đổi số điện thoại mới, kiểm tra xem số mới có bị ai dùng chưa
        if (phone != null && !phone.isEmpty() && !phone.equals(user.getPhone())) {
            if (userDAO.existsByPhone(phone)) {
                request.setAttribute("error", "Phone number is already used by another account.");
                request.getRequestDispatcher("/views/profile.jsp").forward(request, response);
                return;
            }
            user.setPhone(phone);
            userDAO.update(user);
        }

        // Cập nhật thông tin đối tượng Customer
        if (customer != null) {
            customer.setFirstName(firstName);
            customer.setLastName(lastName);
            if (gender != null && !gender.isEmpty()) {
                customer.setGender(gender);
            }
            customerDAO.update(customer);
        }

        // Cập nhật lại đối tượng mới nhất vào Session
        session.setAttribute("user", user);
        session.setAttribute("customer", customer);

        // Gửi thông báo thành công về giao diện profile.jsp
        request.setAttribute("message", "Profile updated successfully!");
        request.getRequestDispatcher("/views/profile.jsp").forward(request, response);
    }
}
