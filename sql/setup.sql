-- Run in MySQL Workbench or: mysql -u root -p < sql/setup.sql
CREATE DATABASE IF NOT EXISTS ticket_booking_db;
USE ticket_booking_db;

-- OPTIONAL: Hibernate (ddl-auto=update) creates this table automatically.
CREATE TABLE IF NOT EXISTS tickets (
    id             BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    passenger_name VARCHAR(100) NOT NULL,
    email          VARCHAR(150) NOT NULL,
    source         VARCHAR(100) NOT NULL,
    destination    VARCHAR(100) NOT NULL,
    phone          VARCHAR(20)  NOT NULL
);

-- OPTIONAL sample data (fictional). Insert only once.
-- INSERT INTO tickets (passenger_name,email,source,destination,phone)
-- VALUES ('Cherry','cherry@example.com','Angallu','Bangalore','1234567890');
