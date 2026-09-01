package com.sportshop.dao;

import com.sportshop.model.Role;
import com.sportshop.model.User;
import com.sportshop.model.UserStatus;
import com.sportshop.util.PasswordUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * UserDAO — Data Access Object cho đối tượng User.
 * Hỗ trợ xác thực, đăng ký, tra cứu và phân quyền theo vai trò (RBAC).
 */
public class UserDAO {

    private static final UserDAO INSTANCE = new UserDAO();

    // In-memory thread-safe store cho giai đoạn mock/kiểm thử (sẵn sàng nối JDBC ở Sprint 3)
    private final Map<String, User> userByEmail = new ConcurrentHashMap<>();
    private final Map<Long, User> userById = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(100);

    private UserDAO() {
        initDefaultUsers();
    }

    public static UserDAO getInstance() {
        return INSTANCE;
    }

    /**
     * Khởi tạo các tài khoản mặc định phục vụ kiểm thử hệ thống
     */
    private void initDefaultUsers() {
        // 1. Tài khoản Quản trị viên (ADMIN)
        User admin = new User(
                1L,
                "Quản Trị Viên",
                "admin@sportshop.vn",
                PasswordUtil.hashPassword("Admin@123"),
                Role.ADMIN,
                "0988888888",
                UserStatus.ACTIVE
        );
        saveUser(admin);

        // 2. Tài khoản Khách hàng (CUSTOMER)
        User customer = new User(
                2L,
                "Nguyễn Văn An",
                "customer@sportshop.vn",
                PasswordUtil.hashPassword("User@123"),
                Role.CUSTOMER,
                "0912345678",
                UserStatus.ACTIVE
        );
        saveUser(customer);
    }

    /**
     * Lấy toàn bộ danh sách khách hàng
     */
    public java.util.List<User> findAllCustomers() {
        java.util.List<User> list = new java.util.ArrayList<>();
        for (User u : userById.values()) {
            if (u.getRole() == Role.CUSTOMER) {
                list.add(u);
            }
        }
        return list;
    }

    private void saveUser(User user) {
        userByEmail.put(user.getEmail().toLowerCase().trim(), user);
        userById.put(user.getId(), user);
    }

    /**
     * Xác thực thông tin đăng nhập (email + raw password)
     *
     * @param email       Email đăng nhập
     * @param rawPassword Mật khẩu chưa băm
     * @return User nếu xác thực thành công, null nếu sai thông tin hoặc tài khoản bị khóa
     */
    public User authenticate(String email, String rawPassword) {
        if (email == null || rawPassword == null || email.trim().isEmpty() || rawPassword.isEmpty()) {
            return null;
        }

        User user = findByEmail(email);
        if (user == null) {
            return null;
        }

        // Kiểm tra mật khẩu
        boolean match = PasswordUtil.verifyPassword(rawPassword, user.getPasswordHash());
        if (match) {
            return user;
        }
        return null;
    }

    /**
     * Tìm người dùng theo Email (không phân biệt chữ hoa/thường)
     */
    public User findByEmail(String email) {
        if (email == null) return null;
        return userByEmail.get(email.toLowerCase().trim());
    }

    /**
     * Tìm người dùng theo ID
     */
    public User findById(Long id) {
        if (id == null) return null;
        return userById.get(id);
    }

    /**
     * Kiểm tra email đã tồn tại hay chưa
     */
    public boolean existsByEmail(String email) {
        return findByEmail(email) != null;
    }

    /**
     * Tạo mới người dùng (đăng ký)
     */
    public boolean create(User user) {
        if (user == null || user.getEmail() == null) {
            return false;
        }
        String normalizedEmail = user.getEmail().toLowerCase().trim();
        if (userByEmail.containsKey(normalizedEmail)) {
            return false;
        }

        if (user.getId() == null) {
            user.setId(idCounter.incrementAndGet());
        }
        if (user.getRole() == null) {
            user.setRole(Role.CUSTOMER);
        }
        if (user.getStatus() == null) {
            user.setStatus(UserStatus.ACTIVE);
        }

        saveUser(user);
        return true;
    }

    /**
     * Cập nhật thông tin người dùng
     */
    public boolean update(User user) {
        if (user == null || user.getId() == null || user.getEmail() == null) {
            return false;
        }
        saveUser(user);
        return true;
    }
}
