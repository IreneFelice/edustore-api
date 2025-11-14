-- Users
INSERT INTO users (user_name, password, role)
VALUES
-- adminpass
--     ('LucasBaas', '$2a$10$CgaROrGRRZe.20Q/sZ4jeugUNxe123.z9gLS67p1k1Id0WLLRVrzG', 'ROLE_ADMIN'),
-- -- wachtwoord123
--     ('Yoyo', '$2a$10$y/ateyDnB0fXOUCqdIGQIe0heYCwkwyobrm2AXJgINS0u3FnuXwBS', 'ROLE_STUDENT'),
--     ('Tim', '$2a$10$lngvqDPV2LpYoYpVc7g2Gesum0czO1krMvqxTdgrk4Y/2sSGdfUwG', 'ROLE_STUDENT'),
--
--     ('Alice', '$2a$10$lngvqDPV2LpYoYpVc7g2Gesum0czO1krMvqxTdgrk4Y/2sSGdfUwG', 'ROLE_CUSTOMER'),
--     ('Bob', '$2a$10$lngvqDPV2LpYoYpVc7g2Gesum0czO1krMvqxTdgrk4Y/2sSGdfUwG', 'ROLE_CUSTOMER'),
--     ('Charlie', '$2a$10$lngvqDPV2LpYoYpVc7g2Gesum0czO1krMvqxTdgrk4Y/2sSGdfUwG', 'ROLE_CUSTOMER'),
--
--     ('Loesje', '$2a$10$y/ateyDnB0fXOUCqdIGQIe0heYCwkwyobrm2AXJgINS0u3FnuXwBS', 'ROLE_STUDENT'),
--     ('jack', '$2a$10$lngvqDPV2LpYoYpVc7g2Gesum0czO1krMvqxTdgrk4Y/2sSGdfUwG', 'ROLE_STUDENT');

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
INSERT INTO persons (id, first_name, last_name, email, profile_label)
VALUES
    (1, 'Lucas', 'Janssen', 'lucas@admin.com', NULL),

    (2, 'Jane', 'Doe', 'jane@student.com', 'StudentProfile'),
    (3, 'Tim', 'Vriend', 'tim@student.com', 'StudentProfile'),
    (7, 'Loes', 'Bergsma', 'loes@student.com', 'StudentProfile'),
    (8, 'Jack', 'Visser', 'jack@student.com', 'StudentProfile'),

    (4, 'Alice', 'Malice', 'alice@customer.com', 'CustomerProfile'),
    (5, 'Bob', 'Job', 'bob@customer.com', 'CustomerProfile'),
    (6, 'Charlie', 'Barlie', 'charlie@customer.com', 'CustomerProfile');

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

