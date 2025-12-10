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
