DROP SCHEMA IF EXISTS `employee-management-database`;
CREATE SCHEMA `employee-management-database`;

use `employee-management-database`;

DROP TABLE IF EXISTS employee;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS shift;
DROP TABLE IF EXISTS postion;
DROP TABLE IF EXISTS skill;
DROP TABLE IF EXISTS qualification;
DROP TABLE IF EXISTS employee_qualification;
DROP TABLE IF EXISTS position_qualification;
DROP TABLE IF EXISTS employee_skill;
DROP TABLE IF EXISTS position_employee_history;

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
("bagażwonia", "rozładowywanie bagaży w bagażowni"),
("melex", "transport pasażerów z parkingu pod terminal");

CREATE TABLE qualification (
	id INT AUTO_INCREMENT PRIMARY KEY,
    qualification_name VARCHAR(255) NOT NULL,
    description TEXT
);

INSERT INTO qualification (qualification_name, description)
VALUES
("stała przepustka", "posiada stałą przepustkę"),
("prawo jazdy", "posiada prawo jazdy"),
("podnoszenie ciężarów", "jest zdolny do pracy z ciężarami"),
("brak stałej przepustki", "niema stałej przepustki więc potrzebuje eksorty");


CREATE TABLE `position` (
    id INT AUTO_INCREMENT PRIMARY KEY,
    position_name VARCHAR(100) NOT NULL,
	description TEXT,
    is_active BOOLEAN NOT NULL,
    is_temporary BOOLEAN NOT NULL,
    skill_id INT,
    FOREIGN KEY (skill_id) REFERENCES `skill`(id)
);

INSERT INTO `position` (position_name, description, is_active, is_temporary, skill_id) 
VALUES 
("ciąg", "ciąg pierwszy", true, false, 1),
("skan", "skan główny", true, false, 2),
("przerwa", "przerwa", true, false, null),
("paszportówka", "paszportówka przy kontroli granicznej", true, false, 3),
("paszportówka (pomoc)", "pomoc przez vke", true, false, null),
("przylot", "obsługa przylotów", true, false, 4),
("przylot (pomoc)", "pomoc przez vke", true, false, null),
("bagażownia", "rozładunek bagaży", true, false, 5),
("bagażownia (pomoc)", "pomoc przez vke", true, false, null),
("melex", "transport pasażerów z parkingu pod terminal", true, false, 6),
("inne", "wszystkie stanowiska stworzone tymczasowo", false, false, null);


CREATE TABLE employee (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone_number VARCHAR(50) NOT NULL,
    id_card_number VARCHAR(50),
    position_id INT,
    FOREIGN KEY (position_id) REFERENCES `position`(id)
);

INSERT INTO employee (first_name, last_name, email, phone_number, id_card_number, position_id) 
VALUES 
('Adam', 'Demski', 'demski@gmail.com', '543241573', 1234, 3),
('Grzegorz', 'Wawoczny', 'wawoczny@gmail.com', '869455731', NULL, 3),
('Katarzyna', 'Piotrowska', 'k.piotrowska@gmail.com', '689473621', 4321, 3),
('Paweł', 'Wiśniewski', 'p.wisniewski@gmail.com', '758924163', 5534, 3),
('Monika', 'Lewandowska', 'm.lewandowska@gmail.com', '632874152', NULL, 3);


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
(1, '2024-12-30', "Zmiana poranna", '06:00:00', '14:00:00'),
(1, '2024-12-15', "Zmiana poranna", '06:00:00', '14:00:00'),
(2, '2024-12-11', "Zmiana popołudniowa", '10:00:00', '18:00:00'),
(3, '2024-12-14', "Zmiana nocna", '22:00:00', '06:00:00'),
(4, '2024-12-12', "Zmiana dzienna", '08:00:00', '16:00:00'),
(5, '2024-12-23', "Zmiana wieczorna", '14:00:00', '22:00:00'),

(2, '2025-01-03', 'Zmiana poranna', '06:00:00', '14:00:00'),
(3, '2025-01-04', 'Zmiana dzienna', '08:00:00', '16:00:00'),
(4, '2025-01-05', 'Zmiana wieczorna', '14:00:00', '22:00:00'),
(5, '2025-01-10', 'Zmiana nocna', '22:00:00', '06:00:00'),
(1, '2025-01-09', 'Zmiana popołudniowa', '10:00:00', '18:00:00');


CREATE TABLE employee_qualification (
    employee_id INT NOT NULL,
    qualification_id INT NOT NULL,
    PRIMARY KEY (employee_id, qualification_id),
    FOREIGN KEY (employee_id) REFERENCES employee(id),
    FOREIGN KEY (qualification_id) REFERENCES qualification(id)
);
INSERT INTO employee_qualification (employee_id, qualification_id)
VALUES
(1, 1),
(2, 1),
(3, 1),
(1, 2),
(2, 2),
(4, 2),
(1, 3),
(5, 3),
(3, 3),
(4, 4),
(5, 4);

CREATE TABLE position_qualification (
    position_id INT NOT NULL,
    qualification_id INT NOT NULL,
    PRIMARY KEY (position_id, qualification_id),
    FOREIGN KEY (position_id) REFERENCES `position`(id),
    FOREIGN KEY (qualification_id) REFERENCES qualification(id)
);

INSERT INTO position_qualification (position_id, qualification_id)
VALUES
(2, 1),
(4, 1),
(5, 4),
(6, 1),
(7, 4),
(8, 2),
(8, 1),
(9, 2),
(9, 4),
(10, 3);

CREATE TABLE employee_skill (
	id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT NOT NULL,
    skill_id INT NOT NULL,
    proficiency_level INT NOT NULL,
    time_experience_in_minutes INT NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employee(id),
    FOREIGN KEY (skill_id) REFERENCES skill(id)
);

INSERT INTO employee_skill (employee_id, skill_id, proficiency_level, time_experience_in_minutes)
VALUES 
(1, 1, 0, 1199),
(1, 2, 1, 1500),
(1, 3, 2, 3599),
(1, 4, 3, 8999),
(1, 5, 0, 500),
(1, 6, 2, 2500),
(2, 1, 1, 1800),
(2, 2, 0, 800),
(2, 3, 3, 8999),
(2, 4, 2, 4000),
(2, 5, 1, 1500),
(2, 6, 0, 1199),
(3, 1, 3, 8999),
(3, 2, 2, 3599),
(3, 3, 1, 2500),
(3, 4, 0, 1199),
(3, 5, 2, 5500),
(3, 6, 1, 1800),
(4, 1, 0, 1500),
(4, 2, 1, 2200),
(4, 3, 3, 8999),
(4, 4, 2, 3599),
(4, 5, 1, 1800),
(4, 6, 0, 800),
(5, 1, 2, 2500),
(5, 2, 3, 8999),
(5, 3, 1, 1199),
(5, 4, 2, 3599),
(5, 5, 0, 1500),
(5, 6, 1, 2000);


CREATE TABLE position_employee_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT NOT NULL,
    position_id INT NOT NULL,
    start_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME,
    is_active BOOLEAN NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employee(id),
    FOREIGN KEY (position_id) REFERENCES `position`(id)
);

INSERT INTO position_employee_history (employee_id, position_id, start_date, start_time, end_time, is_active)
VALUES
(1, 3, '2025-01-03', '18:00:00', null, true),
(2, 3, '2025-01-03', '18:00:00', null, true),
(3, 3, '2025-01-03', '18:00:00', null, true),
(4, 3, '2025-01-03', '18:00:00', null, true),
(5, 3, '2025-01-03', '18:00:00', null, true),
(1, 1, '2023-01-01', '08:00:00', '16:00:00', false),
(1, 2, '2023-06-02', '09:00:00', '17:00:00', false),
(2, 2, '2022-03-01', '07:00:00', '15:00:00', false),
(2, 3, '2023-03-02', '10:00:00', '18:00:00', false),
(3, 1, '2021-05-15', '08:30:00', '16:30:00', false),
(4, 4, '2024-01-01', '09:00:00', '17:00:00', false);
