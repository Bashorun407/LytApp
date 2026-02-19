-- Seed Roles Table
INSERT INTO roles (name, created_at)
VALUES
    ('ADMIN', NOW()),
    ('USER', NOW()),
    ('MANAGER', NOW());


-- Insert initial users (safe upsert)
INSERT INTO users (
    first_name,
    last_name,
    email,
    hashed_password,
    address,
    creation_date,
    modified_date,
    role_id,
    email_verified,
    enabled,
    two_factor_enabled,
    last_login,
    reset_token,
    reset_token_expiry
)
VALUES
  ('John', 'Doe', 'john.doe@example.com', 'hashedpass123', '123 Main St',
   NOW(), NOW(), 1, TRUE, TRUE, TRUE, NOW(), NULL, NULL),

  ('Jane', 'Smith', 'jane.smith@example.com', 'hashedpass456', '456 Oak Ave',
   NOW(), NOW(), 1, FALSE, TRUE, FALSE, NOW(), NULL, NULL)
ON DUPLICATE KEY UPDATE
    first_name = VALUES(first_name),
    last_name = VALUES(last_name),
    hashed_password = VALUES(hashed_password),
    address = VALUES(address),
    modified_date = NOW(),
    role_id = VALUES(role_id),
    email_verified = VALUES(email_verified),
    enabled = VALUES(enabled),
    two_factor_enabled = VALUES(two_factor_enabled),
    last_login = VALUES(last_login),
    reset_token = VALUES(reset_token),
    reset_token_expiry = VALUES(reset_token_expiry);


-- Seed Bills Table
INSERT INTO bills (user_id, meter_number, amount, due_date, status, issued_at)
VALUES
    (1, 'MTR-1001', 150.75, '2025-12-20', 'UNPAID', NOW()),
    (2, 'MTR-1002', 200.50, '2025-12-25', 'UNPAID', NOW()),
    (1, 'MTR-1003', 100.00, '2025-12-22', 'PAID', NOW());


-- Seed Notifications Table
INSERT INTO notifications (user_id, type, message, sent_at, is_read)
VALUES
    (1, 'BILL', 'Your bill #MTR-1001 is due on 2025-12-20', NOW(), FALSE),
    (2, 'BILL', 'Your bill #MTR-1002 is due on 2025-12-25', NOW(), FALSE),
    (1, 'REMINDER', 'Payment received for bill #MTR-1003', NOW(), TRUE);


-- Seed PasswordResetToken Table
INSERT INTO password_reset_tokens (token, user_id, expiry_date)
VALUES
    ('abc123token', 1, '2025-12-31 23:59:59'),
    ('def456token', 2, '2025-12-31 23:59:59'),
    ('ghi789token', 1, '2025-12-25 23:59:59');


-- Seed Payments Table
INSERT INTO payments (bill_id, user_id, amount_paid, payment_method, status, transaction_id, token, paid_at)
VALUES
    (1, 1, 100.00, 'CREDIT_CARD', 'COMPLETED', 'TXN10001', 'TOKEN10001', '2025-12-01 10:00:00'),
    (2, 2, 250.50, 'PAYPAL', 'PENDING', 'TXN10002', 'TOKEN10002', '2025-12-02 11:30:00'),
    (1, 1, 50.00, 'BANK_TRANSFER', 'FAILED', 'TXN10003', 'TOKEN10003', '2025-12-03 14:45:00');


