INSERT INTO users (user_name, first_name, last_name, password, email, role, profile_label)
VALUES
    ('Yoyo', 'Jane', 'Doe', 'wachtwoord123', 'jane@student.com', 'ROLE_STUDENT', 'StudentProfile'),
    ('willSmith', 'Will', 'Smith', 'wachtwoord456', 'will@customer.com', 'ROLE_CUSTOMER', 'CustomerProfile'),
    ('LucasBaas', 'Lucas', 'Janssen', 'adminpass', 'lucas@admin.com', 'ROLE_ADMIN', null),
    ('Tim', 'Tim', 'Vriend', 'wachtwoord123', 'tim@student.com', 'ROLE_STUDENT', 'StudentProfile');

INSERT INTO student_profiles (user_id, school_period)
VALUES
    (1, 'Middenbouw'),
    (4, 'Bovenbouw');

INSERT INTO customer_profiles (user_id, phone_number)
VALUES
    (2, '+31612345678');


