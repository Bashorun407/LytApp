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

-- Optional initial seed data with upsert behavior
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
