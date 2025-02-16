DROP TABLE IF EXISTS Users CASCADE;
CREATE TABLE Users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    userEmail VARCHAR(255) UNIQUE NOT NULL,
    user_password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(15) UNIQUE NOT NULL,
    user_address VARCHAR(255) NOT NULL,
    user_role VARCHAR(255) NOT NULL CHECK (user_role IN ('customer', 'restaurant', 'delivery_agent', 'admin')),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP --ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS Tiffins CASCADE;
CREATE TABLE Tiffins (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tiffin_name VARCHAR(255) NOT NULL,
    tiffin_description TEXT,
    tiffin_image VARCHAR(255),
    category VARCHAR(255),
    price DECIMAL(10,2) NOT NULL,
    restaurant_id INT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP, --ON UPDATE CURRENT_TIMESTAMP
    FOREIGN KEY (restaurant_id) REFERENCES Users(id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS Orders CASCADE;
CREATE TABLE Orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    restaurant_id INT NOT NULL,
    delivery_agent_id INT DEFAULT NULL,
    order_status VARCHAR(255) NOT NULL CHECK (order_status IN ('pending', 'accepted', 'preparing', 'ready', 'in_transit', 'delivered')),
    total_amount DECIMAL(10,2) NOT NULL,
    payment_status VARCHAR(255) NOT NULL CHECK (payment_status IN ('pending', 'completed', 'failed')),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP, --ON UPDATE CURRENT_TIMESTAMP
    FOREIGN KEY (customer_id) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY (restaurant_id) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY (delivery_agent_id) REFERENCES Users(id) ON DELETE SET NULL
);

DROP TABLE IF EXISTS OrderItems CASCADE;
CREATE TABLE OrderItems (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    tiffin_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    total_amount DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES Orders(id) ON DELETE CASCADE,
    FOREIGN KEY (tiffin_id) REFERENCES Tiffins(id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS DeliveryStatus CASCADE;
CREATE TABLE DeliveryStatus (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    delivery_agent_id INT NOT NULL,
    delivery_status VARCHAR(255) NOT NULL CHECK (delivery_status IN ('assigned', 'picked_up', 'on_the_way', 'delivered', 'failed')),
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP, --ON UPDATE CURRENT_TIMESTAMP
    FOREIGN KEY (order_id) REFERENCES Orders(id) ON DELETE CASCADE,
    FOREIGN KEY (delivery_agent_id) REFERENCES Users(id) ON DELETE CASCADE
);
