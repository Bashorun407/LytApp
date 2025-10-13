-- Insert Roles
INSERT INTO roles (name) VALUES
  ('USER'),
  ('ADMIN');

-- Insert Users
INSERT INTO users (first_name, last_name, email, hashed_password, creation_date, modified_date, role_id)
VALUES
  ('John', 'Doe', 'john.doe@example.com', 'hashedpass123', NOW(), NOW(), 1),
  ('Jane', 'Smith', 'jane.smith@example.com', 'hashedpass456', NOW(), NOW(), 1);

-- Insert Bills
INSERT INTO bills (user_id, amount, due_date, status, issued_at)
VALUES
  (1, 150.75, CURRENT_DATE + INTERVAL '30 days', 'UNPAID', NOW()),
  (1, 200.50, CURRENT_DATE + INTERVAL '15 days', 'UNPAID', NOW()),
  (2, 99.99, CURRENT_DATE + INTERVAL '10 days', 'UNPAID', NOW());
