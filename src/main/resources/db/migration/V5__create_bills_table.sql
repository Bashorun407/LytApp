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
