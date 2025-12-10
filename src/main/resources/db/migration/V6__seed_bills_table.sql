-- Seed Bills Table
INSERT INTO bills (user_id, meter_number, amount, due_date, status, issued_at)
VALUES
    (1, 'MTR-1001', 150.75, '2025-12-20', 'UNPAID', NOW()),
    (2, 'MTR-1002', 200.50, '2025-12-25', 'UNPAID', NOW()),
    (1, 'MTR-1003', 100.00, '2025-12-22', 'PAID', NOW());
