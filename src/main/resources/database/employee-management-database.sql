DROP SCHEMA IF EXISTS `employee-management-database`;
CREATE SCHEMA `employee-management-database`;

use `employee-management-database`;

DROP TABLE IF EXISTS employee;
DROP TABLE IF EXISTS user;


CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone_number VARCHAR(50) NOT NULL,
    role ENUM('ROLE_SUPERVISOR', 'ROLE_LEADER') NOT NULL
);

CREATE TABLE employee (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone_number VARCHAR(50) NOT NULL
);


INSERT INTO user (username, password, first_name, last_name, email, phone_number, role) 
VALUES 
('admin', '$2a$10$YeNQSCiAQmGHZhFIZunDeuO4bmvgvEdO..vN6CemsyPJIAaUx2LzW', 'Marcin', 'Burzyński', 'admin@gmail.com', '654352211', 'ROLE_SUPERVISOR'),
('matt', '$2a$10$BpWABldCaTIE3FcpqCzVxOAk.915XZnT485DXQfbgiOzI61HVZMi6', 'Mateusz', 'Judziński', 'matt@gmail.com', '517285370', 'ROLE_LEADER');

INSERT INTO employee (first_name, last_name, email, phone_number) 
VALUES 
('Adam', 'Demski', 'demski@gmail.com', '543241573'),
('Grzegorz', 'Wawoczny', 'wawoczny@gmail.com', '869455731');
