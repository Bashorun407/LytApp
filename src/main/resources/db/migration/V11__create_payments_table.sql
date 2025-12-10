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
