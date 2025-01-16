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
    ('Karolina', 'Kamińska', 'karolina.kaminska@example.com', '123456789', '1000', NULL),
    ('Dominika', 'Rutkowska', 'dominika.rutkowska@example.com', '123456780', '1001', NULL),
    ('Julia', 'Głowacka', 'julia.glowacka@example.com', '123456781', '1002', NULL),
    ('Mateusz', 'Jakubiel', 'mateusz.jakubiel@example.com', '123456782', '1003', NULL),
    ('Mateusz', 'Moliński', 'mateusz.molinski@example.com', '123456783', '1004', NULL),
    ('Wiktoria', 'Dworakowska', 'wiktoria.dworakowska@example.com', '123456784', '1005', NULL),
    ('Amelia', 'Solińska', 'amelia.solinska@example.com', '123456785', '1006', NULL),
    ('Wiktor', 'Listopad', 'wiktor.listopad@example.com', '123456786', '1007', NULL),
    ('Emilia', 'Kantyka', 'emilia.kantyka@example.com', '123456787', '1008', NULL),
    ('Kornelia', 'Sobkowiak', 'kornelia.sobkowiak@example.com', '123456788', '1009', NULL),
    ('Aleksandra', 'Kalata', 'aleksandra.kalata@example.com', '123456789', '1010', NULL),
    ('Daria', 'Wrona', 'daria.wrona@example.com', '123456780', '1011', NULL),
    ('Rafał', 'Ciach', 'rafal.ciach@example.com', '123456781', '1012', NULL),
    ('Grzegorz', 'Bartoszko', 'grzegorz.bartoszko@example.com', '123456782', '1013', NULL),
    ('Joanna', 'Zdeb', 'joanna.zdeb@example.com', '123456783', '1014', NULL),
    ('Piotr', 'Kosicki', 'piotr.kosicki@example.com', '123456784', '1015', NULL),
    ('Dominik', 'Luszczek', 'dominik.luszczek@example.com', '123456785', '1016', NULL),
    ('Maksymilian', 'Grabowski', 'maksymilian.grabowski@example.com', '123456786', '1017', NULL),
    ('Antoni', 'Czopek', 'antoni.czopek@example.com', '123456787', '1018', NULL),
    ('Maksymilian', 'Szypa', 'maksymilian.szypa@example.com', '123456788', '1019', NULL),
    ('Julia', 'Ziętara', 'julia.zietara@example.com', '123456789', '1020', NULL),
    ('Kinga', 'Skurak', 'kinga.skurak@example.com', '123456780', '1021', NULL),
    ('Zuzanna', 'Obłąk', 'zuzanna.oblak@example.com', '123456781', '1022', NULL),
    ('Wojciech', 'Prabucki', 'wojciech.prabucki@example.com', '123456782', '1023', NULL),
    ('Aleksandra', 'Kieszkowska', 'aleksandra.kieszkowska@example.com', '123456783', '1024', NULL),
    ('Martyna', 'Wyciślak', 'martyna.wycislak@example.com', '123456784', '1025', NULL),
    ('Paweł', 'Łukawski', 'pawel.lukawski@example.com', '123456785', '1026', NULL),
    ('Anastasiia', 'Derkach', 'anastasiia.derkach@example.com', '123456786', '1027', NULL),
    ('Oskar', 'Szabatowski', 'oskar.szabatowski@example.com', '123456787', '1028', NULL),
    ('Grzegorz', 'Wawoczny', 'grzegorz.wawoczny@example.com', '123456788', '1029', NULL),
    ('Milena', 'Nowicka', 'milena.nowicka@example.com', '123456789', '1030', NULL),
    ('Katarzyna', 'Zbinkowska', 'katarzyna.zbinkowska@example.com', '123456780', '1031', NULL),
    ('Jakub', 'Maj', 'jakub.maj@example.com', '123456781', '1032', NULL),
    ('Magdalena', 'Goldwasser', 'magdalena.goldwasser@example.com', '123456782', '1033', NULL),
    ('Mikhail', 'Kondratenko', 'mikhail.kondratenko@example.com', '123456783', '1034', NULL),
    ('Natalia', 'Kuriata', 'natalia.kuriata@example.com', '123456784', '1035', NULL),
    ('Oleksii', 'Dyriv', 'oleksii.dyriv@example.com', '123456785', '1036', NULL),
    ('Michał', 'Protaś', 'michal.protas@example.com', '123456786', '1037', NULL),
    ('Maksymilian', 'Stasiak', 'maksymilian.stasiak@example.com', '123456787', '1038', NULL),
    ('Olivier', 'Dryja', 'olivier.dryja@example.com', '123456788', '1039', NULL),
    ('Pola', 'Straszko', 'pola.straszko@example.com', '123456789', '1040', NULL),
    ('Maciej', 'Stefański', 'maciej.stefanski@example.com', '123456780', '1041', NULL),
    ('Szymon', 'Lejczak', 'szymon.lejczak@example.com', '123456781', '1042', NULL),
    ('Diana', 'Klubko', 'diana.klubko@example.com', '123456782', '1043', NULL),
    ('Julia', 'Łękawska', 'julia.lekawska@example.com', '123456783', '1044', NULL),
    ('Illia', 'Klymko', 'illia.klymko@example.com', '123456784', '1045', NULL),
    ('Andżelika', 'Kaniak', 'andzelika.kaniak@example.com', '123456785', '1046', NULL),
    ('Maciej', 'Kozakiewicz', 'maciej.kozakiewicz@example.com', '123456786', '1047', NULL),
    ('Remigiusz', 'Popławski', 'remigiusz.poplawski@example.com', '123456787', '1048', NULL),
    ('Mateusz', 'Dynia', 'mateusz.dynia@example.com', '123456788', '1049', NULL),
    ('Hanna', 'Fedyczkowska', 'hanna.fedyczkowska@example.com', '123456789', '1050', NULL),
    ('Filip', 'Klepak', 'filip.klepak@example.com', '123456780', '1051', NULL),
    ('Julia', 'Nowak', 'julia.nowak@example.com', '123456781', '1052', NULL),
    ('Bartosz', 'Łata', 'bartosz.lata@example.com', '123456782', '1053', NULL),
    ('Konrad', 'Łukaszek', 'konrad.lukaszek@example.com', '123456783', '1054', NULL),
    ('Oliwia', 'Stodolna', 'oliwia.stodolna@example.com', '123456784', '1055', NULL),
    ('Anhelina', 'Somchenko', 'anhelina.somchenko@example.com', '123456785', '1056', NULL),
    ('Natalia', 'Wojtcza', 'natalia.wojtcza@example.com', '123456786', '1057', NULL),
    ('Jakub', 'Walczak', 'jakub.walczak@example.com', '123456787', '1058', NULL),
    ('Nikola', 'Kaznowska', 'nikola.kaznowska@example.com', '123456788', '1059', NULL),
    ('Wiaczesław', 'Lasota', 'wiaczeslaw.lasota@example.com', '123456789', '1060', NULL),
    ('Hanna', 'Banak', 'hanna.banak@example.com', '123456780', '1061', NULL),
    ('Sandra', 'Prokop', 'sandra.prokop@example.com', '123456781', '1062', NULL),
    ('Oliwia', 'Stachera', 'oliwia.stachera@example.com', '123456782', '1063', NULL),
    ('Alessandro', 'Bieniecki', 'alessandro.bieniecki@example.com', '123456783', '1064', NULL),
    ('Julia', 'August', 'julia.august@example.com', '123456784', '1065', NULL),
    ('Emil', 'Wojtkowicz', 'emil.wojtkowicz@example.com', '123456785', '1066', NULL),
    ('Dominika', 'Darłak', 'dominika.darlak@example.com', '123456786', '1067', NULL),
    ('Hubert', 'Jojczyk', 'hubert.jojczyk@example.com', '123456787', '1068', NULL),
    ('Ivan', 'Kondratenko', 'ivan.kondratenko@example.com', '123456788', '1069', NULL),
    ('Vanessa', 'Jarosińska', 'vanessa.jarosinska@example.com', '123456789', '1070', NULL),
    ('Julia', 'Osińska', 'julia.osinska@example.com', '123456780', '1071', NULL),
    ('Wiktor', 'Świerczyński', 'wiktor.swierczynski@example.com', '123456781', '1072', NULL),
    ('Emil', 'Malczak', 'emil.malczak@example.com', '123456782', '1073', NULL),
    ('Zuzanna', 'Kowalska', 'zuzanna.kowalska@example.com', '123456783', '1074', NULL),
    ('Aleksandra', 'Czyż', 'aleksandra.czyz@example.com', '123456784', '1075', NULL),
    ('Nina', 'Stachańczyk', 'nina.stachanczyk@example.com', '123456785', '1076', NULL),
    ('Walentyna', 'Brzoź', 'walentyna.brzoz@example.com', '123456786', '1077', NULL),
    ('Konrad', 'Konieczny', 'konrad.konieczny@example.com', '123456787', '1078', NULL),
    ('Filip', 'Pinkowski', 'filip.pinkowski@example.com', '123456788', '1079', NULL),
    ('Jakub', 'Piskorski', 'jakub.piskorski@example.com', '123456789', '1080', NULL),
    ('Mikołaj', 'Hruszowiec', 'mikolaj.hruszowiec@example.com', '123456780', '1081', NULL),
    ('Mateusz', 'Jojczyk', 'mateusz.jojczyk@example.com', '123456781', '1082', NULL),
    ('Oliwia', 'Czulińska', 'oliwia.czulinska@example.com', '123456782', '1083', NULL),
    ('Agata', 'Kozak', 'agata.kozak@example.com', '123456783', '1084', NULL),
    ('Szymon', 'Kibało', 'szymon.kibalo@example.com', '123456784', '1085', NULL),
    ('Weronika', 'Adamowska', 'weronika.adamowska@example.com', '123456785', '1086', NULL),
    ('Igor', 'Delimata', 'igor.delimata@example.com', '123456786', '1087', NULL),
    ('Kacper', 'Jon', 'kacper.jon@example.com', '123456787', '1088', NULL),
    ('Oliwia', 'Sztal', 'oliwia.sztal@example.com', '123456788', '1089', NULL),
    ('Kinga', 'Wiśniewska', 'kinga.wisniewska@example.com', '123456789', '1090', NULL),
    ('Maja', 'Pajączkowska', 'maja.pajaczkowska@example.com', '123456780', '1091', NULL),
    ('Mateusz', 'Lis', 'mateusz.lis@example.com', '123456781', '1092', NULL),
    ('Nikola', 'Abramowska', 'nikola.abramowska@example.com', '123456782', '1093', NULL),
    ('Wiktoria', 'Jacher', 'wiktoria.jacher@example.com', '123456783', '1094', NULL),
    ('Adam', 'Kadłubowski', 'adam.kadlubowski@example.com', '123456784', '1095', NULL),
    ('Kacper', 'Dolbicki', 'kacper.dolbicki@example.com', '123456785', '1096', NULL),
    ('Mateusz', 'Zieliński', 'mateusz.zielinski@example.com', '123456786', '1097', NULL),
    ('Anna', 'Antoszewska', 'anna.antoszewska@example.com', '123456787', '1098', NULL),
    ('Damian', 'Stanisławski', 'damian.stanislawski@example.com', '123456788', '1099', NULL),
    ('Maksymilian', 'Pormańczuk', 'maksymilian.pormanczuk@example.com', '123456789', '1100', NULL),
    ('Alexander', 'Szytko', 'alexander.szytko@example.com', '123456780', '1101', NULL),
    ('Kuba', 'Grabiński', 'kuba.grabinski@example.com', '123456781', '1102', NULL),
    ('Vladyslav', 'Demianko', 'vladyslav.demianko@example.com', '123456782', '1103', NULL),
    ('Izabela', 'Sawicka', 'izabela.sawicka@example.com', '123456783', '1104', NULL),
    ('Anton', 'Yatsyk', 'anton.yatsyk@example.com', '123456784', '1105', NULL),
    ('Jakub', 'Terpis', 'jakub.terpis@example.com', '123456785', '1106', NULL),
    ('Mikołaj', 'Kęszycki', 'mikolaj.keszycki@example.com', '123456786', '1107', NULL),
    ('Filip', 'Ząb', 'filip.zab@example.com', '123456787', '1108', NULL),
    ('Ksawery', 'Sokalski', 'ksawery.sokalski@example.com', '123456788', '1109', NULL),
    ('Martyna', 'Bagińska', 'martyna.baginska@example.com', '123456789', '1110', NULL),
    ('Filip', 'Lenart', 'filip.lenart@example.com', '123456780', '1111', NULL),
    ('Aleksandra', 'Balinowska', 'aleksandra.balinowska@example.com', '123456781', '1112', NULL),
    ('Marta', 'Petrynik', 'marta.petrynik@example.com', '123456782', '1113', NULL),
    ('Paulina', 'Romanowska', 'paulina.romanowska@example.com', '123456783', '1114', NULL),
    ('Wiktoria', 'Strzelecka', 'wiktoria.strzelecka@example.com', '123456784', '1115', NULL),
    ('Anna', 'Zinovieva', 'anna.zinovieva@example.com', '123456785', '1116', NULL),
    ('Konrad', 'Świebocki', 'konrad.swiebocki@example.com', '123456786', '1117', NULL),
    ('Sara', 'Gozdzek', 'sara.gozdzek@example.com', '123456787', '1118', NULL),
    ('Zuzanna', 'Misdzioł', 'zuzanna.misdziol@example.com', '123456788', '1119', NULL),
    ('Natalia', 'Kuśmierek', 'natalia.kusmierek@example.com', '123456789', '1120', NULL),
    ('Dawid', 'Michałowski', 'dawid.michalowski@example.com', '123456780', '1121', NULL),
    ('Mateusz', 'Mroczyński', 'mateusz.mroczynski@example.com', '123456781', '1122', NULL),
    ('Sofiia', 'Malchaniuk', 'sofiia.malchaniuk@example.com', '123456782', '1123', NULL),
    ('Michelle', 'Martyniak', 'michelle.martyniak@example.com', '123456783', '1124', NULL),
    ('Sara', 'Figaj', 'sara.figaj@example.com', '123456784', '1125', NULL),
    ('Magdalena', 'Kardaś', 'magdalena.kardas@example.com', '123456785', '1126', NULL),
    ('Natalia', 'Kuśmierek', 'natalia.kusmierek@example.com', '123456786', '1127', NULL);



CREATE TABLE shift (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT,
    work_date DATE NOT NULL,
    shift_name VARCHAR(50) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employee(id)
);

-- INSERT INTO shift (employee_id, work_date, shift_name, start_time, end_time) 
-- VALUES 
-- (1, '2024-12-30', "Zmiana poranna", '06:00:00', '14:00:00'),
-- (1, '2024-12-15', "Zmiana poranna", '06:00:00', '14:00:00'),
-- (2, '2024-12-11', "Zmiana popołudniowa", '10:00:00', '18:00:00'),
-- (3, '2024-12-14', "Zmiana nocna", '22:00:00', '06:00:00'),
-- (4, '2024-12-12', "Zmiana dzienna", '08:00:00', '16:00:00'),
-- (5, '2024-12-23', "Zmiana wieczorna", '14:00:00', '22:00:00'),

-- (2, '2025-01-03', 'Zmiana poranna', '06:00:00', '14:00:00'),
-- (3, '2025-01-04', 'Zmiana dzienna', '08:00:00', '16:00:00'),
-- (4, '2025-01-05', 'Zmiana wieczorna', '14:00:00', '22:00:00'),
-- (5, '2025-01-10', 'Zmiana nocna', '22:00:00', '06:00:00'),
-- (1, '2025-01-09', 'Zmiana popołudniowa', '10:00:00', '18:00:00');


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
