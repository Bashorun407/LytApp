-- Seed Payments Table
INSERT INTO payments (bill_id, user_id, amount_paid, payment_method, status, transaction_id, token, paid_at)
VALUES
    (1, 1, 100.00, 'CREDIT_CARD', 'COMPLETED', 'TXN10001', 'TOKEN10001', '2025-12-01 10:00:00'),
    (2, 2, 250.50, 'PAYPAL', 'PENDING', 'TXN10002', 'TOKEN10002', '2025-12-02 11:30:00'),
    (1, 1, 50.00, 'BANK_TRANSFER', 'FAILED', 'TXN10003', 'TOKEN10003', '2025-12-03 14:45:00');
