-- Roles table (good as is)
CREATE TABLE user_roles (
    id SERIAL PRIMARY KEY,
    role_name VARCHAR(20) UNIQUE NOT NULL
);

-- Insert roles (good as is)
INSERT INTO user_roles (role_name) VALUES ('ADMIN'), ('CUSTOMER');

-- Improved users table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES user_roles(id)
);

-- Improved workspaces table
CREATE TABLE workspaces (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    type VARCHAR(50) NOT NULL CHECK (type IN ('OPEN_SPACE', 'PRIVATE_ROOM', 'MEETING_ROOM', 'DESK')),
    price_per_hour DECIMAL(10, 2) NOT NULL,
    capacity INT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Improved bookings table
CREATE TABLE bookings (
    id SERIAL PRIMARY KEY,
    customer_id INT NOT NULL,
    workspace_id INT NOT NULL,
    booking_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'CONFIRMED' CHECK (status IN ('CONFIRMED', 'CANCELLED', 'COMPLETED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES users(id),
    FOREIGN KEY (workspace_id) REFERENCES workspaces(id),
    CONSTRAINT valid_booking_time CHECK (end_time > start_time)
);

-- Removed availabilities table as it's not needed in this simple implementation