DROP SCHEMA IF EXISTS `employee-management-database`;
CREATE SCHEMA `employee-management-database`;

use `employee-management-database`;

DROP TABLE IF EXISTS employee;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS shift;
DROP TABLE IF EXISTS postion;
DROP TABLE IF EXISTS skill;
DROP TABLE IF EXISTS employee_skill;

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

INSERT INTO user (username, password, first_name, last_name, email, phone_number, role) 
VALUES 
('admin', '$2a$10$YeNQSCiAQmGHZhFIZunDeuO4bmvgvEdO..vN6CemsyPJIAaUx2LzW', 'Marcin', 'Burzyński', 'admin@gmail.com', '654352211', 'ROLE_SUPERVISOR'),
('matt', '$2a$10$BpWABldCaTIE3FcpqCzVxOAk.915XZnT485DXQfbgiOzI61HVZMi6', 'Mateusz', 'Judziński', 'matt@gmail.com', '517285370', 'ROLE_LEADER');

CREATE TABLE `position` (
    id INT AUTO_INCREMENT PRIMARY KEY,
    position_name VARCHAR(100) NOT NULL,
	description TEXT,
    is_active BOOLEAN
);

INSERT INTO `position` (position_name, description, is_active) 
VALUES 
("ciąg", "ciąg pierwszy", true),
("skan", "skan główny", true),
("przerwa", "przerwa", true);

CREATE TABLE employee (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone_number VARCHAR(50) NOT NULL,
    position_id INT,
    FOREIGN KEY (position_id) REFERENCES `position`(id)
);

INSERT INTO employee (first_name, last_name, email, phone_number, position_id) 
VALUES 
('Adam', 'Demski', 'demski@gmail.com', '543241573', 1),
('Grzegorz', 'Wawoczny', 'wawoczny@gmail.com', '869455731', 3);


CREATE TABLE shift (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT,
    work_date DATE NOT NULL,
    shift_name VARCHAR(50) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employee(id)
);

INSERT INTO shift (employee_id, work_date, shift_name, start_time, end_time) 
VALUES 
(1, '2024-12-11', "Zmiana poranna", '06:00:00', '14:00:00'),
(1, '2024-12-09', "Zmiana poranna", '06:00:00', '14:00:00'),
(2, '2024-12-11', "Zmiana popołudniowa", '10:00:00', '18:00:00');


CREATE TABLE skill (
    id INT AUTO_INCREMENT PRIMARY KEY,
    skill_name VARCHAR(50) NOT NULL,
    description TEXT
);

INSERT INTO skill (skill_name, description)
VALUES 
("ciąg", "obsługa ciągu"),
("skan", "obsługa skanu");


CREATE TABLE employee_skill (
    employee_id INT NOT NULL,
    skill_id INT NOT NULL,
    proficiency_level VARCHAR(50) NOT NULL,
	PRIMARY KEY (employee_id, skill_id),
    FOREIGN KEY (employee_id) REFERENCES employee(id),
    FOREIGN KEY (skill_id) REFERENCES skill(id)
);

INSERT INTO employee_skill (employee_id, skill_id, proficiency_level)
VALUES 
(1, 1, "BEGINNER"),
(1, 2, "BEGINNER"),
(2, 1, "BEGINNER");



