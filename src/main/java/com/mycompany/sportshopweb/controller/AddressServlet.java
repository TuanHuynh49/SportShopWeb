
package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.AdressDAO;
import com.mycompany.sportshopweb.dao.CustomerDAO;
import com.mycompany.sportshopweb.model.Adress;
import com.mycompany.sportshopweb.model.Customer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/addresses")
public class AddressServlet extends HttpServlet {

    private AdressDAO adressDAO;
    private CustomerDAO customerDAO;

    // Tận dụng vòng đời Servlet: Khởi tạo các DAO một lần duy nhất khi Servlet được nạp
    @Override
    public void init() throws ServletException {
        adressDAO = new AdressDAO();
        customerDAO = new CustomerDAO();
    }

    // Tìm customerId của tài khoản đang đăng nhập
    // Hiển thị danh sách địa chỉ
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("customer") == null) {
            response.sendRedirect(
                    request.getContextPath() + "/LoginServlet");
            return;
        }

        Customer customer = (Customer) session.getAttribute("customer");

        int customerId = customer.getCustomerId();

        List<Adress> addresses = adressDAO.findByCustomerId(customerId);

        request.setAttribute("addresses", addresses);

        request.getRequestDispatcher(
                "/views/address.jsp")
                .forward(request, response);
    }

    // Xử lý thêm, sửa và đặt địa chỉ mặc định
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("customer") == null) {
            response.sendRedirect(
                    request.getContextPath() + "/LoginServlet");
            return;
        }

        Customer customer = (Customer) session.getAttribute("customer");

        int customerId = customer.getCustomerId();

        String action = request.getParameter("action");

        boolean success = false;

        
        if ("insert".equals(action)) {
            success = insertAddress(request, customerId);
        } else if ("update".equals(action)) {
            success = updateAddress(request, customerId);
        } else if ("setDefault".equals(action)) {
            success = setDefaultAddress(request, customerId);
        } else if ("delete".equals(action)) {
            success = deleteAddress(request, customerId);
        } else {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST);
            return;
        }


        if (success) {
            response.sendRedirect(
                    request.getContextPath() + "/addresses");
        } else {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Không thể thực hiện thao tác địa chỉ.");
        }
    }

    // Thêm địa chỉ mới
    private boolean insertAddress(HttpServletRequest request, int customerId) {
        Customer customer = customerDAO.findById(customerId);
        if (customer == null) {
            return false;
        }

        Adress address = new Adress();
        address.setCustomer(customer);

        if (!populateAddressFromRequest(request, address)) {
            return false;
        }

        return adressDAO.insert(address);
    }

    // Cập nhật địa chỉ đã lưu
    private boolean updateAddress(HttpServletRequest request, int customerId) {
        int addressId;
        try {
            addressId = Integer.parseInt(request.getParameter("addressId"));
        } catch (NumberFormatException e) {
            return false;
        }

        // Chỉ lấy địa chỉ thuộc khách hàng đang đăng nhập
        Adress address = adressDAO.findByIdAndCustomerId(addressId, customerId);
        if (address == null) {
            return false;
        }

        if (!populateAddressFromRequest(request, address)) {
            return false;
        }

        return adressDAO.update(address, customerId);
    }

    // Trích xuất và kiểm tra tính hợp lệ của dữ liệu địa chỉ từ form (Áp dụng DRY)
    private boolean populateAddressFromRequest(HttpServletRequest request, Adress address) {
        String province = request.getParameter("province");
        String ward = request.getParameter("ward");
        String detailAddress = request.getParameter("detailAddress");

        if (isBlank(province) || isBlank(ward) || isBlank(detailAddress)) {
            return false;
        }

        address.setProvince(province.trim());
        address.setWard(ward.trim());
        address.setDetailAddress(detailAddress.trim());
        address.setDefault("true".equals(request.getParameter("isDefault")));
        return true;
    }

    // Đặt địa chỉ làm mặc định
    private boolean setDefaultAddress(
            HttpServletRequest request,
            int customerId) {

        int addressId;

        try {
            addressId = Integer.parseInt(
                    request.getParameter("addressId"));
        } catch (NumberFormatException e) {
            return false;
        }

        return adressDAO.setDefault(addressId, customerId);
    }

    // Kiểm tra chuỗi rỗng hoặc chỉ chứa khoảng trắng
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
    
    // Xóa mềm địa chỉ
    
    // Xóa mềm địa chỉ
    private boolean deleteAddress(
            HttpServletRequest request,
            int customerId) {

        String addressIdParam = request.getParameter("addressId");

        if (addressIdParam == null || addressIdParam.trim().isEmpty()) {
            System.out.println("Lỗi xóa địa chỉ: thiếu addressId.");
            return false;
        }

        int addressId;

        try {
            addressId = Integer.parseInt(addressIdParam);
        } catch (NumberFormatException e) {
            System.out.println("Lỗi xóa địa chỉ: addressId không hợp lệ.");
            return false;
        }

        System.out.println("Đang xóa mềm addressId = " + addressId
                + ", customerId = " + customerId);

        boolean success = adressDAO.softDelete(addressId, customerId);

        System.out.println("Kết quả xóa mềm: " + success);

        return success;
    }


}
