-- Database schema for workspace booking system

-- Create enum types
CREATE TYPE booking_status AS ENUM ('CONFIRMED', 'CANCELLED', 'COMPLETED');
CREATE TYPE user_role AS ENUM ('CUSTOMER', 'ADMIN', 'MANAGER');
CREATE TYPE workspace_type AS ENUM ('MEETING_ROOM', 'PRIVATE_OFFICE', 'COWORKING_SPACE', 'EVENT_SPACE');

-- Users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    full_name VARCHAR(255) NOT NULL,
    role user_role NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Workspaces table
CREATE TABLE workspaces (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    type workspace_type NOT NULL,
    price_per_hour DECIMAL(10,2) NOT NULL,
    capacity INTEGER NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT positive_price CHECK (price_per_hour > 0),
    CONSTRAINT positive_capacity CHECK (capacity > 0)
);

-- Bookings table
CREATE TABLE bookings (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    workspace_id BIGINT NOT NULL,
    start_time TIMESTAMP WITH TIME ZONE NOT NULL,
    end_time TIMESTAMP WITH TIME ZONE NOT NULL,
    status booking_status NOT NULL,
    total_price DECIMAL(10,2),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES users(id),
    CONSTRAINT fk_workspace FOREIGN KEY (workspace_id) REFERENCES workspaces(id),
    CONSTRAINT valid_booking_time CHECK (end_time > start_time),
    CONSTRAINT future_booking CHECK (start_time > CURRENT_TIMESTAMP),
    CONSTRAINT positive_price CHECK (total_price IS NULL OR total_price >= 0)
);

-- Create indexes for better performance
CREATE INDEX idx_users_active ON users(is_active);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);

CREATE INDEX idx_workspaces_active ON workspaces(is_active);
CREATE INDEX idx_workspaces_type ON workspaces(type);
CREATE INDEX idx_workspaces_price ON workspaces(price_per_hour);

CREATE INDEX idx_bookings_active ON bookings(is_active);
CREATE INDEX idx_bookings_customer ON bookings(customer_id);
CREATE INDEX idx_bookings_workspace ON bookings(workspace_id);
CREATE INDEX idx_bookings_time_range ON bookings(start_time, end_time);
CREATE INDEX idx_bookings_status ON bookings(status);

