# Java_Andersen_Project
## Coworking Space Reservation Application

### Overview
The Coworking Space Reservation Application is a console-based application that allows users to browse available coworking spaces, make reservations, and manage bookings. It provides an intuitive interface for both customers and admins, making it easy to handle coworking space reservations and management.

### Features

#### User Roles
- **Customer**:
  - Browse available spaces
  - Make reservations
  - Cancel bookings
  - View current reservations

- **Admin**:
  - Manage coworking spaces (add/remove spaces)
  - View all bookings
  - Remove specific bookings

### Workspace Management
Admins can manage workspaces by:
- Adding new workspaces
- Removing existing workspaces
- Viewing details of all available spaces

### Booking Management
Users can:
- View available workspaces with their details
- Make new reservations based on availability
- Cancel existing reservations
- View their booking history

### Flow
1. **Welcome Message**: Upon starting the application, users are greeted with a welcome message.
2. **Main Menu**: Users can choose to register, log in, or exit the application.
3. **Admin Login**: Admins can log in to access admin functionalities.
4. **Customer Login**: Customers can log in to manage their reservations.

### User Registration
Users must register to create an account. During registration, users will provide their username, password, and any other required information. Once registered, users can log in using their credentials.

### Admin Credentials
- **Username**: admina
- **Password**: admin

### Getting Started

#### Prerequisites
- Java Development Kit (JDK) installed
- IDE (e.g., IntelliJ IDEA, Eclipse) for Java development

#### Building and Running the Application
To build and run the application, execute the following command:

```bash
---->cd C:\Users\marko\Downloads\CoworkingSpaceReservation\ui && 
---->mvn clean package && 
---->java -jar target\ui-1.0-SNAPSHOT.jar
