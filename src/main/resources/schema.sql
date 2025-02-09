-- CREATE DATABASE FoodOrderingApp;
-- USE FoodOrderingApp;

CREATE TABLE Users (
    userId INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    userPassword VARCHAR(255) NOT NULL,
    fullName VARCHAR(255) NOT NULL,
    phoneNumber VARCHAR(15) UNIQUE NOT NULL,
    userAddress VARCHAR(255) NOT NULL,
    userRole VARCHAR(255) NOT NULL CHECK (userRole IN ('customer', 'restaurant', 'delivery_agent', 'admin')),
    createdAt DATETIME DEFAULT CURRENT_TIMESTAMP,
    updatedAt DATETIME DEFAULT CURRENT_TIMESTAMP --ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE Tiffins (
    tiffinId INT AUTO_INCREMENT PRIMARY KEY,
    tiffinName VARCHAR(255) NOT NULL,
    tiffinDescription TEXT,
    tiffinImage VARCHAR(255),
    category VARCHAR(255),
    price DECIMAL(10,2) NOT NULL,
    restaurantId INT NOT NULL,
    createdAt DATETIME DEFAULT CURRENT_TIMESTAMP,
    updatedAt DATETIME DEFAULT CURRENT_TIMESTAMP, --ON UPDATE CURRENT_TIMESTAMP
    FOREIGN KEY (restaurantId) REFERENCES Users(userId) ON DELETE CASCADE
);

CREATE TABLE Orders (
    orderId INT AUTO_INCREMENT PRIMARY KEY,
    customerId INT NOT NULL,
    restaurantId INT NOT NULL,
    deliveryAgentId INT DEFAULT NULL,
    orderStatus VARCHAR(255) NOT NULL CHECK (orderStatus IN ('pending', 'accepted', 'preparing', 'ready', 'in_transit', 'delivered')),
    totalAmount DECIMAL(10,2) NOT NULL,
    paymentStatus VARCHAR(255) NOT NULL CHECK (paymentStatus IN ('pending', 'completed', 'failed')),
    createdAt DATETIME DEFAULT CURRENT_TIMESTAMP,
    updatedAt DATETIME DEFAULT CURRENT_TIMESTAMP, --ON UPDATE CURRENT_TIMESTAMP
    FOREIGN KEY (customerId) REFERENCES Users(userId) ON DELETE CASCADE,
    FOREIGN KEY (restaurantId) REFERENCES Users(userId) ON DELETE CASCADE,
    FOREIGN KEY (deliveryAgentId) REFERENCES Users(userId) ON DELETE SET NULL
);

CREATE TABLE OrderItems (
    orderItemId INT AUTO_INCREMENT PRIMARY KEY,
    orderId INT NOT NULL,
    tiffinId INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    totalAmount DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (orderId) REFERENCES Orders(orderId) ON DELETE CASCADE,
    FOREIGN KEY (tiffinId) REFERENCES Tiffins(tiffinId) ON DELETE CASCADE
);

CREATE TABLE DeliveryStatus (
    deliveryStatusId INT AUTO_INCREMENT PRIMARY KEY,
    orderId INT NOT NULL,
    deliveryAgentId INT NOT NULL,
    deliveryStatus VARCHAR(255) NOT NULL CHECK (deliveryStatus IN ('assigned', 'picked_up', 'on_the_way', 'delivered', 'failed')),
    updatedAt DATETIME DEFAULT CURRENT_TIMESTAMP, --ON UPDATE CURRENT_TIMESTAMP
    FOREIGN KEY (orderId) REFERENCES Orders(orderId) ON DELETE CASCADE,
    FOREIGN KEY (deliveryAgentId) REFERENCES Users(userId) ON DELETE CASCADE
);
