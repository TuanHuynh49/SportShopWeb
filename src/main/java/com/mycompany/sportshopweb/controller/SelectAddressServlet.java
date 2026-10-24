
package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.AdressDAO;
import com.mycompany.sportshopweb.model.Adress;
import com.mycompany.sportshopweb.model.Customer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "SelectAddressServlet", urlPatterns = { "/select-address" })
public class SelectAddressServlet extends HttpServlet {

    private AdressDAO adressDAO;

    // Tận dụng vòng đời Servlet: Khởi tạo DAO một lần duy nhất khi Servlet được nạp
    @Override
    public void init() throws ServletException {
        adressDAO = new AdressDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login");
            return;
        }

        Customer customer = (Customer) session.getAttribute("customer");

        String addressIdValue = request.getParameter("addressId");

        int addressId;

        try {
            addressId = Integer.parseInt(addressIdValue);
        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Adress address = adressDAO.findByIdAndCustomerId(
                addressId, customer.getCustomerId());

        if (address == null) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Địa chỉ không hợp lệ.");
            return;
        }

        session.setAttribute("selectedAddressId",
                address.getAddressId());

        response.sendRedirect(
                request.getContextPath() + "/checkout");
    }
}
