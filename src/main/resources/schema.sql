CREATE DATABASE FoodOrderingApp;
USE FoodOrderingApp;

CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(15) UNIQUE NOT NULL,
    address VARCHAR(255) NOT NULL,
    role ENUM('customer', 'restaurant', 'delivery_agent', 'admin') NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE Tiffins (
    tiffin_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    restaurant_id INT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (restaurant_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

CREATE TABLE Orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    restaurant_id INT NOT NULL,
    delivery_agent_id INT DEFAULT NULL,
    status ENUM('pending', 'accepted', 'preparing', 'ready', 'in_transit', 'delivered') DEFAULT 'pending',
    total_price DECIMAL(10,2) NOT NULL,
    payment_status ENUM('pending', 'completed', 'failed') DEFAULT 'pending',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (restaurant_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (delivery_agent_id) REFERENCES Users(user_id) ON DELETE SET NULL
);

CREATE TABLE OrderItems (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    tiffin_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    total_price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (tiffin_id) REFERENCES Tiffins(tiffin_id) ON DELETE CASCADE
);

CREATE TABLE DeliveryStatus (
    delivery_status_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    delivery_agent_id INT NOT NULL,
    status ENUM('assigned', 'picked_up', 'on_the_way', 'delivered', 'failed') DEFAULT 'assigned',
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (delivery_agent_id) REFERENCES Users(user_id) ON DELETE CASCADE
);
