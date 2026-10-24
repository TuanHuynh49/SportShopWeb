# BÁO CÁO TOÀN DIỆN VỀ CÁC BỖI ĐÃ RÀ SOÁT VÀ KHẮC PHỤC (BUG FIX REPORT)
**Dự án:** SportShopWeb - Hệ Thống Bán Đồ Thể Thao Trực Tuyến  
**Người thực hiện:** Tuấn (Leader - Module Tài Khoản & Bảo Mật, Giỏ Hàng, Đơn Hàng & Quản Trị)  
**Kiến trúc:** Chuẩn Jakarta EE MVC (Model - View - Controller), JPA / Hibernate, NetBeans & Tomcat  
**Thời gian hoàn thành:** 24/10/2026  

---

## I. TỔNG QUAN YÊU CẦU & MỤC TIÊU KHẮC PHỤC
Mục tiêu của đợt rà soát và sửa lỗi toàn diện:
1. **Loại bỏ lỗi biên dịch (Compilation Error):** Đảm bảo toàn bộ dự án build thành công 100% trên môi trường Maven / NetBeans.
2. **Khôi phục chuẩn kiến trúc MVC thuần túy:** Tuyệt đối không để rò rỉ mã nguồn truy cập cơ sở dữ liệu (`EntityManager`, `EntityTransaction`, SQL/JPQL) trong tầng Controller (Servlet). Controller chỉ đóng vai trò điều hướng và giao tiếp với tầng Model thông qua Data Access Object (DAO) và Service.
3. **Bịt kín các lỗ hổng bảo mật nghiêm trọng:** Khắc phục nguy cơ Bypass Filter phân quyền Admin, kiểm tra toàn vẹn định danh khách hàng trên từng giao dịch.
4. **Bảo đảm tính toàn vẹn dữ liệu (ACID Transaction):** Đảm bảo các luồng đặt hàng, trừ số lượng tồn kho (stock) và ghi nhận thanh toán diễn ra đồng thời trong một Transaction duy nhất; nếu một bước lỗi thì tự động Rollback hoàn toàn.
5. **Tối ưu hóa hiệu năng và vòng đời Servlet (Servlet Lifecycle):** Loại bỏ việc nạp toàn bộ danh sách vào RAM để duyệt thủ công (In-Memory Loop); khởi tạo tài nguyên DAO thông qua phương thức `init()` thay vì cấp phát vô tội vạ trên từng request.
6. **Làm sạch mã nguồn (Clean Code):** Loại bỏ Dead Code, các import thừa và các câu lệnh `System.out.println` debug không an toàn trên môi trường Production.

---

## II. CHI TIẾT CÁC LỖI ĐÃ ĐƯỢC KHẮC PHỤC

### 1. Lỗi Biên Dịch (Compilation Error) - Khai Báo Trùng Thuộc Tính
- **Vị trí file:** `SportShopWeb/src/main/java/com/mycompany/sportshopweb/model/Adress.java`
- **Nguyên nhân:** Khai báo trùng lặp trường `deleted` (`private boolean deleted;`) ở hai vị trí khác nhau trong class, gây lỗi `duplicate field: deleted` khiến trình biên dịch Java / NetBeans dừng build.
- **Giải pháp xử lý:**
  - Loại bỏ hoàn toàn khai báo trùng lặp.
  - Chuẩn hóa các Constructor: giữ nguyên Constructor mặc định không tham số (No-argument Constructor) bắt buộc theo chuẩn JPA Entity JavaBean, đồng thời áp dụng Constructor Chaining (`this(...)`) cho các Constructor có tham số để loại bỏ mã nguồn trùng lặp và phục vụ Unit Test.

---

### 2. Lỗ Hổng Bảo Mật Vượt Rào Phân Quyền (Security Filter Bypass)
- **Vị trí file:** `SportShopWeb/src/main/java/com/mycompany/sportshopweb/controller/ProductActionServlet.java`
- **Nguyên nhân:** `AdminFilter` cấu hình bảo vệ các URL có pattern `/admin/*` và `/views/admin/*`. Tuy nhiên `ProductActionServlet` lại được đăng ký với 2 URL patterns:
  ```java
  @WebServlet(name = "ProductActionServlet", urlPatterns = { "/admin/product-actions", "/ProductActionServlet" })
  ```
  Kẻ tấn công hoặc người dùng chưa đăng nhập có thể gửi trực tiếp request tới `/ProductActionServlet?action=delete&id=...` để xóa sản phẩm khỏi cơ sở dữ liệu mà hoàn toàn bỏ qua bước kiểm tra quyền Admin của `AdminFilter`.
- **Giải pháp xử lý:**
  - Rút gọn URL pattern chỉ còn duy nhất `/admin/product-actions`.
  - Mọi thao tác thêm/sửa/xóa sản phẩm bắt buộc phải đi qua tiền tố `/admin/` và được `AdminFilter` xác thực phiên đăng nhập (Session) cùng quyền hạn Admin (`AdminAuthDAO.isAdmin`).
  - Xóa bỏ dòng code rác trong `init()`: `getServletContext().setAttribute("ProductDAO", productDAO);`.
  - Tối ưu gọi trực tiếp các phương thức static của `BrandDAO.getAllBrand()` và `CategoryDB.getAllCategory()` thay vì khởi tạo instance thừa.

---

### 3. Vi Phạm Nghiêm Trọng Chuẩn Kiến Trúc MVC & Thất Thoát Dữ Liệu Tồn Kho
- **Vị trí file:** 
  - `SportShopWeb/src/main/java/com/mycompany/sportshopweb/controller/CheckoutServlet.java`
  - `SportShopWeb/src/main/java/com/mycompany/sportshopweb/dao/OrderDAO.java`
- **Hiện trạng lỗi trước khi sửa:**
  - Trong phương thức `createOrder()` của `CheckoutServlet`, Controller tự khởi tạo `EntityManager em = DBUtil.getEmFactory().createEntityManager();` và tự điều khiển `EntityTransaction trans = em.getTransaction();`. Điều này vi phạm nghiêm trọng nguyên lý phân tầng MVC (Controller nhúng trực tiếp Data Access Layer).
  - Nghiêm trọng hơn: Đơn hàng sau khi lưu chỉ ghi nhận bảng `orders`, **hoàn toàn không lưu danh sách chi tiết đơn hàng (`order_items`)** và **không trừ số lượng tồn kho (`quantity`) của sản phẩm (`ProductVariant`)** khi thanh toán COD!
- **Giải pháp xử lý:**
  - Xây dựng phương thức đóng gói giao dịch ACID hoàn chỉnh tại Model: `OrderDAO.createOrderTransaction(...)`.
  - Phương thức này trong `OrderDAO` thực hiện nguyên tử (Atomicity) 4 tác vụ trong 1 Transaction duy nhất:
    1. Lưu bản ghi `Order`.
    2. Duyệt từng sản phẩm trong giỏ hàng, lưu bản ghi `OrderItem` tương ứng.
    3. Cập nhật giảm số lượng tồn kho `quantity` của từng `ProductVariant` tương ứng; nếu số lượng tồn không đủ sẽ chủ động ném ngoại lệ để Rollback.
    4. Ghi nhận bản ghi thanh toán `Payment` (với phương thức COD, trạng thái PENDING).
  - Tái cấu trúc `CheckoutServlet`: Loại bỏ hoàn toàn `EntityManager`, `EntityTransaction`, `DBUtil`. Controller chỉ thực hiện validate dữ liệu đầu vào và gọi `orderDAO.createOrderTransaction(...)`.
  - Loại bỏ các DAO và model không còn phụ thuộc trực tiếp trong `CheckoutServlet` (`OrderItemDAO`, `PaymentDAO`, `ProductVariantDAO`, `LocalDateTime`, `Date`).

---

### 4. Lỗi 404 Not Found & Trải Nghiệm Khách Hàng tại Chat AI
- **Vị trí file:** `SportShopWeb/src/main/java/com/mycompany/sportshopweb/controller/RAGChatServlet.java`
- **Nguyên nhân:** Khi người dùng truy cập trực tiếp bằng phương thức GET vào đường dẫn `/chat`, servlet thực hiện `request.getRequestDispatcher("rag-chat.jsp").forward(request, response);`. Do trong dự án không có file `rag-chat.jsp` (giao diện chat là widget tích hợp), người dùng bị trả về lỗi HTTP 404 Not Found.
- **Giải pháp xử lý:** Sửa `doGet()` thành `response.sendRedirect(request.getContextPath() + "/");` để chuyển hướng an toàn về trang chủ.

---

### 5. Truy Vấn Không Tối Ưu (In-Memory Loop) Gây Suy Giảm Hiệu Năng
- **Vị trí file:** 
  - `SportShopWeb/src/main/java/com/mycompany/sportshopweb/controller/ConfirmOrderServlet.java`
  - `SportShopWeb/src/main/java/com/mycompany/sportshopweb/controller/CancelOrderServlet.java`
  - `SportShopWeb/src/main/java/com/mycompany/sportshopweb/controller/OrderDetailServlet.java`
  - `SportShopWeb/src/main/java/com/mycompany/sportshopweb/controller/CheckoutServlet.java`
- **Nguyên nhân:** Khi cần tìm kiếm một đơn hàng theo `orderId` của khách hàng đang đăng nhập, các Servlet trước đây đều gọi `orderDAO.findByCustomerId(customerId)` để kéo toàn bộ lịch sử đơn hàng của khách hàng về bộ nhớ RAM của Server, sau đó dùng vòng lặp `for` để đối chiếu từng đơn:
  ```java
  List<Order> orders = orderDAO.findByCustomerId(customer.getCustomerId());
  for (Order order : orders) {
      if (order.getOrderId() == orderId) {
          selectedOrder = order;
          break;
      }
  }
  ```
  Nếu khách hàng có hàng trăm đơn hàng, việc nạp toàn bộ danh sách kèm theo chi tiết từng đơn gây lãng phí bộ nhớ nghiêm trọng và làm chậm thời gian xử lý.
- **Giải pháp xử lý:**
  - Bổ sung phương thức truy vấn chuẩn xác trong `OrderDAO`:
    ```java
    public Order findByIdAndCustomerId(int orderId, int customerId)
    ```
    Sử dụng câu lệnh JPQL có điều kiện `WHERE o.orderId = :orderId AND c.customerId = :customerId` kết hợp fetch join.
  - Thay thế toàn bộ các đoạn mã duyệt vòng lặp RAM trong `ConfirmOrderServlet`, `CancelOrderServlet`, `OrderDetailServlet`, `CheckoutServlet` bằng một lời gọi duy nhất tới `orderDAO.findByIdAndCustomerId(...)`.

---

### 6. Xử Lý Transaction Thiếu An Toàn (Unsafe Rollback)
- **Vị trí file:** `SportShopWeb/src/main/java/com/mycompany/sportshopweb/dao/VoucherDAO.java`
- **Nguyên nhân:** Trong khối `catch` của phương thức `insertVoucher()`, câu lệnh `trans.rollback();` được gọi trực tiếp mà không kiểm tra xem Transaction đã được kích hoạt hay chưa (`trans.isActive()`). Nếu ngoại lệ xảy ra trước hoặc trong khi `trans.begin()`, việc gọi `rollback()` sẽ ném ra `IllegalStateException`, che giấu nguyên nhân gốc của lỗi (Root Cause).
- **Giải pháp xử lý:** Chuẩn hóa cấu trúc an toàn:
  ```java
  } catch (Exception e) {
      if (trans.isActive()) {
          trans.rollback();
      }
  } finally {
      em.close();
  }
  ```
  Đồng thời tách định dạng xuống dòng sạch sẽ giữa phương thức `updateVoucher()` và `findActiveVouchers()`.

---

### 7. Vi Phạm Chuẩn Vòng Đời Servlet (Servlet Lifecycle Violation)
- **Vị trí file:**
  - `SportShopWeb/src/main/java/com/mycompany/sportshopweb/controller/AdminOrderServlet.java`
  - `SportShopWeb/src/main/java/com/mycompany/sportshopweb/controller/AdminCustomerServlet.java`
  - `SportShopWeb/src/main/java/com/mycompany/sportshopweb/controller/AdminDashboardServlet.java`
  - `SportShopWeb/src/main/java/com/mycompany/sportshopweb/controller/ProductVariantServlet.java`
  - `SportShopWeb/src/main/java/com/mycompany/sportshopweb/controller/AdminAlbumServlet.java`
  - `SportShopWeb/src/main/java/com/mycompany/sportshopweb/controller/CancelOrderServlet.java`
  - `SportShopWeb/src/main/java/com/mycompany/sportshopweb/controller/OrderDetailServlet.java`
  - `SportShopWeb/src/main/java/com/mycompany/sportshopweb/controller/VNPayServlet.java`
  - `SportShopWeb/src/main/java/com/mycompany/sportshopweb/controller/AdminProductServlet.java`
  - `SportShopWeb/src/main/java/com/mycompany/sportshopweb/controller/ProductActionServlet.java`
- **Nguyên nhân:**
  - Một số Servlet khởi tạo DAO ngay tại thời điểm khai báo trường (`private final ...DAO = new ...DAO()`), hoặc tạo mới bên trong phương thức `doGet()` trên từng request (như `new AdminDashboardDAO()` trong `AdminDashboardServlet`).
  - Một số Servlet thiếu lời gọi `super.init();` hoặc thiếu chữ ký chuẩn `throws ServletException`.
- **Giải pháp xử lý:**
  - Khai báo biến thành viên không gán giá trị tại dòng khai báo.
  - Chuyển toàn bộ việc khởi tạo đối tượng DAO/Service vào phương thức vòng đời `@Override public void init() throws ServletException { super.init(); ... }`. Servlet Container (Tomcat) chỉ nạp và tạo các instance này một lần duy nhất trong suốt vòng đời của Servlet, tiết kiệm CPU và bộ nhớ.

---

### 8. Lỗi Đọc Cấu Hình Môi Trường (.env Missing Crash)
- **Vị trí file:** `SportShopWeb/src/main/java/com/mycompany/sportshopweb/util/VNPayConfig.java`
- **Nguyên nhân:** `VNPayConfig` trước đây chỉ đọc thông qua `System.getenv(name)`. Khi chạy ứng dụng trên máy phát triển cục bộ qua NetBeans / Tomcat, các biến cấu hình thanh toán VNPay nằm trong file `.env` ở thư mục gốc của project, dẫn đến `System.getenv()` trả về `null` và văng `IllegalStateException: Missing environment variable: ...` ngay khi người dùng chọn thanh toán VNPay.
- **Giải pháp xử lý:**
  - Tích hợp thư viện `Dotenv` (`io.github.cdimascio.dotenv.Dotenv`) với cờ `.ignoreIfMissing()`.
  - Cơ chế ưu tiên: Trước tiên tìm giá trị trong file `.env`, nếu không có sẽ tự động Fallback sang `System.getenv(name)`. Cách làm này hoàn toàn tương thích và đồng bộ với giải pháp đã áp dụng tại `CloudinaryConfig.java`.

---

### 9. Dọn Dẹp Mã Nguồn (Dead Code & Debug Statement Clean Up)
- **Vị trí file:**
  - `SportShopWeb/src/main/java/com/mycompany/sportshopweb/dao/CartDAO.java`: Xóa các câu lệnh `System.out.println("Số CartItem qua Cart: ...")` và `System.out.println("Đã lưu Cart, customerId = ...")`.
  - `SportShopWeb/src/main/java/com/mycompany/sportshopweb/dao/BrandDAO.java`: Xóa import thừa không sử dụng `com.mycompany.sportshopweb.model.Product;`.
  - `SportShopWeb/src/main/java/com/mycompany/sportshopweb/util/CategoryDB.java`: Xóa import thừa không sử dụng `com.mycompany.sportshopweb.model.Brand;`.
  - `SportShopWeb/src/main/java/com/mycompany/sportshopweb/controller/CancelOrderServlet.java`: Xóa import thừa `java.util.List;`.

---

## III. BẢNG TỔNG HỢP CÁC FILE ĐÃ ĐƯỢC CHỈNH SỬA

| STT | Tên File Đã Sửa | Phân Loại | Vấn Đề Gốc | Kết Quả Sau Khi Khắc Phục |
| :--- | :--- | :--- | :--- | :--- |
| 1 | `model/Adress.java` | Model | Trùng lặp trường `deleted` gây lỗi biên dịch. | Hết lỗi biên dịch, chuẩn hóa Constructor Chaining. |
| 2 | `dao/OrderDAO.java` | Model (DAO) | Thiếu hàm tìm kiếm chính xác và thiếu transaction trừ tồn kho. | Bổ sung `findByIdAndCustomerId` và `createOrderTransaction` ACID. |
| 3 | `controller/CheckoutServlet.java` | Controller | Vi phạm MVC (dùng `EntityManager`), mất chi tiết đơn và tồn kho. | Tách 100% EntityManager ra khỏi Controller, ủy quyền toàn bộ cho DAO. |
| 4 | `controller/ProductActionServlet.java` | Controller | URL pattern `/ProductActionServlet` bypass `AdminFilter`. | Chỉ giữ `/admin/product-actions`, bảo đảm phân quyền Admin an toàn. |
| 5 | `controller/RAGChatServlet.java` | Controller | GET request forward tới trang `rag-chat.jsp` không tồn tại (404). | Chuyển hướng an toàn về trang chủ (`/`). |
| 6 | `controller/ConfirmOrderServlet.java` | Controller | Vòng lặp duyệt đơn hàng trong RAM, thiếu `super.init()`. | Dùng `orderDAO.findByIdAndCustomerId()`, chuẩn hóa `init()`. |
| 7 | `controller/CancelOrderServlet.java` | Controller | Duyệt vòng lặp RAM tìm đơn hàng, khởi tạo DAO tại khai báo. | Dùng `orderDAO.findByIdAndCustomerId()`, đưa DAO vào `init()`. |
| 8 | `controller/OrderDetailServlet.java` | Controller | Duyệt vòng lặp RAM tìm đơn hàng, khởi tạo DAO kép. | Dùng `orderDAO.findByIdAndCustomerId()`, chuẩn hóa `init()`. |
| 9 | `controller/AdminOrderServlet.java` | Controller | Khởi tạo DAO trực tiếp tại dòng khai báo thuộc tính. | Chuyển khởi tạo vào `init()`, chuẩn hóa vòng đời Servlet. |
| 10 | `controller/AdminCustomerServlet.java` | Controller | Khởi tạo DAO trực tiếp tại dòng khai báo thuộc tính. | Chuyển khởi tạo vào `init()`, chuẩn hóa vòng đời Servlet. |
| 11 | `controller/AdminDashboardServlet.java` | Controller | Tạo instance `AdminDashboardDAO` mới trên mỗi request GET. | Chuyển `dashboardDAO` thành field và khởi tạo trong `init()`. |
| 12 | `controller/ProductVariantServlet.java` | Controller | Khởi tạo DAO trực tiếp tại dòng khai báo thuộc tính. | Chuyển khởi tạo vào `init()`, chuẩn hóa vòng đời Servlet. |
| 13 | `controller/AdminAlbumServlet.java` | Controller | Khởi tạo DAO trực tiếp tại dòng khai báo thuộc tính. | Chuyển khởi tạo vào `init()`, chuẩn hóa vòng đời Servlet. |
| 14 | `controller/AdminProductServlet.java` | Controller | Thiếu `super.init()` và `throws ServletException`. | Chuẩn hóa `init()` theo quy chuẩn Jakarta EE. |
| 15 | `controller/VNPayServlet.java` | Controller | Khởi tạo `PaymentService` tại dòng khai báo thuộc tính. | Chuyển khởi tạo vào `init()`, chuẩn hóa vòng đời Servlet. |
| 16 | `dao/VoucherDAO.java` | Model (DAO) | Rollback không kiểm tra `isActive()`, format dính dòng. | Kiểm tra `if (trans.isActive()) trans.rollback();`, định dạng sạch. |
| 17 | `dao/CartDAO.java` | Model (DAO) | `System.out.println` rác in dữ liệu giỏ hàng ra console. | Dọn sạch toàn bộ lệnh debug. |
| 18 | `dao/BrandDAO.java` | Model (DAO) | Import thừa class `Product`. | Xóa bỏ import thừa. |
| 19 | `util/CategoryDB.java` | Model / Util | Import thừa class `Brand`. | Xóa bỏ import thừa. |
| 20 | `util/VNPayConfig.java` | Util | Chỉ đọc biến hệ thống, văng ngoại lệ khi chạy cục bộ với `.env`. | Tích hợp đọc cấu hình từ file `.env` với Fallback sang System env. |

---

## IV. KẾT LUẬN & ĐÁNH GIÁ ĐẠT CHUẨN
1. **Kiến Trúc MVC:** Hệ thống đã tách bạch hoàn toàn:
   - **Model:** Chứa toàn bộ nghiệp vụ (Entities, DAOs, Services, Database Transactions).
   - **View:** Các file JSP thuần túy trình bày, nhận dữ liệu qua `requestScope` và `sessionScope`.
   - **Controller:** Các Servlet đóng vai trò Router/Controller thanh thoát, nhận request, kiểm tra hợp lệ, điều phối Model và chỉ định View.
2. **Tính Ổn Định & An Toàn:** 
   - Loại bỏ triệt để các nguy cơ vượt quyền quản trị.
   - Bảo đảm giao dịch đặt hàng không bao giờ bị lệch dữ liệu tồn kho hoặc mất chi tiết mặt hàng.
   - Không còn lỗi 404 hoặc 500 do xung đột biến môi trường.
3. **Mã Nguồn Sạch:** Toàn bộ mã nguồn tuân thủ nghiêm ngặt quy định không chèn comment số thứ tự tùy tiện, bảo đảm tính học thuật và sẵn sàng phục vụ cho buổi báo cáo/vấn đáp đồ án.
