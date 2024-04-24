CREATE DATABASE IF NOT EXISTS bike_location;

USE bike_location;

CREATE TABLE IF NOT EXISTS bikes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    last_service DATETIME(6),
    latitude DOUBLE,
    longitude DOUBLE,
    stand_id BIGINT,
    FOREIGN KEY (stand_id) REFERENCES stands(id)
);

CREATE TABLE IF NOT EXISTS rides (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    end_timestamp DATETIME(6),
    start_timestamp DATETIME(6),
    user_id BIGINT,
    bike_id BIGINT,
    end_stand_id BIGINT,
    start_stand_id BIGINT,
    FOREIGN KEY (bike_id) REFERENCES bikes(id),
    FOREIGN KEY (end_stand_id) REFERENCES stands(id),
    FOREIGN KEY (start_stand_id) REFERENCES stands(id)
);

CREATE TABLE IF NOT EXISTS stands (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    latitude DOUBLE,
    longitude DOUBLE,
    name VARCHAR(255)
);

CREATE DATABASE IF NOT EXISTS bike_service;

USE bike_service;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email_address VARCHAR(255),
    name VARCHAR(255),
    password VARCHAR(255),
    role TINYINT
);






