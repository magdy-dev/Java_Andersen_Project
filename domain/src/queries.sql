-- Already exists in your schema
-- INSERT INTO user_roles (role_name) VALUES ('ADMIN'), ('CUSTOMER');

INSERT INTO users (username, password, email, full_name, role_id) VALUES
-- Admins (role_id = 1)
('admin1', 'password123', 'admin1@cowork.com', 'magdy', 1),
('admin2', 'password1234', 'admin2@cowork.com', 'yura', 1),

-- Customers (role_id = 2)
('alice_w', 'password12345', 'alice@email.com', 'Alice Williams', 2),
('bob_m', 'password123456', 'bob@email.com', 'Bob Miller', 2),
('charlie_b', 'password123411', 'charlie@email.com', 'Charlie Brown', 2),
('diana_k', 'password123422', 'diana@email.com', 'Diana King', 2),
('evan_s', 'password123455', 'evan@email.com', 'Evan Stone', 2);

INSERT INTO workspaces (name, description, type, price_per_hour, capacity) VALUES
-- Open spaces
('Open Space A', 'Spacious open area with natural light', 'OPEN_SPACE', 5.00, 20),
('Open Space B', 'Quiet zone with ergonomic chairs', 'OPEN_SPACE', 6.50, 15),

-- Private rooms
('Private Room 101', 'Small private office with desk', 'PRIVATE_ROOM', 15.00, 1),
('Private Room 102', 'Medium private office for 2 people', 'PRIVATE_ROOM', 25.00, 2),
('Private Room 103', 'Large private office for 4 people', 'PRIVATE_ROOM', 40.00, 4),

-- Meeting rooms
('Meeting Room A', 'Boardroom style for 8 people', 'MEETING_ROOM', 30.00, 8),
('Meeting Room B', 'Creative space with whiteboards', 'MEETING_ROOM', 35.00, 6),

-- Desks
('Hot Desk 1', 'Flexible desk in common area', 'DESK', 8.00, 1),
('Hot Desk 2', 'Standing desk option', 'DESK', 10.00, 1),
('Premium Desk', 'Corner desk with extra monitors', 'DESK', 12.00, 1);

INSERT INTO bookings (customer_id, workspace_id, booking_date, start_time, end_time, status, total_price) VALUES
-- Alice's bookings
(3, 1, '2023-06-01', '09:00:00', '12:00:00', 'COMPLETED', 15.00),
(3, 3, '2023-06-15', '10:00:00', '15:00:00', 'COMPLETED', 75.00),
(3, 6, '2023-07-01', '13:00:00', '16:00:00', 'CONFIRMED', 90.00),

-- Bob's bookings
(4, 2, '2023-06-02', '08:00:00', '17:00:00', 'COMPLETED', 58.50),
(4, 9, '2023-06-20', '14:00:00', '18:00:00', 'COMPLETED', 32.00),
(4, 5, '2023-07-05', '09:00:00', '13:00:00', 'CONFIRMED', 100.00),

-- Charlie's bookings
(5, 4, '2023-06-10', '11:00:00', '16:00:00', 'COMPLETED', 125.00),
(5, 7, '2023-06-25', '08:30:00', '10:30:00', 'CANCELLED', 70.00),
(5, 8, '2023-07-10', '12:00:00', '17:00:00', 'CONFIRMED', 50.00),

-- Diana's bookings
(6, 10, '2023-06-05', '09:00:00', '18:00:00', 'COMPLETED', 108.00),
(6, 1, '2023-06-18', '13:00:00', '17:00:00', 'COMPLETED', 20.00),
(6, 2, '2023-07-15', '10:00:00', '12:00:00', 'CONFIRMED', 13.00),

-- Evan's bookings
(7, 3, '2023-06-08', '14:00:00', '18:00:00', 'COMPLETED', 60.00),
(7, 6, '2023-06-22', '08:00:00', '12:00:00', 'COMPLETED', 120.00),
(7, 4, '2023-07-20', '09:00:00', '11:00:00', 'CONFIRMED', 50.00);




