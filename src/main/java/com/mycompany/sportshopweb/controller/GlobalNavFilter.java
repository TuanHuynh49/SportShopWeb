package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.AlbumDAO;
import com.mycompany.sportshopweb.dao.BrandDAO;
import com.mycompany.sportshopweb.util.CategoryDB;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@WebFilter(filterName = "GlobalNavFilter", urlPatterns = { "/*" })
public class GlobalNavFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(
            jakarta.servlet.ServletRequest request,
            jakarta.servlet.ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String uri = httpRequest.getRequestURI();

        // Bỏ qua tài nguyên tĩnh (.css, .js, ảnh, fonts) để tối ưu hiệu năng
        if (!uri.contains("/assets/") && !uri.endsWith(".css") && !uri.endsWith(".js")
                && !uri.endsWith(".png") && !uri.endsWith(".jpg") && !uri.endsWith(".jpeg")
                && !uri.endsWith(".svg") && !uri.endsWith(".ico")) {

            ServletContext application = httpRequest.getServletContext();

            // Nạp dữ liệu vào applicationScope (chỉ chạy 1 lần khi chưa có)
            if (application.getAttribute("categoryList") == null) {
                try {
                    application.setAttribute("categoryList", CategoryDB.getAllCategory());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (application.getAttribute("brandList") == null) {
                try {
                    application.setAttribute("brandList", BrandDAO.getAllBrand());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (application.getAttribute("albumList") == null) {
                try {
                    application.setAttribute("albumList", AlbumDAO.getAllAlbums());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
