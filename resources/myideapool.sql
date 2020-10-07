CREATE TABLE Business (
	id INT PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL,
	address VARCHAR(255) NOT NULL
);

INSERT INTO Business (name, address) VALUES ("My Consulting Business", "129 W 81st St New York, NY 10024 USA");