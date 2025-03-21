-- Insert roles
INSERT INTO user_roles (role_name) VALUES
('ADMIN'),
('CUSTOMER');

-- Insert users  users with ADMIN and CUSTOMER roles)
INSERT INTO users (username, password, role_id) VALUES
('magy', 'password123', 2),
('yury', 'password456', 2),
('john', 'password789', 2),
('jane', 'password012', 2),
('bob', 'password345', 2),
('alice', 'password678', 2),
('charlie', 'password901', 1),
('dave', 'password234', 1),
('eve', 'password567', 1),
('frank', 'password890', 2),
('grace', 'password135', 2),
('heidi', 'password246', 2);

-- Insert workspaces
INSERT INTO workspaces (name, description) VALUES
('Workspace 1', 'This is workspace 1'),
('Workspace 2', 'This is workspace 2'),
('Workspace 3', 'This is workspace 3'),
('Workspace 4', 'This is workspace 4'),
('Workspace 5', 'This is workspace 5'),
('Workspace 6', 'This is workspace 6'),
('Workspace 7', 'This is workspace 7'),
('Workspace 8', 'This is workspace 8');

-- Insert bookings  bookings for users)
INSERT INTO bookings (customer_id, workspace_id, start_time, end_time) VALUES
(1, 1, '09:00:00', '17:00:00'),
(2, 2, '10:00:00', '18:00:00'),
(3, 3, '11:00:00', '19:00:00'),
(4, 4, '12:00:00', '20:00:00'),
(5, 5, '13:00:00', '21:00:00'),
(6, 6, '14:00:00', '22:00:00'),
(7, 7, '15:00:00', '23:00:00'),
(8, 8, '16:00:00', '24:00:00'),
(9, 1, '09:00:00', '17:00:00'),
(10, 2, '10:00:00', '18:00:00'),
(11, 3, '11:00:00', '19:00:00'),
(12, 4, '12:00:00', '20:00:00');

-- Insert availabilities (more availabilities for workspaces)
INSERT INTO availabilities (workspace_id, date, time, capacity, remaining) VALUES
(1, '2023-03-01', '09:00:00', 10, 10),
(1, '2023-03-02', '09:00:00', 10, 10),
(2, '2023-03-01', '10:00:00', 15, 15),
(2, '2023-03-02', '10:00:00', 15, 15),
(3, '2023-03-01', '11:00:00', 20, 20),
(3, '2023-03-02', '11:00:00', 20, 20),
(4, '2023-03-01', '12:00:00', 25, 25),
(4, '2023-03-02', '12:00:00', 25, 25),
(5, '2023-03-01', '13:00:00', 30, 30),
(5, '2023-03-02', '13:00:00', 30, 30),
(6, '2023-03-01', '14:00:00', 35, 35),
(6, '2023-03-02', '14:00:00', 35, 35),
(7, '2023-03-01', '15:00:00', 40, 40),
(7, '2023-03-02', '15:00:00', 40, 40),
(8, '2023-03-01', '16:00:00', 45, 45),
(8, '2023-03-02', '16:00:00', 45, 45);