-- Create Roles Table
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    created_at DATETIME
);


-- Create the Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,

    email VARCHAR(255) NOT NULL UNIQUE,
    hashed_password VARCHAR(255) NOT NULL,

    address VARCHAR(255),

    creation_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    modified_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    role_id BIGINT,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(id),

    email_verified BOOLEAN DEFAULT FALSE,
    enabled BOOLEAN DEFAULT TRUE,
    two_factor_enabled BOOLEAN DEFAULT FALSE,

    last_login DATETIME NULL,

    reset_token VARCHAR(255),
    reset_token_expiry DATETIME NULL
);


-- V3__create_bills_table.sql
-- Flyway migration to create the 'bills' table

CREATE TABLE IF NOT EXISTS bills (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    meter_number VARCHAR(255) NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    due_date DATE,
    status ENUM('PAID', 'UNPAID', 'OVERDUE') DEFAULT 'UNPAID',
    issued_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT FK_bill_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- V4__create_notifications_table.sql
CREATE TABLE notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    type VARCHAR(255),
    message TEXT,
    sent_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    is_read BOOLEAN DEFAULT FALSE,
    CONSTRAINT FK_notifications_user FOREIGN KEY (user_id) REFERENCES users(id)
);


-- Create PasswordResetToken Table
CREATE TABLE password_reset_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    expiry_date DATETIME NOT NULL,
    CONSTRAINT fk_password_reset_user FOREIGN KEY (user_id) REFERENCES users(id)
);


-- Create Payments Table
CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bill_id BIGINT,
    user_id BIGINT,
    amount_paid DECIMAL(19,2) NOT NULL,
    payment_method VARCHAR(255),
    status VARCHAR(50),
    transaction_id VARCHAR(255) UNIQUE,
    token VARCHAR(255) UNIQUE,
    paid_at DATETIME,
    CONSTRAINT fk_payment_bill FOREIGN KEY (bill_id) REFERENCES bills(id),
    CONSTRAINT fk_payment_user FOREIGN KEY (user_id) REFERENCES users(id)
);

