
CREATE TABLE actor ( 
actor_id SMALLINT UNSIGNED(5) NOT NULL, 
first_name VARCHAR(45) NOT NULL, 
last_name VARCHAR(45) NOT NULL, 
last_update TIMESTAMP(19) NOT NULL, 
PRIMARY KEY (actor_id), 
UNIQUE (actor_id), 

); 

CREATE TABLE address ( 
address_id SMALLINT UNSIGNED(5) NOT NULL, 
address VARCHAR(50) NOT NULL, 
address2 VARCHAR(50) NOT NULL, 
district VARCHAR(20) NOT NULL, 
city_id SMALLINT UNSIGNED(5) NOT NULL, 
postal_code VARCHAR(10) NOT NULL, 
phone VARCHAR(20) NOT NULL, 
last_update TIMESTAMP(19) NOT NULL, 
PRIMARY KEY (address_id), 
UNIQUE (address_id), 
FOREIGN KEY (city_id) REFERENCES city(city_id)
); 

CREATE TABLE bubu ( 
bibi TEXT(65535) NOT NULL, 
baba TEXT(65535) NOT NULL, 
PRIMARY KEY (), 
UNIQUE (), 

); 

CREATE TABLE category ( 
category_id TINYINT UNSIGNED(3) NOT NULL, 
name VARCHAR(25) NOT NULL, 
last_update TIMESTAMP(19) NOT NULL, 
PRIMARY KEY (category_id), 
UNIQUE (category_id), 

); 

CREATE TABLE city ( 
city_id SMALLINT UNSIGNED(5) NOT NULL, 
city VARCHAR(50) NOT NULL, 
country_id SMALLINT UNSIGNED(5) NOT NULL, 
last_update TIMESTAMP(19) NOT NULL, 
PRIMARY KEY (city_id), 
UNIQUE (city_id), 
FOREIGN KEY (country_id) REFERENCES country(country_id)
); 

CREATE TABLE country ( 
country_id SMALLINT UNSIGNED(5) NOT NULL, 
country VARCHAR(50) NOT NULL, 
last_update TIMESTAMP(19) NOT NULL, 
PRIMARY KEY (country_id), 
UNIQUE (country_id), 

); 

CREATE TABLE customer ( 
customer_id SMALLINT UNSIGNED(5) NOT NULL, 
store_id TINYINT UNSIGNED(3) NOT NULL, 
first_name VARCHAR(45) NOT NULL, 
last_name VARCHAR(45) NOT NULL, 
email VARCHAR(50) NOT NULL, 
address_id SMALLINT UNSIGNED(5) NOT NULL, 
active BIT NOT NULL, 
create_date DATETIME(19) NOT NULL, 
last_update TIMESTAMP(19) NOT NULL, 
PRIMARY KEY (customer_id), 
UNIQUE (customer_id), 
FOREIGN KEY (address_id) REFERENCES address(address_id), 
FOREIGN KEY (store_id) REFERENCES store(store_id)
); 

CREATE TABLE film ( 
film_id SMALLINT UNSIGNED(5) NOT NULL, 
title VARCHAR(255) NOT NULL, 
description TEXT(65535) NOT NULL, 
release_year YEAR NOT NULL, 
language_id TINYINT UNSIGNED(3) NOT NULL, 
original_language_id TINYINT UNSIGNED(3) NOT NULL, 
rental_duration TINYINT UNSIGNED(3) NOT NULL, 
rental_rate DECIMAL(4) NOT NULL, 
length SMALLINT UNSIGNED(5) NOT NULL, 
replacement_cost DECIMAL(5) NOT NULL, 
rating ENUM(5) NOT NULL, 
special_features SET(54) NOT NULL, 
last_update TIMESTAMP(19) NOT NULL, 
PRIMARY KEY (film_id), 
UNIQUE (film_id), 
FOREIGN KEY (language_id) REFERENCES language(language_id), 
FOREIGN KEY (original_language_id) REFERENCES language(language_id)
); 

CREATE TABLE film_actor ( 
actor_id SMALLINT UNSIGNED(5) NOT NULL, 
film_id SMALLINT UNSIGNED(5) NOT NULL, 
last_update TIMESTAMP(19) NOT NULL, 
PRIMARY KEY (actor_id, film_id), 
UNIQUE (actor_id, film_id), 
FOREIGN KEY (actor_id) REFERENCES actor(actor_id), 
FOREIGN KEY (film_id) REFERENCES film(film_id)
); 

CREATE TABLE film_category ( 
film_id SMALLINT UNSIGNED(5) NOT NULL, 
category_id TINYINT UNSIGNED(3) NOT NULL, 
last_update TIMESTAMP(19) NOT NULL, 
PRIMARY KEY (category_id, film_id), 
UNIQUE (film_id, category_id), 
FOREIGN KEY (category_id) REFERENCES category(category_id), 
FOREIGN KEY (film_id) REFERENCES film(film_id)
); 

CREATE TABLE film_text ( 
film_id SMALLINT(5) NOT NULL, 
title VARCHAR(255) NOT NULL, 
description TEXT(65535) NOT NULL, 
PRIMARY KEY (film_id), 
UNIQUE (film_id), 

); 

CREATE TABLE inventory ( 
inventory_id MEDIUMINT UNSIGNED(8) NOT NULL, 
film_id SMALLINT UNSIGNED(5) NOT NULL, 
store_id TINYINT UNSIGNED(3) NOT NULL, 
last_update TIMESTAMP(19) NOT NULL, 
PRIMARY KEY (inventory_id), 
UNIQUE (inventory_id), 
FOREIGN KEY (film_id) REFERENCES film(film_id), 
FOREIGN KEY (store_id) REFERENCES store(store_id)
); 

CREATE TABLE language ( 
language_id TINYINT UNSIGNED(3) NOT NULL, 
name CHAR(20) NOT NULL, 
last_update TIMESTAMP(19) NOT NULL, 
PRIMARY KEY (language_id), 
UNIQUE (language_id), 

); 

CREATE TABLE payment ( 
payment_id SMALLINT UNSIGNED(5) NOT NULL, 
customer_id SMALLINT UNSIGNED(5) NOT NULL, 
staff_id TINYINT UNSIGNED(3) NOT NULL, 
rental_id INT(10) NOT NULL, 
amount DECIMAL(5) NOT NULL, 
payment_date DATETIME(19) NOT NULL, 
last_update TIMESTAMP(19) NOT NULL, 
PRIMARY KEY (payment_id), 
UNIQUE (payment_id), 
FOREIGN KEY (customer_id) REFERENCES customer(customer_id), 
FOREIGN KEY (rental_id) REFERENCES rental(rental_id), 
FOREIGN KEY (staff_id) REFERENCES staff(staff_id)
); 

CREATE TABLE rental ( 
rental_id INT(10) NOT NULL, 
rental_date DATETIME(19) NOT NULL, 
inventory_id MEDIUMINT UNSIGNED(8) NOT NULL, 
customer_id SMALLINT UNSIGNED(5) NOT NULL, 
return_date DATETIME(19) NOT NULL, 
staff_id TINYINT UNSIGNED(3) NOT NULL, 
last_update TIMESTAMP(19) NOT NULL, 
PRIMARY KEY (rental_id), 
UNIQUE (rental_id, rental_date, inventory_id, customer_id), 
FOREIGN KEY (customer_id) REFERENCES customer(customer_id), 
FOREIGN KEY (inventory_id) REFERENCES inventory(inventory_id), 
FOREIGN KEY (staff_id) REFERENCES staff(staff_id)
); 

CREATE TABLE staff ( 
staff_id TINYINT UNSIGNED(3) NOT NULL, 
first_name VARCHAR(45) NOT NULL, 
last_name VARCHAR(45) NOT NULL, 
address_id SMALLINT UNSIGNED(5) NOT NULL, 
picture BLOB(65535) NOT NULL, 
email VARCHAR(50) NOT NULL, 
store_id TINYINT UNSIGNED(3) NOT NULL, 
active BIT NOT NULL, 
username VARCHAR(16) NOT NULL, 
password VARCHAR(40) NOT NULL, 
last_update TIMESTAMP(19) NOT NULL, 
PRIMARY KEY (staff_id), 
UNIQUE (staff_id), 
FOREIGN KEY (address_id) REFERENCES address(address_id), 
FOREIGN KEY (store_id) REFERENCES store(store_id)
); 

CREATE TABLE store ( 
store_id TINYINT UNSIGNED(3) NOT NULL, 
manager_staff_id TINYINT UNSIGNED(3) NOT NULL, 
address_id SMALLINT UNSIGNED(5) NOT NULL, 
last_update TIMESTAMP(19) NOT NULL, 
PRIMARY KEY (store_id), 
UNIQUE (store_id, manager_staff_id), 
FOREIGN KEY (address_id) REFERENCES address(address_id), 
FOREIGN KEY (manager_staff_id) REFERENCES staff(staff_id)
); 

CREATE VIEW actor_info AS 
SELECT actor_id, first_name, last_name, film_info

CREATE VIEW customer_list AS 
SELECT ID, name, address, zip code, phone, city, country, notes, SID

CREATE VIEW film_list AS 
SELECT FID, title, description, category, price, length, rating, actors

CREATE VIEW nicer_but_slower_film_list AS 
SELECT FID, title, description, category, price, length, rating, actors

CREATE VIEW sales_by_film_category AS 
SELECT category, total_sales

CREATE VIEW sales_by_store AS 
SELECT store, manager, total_sales

CREATE VIEW staff_list AS 
SELECT ID, name, address, zip code, phone, city, country, SID
