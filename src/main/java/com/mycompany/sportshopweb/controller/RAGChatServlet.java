package com.mycompany.sportshopweb.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "RAGChatServlet", urlPatterns = {"/rag-chat"})
public class RAGChatServlet extends HttpServlet {

    private static final String PYTHON_RAG_URL = System.getenv().get(
        "PYTHON_RAG_URL"
    );

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("rag-chat.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");

        String userQuery = request.getParameter("query");
        StringBuilder aiAnswer = new StringBuilder();

        if (userQuery != null && !userQuery.trim().isEmpty()) {
            try {
                // Xử lý escape các ký tự đặc biệt để tạo JSON payload an toàn
                String escapedQuery = userQuery.replace("\\", "\\\\").replace("\"", "\\\"");
                String jsonPayload = "{\"query\": \"" + escapedQuery + "\", \"top_k\": 3}";

                // Sử dụng HttpClient để gọi sang FastAPI Python Service
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest httpRequest = HttpRequest.newBuilder()
                        .uri(URI.create(PYTHON_RAG_URL))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(jsonPayload, StandardCharsets.UTF_8))
                        .build();

                HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

                if (httpResponse.statusCode() == 200) {
                    aiAnswer.append(httpResponse.body());
                } else {
                    aiAnswer.append("Lỗi từ hệ thống RAG AI (Code: ").append(httpResponse.statusCode()).append(")");
                }

            } catch (Exception e) {
                e.printStackTrace();
                aiAnswer.append("Lỗi kết nối tới hệ thống RAG AI: ").append(e.getMessage());
            }
        } else {
            aiAnswer.append("Vui lòng nhập câu hỏi hợp lệ.");
        }

        PrintWriter out = response.getWriter();
        out.print(aiAnswer.toString().trim());
        out.flush();
    }
}