-- Users
INSERT INTO users (user_name, password, role)
VALUES
    ('LucasBaas', 'adminpass', 'ROLE_ADMIN'),

    ('Yoyo', 'wachtwoord123', 'ROLE_STUDENT'),
    ('Tim', 'wachtwoord123', 'ROLE_STUDENT'),

    ('Alice', 'wachtwoord123', 'ROLE_CUSTOMER'),
    ('Bob', 'wachtwoord123', 'ROLE_CUSTOMER'),
    ('Charlie', 'wachtwoord123', 'ROLE_CUSTOMER');

-- Persons
INSERT INTO persons (id, first_name, last_name, email, profile_label)
VALUES
    (1, 'Lucas', 'Janssen', 'lucas@admin.com', NULL),

    (2, 'Jane', 'Doe', 'jane@student.com', 'StudentProfile'),
    (3, 'Tim', 'Vriend', 'tim@student.com', 'StudentProfile'),

    (4, 'Alice', 'Malice', 'alice@customer.com', 'CustomerProfile'),
    (5, 'Bob', 'Job', 'bob@customer.com', 'CustomerProfile'),
    (6, 'Charlie', 'Barlie', 'charlie@customer.com', 'CustomerProfile');

-- Student Profiles
INSERT INTO student_profiles (id, school_period)
VALUES
    (2, 'Middenbouw'),
    (3, 'Bovenbouw');

-- Customer Profiles
INSERT INTO customer_profiles (id, phone_number)
VALUES
    (4, '+31611111111'),
    (5, '+31622222222'),
    (6, '+31633333333');

