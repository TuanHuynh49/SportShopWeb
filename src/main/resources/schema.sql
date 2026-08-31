-- =============================================================================
-- SportShop Database Schema — Chuẩn hóa 100% theo OR-Mapping-Website-Ban-Do-The-Thao.pdf
-- 17 Objects | 18 Requirements (10 Customer + 8 Admin)
-- =============================================================================

CREATE DATABASE IF NOT EXISTS sportshop_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE sportshop_db;

-- 1. Table: customer (Object: Customer)
CREATE TABLE IF NOT EXISTS customer (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    password_hash VARCHAR(255) NOT NULL,
    gender VARCHAR(10) DEFAULT 'OTHER',
    date_of_birth DATE DEFAULT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    role VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Table: address (Object: Address)
CREATE TABLE IF NOT EXISTS address (
    address_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    receiver_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    province VARCHAR(100) NOT NULL,
    district VARCHAR(100) NOT NULL,
    ward VARCHAR(100) NOT NULL,
    detail_address VARCHAR(255) NOT NULL,
    is_default BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_address_customer FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Table: category (Object: Category)
CREATE TABLE IF NOT EXISTS category (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    icon VARCHAR(50) DEFAULT NULL,
    parent_id INT DEFAULT NULL,
    status BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_category_parent FOREIGN KEY (parent_id) REFERENCES category(category_id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Table: brand (Object: Brand)
CREATE TABLE IF NOT EXISTS brand (
    brand_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    logo_url VARCHAR(255),
    description TEXT,
    status BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Table: product (Object: Product)
CREATE TABLE IF NOT EXISTS product (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    brand_id INT NOT NULL,
    category_id INT NOT NULL,
    status BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_product_brand FOREIGN KEY (brand_id) REFERENCES brand(brand_id) ON DELETE RESTRICT,
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES category(category_id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. Table: product_variant (Object: ProductVariant)
CREATE TABLE IF NOT EXISTS product_variant (
    variant_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    size VARCHAR(20) NOT NULL,
    color VARCHAR(50) NOT NULL,
    sku VARCHAR(50) NOT NULL UNIQUE,
    extra_price DECIMAL(12, 2) DEFAULT 0.00,
    status BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_variant_product FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. Table: product_image (Object: ProductImage)
CREATE TABLE IF NOT EXISTS product_image (
    image_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    is_thumbnail BOOLEAN DEFAULT FALSE,
    sort_order INT DEFAULT 0,
    CONSTRAINT fk_image_product FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 8. Table: inventory (Object: Inventory — 1-1 với ProductVariant)
CREATE TABLE IF NOT EXISTS inventory (
    inventory_id INT AUTO_INCREMENT PRIMARY KEY,
    variant_id INT NOT NULL UNIQUE,
    quantity INT NOT NULL DEFAULT 0,
    reserved_quantity INT NOT NULL DEFAULT 0,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_inventory_variant FOREIGN KEY (variant_id) REFERENCES product_variant(variant_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 9. Table: cart (Object: Cart — 1-1 với Customer)
CREATE TABLE IF NOT EXISTS cart (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL UNIQUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_cart_customer FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 10. Table: cart_item (Object: CartItem)
CREATE TABLE IF NOT EXISTS cart_item (
    cart_item_id INT AUTO_INCREMENT PRIMARY KEY,
    cart_id INT NOT NULL,
    variant_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    price_at_add DECIMAL(12, 2) NOT NULL,
    CONSTRAINT fk_cartitem_cart FOREIGN KEY (cart_id) REFERENCES cart(cart_id) ON DELETE CASCADE,
    CONSTRAINT fk_cartitem_variant FOREIGN KEY (variant_id) REFERENCES product_variant(variant_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 11. Table: voucher (Object: Voucher)
CREATE TABLE IF NOT EXISTS voucher (
    voucher_id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    discount_type VARCHAR(20) NOT NULL, -- 'PERCENT' hoặc 'FIXED'
    discount_value DECIMAL(12, 2) NOT NULL,
    min_order_amount DECIMAL(12, 2) DEFAULT 0.00,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    usage_limit INT DEFAULT 100,
    status BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 12. Table: orders (Object: Order)
CREATE TABLE IF NOT EXISTS orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    order_code VARCHAR(50) NOT NULL UNIQUE,
    customer_id INT NOT NULL,
    address_id INT NOT NULL,
    voucher_id INT DEFAULT NULL,
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(12, 2) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    payment_method VARCHAR(30) NOT NULL DEFAULT 'COD',
    shipping_method VARCHAR(30) NOT NULL DEFAULT 'STANDARD',
    CONSTRAINT fk_order_customer FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON DELETE RESTRICT,
    CONSTRAINT fk_order_address FOREIGN KEY (address_id) REFERENCES address(address_id) ON DELETE RESTRICT,
    CONSTRAINT fk_order_voucher FOREIGN KEY (voucher_id) REFERENCES voucher(voucher_id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 13. Table: order_item (Object: OrderItem)
CREATE TABLE IF NOT EXISTS order_item (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    variant_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(12, 2) NOT NULL,
    subtotal DECIMAL(12, 2) NOT NULL,
    CONSTRAINT fk_orderitem_order FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    CONSTRAINT fk_orderitem_variant FOREIGN KEY (variant_id) REFERENCES product_variant(variant_id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 14. Table: payment (Object: Payment — 1-1 với Order)
CREATE TABLE IF NOT EXISTS payment (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL UNIQUE,
    payment_method VARCHAR(30) NOT NULL,
    amount DECIMAL(12, 2) NOT NULL,
    payment_status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    transaction_code VARCHAR(100) DEFAULT NULL,
    paid_at DATETIME DEFAULT NULL,
    CONSTRAINT fk_payment_order FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 15. Table: review (Object: Review)
CREATE TABLE IF NOT EXISTS review (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    customer_id INT NOT NULL,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_review_product FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE,
    CONSTRAINT fk_review_customer FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 16. Table: voucher_usage (Object: VoucherUsage — 1-1 với Order trong phạm vi project)
CREATE TABLE IF NOT EXISTS voucher_usage (
    usage_id INT AUTO_INCREMENT PRIMARY KEY,
    voucher_id INT NOT NULL,
    customer_id INT NOT NULL,
    order_id INT NOT NULL UNIQUE,
    used_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_voucherusage_voucher FOREIGN KEY (voucher_id) REFERENCES voucher(voucher_id) ON DELETE CASCADE,
    CONSTRAINT fk_voucherusage_customer FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON DELETE CASCADE,
    CONSTRAINT fk_voucherusage_order FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 17. Table: order_status_history (Object: OrderStatusHistory)
CREATE TABLE IF NOT EXISTS order_status_history (
    history_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    status VARCHAR(30) NOT NULL,
    note VARCHAR(255),
    changed_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    changed_by VARCHAR(100),
    CONSTRAINT fk_history_order FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
