-- Seed Roles Table
INSERT INTO roles (name, created_at)
VALUES
    ('ADMIN', NOW()),
    ('USER', NOW()),
    ('MANAGER', NOW());
