-- Users
INSERT INTO users (user_name, password, role)
VALUES
-- adminpass
('LucasBaas', '$2y$10$2MKeX/xCH1l2JqScMvvPpecA0FTSuQ1h01ds07rOzoHz0bTAHjbfW', 'ROLE_ADMIN'),

-- wachtwoord123
('Yoyo', '$2y$10$rKu8uI9oeNjaXuUI1vlLz.f97I7o6tGCAz1FZ9zWo9bnU4lRJp98C', 'ROLE_STUDENT'),
('Tim', '$2y$10$rKu8uI9oeNjaXuUI1vlLz.f97I7o6tGCAz1FZ9zWo9bnU4lRJp98C', 'ROLE_STUDENT'),

('Alice', '$2y$10$rKu8uI9oeNjaXuUI1vlLz.f97I7o6tGCAz1FZ9zWo9bnU4lRJp98C', 'ROLE_CUSTOMER'),
('Bob', '$2y$10$rKu8uI9oeNjaXuUI1vlLz.f97I7o6tGCAz1FZ9zWo9bnU4lRJp98C', 'ROLE_CUSTOMER'),
('Charlie', '$2y$10$rKu8uI9oeNjaXuUI1vlLz.f97I7o6tGCAz1FZ9zWo9bnU4lRJp98C', 'ROLE_CUSTOMER'),

('Loesje', '$2y$10$rKu8uI9oeNjaXuUI1vlLz.f97I7o6tGCAz1FZ9zWo9bnU4lRJp98C', 'ROLE_STUDENT'),
('jack', '$2y$10$rKu8uI9oeNjaXuUI1vlLz.f97I7o6tGCAz1FZ9zWo9bnU4lRJp98C', 'ROLE_STUDENT');


-- Persons
INSERT INTO persons (id, first_name, last_name, email)
VALUES
    (1, 'Lucas', 'Janssen', 'lucas@admin.com'),

    (2, 'Jane', 'Doe', 'jane@student.com'),
    (3, 'Tim', 'Vriend', 'tim@student.com'),
    (7, 'Loes', 'Bergsma', 'loes@student.com'),
    (8, 'Jack', 'Visser', 'jack@student.com'),

    (4, 'Alice', 'Malice', 'alice@customer.com'),
    (5, 'Bob', 'Job', 'bob@customer.com'),
    (6, 'Charlie', 'Barlie', 'charlie@customer.com');

-- Student Profiles
INSERT INTO student_profiles (id, school_period)
VALUES
    (2, 'Middenbouw'),
    (3, 'Bovenbouw'),
    (7, 'Bovenbouw'),
    (8, 'Middenbouw');

-- Customer Profiles
INSERT INTO customer_profiles (id, phone_number)
VALUES
    (4, '+31611111111'),
    (5, '+31622222222'),
    (6, '+31633333333');

INSERT INTO products (name, description, price, stock_quantity, cost_price, sold_amount, maker_id)
VALUES
    ('Asbak', 'Gemaakt van klei', 1.00, 2, 0.75, 0, 2);
