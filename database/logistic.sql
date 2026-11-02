
CREATE DATABASE IF NOT EXISTS logistic_db;

USE logistic_db;

-- Table 1: Shipments
CREATE TABLE shipments (
    shipment_id INT AUTO_INCREMENT PRIMARY KEY,
    source VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    weight DECIMAL(10,2) NOT NULL,
    shipment_date DATE NOT NULL,
    status VARCHAR(30) DEFAULT 'Pending'
);

-- Table 2: Routes
CREATE TABLE routes (
    route_id INT AUTO_INCREMENT PRIMARY KEY,
    source VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    distance DECIMAL(10,2) NOT NULL,
    transport_mode VARCHAR(50) NOT NULL
);

-- Table 3: Transportation Costs
CREATE TABLE transportation_cost (
    cost_id INT AUTO_INCREMENT PRIMARY KEY,
    route_id INT NOT NULL,
    fuel_cost_per_km DECIMAL(10,2) NOT NULL,
    driver_charges DECIMAL(10,2) DEFAULT 0,
    other_charges DECIMAL(10,2) DEFAULT 0,
    FOREIGN KEY (route_id) REFERENCES routes(route_id)
);

-- Sample routes
INSERT INTO routes
(source, destination, distance, transport_mode)
VALUES
('Ranchi', 'Patna', 330, 'Truck'),
('Ranchi', 'Patna', 350, 'Truck'),
('Ranchi', 'Jamshedpur', 130, 'Truck'),
('Ranchi', 'Dhanbad', 165, 'Truck');

-- Sample transportation costs
INSERT INTO transportation_cost
(route_id, fuel_cost_per_km, driver_charges, other_charges)
VALUES
(1, 18, 1500, 500),
(2, 16, 1300, 400),
(3, 17, 900, 300),
(4, 19, 1000, 350);
