-- =========================================================
-- DATABASE: Variable Electricity Tariff Configuration & Indicator
-- =========================================================
-- This script initializes the smart_tariff_system database.
-- Run this in MySQL if the database doesn't already exist.
-- =========================================================

CREATE DATABASE IF NOT EXISTS smart_tariff_system;
USE smart_tariff_system;

-- =========================================================
-- 1️⃣ Table: districts
-- =========================================================
-- Stores district information and electricity provider details.
CREATE TABLE IF NOT EXISTS districts (
    district_id INT AUTO_INCREMENT PRIMARY KEY,
    district_name VARCHAR(100) NOT NULL,
    provider_name VARCHAR(100) NOT NULL,
    region_code VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Sample districts (insert only if table is empty)
INSERT IGNORE INTO districts (district_id, district_name, provider_name, region_code)
VALUES 
(1, 'Udupi', 'MESCOM', 'UD001'),
(2, 'Bangalore', 'BESCOM', 'BLR001'),
(3, 'Mangalore', 'MESCOM', 'MNG001');

-- =========================================================
-- 2️⃣ Table: tariff_settings
-- =========================================================
-- Stores tariff threshold levels for each district.
CREATE TABLE IF NOT EXISTS tariff_settings (
    tariff_id INT AUTO_INCREMENT PRIMARY KEY,
    district_id INT NOT NULL,
    low_limit DECIMAL(10,2) NOT NULL,
    medium_limit DECIMAL(10,2) NOT NULL,
    high_limit DECIMAL(10,2) NOT NULL,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (district_id) REFERENCES districts(district_id) ON DELETE CASCADE
);

-- Sample tariff thresholds (insert only if table is empty)
INSERT IGNORE INTO tariff_settings (tariff_id, district_id, low_limit, medium_limit, high_limit)
VALUES
(1, 1, 2.00, 4.00, 6.00),
(2, 2, 3.00, 5.00, 7.00),
(3, 3, 2.50, 4.50, 6.50);

-- =========================================================
-- 3️⃣ Table: usage_data
-- =========================================================
-- Stores real-time or simulated electricity usage per district.
CREATE TABLE IF NOT EXISTS usage_data (
    usage_id INT AUTO_INCREMENT PRIMARY KEY,
    district_id INT NOT NULL,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    power_usage DECIMAL(10,2) NOT NULL,        -- e.g., kWh
    cost DECIMAL(10,2) NOT NULL,               -- calculated tariff cost
    tariff_level VARCHAR(10) NOT NULL,         -- 'Low', 'Medium', 'High'
    FOREIGN KEY (district_id) REFERENCES districts(district_id) ON DELETE CASCADE
);

-- Sample usage data
INSERT IGNORE INTO usage_data (usage_id, district_id, power_usage, cost, tariff_level)
VALUES
(1, 1, 1.8, 3.60, 'Low'),
(2, 1, 3.5, 7.00, 'Medium'),
(3, 1, 6.2, 12.40, 'High'),
(4, 2, 4.2, 8.40, 'Medium'),
(5, 3, 2.1, 4.20, 'Low');

-- =========================================================
-- 4️⃣ Table: admin_users
-- =========================================================
-- For login and admin panel access.
CREATE TABLE IF NOT EXISTS admin_users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Sample admin (password should be hashed in production)
INSERT IGNORE INTO admin_users (id, username, password, email)
VALUES (1, 'Akash', 'akash@0987', 'akash@example.com');
