-- Create the database
CREATE DATABASE IF NOT EXISTS bus_reservation;
USE bus_reservation;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    user_type ENUM('ADMIN', 'CUSTOMER') DEFAULT 'CUSTOMER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create buses table
CREATE TABLE IF NOT EXISTS buses (
    bus_id INT AUTO_INCREMENT PRIMARY KEY,
    bus_number VARCHAR(20) NOT NULL UNIQUE,
    bus_name VARCHAR(100) NOT NULL,
    source_location VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    departure_time TIME NOT NULL,
    arrival_time TIME NOT NULL,
    journey_date DATE NOT NULL,
    total_seats INT NOT NULL,
    available_seats INT NOT NULL,
    fare DECIMAL(10, 2) NOT NULL,
    bus_type ENUM('AC', 'NON_AC', 'SLEEPER', 'SEMI_SLEEPER') NOT NULL
);

-- Create reservations table
CREATE TABLE IF NOT EXISTS reservations (
    reservation_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    bus_id INT NOT NULL,
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    journey_date DATE NOT NULL,
    number_of_seats INT NOT NULL,
    total_fare DECIMAL(10, 2) NOT NULL,
    status ENUM('CONFIRMED', 'CANCELLED', 'PENDING') DEFAULT 'PENDING',
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (bus_id) REFERENCES buses(bus_id)
);

-- Create seats table to track individual seats
CREATE TABLE IF NOT EXISTS seats (
    seat_id INT AUTO_INCREMENT PRIMARY KEY,
    reservation_id INT,
    bus_id INT NOT NULL,
    seat_number VARCHAR(10) NOT NULL,
    is_booked BOOLEAN DEFAULT FALSE,
    journey_date DATE NOT NULL,
    FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id) ON DELETE SET NULL,
    FOREIGN KEY (bus_id) REFERENCES buses(bus_id),
    UNIQUE KEY unique_seat (bus_id, seat_number, journey_date)
);

-- Insert admin user
INSERT INTO users (username, password, full_name, email, user_type)
VALUES ('admin', 'admin123', 'System Administrator', 'admin@busreservation.com', 'ADMIN');

-- Insert some sample buses
INSERT INTO buses (bus_number, bus_name, source_location, destination, departure_time, arrival_time, journey_date, total_seats, available_seats, fare, bus_type)
VALUES 
('B001', 'Express Traveller', 'New York', 'Washington DC', '08:00:00', '14:00:00', '2023-06-15', 40, 40, 45.50, 'AC'),
('B002', 'Night Rider', 'Boston', 'Chicago', '22:00:00', '10:00:00', '2023-06-15', 36, 36, 75.00, 'SLEEPER'),
('B003', 'City Connector', 'San Francisco', 'Los Angeles', '09:30:00', '16:30:00', '2023-06-16', 45, 45, 35.75, 'NON_AC');

-- Create a procedure to book seats
DELIMITER //
CREATE PROCEDURE book_seats(
    IN p_user_id INT,
    IN p_bus_id INT,
    IN p_journey_date DATE,
    IN p_number_of_seats INT
)
BEGIN
    DECLARE v_available_seats INT;
    DECLARE v_fare DECIMAL(10, 2);
    DECLARE v_total_fare DECIMAL(10, 2);
    DECLARE v_reservation_id INT;
    
    -- Get available seats and fare
    SELECT available_seats, fare INTO v_available_seats, v_fare
    FROM buses WHERE bus_id = p_bus_id;
    
    -- Check if enough seats are available
    IF v_available_seats >= p_number_of_seats THEN
        -- Calculate total fare
        SET v_total_fare = v_fare * p_number_of_seats;
        
        -- Create reservation
        INSERT INTO reservations (user_id, bus_id, journey_date, number_of_seats, total_fare, status)
        VALUES (p_user_id, p_bus_id, p_journey_date, p_number_of_seats, v_total_fare, 'CONFIRMED');
        
        -- Get the reservation ID
        SET v_reservation_id = LAST_INSERT_ID();
        
        -- Update available seats
        UPDATE buses SET available_seats = available_seats - p_number_of_seats
        WHERE bus_id = p_bus_id;
        
        -- Return the reservation ID
        SELECT v_reservation_id AS reservation_id, 'Booking successful' AS message;
    ELSE
        -- Not enough seats available
        SELECT 0 AS reservation_id, 'Not enough seats available' AS message;
    END IF;
END //
DELIMITER ;
select * from users;
select * from buses;
select * from reservations;
