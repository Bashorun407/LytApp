CREATE TABLE IF NOT EXISTS bills (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    amount DECIMAL(10,2) NOT NULL,
    due_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'UNPAID',
    issued_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


--insert values into bills table
INSERT INTO bills (user_id, meter_number, amount, due_date, status, issued_at)
VALUES
(1, 'MTR001', 100.50, '2025-11-15', 'UNPAID', '2025-10-10T08:30:00'),

(2, 'MTR002', 200.00, '2025-11-20', 'PAID', '2025-10-05T09:45:00'),

(3, 'MTR003', 150.75, '2025-10-10', 'OVERDUE', '2025-09-01T12:00:00'),

(4, 'MTR004', 500.86, '2025-12-01', 'UNPAID', '2025-10-12T14:15:00');
ON CONFLICT (name) DO NOTHING;