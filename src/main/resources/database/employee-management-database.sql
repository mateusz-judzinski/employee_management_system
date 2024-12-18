DROP SCHEMA IF EXISTS `employee-management-database`;
CREATE SCHEMA `employee-management-database`;

use `employee-management-database`;

DROP TABLE IF EXISTS employee;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS shift;
DROP TABLE IF EXISTS postion;
DROP TABLE IF EXISTS skill;
DROP TABLE IF EXISTS employee_skill;
DROP TABLE IF EXISTS position_employee_history;
DROP TABLE IF EXISTS shift_employee_history;

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
("przerwa", "przerwa", true),
("paszportówka", "paszportówka przy kontroli granicznej", true),
("przylot", "obsługa przylotów", false),
("melex", "transport pasażerów z parkingu pod terminal", false);

CREATE TABLE employee (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone_number VARCHAR(50) NOT NULL,
    id_card_number INT,
    position_id INT,
    FOREIGN KEY (position_id) REFERENCES `position`(id)
);

INSERT INTO employee (first_name, last_name, email, phone_number, id_card_number, position_id) 
VALUES 
('Adam', 'Demski', 'demski@gmail.com', '543241573', 1234, 1),
('Grzegorz', 'Wawoczny', 'wawoczny@gmail.com', '869455731', null, 3),
('Katarzyna', 'Piotrowska', 'k.piotrowska@gmail.com', '689473621', 4321, 2),
('Paweł', 'Wiśniewski', 'p.wisniewski@gmail.com', '758924163', 5534, 5),
('Monika', 'Lewandowska', 'm.lewandowska@gmail.com', '632874152', null, 4);


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
(1, '2024-12-15', "Zmiana poranna", '06:00:00', '14:00:00'),
(2, '2024-12-11', "Zmiana popołudniowa", '10:00:00', '18:00:00'),
(3, '2024-12-14', "Zmiana nocna", '22:00:00', '06:00:00'),
(4, '2024-12-12', "Zmiana dzienna", '08:00:00', '16:00:00'),
(5, '2024-12-13', "Zmiana wieczorna", '14:00:00', '22:00:00'),

(2, '2025-01-03', 'Zmiana poranna', '06:00:00', '14:00:00'),
(3, '2025-01-04', 'Zmiana dzienna', '08:00:00', '16:00:00'),
(4, '2025-01-05', 'Zmiana wieczorna', '14:00:00', '22:00:00'),
(5, '2025-01-06', 'Zmiana nocna', '22:00:00', '06:00:00'),
(1, '2025-01-07', 'Zmiana popołudniowa', '10:00:00', '18:00:00');


CREATE TABLE skill (
    id INT AUTO_INCREMENT PRIMARY KEY,
    skill_name VARCHAR(50) NOT NULL,
    description TEXT
);

INSERT INTO skill (skill_name, description)
VALUES 
("ciąg", "obsługa ciągu"),
("skan", "obsługa skanu"),
("paszportówka", "pomoc pasażerom na kontrolii granicznej"),
("przyloty", "obsługa pasażerów na strefie przylotów"),
("melex", "transport pasażerów z parkingu pod terminal");


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
(1, 1, "początkujący"),
(1, 2, "początkujący"),
(2, 1, "średnio zaawansowany"),
(2, 5, "ekspert"),
(3, 3, "zaawansowany"),
(3, 4, "początkujący"),
(4, 2, "średnio zaawansowany"),
(4, 3, "początkujący"),
(5, 1, "ekspert"),
(5, 4, "zaawansowany");


CREATE TABLE position_employee_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT NOT NULL,
    position_id INT NOT NULL,
    start_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employee(id),
    FOREIGN KEY (position_id) REFERENCES `position`(id)
);

INSERT INTO position_employee_history (employee_id, position_id, start_date, start_time, end_time)
VALUES
(1, 1, '2023-01-01', '08:00:00', '16:00:00'),
(1, 2, '2023-06-02', '09:00:00', '17:00:00'),
(2, 2, '2022-03-01', '07:00:00', '15:00:00'),
(2, 3, '2023-03-02', '10:00:00', '18:00:00'),
(3, 1, '2021-05-15', '08:30:00', '16:30:00'),
(4, 4, '2024-01-01', '09:00:00', '17:00:00');


CREATE TABLE shift_employee_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT NOT NULL,
    work_date DATE NOT NULL,
    shift_name VARCHAR(50) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employee(id)
);

INSERT INTO shift_employee_history (employee_id, work_date, shift_name, start_time, end_time)
VALUES
(1, '2024-05-01', 'Ranna', '08:00:00', '16:00:00'),
(1, '2024-05-02', 'Wieczorna', '16:00:00', '00:00:00'),
(2, '2024-05-01', 'Nocna', '00:00:00', '08:00:00'),
(2, '2024-05-02', 'Ranna', '08:00:00', '16:00:00'),
(3, '2024-05-03', 'Wieczorna', '16:00:00', '00:00:00'),
(4, '2024-05-04', 'Ranna', '08:00:00', '16:00:00');
