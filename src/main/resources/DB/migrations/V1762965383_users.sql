-- Create the Users table reflecting the updated entity class

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    hashed_password VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    role_id BIGINT REFERENCES roles(id),
    email_verified BOOLEAN DEFAULT FALSE,
    enabled BOOLEAN DEFAULT TRUE,
    two_factor_enabled BOOLEAN DEFAULT FALSE,
    last_login TIMESTAMP
);

-- Optional initial data insertion (update to match new structure)
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
    last_login
)
VALUES
  ('John', 'Doe', 'john.doe@example.com', 'hashedpass123', '123 Main St', NOW(), NOW(), 1, TRUE, TRUE, TRUE, NOW()),
  ('Jane', 'Smith', 'jane.smith@example.com', 'hashedpass456', '456 Oak Ave', NOW(), NOW(), 1, FALSE, TRUE, FALSE, NOW())
ON CONFLICT (email) DO NOTHING;
