-- Users
INSERT INTO users (user_name, password, role)
VALUES ('LucasBaas', '$2y$10$2MKeX/xCH1l2JqScMvvPpecA0FTSuQ1h01ds07rOzoHz0bTAHjbfW', 'ROLE_ADMIN'),

       ('Yoyo', '$2y$10$rKu8uI9oeNjaXuUI1vlLz.f97I7o6tGCAz1FZ9zWo9bnU4lRJp98C', 'ROLE_STUDENT'),
       ('Tim', '$2y$10$rKu8uI9oeNjaXuUI1vlLz.f97I7o6tGCAz1FZ9zWo9bnU4lRJp98C', 'ROLE_STUDENT'),

       ('Alice', '$2y$10$rKu8uI9oeNjaXuUI1vlLz.f97I7o6tGCAz1FZ9zWo9bnU4lRJp98C', 'ROLE_CUSTOMER'),
       ('Bob', '$2y$10$rKu8uI9oeNjaXuUI1vlLz.f97I7o6tGCAz1FZ9zWo9bnU4lRJp98C', 'ROLE_CUSTOMER'),
       ('Charlie', '$2y$10$rKu8uI9oeNjaXuUI1vlLz.f97I7o6tGCAz1FZ9zWo9bnU4lRJp98C', 'ROLE_CUSTOMER'),

       ('Loesje', '$2y$10$rKu8uI9oeNjaXuUI1vlLz.f97I7o6tGCAz1FZ9zWo9bnU4lRJp98C', 'ROLE_STUDENT'),
       ('jack', '$2y$10$rKu8uI9oeNjaXuUI1vlLz.f97I7o6tGCAz1FZ9zWo9bnU4lRJp98C', 'ROLE_STUDENT');


-- Persons
INSERT INTO persons (id, first_name, last_name, email)
VALUES (1, 'Lucas', 'Janssen', 'lucas@admin.com'),

       (2, 'Jane', 'Doe', 'jane@student.com'),
       (3, 'Tim', 'Vriend', 'tim@student.com'),
       (7, 'Loes', 'Bergsma', 'loes@student.com'),
       (8, 'Jack', 'Visser', 'jack@student.com'),

       (4, 'Alice', 'Malice', 'alice@customer.com'),
       (5, 'Bob', 'Job', 'bob@customer.com'),
       (6, 'Charlie', 'Barlie', 'charlie@customer.com');

-- Student Profiles
INSERT INTO student_profiles (id, team)
VALUES (2, 'bovenbouw'),
       (3, 'bovenbouw'),
       (7, 'onderbouw'),
       (8, 'middenbouw');

-- Customer Profiles
INSERT INTO customer_profiles (id, phone_number)
VALUES (4, '0611111111'),
       (5, '0622222222'),
       (6, '0633333333');

-- Products
INSERT INTO products (name, description, price, stock_quantity, cost_price, maker_id)
VALUES ('Vogelhuisje', 'Gemaakt door Tim, voor koolmeesjes.', 1.00, 2, 0.75, 3),
       ('Schilderij', 'Zonsondergang', 5.00, 1, 2.00, 3),
       ('Vogelhuisje 2', 'Gemaakt met liefde.', 1.00, 2, 0.75, 7),
       ('Schilderij 2', 'Mijn lieverlingskleuren', 5.00, 1, 2.00, 2),
       ('Vogelhuisje 3', 'All birds matter.', 1.00, 2, 0.75, 8),
       ('Schilderij 3', '"Mama en ik" Alleen de mama van Loes mag dit kopen!', 5.00, 1, 2.00, 7);


-- Carts
INSERT INTO carts (customer_id)
VALUES (4);


-- Cart Items
INSERT INTO cart_items (product_id, quantity, cart_id)
VALUES (1, 2, 1),
       (2, 1, 1);

-- Order
INSERT INTO orders (customer_id, order_date, total_price, status)
VALUES (4, '2026-05-05 13:12:00', 1.00, 'READY'),
       (5, '2026-05-05 14:00:00', 15.00, 'PENDING');

-- Order Items
INSERT INTO order_items (order_id, product_id, quantity, product_name, product_price, subtotal)
VALUES (1, 1, 1, 'Vogelhuisje', 1.00, 1.00),
       (2, 4, 2, 'Schilderij 2', 5.00, 10.00),
       (2, 2, 1, 'Schilderij', 5.00, 5.00);