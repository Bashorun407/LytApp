-- Seed PasswordResetToken Table
INSERT INTO password_reset_tokens (token, user_id, expiry_date)
VALUES
    ('abc123token', 1, '2025-12-31 23:59:59'),
    ('def456token', 2, '2025-12-31 23:59:59'),
    ('ghi789token', 1, '2025-12-25 23:59:59');
