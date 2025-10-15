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
--INSERT INTO bills (user_id, amount, due_date, status, issued_at)
--VALUES
--  (150.75, CURRENT_DATE + INTERVAL '30 days', 'UNPAID', NOW()),
--  (200.50, CURRENT_DATE + INTERVAL '15 days', 'UNPAID', NOW()),
--  (99.99, CURRENT_DATE + INTERVAL '10 days', 'UNPAID', NOW());

-- Insert dummy bills

INSERT INTO bills (user_id, meter_number, amount, due_date, status, issued_at)
VALUES
(1, 'MTR001', 100.50, '2025-11-15', 'UNPAID', '2025-10-10T08:30:00'),

(1, 'MTR002', 200.00, '2025-11-20', 'PAID', '2025-10-05T09:45:00'),

(2, 'MTR003', 150.75, '2025-10-10', 'OVERDUE', '2025-09-01T12:00:00'),

(3, 'MTR004', 300.00, '2025-12-01', 'UNPAID', '2025-10-12T14:15:00'),

(3, 'MTR005', 120.00, '2025-11-01', 'PAID', '2025-10-01T10:00:00');

