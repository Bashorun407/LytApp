-- Seed Notifications Table
INSERT INTO notifications (user_id, type, message, sent_at, is_read)
VALUES
    (1, 'BILL', 'Your bill #MTR-1001 is due on 2025-12-20', NOW(), FALSE),
    (2, 'BILL', 'Your bill #MTR-1002 is due on 2025-12-25', NOW(), FALSE),
    (1, 'REMINDER', 'Payment received for bill #MTR-1003', NOW(), TRUE);
