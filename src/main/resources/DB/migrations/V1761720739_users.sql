CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    hashed_password VARCHAR(255) NOT NULL,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    role_id BIGINT REFERENCES roles(id),
    email_verified BOOLEAN DEFAULT FALSE,
    two_factor_enabled BOOLEAN DEFAULT FALSE,
    last_login TIMESTAMP
);

INSERT INTO users (first_name, last_name, email, hashed_password, creation_date, modified_date, role_id)
VALUES
  ('John', 'Doe', 'john.doe@example.com', 'hashedpass123', NOW(), NOW(), 1),
  ('Jane', 'Smith', 'jane.smith@example.com', 'hashedpass456', NOW(), NOW(), 1);
  ON CONFLICT (name) DO NOTHING;