-- Smart Library Management System - Database Schema

-- Clear existing tables if they exist
DROP TABLE IF EXISTS audit_logs;
DROP TABLE IF EXISTS fines;
DROP TABLE IF EXISTS loans;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS books;

-- 1. Books Table
CREATE TABLE books (
    book_id SERIAL PRIMARY KEY,
    isbn VARCHAR(20) UNIQUE NOT NULL,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    total_copies INT NOT NULL DEFAULT 1,
    available_copies INT NOT NULL DEFAULT 1,
    CONSTRAINT chk_copies CHECK (available_copies <= total_copies AND available_copies >= 0)
);

-- 2. Students Table
CREATE TABLE students (
    student_id SERIAL PRIMARY KEY,
    student_code VARCHAR(20) UNIQUE NOT NULL, -- e.g., STU001
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    course VARCHAR(100),
    department VARCHAR(100)
);

-- 3. Users Table (Authentication)
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('ADMIN', 'STUDENT')),
    student_id INT UNIQUE REFERENCES students(student_id) ON DELETE CASCADE
);

-- 4. Loans Table
CREATE TABLE loans (
    loan_id SERIAL PRIMARY KEY,
    book_id INT NOT NULL REFERENCES books(book_id),
    student_id INT NOT NULL REFERENCES students(student_id),
    borrow_date DATE NOT NULL DEFAULT CURRENT_DATE,
    due_date DATE NOT NULL,
    return_date DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'RETURNED', 'OVERDUE'))
);

-- 5. Fines Table
CREATE TABLE fines (
    fine_id SERIAL PRIMARY KEY,
    loan_id INT NOT NULL REFERENCES loans(loan_id),
    amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'PAID')),
    paid_date TIMESTAMP
);

-- 6. Audit Logs Table
CREATE TABLE audit_logs (
    log_id SERIAL PRIMARY KEY,
    action VARCHAR(50) NOT NULL,
    user_id INT REFERENCES users(user_id),
    details TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Sample Data for Testing
INSERT INTO books (isbn, title, author, category, total_copies, available_copies) VALUES
('978-0134685991', 'Effective Java', 'Joshua Bloch', 'Programming', 5, 5),
('978-0132350884', 'Clean Code', 'Robert C. Martin', 'Programming', 3, 3),
('978-0134494166', 'Clean Architecture', 'Robert C. Martin', 'Architecture', 2, 2);

INSERT INTO students (student_code, name, email, course, department) VALUES
('STU001', 'John Doe', 'john.doe@university.edu', 'Computer Science', 'IT'),
('STU002', 'Jane Smith', 'jane.smith@university.edu', 'Software Engineering', 'IT');

-- Admin user (password: admin123) - In real app, this should be pre-hashed
-- Using a placeholder hash for now
INSERT INTO users (username, password_hash, role) VALUES
('admin', '$2a$10$e7v.T.J8y.I/fR/v6E.qUeG.mB1W7J9Fv8h8j7i6H5G4F3E2D1C0B', 'ADMIN');
