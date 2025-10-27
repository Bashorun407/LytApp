- LightPay Database Initialization

-- Create tables if they don't exist
CREATE TABLE IF NOT EXISTS roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

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

CREATE TABLE IF NOT EXISTS bills (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    amount DECIMAL(10,2) NOT NULL,
    due_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'UNPAID',
    issued_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS payments (
    id BIGSERIAL PRIMARY KEY,
    bill_id BIGINT NOT NULL REFERENCES bills(id),
    user_id BIGINT NOT NULL REFERENCES users(id),
    amount_paid DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(50),
    status VARCHAR(20) DEFAULT 'PENDING',
    transaction_id VARCHAR(100) UNIQUE,
    paid_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    type VARCHAR(50),
    message TEXT,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_read BOOLEAN DEFAULT FALSE
);

-- Insert initial data
INSERT INTO roles (name) VALUES
('USER'),
('ADMIN')
ON CONFLICT (name) DO NOTHING;

-- Insert sample user (password is 'password123')
INSERT INTO users (first_name, last_name, email, hashed_password, role_id) VALUES
('John', 'Doe', 'john.doe@example.com', '$2a$10$xyz123encryptedpasswordhash', 1)
ON CONFLICT (email) DO NOTHING;

-- Insert sample bills
INSERT INTO bills (user_id, amount, due_date, status) VALUES
(1, 150.75, CURRENT_DATE + INTERVAL '30 days', 'UNPAID'),
(1, 200.50, CURRENT_DATE + INTERVAL '15 days', 'UNPAID')
ON CONFLICT DO NOTHING;