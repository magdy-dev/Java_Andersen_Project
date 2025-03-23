-- Insert roles
INSERT INTO user_roles (role_name) VALUES ('ADMIN'), ('CUSTOMER');

-- Create the users table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role_id INT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES user_roles(id)
);

-- Create the workspaces table
CREATE TABLE workspaces (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    -- Additional fields can be added as needed
);

-- Create the bookings table
CREATE TABLE bookings (
    id SERIAL PRIMARY KEY,
    customer_id INT NOT NULL,
    workspace_id INT NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES users(id),
    FOREIGN KEY (workspace_id) REFERENCES workspaces(id)
);

-- Create the availabilities table
CREATE TABLE availabilities (
    id SERIAL PRIMARY KEY,
    workspace_id INT NOT NULL,
    date DATE NOT NULL,
    time TIME NOT NULL,
    capacity INT NOT NULL,
    remaining INT NOT NULL,
    FOREIGN KEY (workspace_id) REFERENCES workspaces(id)
);