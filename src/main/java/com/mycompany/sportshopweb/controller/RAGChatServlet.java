package com.mycompany.sportshopweb.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "RAGChatServlet", urlPatterns = {"/rag-chat"})
public class RAGChatServlet extends HttpServlet {

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
                // 1. Lấy đường dẫn thực tế của ứng dụng web trên server/máy local
                String webAppPath = getServletContext().getRealPath("/");
                
                // 2. Định vị thư mục python-rag nằm ngang hàng với thư mục mã nguồn/webapp của project
                File projectRoot = new File(webAppPath).getParentFile();
                if (projectRoot == null) {
                    projectRoot = new File("."); // Fallback về thư mục hiện tại nếu chạy môi trường đặc biệt
                }
                
                // Thư mục chứa các script python của bạn
                File pythonRagDir = new File(projectRoot.getParentFile(), "python-rag");
                if (!pythonRagDir.exists()) {
                    // Thử tìm theo cấu trúc khi build đóng gói chuẩn
                    pythonRagDir = new File(projectRoot, "python-rag");
                }

                File ragScript = new File(pythonRagDir, "query_rag.py");

                // 3. Chọn lệnh Python (Ưu tiên gọi 'python' hoặc 'python3' từ hệ thống đã cài sẵn môi trường)
                String pythonCommand = new File(
                 pythonRagDir,
                  ".venv\\Scripts\\python.exe"
                ).getAbsolutePath(); 
                // Nếu chạy trên Linux/Ubuntu server có thể là "python3"

                // 4. Khởi tạo ProcessBuilder với đường dẫn hoàn toàn tương đối / động
                ProcessBuilder pb = new ProcessBuilder(
                    pythonCommand, 
                    ragScript.getAbsolutePath(), 
                    userQuery
                );
                
                // Đặt thư mục làm việc (Working Directory) để Python nhận diện file .env chính xác
                if (pythonRagDir.exists()) {
                    pb.directory(pythonRagDir);
                }
                pb.redirectErrorStream(true);

                Process process = pb.start();

                // Đọc kết quả trả về từ script Python bằng chuẩn UTF-8
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        aiAnswer.append(line).append("\n");
                    }
                }

                int exitCode = process.waitFor();
                if (exitCode != 0 && aiAnswer.length() == 0) {
                    aiAnswer.append("Lỗi tiến trình Python (Exit Code: ").append(exitCode).append(")");
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